package com.tool.goalias.annotation;

import com.tool.goalias.enums.FlowGradeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GoaliasHot {

    FlowGradeEnum grade();

    int count();

    int duration();
}
