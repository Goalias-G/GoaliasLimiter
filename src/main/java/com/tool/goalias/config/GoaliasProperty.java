package com.tool.goalias.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "goalias")
public class GoaliasProperty {
    private int hotCacheSeconds = 60;

    private boolean enableLog = true;

    public int getHotCacheSeconds() {
        return hotCacheSeconds;
    }

    public void setHotCacheSeconds(int hotCacheSeconds) {
        this.hotCacheSeconds = hotCacheSeconds;
    }

    public boolean isEnableLog() {
        return enableLog;
    }

    public void setEnableLog(boolean enableLog) {
        this.enableLog = enableLog;
    }
}
