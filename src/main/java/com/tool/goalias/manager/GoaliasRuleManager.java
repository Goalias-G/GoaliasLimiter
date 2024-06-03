package com.tool.goalias.manager;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.tool.goalias.annotation.GoaliasFallback;
import com.tool.goalias.annotation.GoaliasHot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoaliasRuleManager {
    private static final Logger logger = LoggerFactory.getLogger(GoaliasRuleManager.class);

    public static void registerFallBackRule(GoaliasFallback goaliasFallback,String resourceName){
        FlowRule rule = new FlowRule();
        rule.setResource(resourceName);
        rule.setGrade(goaliasFallback.grade().getGrade());//QPS or Thread
        rule.setCount(goaliasFallback.count());
        rule.setLimitApp("default");

        FlowRuleManager.loadRules(ListUtil.toList(rule));
        logger.info("[Goalias] Add Fallback Rule [{}]", resourceName);
    }

    public static void registerHotRule(GoaliasHot goaliasHot, String resourceKey){

        if (!ParamFlowRuleManager.hasRules(resourceKey)){
            ParamFlowRule rule = new ParamFlowRule();

            rule.setResource(resourceKey);
            rule.setGrade(goaliasHot.grade().getGrade());
            rule.setCount(goaliasHot.count());
            rule.setDurationInSec(goaliasHot.duration());
            rule.setParamIdx(0);

            ParamFlowRuleManager.loadRules(ListUtil.toList(rule));
            logger.info("[Goalias] Add Hot Rule [{}]", rule.getResource());
        }
    }
}
