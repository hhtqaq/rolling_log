package com.ecjtu.hht.rolling_log.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hht
 * @date 2020/3/23 19:32
 */
@Component
@ServerEndpoint("/websocket/{sid}")
@Slf4j
public class WebSocketServer {

    /**
     * 当前在线连接数,ThreadSafe
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 线程安全set，用来存放每个客户端对应的MyWebSocket对象，ThreadSafe
     */
    private static CopyOnWriteArraySet<WebSocketServer> websocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端建立连接的会话  需要用它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收的sid
     */
    private String sid = "";


    /**
     * 连接建立成功调用的方法
     *
     * @param session 当前会话
     * @param sid     会话id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        websocketSet.add(this);
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + onlineCount.addAndGet(1));
    }

    /**
     * 与客户端断开连接时调用的方法
     */
    @OnClose
    public void onClose() {
        websocketSet.remove(this);
        //用的都是乐观锁cas
        log.info("有一个连接关闭，当前在线人数为" + onlineCount.decrementAndGet());
    }

    /**
     * 收到客户端消息后调用的方法，
     *
     * @param session 当前会话
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("收到来自窗口" + this.sid + "的信息" + message);
        //群发消息
        websocketSet.forEach(webSocketServer -> {
            try {
                webSocketServer.sendMessage(message);
            } catch (IOException e) {
                log.error("推送消息 '{}' 失败", message, e);
            }
        });
    }

    /**
     * @param session 当前会话
     * @param error   错误信息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误", error);
    }

    /**
     * 服务器主动推送
     * <p>
     * 抛出异常 由调用方自己决定异常如何处理。
     * 下层不知道具体的业务 向上抛
     *
     * @param message 推送内容
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     */
    public void sendCustomizeMessage(String message, @PathParam("sid") String sid) {
        log.info("推送消息给" + sid + ",推送内容为：" + message);
        //推送给指定sid消息
        websocketSet.stream().filter(webSocketServer -> webSocketServer.sid.equals(sid)).forEach(webSocketServer -> {
            try {
                webSocketServer.sendMessage(message);
            } catch (IOException e) {
                log.error("推送消息 '{}' 失败", message, e);
            }
        });
    }

}
