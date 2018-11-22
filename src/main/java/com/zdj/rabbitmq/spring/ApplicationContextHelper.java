package com.zdj.rabbitmq.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fulanto
 * @date 2018-03-28
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext != null ? applicationContext.getBean(beanName) : null;
    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext != null ? applicationContext.getBean(type) : null;
    }

    public static <T> T getBean(String beanName, Class<T> type) {
        return applicationContext != null ? applicationContext.getBean(beanName, type) : null;
    }
}
