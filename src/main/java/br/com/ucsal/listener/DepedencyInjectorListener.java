package br.com.ucsal.listener;

import br.com.ucsal.util.ManagerClassUtil;
import br.com.ucsal.annotations.Named;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.reflections.Reflections;

import java.util.Set;

@WebListener
public class DepedencyInjectorListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Iniciando escaneamento de classes...");

        Reflections reflections = new Reflections("br.com.ucsal");

        // Registra todas as classes anotadas com @Named
        Set<Class<?>> namedClasses = reflections.getTypesAnnotatedWith(Named.class);
        for (Class<?> clazz : namedClasses) {
            Named namedAnnotation = clazz.getAnnotation(Named.class);
            System.out.println("Registrando implementação: " + clazz.getName() + " com nome: " + namedAnnotation.value());
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                ManagerClassUtil.register(interfaces[0], clazz);
            }
        }


    }


}