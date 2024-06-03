package com.tool.goalias.manager;

import com.alibaba.csp.sentinel.util.function.Tuple2;
import com.tool.goalias.enums.GoaliasStrategyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class GoaliasMethodManager {
    private static final Logger log = LoggerFactory.getLogger(GoaliasMethodManager.class);

    private static final Map<String, Tuple2<GoaliasStrategyEnum, Annotation>> goaliasMethodMap = new HashMap<>();

    public static void addGoaliasMethod(String methodName, Tuple2<GoaliasStrategyEnum, Annotation> tuple2) {
            log.info("[GOALIAS] Register goalias method:[{}{}]",tuple2.r1.name(),methodName);
            goaliasMethodMap.put(methodName,tuple2);
    }

    public static Tuple2<GoaliasStrategyEnum, Annotation> getGoaliasMethod(String methodName) {
        return goaliasMethodMap.get(methodName);
    }

    public  static boolean isContain(String methodName){
        return goaliasMethodMap.containsKey(methodName);
    }

}
