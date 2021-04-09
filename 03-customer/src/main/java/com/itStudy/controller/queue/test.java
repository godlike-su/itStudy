package com.itStudy.controller.queue;

import com.itStudy.spring.AfRestData;
import com.itStudy.stream.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

//@Controller
//@EnableBinding(StreamClient.class)
public class test
{
    @Autowired
    private StreamClient streamClient;

    @GetMapping("/send")
    public Object send()
    {
        String serial = UUID.randomUUID().toString();
        streamClient.output().send(MessageBuilder.withPayload(serial).build());
        return new AfRestData(serial);
    }

}
