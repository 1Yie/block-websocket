package moe.ingstar.block_websocket.server;

import moe.ingstar.block_websocket.config.ConfigManager;
import moe.ingstar.block_websocket.config.ModConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import java.io.IOException;

public class WebSocketResponseHandler {
    private Server server;

    public void startWebSocketServer() throws Exception {

        ModConfig modConfig = ConfigManager.loadConfig();

        server = new Server(modConfig.serverPort);

        WebSocketHandler handler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(WebSocketServerEndpoint.class);
            }
        };

        server.setHandler(handler);
        server.start();
    }

    public void stopWebSocketServer() throws Exception {
        if (server != null) {
            server.stop();
            System.out.println("WebSocket服务器已停止");
        }
    }

    @WebSocket
    public static class WebSocketServerEndpoint {
        @OnWebSocketConnect
        public void onConnect(Session session) {
            System.out.println("WebSocket连接已建立");
        }

        @OnWebSocketMessage
        public void onMessage(Session session, String message) {
            System.out.println("收到WebSocket消息: " + message);

            if (message.equals("ping")) {
                // 处理ping请求
                String response = "pong";
                try {
                    session.getRemote().sendString(response);
                    System.out.println("发送ping响应: " + response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @OnWebSocketClose
        public void onClose(Session session, int statusCode, String reason) {
            System.out.println("WebSocket连接已关闭: " + statusCode + " - " + reason);
        }
    }
}