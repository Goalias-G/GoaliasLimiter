package com.tool.goalias.proxy;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.MethodUtil;
import com.tool.goalias.annotation.GoaliasFallback;
import com.tool.goalias.annotation.GoaliasHot;
import com.tool.goalias.enums.GoaliasStrategyEnum;
import com.tool.goalias.manager.GoaliasMethodManager;
import com.tool.goalias.manager.GoaliasRuleManager;
import com.tool.goalias.sph.SphEngine;
import com.tool.goalias.util.SerialsUtil;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.attribute.MethodAttributeAppender;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GoaliasByteBuddyProxy {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Object bean;

    private final Class<?> originalClazz;

    private Map<String, Method> methodCache = new HashMap<>();

    public GoaliasByteBuddyProxy(Object bean, Class<?> originalClazz) {
        this.bean = bean;
        this.originalClazz = originalClazz;
    }

    public Object proxy() throws Exception{
        return new ByteBuddy().subclass(originalClazz)
                .name(StrUtil.format("{}$ByteBuddy${}", originalClazz.getName(), SerialsUtil.generateShortUUID()))
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new AopInvocationHandler()))
                .attribute(MethodAttributeAppender.ForInstrumentedMethod.INCLUDING_RECEIVER)
                .annotateType(bean.getClass().getAnnotations())
                .make()
                .load(GoaliasByteBuddyProxy.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .newInstance();
    }

    public class AopInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodStr = MethodUtil.resolveMethodName(method);

            Method realMethod;
            if (methodCache.containsKey(methodStr)){
                realMethod = methodCache.get(methodStr);
            }else{
                realMethod = ReflectUtil.getMethod(bean.getClass(), method.getName(), method.getParameterTypes());
                methodCache.put(methodStr, realMethod);
            }

            if (GoaliasMethodManager.isContain(methodStr)){
                GoaliasStrategyEnum goaliasStrategyEnum = GoaliasMethodManager.getGoaliasMethod(methodStr).r1;
                Annotation anno = GoaliasMethodManager.getGoaliasMethod(methodStr).r2;

                if (anno instanceof GoaliasFallback){
                    GoaliasRuleManager.registerFallBackRule((GoaliasFallback) anno, methodStr);
                }else if (anno instanceof GoaliasHot){
                    GoaliasRuleManager.registerHotRule((GoaliasHot) anno, methodStr);
                }else{
                    throw new RuntimeException("annotation type error");
                }

                return SphEngine.process(bean, realMethod, args, methodStr, goaliasStrategyEnum);
            }else {//正常不会出现 排错处理
                return ReflectUtil.invoke(bean, realMethod, args);
            }
        }
    }

}
