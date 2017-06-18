package io.github.k_gregory.insurance.service.consult;

import io.github.k_gregory.insurance.service.consult.data.SupportMessage;
import io.github.k_gregory.insurance.service.consult.impl.ConsultClient;
import io.github.k_gregory.insurance.service.consult.impl.ConsultSupporter;


import java.util.List;
import java.util.Set;

public interface ConsultMediator {
    void connectClient(ConsultClient client);
    void acceptClientQuestion(ConsultClient client, String message);
    void disconnectClient(ConsultClient client);

    void connectSupporter(ConsultSupporter supporter);
    Set<String> listFreeClients();
    void acceptClient(ConsultSupporter supporter, String clientId);
    void unacceptClient(ConsultSupporter supporter, String clientId);
    List<SupportMessage> listClientMessages(String clientId);
    void acceptSupporterAnswer(ConsultSupporter supporter, String to, String msg);
    void disconnectSupporter(ConsultSupporter supporter);
}
