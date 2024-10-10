package org.skvdb.bpp;

import org.skvdb.annotation.ControllerMapping;
import org.skvdb.controller.Controller;
import org.skvdb.service.ControllerMappingService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ControllerMappingAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private ControllerMappingService controllerMappingService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(ControllerMapping.class) && bean instanceof Controller) {
            String methodName = beanClass.getAnnotation(ControllerMapping.class).name();
            controllerMappingService.put(methodName, beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
