package org.uhanov.model.patcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityPatcher<T> {
    public static final int INDEX_OF_PROPERTY = 3;

    public T patchEntity(T original, T patch) {
        if (!original.getClass().equals(patch.getClass())) {
            throw new IllegalArgumentException();
        }
        Class<T> definition = (Class<T>) original.getClass();
        List<Method> setters = getMethodsByPrefix("set", definition);
        List<Method> getters = getMethodsByPrefix("get", definition);

        getters.removeIf(method -> method.getName().equals("getClass"));


        setters.sort(Comparator.comparing(m -> m.getName().substring(INDEX_OF_PROPERTY)));
        getters.sort(Comparator.comparing(m -> m.getName().substring(INDEX_OF_PROPERTY)));//Comparator.comparing(m -> m.getName().substring(3))

        for (int i = 0; i < setters.size(); i++) {
            try {
                Method getter = getters.get(i);
                Object valueToInject = getter.invoke(patch) == null ? getter.invoke(original)
                        : getter.invoke(patch);
                Method setter = setters.get(i);
                setter.invoke(original, setter.getParameterTypes()[0].cast(valueToInject));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }

        }
        return original;
    }

    private List<Method> getMethodsByPrefix(String prefix, Class<T> definition) {
        return Arrays.stream(definition.getMethods())
                .filter(method -> method.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }
}
