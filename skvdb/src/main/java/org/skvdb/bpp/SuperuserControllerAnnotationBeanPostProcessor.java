package org.skvdb.bpp;

import org.skvdb.annotation.SuperuserController;
import org.skvdb.controller.Controller;
import org.skvdb.exception.ForbiddenMethodException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class SuperuserControllerAnnotationBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Class<?>> controllerMap = new HashMap<>();

    @Autowired
    private UserService userService;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(SuperuserController.class) && bean instanceof Controller) {
            controllerMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = controllerMap.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Request request = (Request) args[0];
                    String username = request.getUsername();

                    if (userService.isSuperuser(username)) {
                        return method.invoke(bean, args);
                    }
                    throw new ForbiddenMethodException();
                }
            });
        }
        return bean;
    }
}
