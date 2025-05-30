package com.framework.gw.common.utils;

import com.framework.gw.provider.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

public class BeanUtils {

    public static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }

}
