package com.tool.goalias.annotation;

import com.tool.goalias.enums.FlowGradeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GoaliasFallback {

    FlowGradeEnum grade();

    int count();
}
