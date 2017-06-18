package io.github.k_gregory.insurance.service.consult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.github.k_gregory.insurance.service.consult.data.ConsultNotification;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public abstract class ConsultCollegaue extends TextWebSocketHandler {
    protected WebSocketSession session;
    protected static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
    protected final ConsultMediator mediator;

    protected ConsultCollegaue(ConsultMediator mediator) {
        this.mediator = mediator;
    }

    public String getId(){
        return session.getId();
    }

    @Override
    public final void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        disconnectFromMediator(mediator);
    }
    public abstract void disconnectFromMediator(ConsultMediator mediator);

    @Override
    public final void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        connectToMediator(mediator);
    }

    public abstract void connectToMediator(ConsultMediator mediator);

    public void sendNotification(ConsultNotification o) throws IOException {
        if(!session.isOpen()) throw new IOException("Session already closed");
        String msg = writer.writeValueAsString(o);
        session.sendMessage(new TextMessage(msg));
    }
}
