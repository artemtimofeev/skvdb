package org.skvdb.service;

import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.controller.Controller;
import org.skvdb.exception.ControllerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ControllerMappingService {
    private final ConcurrentHashMap<String, String> methodNameToBeanName = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Controller> methodNameToBean = new ConcurrentHashMap<>();
    private volatile AtomicBoolean isInitialized = new AtomicBoolean(false);
    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger logger = LogManager.getLogger();

    public void put(String methodName, String beanName) {
        methodNameToBeanName.put(methodName, beanName);
    }

    synchronized public void init() {
        if (!isInitialized.get()) {
            for (Map.Entry<String, String> entry : methodNameToBeanName.entrySet()) {
                String methodName = entry.getKey();
                String beanName = entry.getValue();
                Controller controller = (Controller) applicationContext.getBean(beanName);
                methodNameToBean.put(methodName, controller);
                logger.debug("Зарегистрирован контроллер {} для метода {}", beanName, methodName);
            }
            isInitialized.set(true);
        }
    }

    synchronized public Controller getController(String methodName) throws ControllerNotFoundException {
        if (!isInitialized.get()) {
            init();
        }
        if (methodNameToBean.containsKey(methodName)) {
            return methodNameToBean.get(methodName);
        }
        throw new ControllerNotFoundException();
    }
}
