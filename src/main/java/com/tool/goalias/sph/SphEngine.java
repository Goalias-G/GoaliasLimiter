package com.tool.goalias.sph;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;

import com.tool.goalias.enums.GoaliasStrategyEnum;
import com.tool.goalias.manager.GoaliasStrategyManager;
import com.tool.goalias.spring.GoaliasConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class SphEngine {

    private static final Logger logger = LoggerFactory.getLogger(SphEngine.class);

    public static Object process(Object bean, Method method, Object[] args, String methodStr, GoaliasStrategyEnum goaliasStrategyEnum) throws Throwable{
        switch (goaliasStrategyEnum){
            case FALLBACK:
                if (SphO.entry(methodStr)){//if-else注册资源
                    try{
                        return ReflectUtil.invoke(bean, method, args);
                    }finally {
                        SphO.exit();
                    }
                }else{//要进行降级
                    if (GoaliasConfigHolder.getGoaliasProperty().isEnableLog()){
                        logger.info("[Goalias]Trigger fallback strategy for [{}], args: [{}]", methodStr, JSON.toJSONString(args));
                    }
                    return GoaliasStrategyManager.getStrategy(goaliasStrategyEnum).process(bean, method, args);
                }
            case HOT_METHOD:
                String convertParam = DigestUtil.md5Hex(JSON.toJSONString(args));
                Entry entry = null;
                try{//try-cache注册资源
                    entry = SphU.entry(methodStr, EntryType.IN, 1, convertParam);
                    return ReflectUtil.invoke(bean, method, args);
                }catch (BlockException e){
                    if (GoaliasConfigHolder.getGoaliasProperty().isEnableLog()){
                        logger.info("[Goalias]Trigger hotspot strategy for [{}], args: [{}]",  methodStr, JSON.toJSONString(args));
                    }

                    return GoaliasStrategyManager.getStrategy(goaliasStrategyEnum).process(bean, method, args);
                }finally {
                    if (entry != null){
                        entry.exit(1, convertParam);
                    }
                }
            default:
                throw new Exception("[Goalias] Strategy error!");
        }
    }
}
