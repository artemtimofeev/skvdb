package org.skvdb.bpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skvdb.annotation.Authorization;
import org.skvdb.exception.ForbiddenMethodException;
import org.skvdb.security.AuthorityType;
import org.skvdb.security.AuthorizationFilter;
import org.skvdb.server.network.dto.Request;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorizationAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, Class<?>> controllerMap = new HashMap<>();
    private final Map<String, AuthorityType> beanNameToAuthorityType = new HashMap<>();
    private final List<AuthorizationFilter> authorizationFilterList = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Authorization.class)) {
            AuthorityType authorityType = beanClass.getAnnotation(Authorization.class).authorityType();
            controllerMap.put(beanName, beanClass);
            beanNameToAuthorityType.put(beanName, authorityType);
        }
        if (bean instanceof AuthorizationFilter) {
            authorizationFilterList.add((AuthorizationFilter) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = controllerMap.get(beanName);
        AuthorityType authorityType = beanNameToAuthorityType.get(beanName);
        if (beanClass != null) {
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Request request = (Request) args[0];
                    boolean isSuccessfulAuthorization = true;
                    for (AuthorizationFilter authorizationFilter : authorizationFilterList) {
                        isSuccessfulAuthorization = isSuccessfulAuthorization && authorizationFilter.check(request, authorityType);
                    }
                    logger.debug("Авторизация проведена: {}", isSuccessfulAuthorization);
                    if (isSuccessfulAuthorization) {
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
