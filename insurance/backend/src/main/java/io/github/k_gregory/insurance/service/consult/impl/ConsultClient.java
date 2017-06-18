package io.github.k_gregory.insurance.service.consult.impl;

import io.github.k_gregory.insurance.service.consult.ConsultCollegaue;
import io.github.k_gregory.insurance.service.consult.ConsultMediator;
import io.github.k_gregory.insurance.service.consult.data.FreeClientListNotification;
import io.github.k_gregory.insurance.service.consult.data.NewAnswerNotification;
import io.github.k_gregory.insurance.service.consult.data.SupportMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ConsultClient extends ConsultCollegaue {
    @Autowired
    protected ConsultClient(ConsultMediator mediator) {
        super(mediator);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String text = objectMapper.readTree(message.getPayload()).asText();
        mediator.acceptClientQuestion(this, text);
    }

    @Override
    public void disconnectFromMediator(ConsultMediator mediator) {
        mediator.disconnectClient(this);
    }

    @Override
    public void connectToMediator(ConsultMediator mediator) {
        mediator.connectClient(this);
    }
}
