package io.github.k_gregory.insurance.configuration.custom;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

class AuthorizingWebSocketHandlerProxy implements WebSocketHandler {
    private final static MySessionRunnable authRun = (session, runnable) -> {
        Authentication originalAuth = SecurityContextHolder.getContext().getAuthentication();
        Authentication handlerAuth = (Authentication) session
                .getAttributes().get(BetterRegistration.AUTH_FIELD);
        SecurityContextHolder.getContext().setAuthentication(handlerAuth);

        try {
            runnable.run();
        } finally {
            SecurityContextHolder.getContext().setAuthentication(originalAuth);
        }
    };
    private final WebSocketHandler delegate;

    AuthorizingWebSocketHandlerProxy(WebSocketHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        authRun.run(session, () -> delegate.afterConnectionEstablished(session));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        authRun.run(session, () -> delegate.handleMessage(session, message));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        authRun.run(session, () -> delegate.handleTransportError(session, exception));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        authRun.run(session, () -> delegate.afterConnectionClosed(session, closeStatus));
    }

    @Override
    public boolean supportsPartialMessages() {
        return delegate.supportsPartialMessages();
    }

    @FunctionalInterface
    private interface MyRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface MySessionRunnable {
        void run(WebSocketSession session, MyRunnable runnable) throws Exception;
    }
}

class BetterRegistration implements WebSocketHandlerRegistration {
    static final String AUTH_FIELD = "_auth";
    private WebSocketHandlerRegistration original;

    public BetterRegistration(WebSocketHandlerRegistration original) {
        this.original = original.addInterceptors(new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                attributes.put(AUTH_FIELD, SecurityContextHolder.getContext().getAuthentication());
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        });
    }

    @Override
    public WebSocketHandlerRegistration addHandler(WebSocketHandler handler, String... paths) {
        original = original.addHandler(new AuthorizingWebSocketHandlerProxy(handler), paths);
        return original;
    }

    @Override
    public WebSocketHandlerRegistration setHandshakeHandler(HandshakeHandler handshakeHandler) {
        original = original.setHandshakeHandler(handshakeHandler);
        return original;
    }

    @Override
    public WebSocketHandlerRegistration addInterceptors(HandshakeInterceptor... interceptors) {
        original = original.addInterceptors(interceptors);
        return original;
    }

    @Override
    public WebSocketHandlerRegistration setAllowedOrigins(String... origins) {
        original = original.setAllowedOrigins(origins);
        return original;
    }

    @Override
    public SockJsServiceRegistration withSockJS() {
        return original.withSockJS();
    }
}

class BetterRegistry implements WebSocketHandlerRegistry {
    private WebSocketHandlerRegistry registry;

    BetterRegistry(WebSocketHandlerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public WebSocketHandlerRegistration addHandler(WebSocketHandler webSocketHandler, String... paths) {
        WebSocketHandlerRegistration original = registry.addHandler(
                new AuthorizingWebSocketHandlerProxy(webSocketHandler), paths
        );
        return new BetterRegistration(original);
    }
}

public abstract class SecurityAwareWebSocketConfig implements WebSocketConfigurer {
    @Override
    public final void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        customizeWebSocketHandlers(new BetterRegistry(registry));
    }

    public abstract void customizeWebSocketHandlers(WebSocketHandlerRegistry registry);
}
