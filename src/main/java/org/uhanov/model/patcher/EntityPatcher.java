package org.uhanov.model.patcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityPatcher<T, V> {
    public static final int INDEX_OF_PROPERTY = 3;

    public T patchEntity(T original, V patch) {
        List<Method> setters = getMethodsByPrefix("set", original.getClass());
        List<Method> getters = getMethodsByPrefix("get", patch.getClass());
        List<Method> originalGetters = getMethodsByPrefix("get", original.getClass());

        getters.removeIf(method -> method.getName().equals("getClass"));
        originalGetters.removeIf(method -> method.getName().equals("getClass"));

        setters.sort(methodComparator());
        getters.sort(methodComparator());
        originalGetters.sort(methodComparator());

        for (int i = 0; i < setters.size(); i++) {
            try {
                Method getter = getters.get(i);
                Method originalGetter = originalGetters.get(i);
                Method setter = setters.get(i);
                if (!getter.getName().substring(INDEX_OF_PROPERTY).equals(setter.getName().substring(INDEX_OF_PROPERTY))
                        || !setter.getParameters()[0].getType().equals(getter.getReturnType())) {
                    continue;
                }
                Object valueToInject = getter.invoke(patch) == null
                        ? originalGetter.invoke(original)
                        : getter.invoke(patch);

                setter.invoke(original, setter.getParameterTypes()[0].cast(valueToInject));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }

        }
        return original;
    }

    private static Comparator<Method> methodComparator() {
        return Comparator.comparing(m -> m.getName().substring(INDEX_OF_PROPERTY));
    }

    private static List<Method> getMethodsByPrefix(String prefix, Class definition) {
        return Arrays.stream(definition.getMethods())
                .filter(method -> method.getName().startsWith(prefix))
                .collect(Collectors.toList());
    }
}
