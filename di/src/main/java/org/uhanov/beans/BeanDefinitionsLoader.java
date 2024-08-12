package org.uhanov.beans;

import org.reflections.Reflections;
import org.uhanov.annotation.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanDefinitionsLoader {
    public final Map<String, Class<?>> beanDefinitions = new HashMap<>();

    private BeanDefinitionsLoader() {
    }

    public static BeanDefinitionsLoader loadDefinitions(Object... params) {
        var instance = new BeanDefinitionsLoader();
        Reflections reflections = new Reflections(params);
        reflections.getTypesAnnotatedWith(Component.class).stream()
                .forEach(clazz -> instance.beanDefinitions.put(clazz.getCanonicalName(), clazz));
        return instance;
    }

    public List<Class<?>> getAllLoadedClass() {
        return List.copyOf(beanDefinitions.values());
    }

}
