package com.tool.goalias.strategy;

import com.tool.goalias.enums.GoaliasStrategyEnum;

import java.lang.reflect.Method;

public interface GoaliasStrategy {

    GoaliasStrategyEnum getStrategy();

    Object process(Object bean, Method method, Object[] args) throws Exception;
}
