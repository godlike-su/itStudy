package com.itStudy.controller.webSocket;

import com.alibaba.fastjson.JSONObject;
import com.itStudy.entity.Chat;
import com.itStudy.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSONObject
 * sendMessage 为发送的信息
 */
@ServerEndpoint("/message/webSocket/{userId}")
@Component
@Slf4j
public class messageWebSocket
{
    static ChatService chatService;

    @Autowired
    public void setChatService(ChatService chatService)
    {
        messageWebSocket.chatService = chatService;
    }

    /**
     * 存放所有在线的客户端
     */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    //当前发消息的人员编号
    private String userId = "";
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session WebSocketsession;

    @OnOpen
    public void onOpen(@PathParam(value = "userId") String param, Session session)
    {

        if (param == null && param.length() <= 0)
        {
            return;
        }

        this.userId = param;
        this.WebSocketsession = session;
        //将新用户存入在线的组
        clients.put(this.userId, this.WebSocketsession);
        log.info("有新的客户端连接了: {}", this.userId);
    }

    /**
     * 客户端关闭
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session)
    {
        //将掉线的用户移除在线的组里
        clients.remove(this.userId);
        log.info("有用户断开了, id为:{}", this.userId);
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable)
    {
        throwable.printStackTrace();
    }

    /**
     * 收到客户端发来消息
     *
     * @param message 消息对象
     */
    @OnMessage
    public void onMessage(String message)
    {
        JSONObject jreq = JSONObject.parseObject(message);
//        log.info("服务端收到客户端发来的消息: {}", jreq);
        boolean publics = jreq.getBooleanValue("publics");
        //给指定人发信息
        if (publics)
        {
            //群发信息
            this.sendAll(message);
        } else
        {
            this.sendToUser(message);
        }
    }

    /**
     * 指定人发消息
     *
     * @param message 消息内容
     */
    private void sendToUser(String message)
    {
        JSONObject jreq = JSONObject.parseObject(message);
        String sendMessage = jreq.getString("sendMessage");
        String sendUserId = jreq.getString("sendUserId");
        String receiveUserId = jreq.getString("receiveUserId");
        Date sendTime = new Date();

        Chat chat = JSONObject.parseObject(jreq.toString(), Chat.class);
        chat.setPublics((byte) 0);
        chat.setSendTime(new Date());
        chat.setImg1("");
        try
        {
            if (clients.get(sendUserId) != null)
            {
                //接收人是否在线判断
                if(clients.get(receiveUserId) != null)
                {
                    JSONObject data = new JSONObject(true);
                    data.put("sendUserId", this.userId);
                    data.put("receiveUserId", receiveUserId);
                    data.put("sendMessage", sendMessage);
                    data.put("sendTime", sendTime);
                    clients.get(receiveUserId).getAsyncRemote().sendText(JSONObject.toJSONString(data));
                    if (!chat.getSendMessage().equals("::connect::"))
                    {
                        chat.setStatus((byte) 0);
                        chatService.saveChat(chat);
                    }
                }
                else
                {
                    //不在线处理
                    //存入数据库，未读数据+1
                    chat.setStatus((byte) 1);
                    chatService.saveChat(chat);
                }
            }
        } catch (Exception e)
        {
            log.error("sendToUser: " + e.getMessage());
        }

    }

    /**
     * 群发消息
     *
     * @param message 消息内容
     */
    private void sendAll(String message)
    {
        JSONObject jreq = JSONObject.parseObject(message);
        String sendMessage = jreq.getString("sendMessage");
        Date sendTime = new Date();
        //传送的数据
        JSONObject data = new JSONObject(true);
        data.put("sendMessage", sendMessage);
        data.put("sendTime", sendTime);
        //遍历HashMap
        for (String key : clients.keySet())
        {
            try
            {
                //判断接收用户是否是当前发消息的用户
                if (!userId.equals(key))
                {
                    clients.get(key).getBasicRemote().sendText(JSONObject.toJSONString(data));
                }
            } catch (Exception e)
            {
                e.getMessage();
            }
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getNowTime()
    {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }


}
