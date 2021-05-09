package com.itStudyService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableEurekaClient //开启eureka
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.itStudyService.dao")
@EnableScheduling
@EnableFeignClients
//@EnableWebSocket  //开启WebSocket，打包成war包需要注释掉
public class ServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }
}
