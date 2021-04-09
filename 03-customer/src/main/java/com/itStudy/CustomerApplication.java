package com.itStudy;

import com.itStudy.stream.StreamClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableEurekaClient //开启eureka
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.itStudy.dao")
@EnableScheduling
@EnableWebSocket  //开启WebSocket，打包成war包需要注释掉
public class CustomerApplication
{
//    @Bean
//    public PageHelper createPaeHelper(){
//        PageHelper page= new PageHelper();
//        return page;
//    }

    public static void main(String[] args)
    {
        SpringApplication.run(CustomerApplication.class, args);
    }


}
