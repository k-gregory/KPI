package io.github.k_gregory.insurance.service.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class PingFilterWebSocketDecorator extends WebSocketHandlerDecorator {
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if(message instanceof TextMessage){
            TextMessage msg = (TextMessage) message;
            if(msg.getPayload().equals("ping")) return;
        }
        getDelegate().handleMessage(session, message);
    }

    public PingFilterWebSocketDecorator(WebSocketHandler delegate) {
        super(delegate);
    }
}
