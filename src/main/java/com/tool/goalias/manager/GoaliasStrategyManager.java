package com.tool.goalias.manager;

import com.tool.goalias.enums.GoaliasStrategyEnum;
import com.tool.goalias.strategy.GoaliasStrategy;

import java.util.HashMap;
import java.util.Map;

//将GoaliasStrategyEnum和具体策略联系起来
public class GoaliasStrategyManager {

    private static final Map<GoaliasStrategyEnum, GoaliasStrategy> strategyMap = new HashMap<>();

    public static void addStrategy(GoaliasStrategy strategy){
        strategyMap.put(strategy.getStrategy(), strategy);
    }

    public static GoaliasStrategy getStrategy(GoaliasStrategyEnum strategyEnum){
        return strategyMap.get(strategyEnum);
    }

}
