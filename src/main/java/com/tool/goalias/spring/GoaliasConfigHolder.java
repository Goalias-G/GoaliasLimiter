package com.tool.goalias.spring;

import com.tool.goalias.config.GoaliasProperty;

public class GoaliasConfigHolder {
    private static GoaliasProperty goaliasProperty;

    public static GoaliasProperty getGoaliasProperty() {
        return goaliasProperty;
    }

    public static void setGoaliasProperty(GoaliasProperty goaliasProperty) {
        GoaliasConfigHolder.goaliasProperty = goaliasProperty;
    }
}
