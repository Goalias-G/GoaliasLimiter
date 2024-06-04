package com.tool.goalias.spring;

import com.tool.goalias.config.GoaliasProperty;
import com.tool.goalias.strategy.FallBackStrategy;
import com.tool.goalias.strategy.MethodHotStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GoaliasProperty.class)
public class GoaliasAutoConfiguration {

    @Bean
    public GoaliasScanner goaliasScanner()
    {
        return new GoaliasScanner();
    }
    @Bean
    public GoaliasProperty goaliasProperty(){
        return new GoaliasProperty();
    }
    @Bean
    public FallBackStrategy fallBackStrategy(){
        return new FallBackStrategy();
    }
    @Bean
    public MethodHotStrategy methodHotStrategy(){
        return new MethodHotStrategy();
    }
}
