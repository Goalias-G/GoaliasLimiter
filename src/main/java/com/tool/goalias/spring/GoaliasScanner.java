package com.tool.goalias.spring;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.tool.goalias.annotation.GoaliasFallback;
import com.tool.goalias.annotation.GoaliasHot;
import com.tool.goalias.enums.GoaliasStrategyEnum;
import com.tool.goalias.manager.GoaliasMethodManager;
import com.tool.goalias.manager.GoaliasStrategyManager;
import com.tool.goalias.proxy.GoaliasByteBuddyProxy;
import com.tool.goalias.strategy.GoaliasStrategy;
import com.tool.goalias.util.ProxyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class GoaliasScanner implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {//对所有bean进行后置处理
        Class<?> clazz = ProxyUtil.getUserClass(bean.getClass());

        if (GoaliasStrategy.class.isAssignableFrom(clazz)){
            GoaliasStrategyManager.addStrategy((GoaliasStrategy) bean);
            return bean;
        }

        AtomicBoolean needProxy=new AtomicBoolean(false);
        Arrays.stream(clazz.getMethods()).forEach(method -> {
            GoaliasFallback goaliasFallback = searchAnnotation(method, GoaliasFallback.class);
            if (ObjectUtil.isNotNull(goaliasFallback)){
                GoaliasMethodManager.addGoaliasMethod(method.getName(),new Tuple2<>(GoaliasStrategyEnum.FALLBACK,goaliasFallback));
                needProxy.set(true);
            }

            GoaliasHot goaliasHot = searchAnnotation(method, GoaliasHot.class);
            if (ObjectUtil.isNotNull(goaliasHot)){
                GoaliasMethodManager.addGoaliasMethod(method.getName(),new Tuple2<>(GoaliasStrategyEnum.HOT_METHOD,goaliasHot));
                needProxy.set(true);
            }
        });
        if (needProxy.get()){
            return bean;//TODO
        }else return bean;
    }

    private <A extends Annotation> A searchAnnotation(Method method, Class<A> annotationType){
        A anno = AnnotationUtil.getAnnotation(method, annotationType);
        //从接口层面向上搜索
        if (anno == null){
            Class<?>[] ifaces = method.getDeclaringClass().getInterfaces();

            for (Class<?> ifaceClass : ifaces){
                Method ifaceMethod = ReflectUtil.getMethod(ifaceClass, method.getName(), method.getParameterTypes());
                if (ifaceMethod != null) {
                    anno = searchAnnotation(ifaceMethod, annotationType);
                    break;
                }
            }
        }

        //从父类逐级向上搜索
        if (anno == null){
            Class<?> superClazz = method.getDeclaringClass().getSuperclass();
            if (superClazz != null){
                Method superMethod = ReflectUtil.getMethod(superClazz, method.getName(), method.getParameterTypes());
                if (superMethod != null){
                    return searchAnnotation(superMethod, annotationType);
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }

        return anno;
    }
}
