package br.com.ucsal.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ManagerClassUtil {

    private static final Map<Class<?>, Set<Class<?>>> interfaceImplementations = new HashMap<>();


    public static void register(Class<?> interfaceType, Class<?> implementation) {
        interfaceImplementations.computeIfAbsent(interfaceType, key -> new HashSet<>()).add(implementation);
    }

    public static Set<Class<?>> getImplementations(Class<?> interfaceType) {
        return interfaceImplementations.getOrDefault(interfaceType, Set.of());
    }
}

