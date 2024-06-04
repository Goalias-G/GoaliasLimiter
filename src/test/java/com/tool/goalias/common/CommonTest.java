package com.tool.goalias.common;

import com.tool.goalias.common.bean.TestBean;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@TestPropertySource(value = "classpath:application.yml")
@SpringBootTest(classes = CommonTest.class)
@EnableAutoConfiguration
@ComponentScan({"com.tool.goalias.common.bean"})
@Component
public class CommonTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TestBean testBean;

    @Test
    public void test1(){
        for (int i = 0; i < 10; i++) {
            log.info(testBean.sayHi1("gws"));
        }
    }

    @Test
    public void test2(){
        for (int i = 0; i < 50; i++) {
            log.info(testBean.sayHi2("gws"));
        }
    }

    @Test
    public void test3(){
        for (int i = 0; i < 50; i++) {
            log.info(testBean.sayHi2("gws"+i));
        }
    }
    @Test
    public void test4(){
        System.out.println(testBean.test());
    }
}
