package org.uhanov.beans;

import org.uhanov.annotation.Autowire;
import org.uhanov.annotation.Value;
import org.uhanov.exception.FailedToCreateInstanceException;
import org.uhanov.exception.NoSuchBeanException;
import org.uhanov.exception.ToManyBeansException;
import org.uhanov.exception.ToManyConstructorsException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class BeanFactory {
    static {
        Properties properties = new Properties();
        try (FileInputStream applicationProperties =
                     new FileInputStream("main/src/main/resources/application.properties")) {
            properties.load(new InputStreamReader(applicationProperties, StandardCharsets.UTF_8));
            properties.forEach((key, value) -> System.setProperty((String) key, (String) value));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BeanFactory b = new BeanFactory(BeanDefinitionsLoader.loadDefinitions(""));
        b.createBeans();
        b.injectValues();
        beanFactory = b;
    }

    private static final BeanFactory beanFactory;
    private final BeanDefinitionsLoader beanDefinitionsLoader;
    public final Map<String, Object> createdBeans = new HashMap<>();

    private BeanFactory(BeanDefinitionsLoader beanDefinitionsLoader) {
        this.beanDefinitionsLoader = beanDefinitionsLoader;
    }

    public static BeanFactory getInstance() {
        return beanFactory;
    }

    public Object getBeanByCanonicalName(String canonicalName) {
        return createdBeans.get(canonicalName);
    }

    private void createBeans() {
        beanDefinitionsLoader
                .getAllLoadedClass()
                .stream()
                .forEach(this::instantiateBean);
    }

    private void injectValues() {
        createdBeans.values().stream()
                .map(Object::getClass)
                .map(Class::getDeclaredFields)
                .map(List::of)
                .forEach(list -> list.forEach(this::injectFromProperties));
    }

    private void injectFromProperties(Field field) {
        if (!field.isAnnotationPresent(Value.class)) {
            return;
        }
        try {
            Object bean = createdBeans.get(field.getDeclaringClass().getCanonicalName());
            Class<?> fieldType = field.getType();
            Object annotation = field.getAnnotation(Value.class);
            String valueAlias = Value.class.getMethod("value").invoke(annotation).toString();

            if (field.trySetAccessible()) {
                field.set(bean, fieldType.cast(System.getProperty(valueAlias)));
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    private Object instantiateBean(Class<?> beanDefinition) {
        Object newBean;
        if (isInjectWithConstructor(beanDefinition)) {
            newBean = instantiateWithConstructor(beanDefinition);
            createdBeans.put(beanDefinition.getCanonicalName(), newBean);
            return newBean;
        }

        if (isInjectWithSetters(beanDefinition)) {
            newBean = instantiateWithSetters(beanDefinition);
            createdBeans.put(beanDefinition.getCanonicalName(), newBean);
            return newBean;
        }

        if (isInjectWithField(beanDefinition)) {
            newBean = instantiateWithFields(beanDefinition);
            createdBeans.put(beanDefinition.getCanonicalName(), newBean);
            return newBean;
        }

        newBean = instantiateWithDefaultConstrictor(beanDefinition);
        createdBeans.put(beanDefinition.getCanonicalName(), newBean);
        return newBean;
    }

    private Object instantiateWithDefaultConstrictor(Class<?> beanDefinition) {
        if (createdBeans.containsKey(beanDefinition.getCanonicalName())) {
            return createdBeans.get(beanDefinition.getCanonicalName());
        }
        try {
            return beanDefinition.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new FailedToCreateInstanceException("If bean dnt need DI, they must have default constructor"
                    + beanDefinition);
        }
    }


    private Object instantiateWithFields(Class<?> beanDefinition) {
        Object newBean;
        try {
            if (createdBeans.containsKey(beanDefinition.getCanonicalName())) {
                newBean = createdBeans.get(beanDefinition.getCanonicalName());
            } else {
                newBean = beanDefinition.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new FailedToCreateInstanceException("Bean with setters DI must have default constructor classType="
                    + beanDefinition);
        }
        List<Field> fields = Arrays.stream(beanDefinition.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowire.class))
                .collect(Collectors.toList());
        fields
                .stream()
                .forEach(field -> injectWithField(newBean, field));
        return newBean;
    }

    private void injectWithField(Object bean, Field field) {
        Class<?> fieldType = field.getType();
        if (field.trySetAccessible()) {
            try {
                field.set(bean, getOrCreateBeanByName(fieldType.getCanonicalName()));
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new FailedToCreateInstanceException();
            }
        }
        throw new FailedToCreateInstanceException("Cant change field=" + field);
    }

    private Object instantiateWithSetters(Class<?> beanDefinition) {
        Object newBean;
        try {
            if (createdBeans.containsKey(beanDefinition.getCanonicalName())) {
                newBean = createdBeans.get(beanDefinition.getCanonicalName());
            } else {
                newBean = beanDefinition.getConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new FailedToCreateInstanceException("Bean with setters DI must have default constructor classType="
                    + beanDefinition);
        }
        List<Method> setters = Arrays.stream(beanDefinition.getMethods())
                .filter(method -> method.isAnnotationPresent(Autowire.class)
                        && method.getReturnType().isAssignableFrom(void.class)
                        && method.getParameters().length == 1)
                .collect(Collectors.toList());
        setters.stream()
                .forEach(setter -> injectWithSetter(newBean, setter));
        return newBean;
    }

    private void injectWithSetter(Object bean, Method setter) {
        Class<?> parameter = setter.getParameters()[0].getType();
        try {
            setter.invoke(bean, getOrCreateBeanByName(parameter.getCanonicalName()));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            throw new FailedToCreateInstanceException("Setters must be public and dependency must exist class="
                    + bean.getClass() + " setter=" + setter);
        }
    }

    public Object instantiateWithConstructor(Class<?> beanDefinition) {
        Constructor<?> constructor = Arrays.stream(beanDefinition.getConstructors())
                .filter(c -> c.isAnnotationPresent(Autowire.class))
                .findFirst().get();//На npe уже была проверка
        List<String> constructorParametersTypes = Arrays.stream(constructor.getParameters())
                .map(parameter -> parameter.getType().getCanonicalName())
                .collect(Collectors.toList());

        Object object;
        try {
            object = constructor.newInstance(
                    buildParameters(constructorParametersTypes)
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new FailedToCreateInstanceException("Cant instance with constructor class=" + beanDefinition);
        }
        return object;
    }

    private Object[] buildParameters(List<String> constructorParametersTypes) {
        return constructorParametersTypes.stream()
                .map(this::getOrCreateBeanByName)
                .toArray();
    }

    private Object getOrCreateBeanByName(String typeOfPropertyForInject) {
        Class<?> classTypeForInject;
        try {
            classTypeForInject = Class.forName(typeOfPropertyForInject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new NoSuchBeanException("Cant create bean type=" + typeOfPropertyForInject);
        }
        List<Object> beansToInject = createdBeans.values()
                .stream()
                .filter(candidateBean -> classTypeForInject.isAssignableFrom(candidateBean.getClass()))
                .collect(Collectors.toList());
        if (beansToInject.size() > 1) {
            throw new ToManyBeansException("To many candidate for DI with type=" + classTypeForInject);
        }
        if (beansToInject.size() == 1) {
            return beansToInject.get(0);
        }

        List<Class<?>> beansTypeToInject = beanDefinitionsLoader.getAllLoadedClass()
                .stream()
                .filter(classTypeForInject::isAssignableFrom)
                .collect(Collectors.toList());

        if (beansTypeToInject.size() > 1) {
            throw new ToManyBeansException("To many candidate for DI with type=" + classTypeForInject);
        }
        if (beansTypeToInject.size() == 1) {
            return instantiateBean(beansTypeToInject.get(0));
        }
        throw new NoSuchBeanException("No candidate for inject Assignable From type" + typeOfPropertyForInject);
    }

    public boolean isInjectWithConstructor(Class<?> clazz) {
        long numberOfAutowiredConstructor = Arrays.stream(clazz.getConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowire.class))
                .count();
        if (numberOfAutowiredConstructor < 1) {
            return false;
        }
        if (numberOfAutowiredConstructor == 1) {
            return true;
        }
        throw new ToManyConstructorsException("More than one constructor with @Autowire in class="
                + clazz.getCanonicalName());
    }

    public boolean isInjectWithSetters(Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .anyMatch(method -> method.isAnnotationPresent(Autowire.class));
    }

    public boolean isInjectWithField(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(Autowire.class));
    }

}
