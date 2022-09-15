package com.example;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.example")
@PropertySource("classpath:sport.properties")
@PropertySource("classpath:mylogger.properties")
public class SportConfig {

}
