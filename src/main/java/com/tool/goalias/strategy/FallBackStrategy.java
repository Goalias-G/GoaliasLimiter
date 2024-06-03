package com.tool.goalias.strategy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.tool.goalias.enums.GoaliasStrategyEnum;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FallBackStrategy implements GoaliasStrategy{

    private final Map<String, Method> fallBackMethodMap = new ConcurrentHashMap<>();

    @Override
    public GoaliasStrategyEnum getStrategy() {
        return GoaliasStrategyEnum.FALLBACK;
    }

    @Override
    public Object process(Object bean, Method method, Object[] args) throws Exception {

        String fallbackMethodName = StrUtil.format("{}Fallback", method.getName());
        Method fallbackMethod;
        if (fallBackMethodMap.containsKey(fallbackMethodName)){
            fallbackMethod = fallBackMethodMap.get(fallbackMethodName);
        }else{
            fallbackMethod = ReflectUtil.getMethod(bean.getClass(), fallbackMethodName, method.getParameterTypes());
            fallBackMethodMap.put(fallbackMethodName, fallbackMethod);
        }

        if (ObjectUtil.isNull(fallbackMethod)){
            throw new RuntimeException(StrUtil.format("[Goalias] Can't find fallback method [{}] in bean [{}]", fallbackMethodName, bean.getClass().getName()));
        }
        return ReflectUtil.invoke(bean, fallbackMethod, args);
    }
}
