package com.tool.goalias.spring;

import com.tool.goalias.config.GoaliasProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GoaliasProperty.class)
public class GoaliasAutoConfiguration {

}
