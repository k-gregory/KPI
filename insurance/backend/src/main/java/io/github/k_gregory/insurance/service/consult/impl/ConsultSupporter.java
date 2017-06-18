package io.github.k_gregory.insurance.service.consult.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.k_gregory.insurance.service.consult.ConsultCollegaue;
import io.github.k_gregory.insurance.service.consult.ConsultMediator;
import io.github.k_gregory.insurance.service.consult.data.FreeClientListNotification;
import io.github.k_gregory.insurance.service.consult.data.MessageListNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ConsultSupporter extends ConsultCollegaue {
    @Autowired
    protected ConsultSupporter(ConsultMediator mediator) {
        super(mediator);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode rootNode = objectMapper.readTree(message.getPayload());
        String tag = rootNode.get("tag").asText();
        switch (tag){
            case "do-list-free": {
                sendNotification(new FreeClientListNotification(mediator.listFreeClients()));
                break;
            }
            case "do-assign-client": {
                mediator.acceptClient(this, rootNode.get("clientId").asText());
                break;
            }
            case "do-answer":{
                String text = rootNode.get("message").asText();
                String clientId = rootNode.get("clientId").asText();
                mediator.acceptSupporterAnswer(this, clientId, text);
                break;
            }
            case "do-list-client-messages": {
                String clientId = rootNode.get("clientId").asText();
                sendNotification(new MessageListNotification(clientId, mediator.listClientMessages(clientId)));
                break;
            }
            case "do-unassign-client":{
                String clientId = rootNode.get("clientId").asText();
                mediator.unacceptClient(this, clientId);
                break;
            }
        }
    }

    @Override
    public void disconnectFromMediator(ConsultMediator mediator) {
        mediator.disconnectSupporter(this);
    }

    @Override
    public void connectToMediator(ConsultMediator mediator) {
        mediator.connectSupporter(this);
    }
}
