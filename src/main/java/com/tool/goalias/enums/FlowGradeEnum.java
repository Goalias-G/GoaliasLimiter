package com.tool.goalias.enums;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;

public enum FlowGradeEnum {
    FLOW_GRADE_THREAD(RuleConstant.FLOW_GRADE_THREAD),
    FLOW_GRADE_QPS(RuleConstant.FLOW_GRADE_QPS);

    private int grade;

    FlowGradeEnum(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
