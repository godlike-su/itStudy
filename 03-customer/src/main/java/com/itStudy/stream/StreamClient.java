package com.itStudy.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient
{
    @Output("itStudyCustomer")
    MessageChannel output();  //发送信息

    @Input("itStudyCustomer")
    SubscribableChannel input();  //接受消息

}
