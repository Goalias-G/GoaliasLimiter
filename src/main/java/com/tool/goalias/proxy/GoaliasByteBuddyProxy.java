package com.tool.goalias.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GoaliasByteBuddyProxy {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Object bean;

    private final Class<?> originalClazz;

    private Map<String, Method> methodCache = new HashMap<>();

    public GoaliasByteBuddyProxy(Object bean, Class<?> originalClazz) {
        this.bean = bean;
        this.originalClazz = originalClazz;
    }
}
