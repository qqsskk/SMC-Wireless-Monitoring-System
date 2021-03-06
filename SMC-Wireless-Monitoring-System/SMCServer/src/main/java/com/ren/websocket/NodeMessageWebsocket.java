package com.ren.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/nodeWebSocket")
/**
 * 建立WS连接与我的客户端的浏览器之间
 * */
public class NodeMessageWebsocket {

    //建立连接的会话
    private javax.websocket.Session session = null;

    //加载监听线程
    MessageUpdateListener thread1 = new MessageUpdateListener();
    Thread thread = new Thread(thread1);

    //配置所有连接到此WebSocket节点的客户端的集合
    private static CopyOnWriteArraySet<NodeMessageWebsocket> nodeMessageWebsocketsSet = new CopyOnWriteArraySet<NodeMessageWebsocket>();

    //当连接建立的时候就开启监听线程
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        nodeMessageWebsocketsSet.add(this);
        thread.start();
    }

    //当关闭网页的时候关闭线程，同时在集合中移除此连接的客户端s
    @OnClose
    public void onClose() {
        thread1.stopMe();
        nodeMessageWebsocketsSet.remove(this);
    }

    //向客户端发送信息及指令
    @OnMessage
    public synchronized void onMessage(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @throws IOException 发送自定义信号，“1”表示告诉前台，数据库发生改变了，需要刷新
     *                     当没有发生变化的时候就正常输出
     */
    //如果在线程中已经判断出来是数据库已经发生了变化就会传入change的指令，同时向客户端发送“1”的指令告知
    //如果没有发生改变的话就直接正常的把数据中数据信息发送给客户端
    public void sendMessage(String message) throws IOException {
        if (message == "change") {
            for (NodeMessageWebsocket item : nodeMessageWebsocketsSet) {
                item.session.getBasicRemote().sendText("1");
            }
        } else {
            for (NodeMessageWebsocket item : nodeMessageWebsocketsSet) {
                item.session.getBasicRemote().sendText(message);
            }
        }

    }
}