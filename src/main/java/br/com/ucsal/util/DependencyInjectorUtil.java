package br.com.ucsal.util;

import br.com.ucsal.annotations.Inject;
import br.com.ucsal.annotations.Named;
import br.com.ucsal.annotations.Rota;
import br.com.ucsal.annotations.Singleton;
import br.com.ucsal.controller.Command;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DependencyInjectorUtil {
    private static final DependencyInjectorUtil INSTANCE = new DependencyInjectorUtil();
    private static final Map<Class<?>, Object> singletons = new HashMap<>();

    private DependencyInjectorUtil() {
    }

    public static DependencyInjectorUtil getInstance() {
        return INSTANCE;
    }

    public  Command getRota(String path) throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections();
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Rota.class);

        for (Class<?> clazz : annotatedClasses) {
            Rota rota = clazz.getAnnotation(Rota.class);
            String[] rotas = rota.value();

            if (Arrays.stream(rotas).toList().contains(path)) {
                return (Command) clazz.newInstance();
            }
        }

        return null;
    }

    public void initialize(Object object) throws Exception {
        Class<?> clazz = object.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Inject injectAnnotation = field.getAnnotation(Inject.class);
                String qualifier = injectAnnotation.value();

                Class<?> fieldType = field.getType();
                Object instance;

                if (fieldType.isInterface()) {
                    // Localiza a implementação para a interface
                    instance = findImplementation(fieldType, qualifier);
                } else {
                    // Cria ou reutiliza a instância
                    instance = getInstance(fieldType);
                }

                // Torna o campo acessível e injeta a dependência
                field.setAccessible(true);
                field.set(object, instance);

                // Inicializa recursivamente o objeto injetado
                initialize(instance);
            }
        }
    }

    private Object getInstance(Class<?> clazz) throws Exception {
        if (clazz.isAnnotationPresent(Singleton.class)) {
            return getSingletonInstance(clazz);
        }
        return clazz.getDeclaredConstructor().newInstance();
    }

    private Object getSingletonInstance(Class<?> clazz) throws Exception {
        if (!singletons.containsKey(clazz)) {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            singletons.put(clazz, instance);
        }
        return singletons.get(clazz);
    }

    private Object findImplementation(Class<?> interfaceType, String qualifier) throws Exception {
        Set<Class<?>> implementations = ManagerClassUtil.getImplementations(interfaceType);

        if (implementations.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma implementação encontrada para " + interfaceType.getName());
        }

        for (Class<?> implementation : implementations) {
            Named namedAnnotation = implementation.getAnnotation(Named.class);
            if (namedAnnotation != null && namedAnnotation.value().equals(qualifier)) {
                return getInstance(implementation);
            }
        }

        throw new IllegalArgumentException("Nenhuma implementação encontrada para " + interfaceType.getName() + " com o qualificador '" + qualifier + "'");
    }

}
