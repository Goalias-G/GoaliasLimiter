package com.tool.goalias.common.bean;

import com.tool.goalias.annotation.GoaliasFallback;
import com.tool.goalias.annotation.GoaliasHot;
import com.tool.goalias.config.GoaliasProperty;
import com.tool.goalias.enums.FlowGradeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TestBean {
    @Resource
    private GoaliasProperty goaliasProperty;

    @GoaliasFallback(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 2)
    public String sayHi1(String name){
        return "hi,"+name;
    }

    @GoaliasHot(grade = FlowGradeEnum.FLOW_GRADE_QPS, count = 8, duration = 2)
    public String sayHi2(String name){
        try {
            Thread.sleep(200);
        }catch (Exception ignored){
        }
        return "hi,"+name;
    }


    public String sayHi1Fallback(String name){
        return "fallback return";
    }
    public int test(){
        return goaliasProperty.getHotCacheSeconds();
    }
}
