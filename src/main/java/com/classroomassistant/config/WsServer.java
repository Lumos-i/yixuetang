package com.classroomassistant.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zrq
 * @ClassName WsSever
 * @date 2022/11/2 17:29
 * @Description TODO
 */

@Component
@Slf4j
@ServerEndpoint("/webSocket/{bizid}")
public class WsServer {

    private static final Map<String, Set<Session>> bizids = new ConcurrentHashMap();

    @OnOpen
    public void connect(@PathParam("bizid") String bizid, Session session) throws Exception {
        // 将session按照视频id进行存储
        if (!bizids.containsKey(bizid)) {
            // 判断视频id是否存在，不存在则添加
            Set<Session> video = new HashSet<>();
            // 添加用户
            video.add(session);
            bizids.put(bizid, video);
        } else {
            // 视频id，已存在直接将用户添加进去
            bizids.get(bizid).add(session);
        }
    }

    @OnClose
    public void disConnect(@PathParam("bizid") String bizid, Session session) {
        bizids.get(bizid).remove(session);
    }

    @OnMessage
    public void receiveMsg(@PathParam("bizid") String bizid,
                           String msg) throws Exception {
        // 推送消息
        broadcast(bizid, msg);
    }

    // 按照视频id进行推送
    public static void broadcast(String bizid, String msg) throws Exception {
        for (Session session : bizids.get(bizid)) {
            session.getBasicRemote().sendText(msg);
        }
    }

}