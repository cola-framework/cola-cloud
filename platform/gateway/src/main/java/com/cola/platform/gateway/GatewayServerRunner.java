package com.cola.platform.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by jiachen.shi on 7/7/2017.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class GatewayServerRunner {

    private static Logger logger = LoggerFactory.getLogger(GatewayServerRunner.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayServerRunner.class);
        app.run(args);
    }

}
