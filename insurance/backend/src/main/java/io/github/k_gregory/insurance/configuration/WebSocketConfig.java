package io.github.k_gregory.insurance.configuration;

import io.github.k_gregory.insurance.configuration.custom.SecurityAwareWebSocketConfig;
import io.github.k_gregory.insurance.service.consult.impl.ConsultClient;
import io.github.k_gregory.insurance.service.consult.impl.ConsultSupporter;
import io.github.k_gregory.insurance.service.websocket.PingFilterWebSocketDecorator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;


@Configuration
@EnableWebSocket
public class WebSocketConfig extends SecurityAwareWebSocketConfig {
    private final
    BeanFactory beanFactory;

    @Autowired
    public WebSocketConfig(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void customizeWebSocketHandlers(WebSocketHandlerRegistry registry) {
        PerConnectionWebSocketHandler consultSupporter =
                new PerConnectionWebSocketHandler(ConsultSupporter.class);
        consultSupporter.setBeanFactory(beanFactory);

        PerConnectionWebSocketHandler consultClient =
                new PerConnectionWebSocketHandler(ConsultClient.class);
        consultClient.setBeanFactory(beanFactory);

        registry
                .addHandler(
                        new PingFilterWebSocketDecorator(consultSupporter),
                        "/ws/consult/support")
                .addHandler(
                        new PingFilterWebSocketDecorator(consultClient),
                        "/ws/consult/client"
                );
    }
}
