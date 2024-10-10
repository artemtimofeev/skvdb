package org.skvdb.bpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.annotation.Authentication;
import org.skvdb.exception.ForbiddenMethodException;
import org.skvdb.server.network.dto.Request;
import org.skvdb.security.AuthenticationFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthenticationAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, Class<?>> controllerMap = new HashMap<>();
    private final List<AuthenticationFilter> authenticationFilterList = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Authentication.class)) {
            controllerMap.put(beanName, beanClass);
        }
        if (bean instanceof AuthenticationFilter) {
            authenticationFilterList.add((AuthenticationFilter) bean);
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
                    boolean isSuccessfulAuthentication = true;
                    for (AuthenticationFilter authenticationFilter : authenticationFilterList) {
                        isSuccessfulAuthentication = isSuccessfulAuthentication && authenticationFilter.check(request);
                    }
                    logger.debug("Аутентификация проведена: {}", isSuccessfulAuthentication);
                    if (isSuccessfulAuthentication) {
                        Object retVal = method.invoke(bean, args);
                        return retVal;
                    }
                    throw new ForbiddenMethodException();
                }
            });
        }
        return bean;
    }
}
