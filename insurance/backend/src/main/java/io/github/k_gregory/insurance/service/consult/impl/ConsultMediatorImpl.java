package io.github.k_gregory.insurance.service.consult.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import io.github.k_gregory.insurance.service.consult.ConsultCollegaue;
import io.github.k_gregory.insurance.service.consult.ConsultMediator;
import io.github.k_gregory.insurance.service.consult.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsultMediatorImpl implements ConsultMediator {
    private final static Logger log = LoggerFactory.getLogger(ConsultMediatorImpl.class);

    private final Map<String, ConsultClient> clientIds = new HashMap<>();
    private final Set<ConsultClient> freeClients = new HashSet<>();
    private final Set<ConsultSupporter> supporters = new HashSet<>();
    private final ListMultimap<ConsultClient, SupportMessage> clientMessages = ArrayListMultimap.create();
    private final Map<ConsultClient, ConsultSupporter> clientConsulter = new HashMap<>();
    private final Multimap<ConsultSupporter, ConsultClient> consultClients = HashMultimap.create();


    @Override
    public synchronized void connectClient(ConsultClient client) {
        if (clientIds.put(client.getId(), client) != null) {
            log.warn("Added client that was in the list");
        }

        if (!freeClients.add(client)) {
            log.warn("New client was in the set");
        }

        NewClientNotification newClientNotification = new NewClientNotification(client.getId());
        for (ConsultSupporter s : supporters)
            try {
                s.sendNotification(newClientNotification);
            } catch (IOException e) {
                log.warn("Can't sendNotification supporter about connect", e);
            }

    }

    @Override
    public synchronized void acceptClientQuestion(ConsultClient client, String messageText) {
        SupportMessage message = new SupportMessage(messageText, false);
        clientMessages.put(client, message);

        NewQuestionNotification notification = new NewQuestionNotification(client.getId(), message);
        for(ConsultSupporter supporter: supporters)
        try {
            supporter.sendNotification(notification);

        } catch (IOException e) {
            log.warn("Can't sendNotification supporter about new question", e);
        }
    }

    @Override
    public synchronized void disconnectClient(ConsultClient client) {
        ConsultSupporter supporter = clientConsulter.get(client);

        for(ConsultSupporter s: supporters)
            try {
                s.sendNotification(new ClientDisconnectNotification(client.getId()));
            } catch (Throwable e) {
                log.warn("Can't sendNotification supporter about client disconnect", e);
            }

        if (supporter != null) { //Supporter was assign, client not free
            if (supporter != clientConsulter.remove(client))
                log.warn("Supporter was not assigned");
            if(!consultClients.remove(supporter, client))
                log.warn("Client was not assigned");
        } else { //Client was free
            if (!freeClients.remove(client))
                log.warn("Client was not in free set");
        }

        if (client != clientIds.remove(client.getId()))
            log.warn("Wrong ID mapping");

        clientMessages.removeAll(client);

    }

    @Override
    public synchronized void connectSupporter(ConsultSupporter supporter) {
        if (!supporters.add(supporter))
            log.warn("Supporter already was in the set");
    }

    @Override
    public synchronized Set<String> listFreeClients() {
        return freeClients
                .stream()
                .map(ConsultCollegaue::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public synchronized void acceptClient(ConsultSupporter supporter, String clientId) {
        ConsultClient client = clientIds.get(clientId);
        if (client == null) {
            log.warn("Supporter tried to accept non-existent client");
        } else if (!freeClients.contains(client)) {
            log.warn("Supporter tried to accept non-free client");
        } else {
            for(ConsultSupporter s : supporters)
                try {
                    s.sendNotification(new ClientAssignedNotification(clientId, s==supporter));
                } catch (IOException e) {
                    log.warn("Can't notify supporter about assign", e);
                }
            if (clientConsulter.put(client, supporter) != null)
                log.warn("Client already had supporter");
            if (!consultClients.put(supporter, client))
                log.warn("Client was already accepted");
            if (!freeClients.remove(client))
                log.warn("Client wasn't in free clientIds set");
        }

    }

    @Override
    public synchronized void unacceptClient(ConsultSupporter supporter, String clientId) {
        ConsultClient client = clientIds.get(clientId);
        if (client == null)
            log.warn("Supporter tried to un-accept non-existent client");
        else {
            NewClientNotification newClientNotification = new NewClientNotification(clientId);
            for(ConsultSupporter s : supporters)
                try {
                    s.sendNotification(newClientNotification);
                } catch (IOException e) {
                    log.warn("Can't notify supporter about new client", e);
                }
            if (supporter != clientConsulter.remove(client))
                log.warn("Client wasn't assign to supporter");
            if (!consultClients.remove(supporter, client))
                log.warn("Supporter wasn't assigned to client");
            if (!freeClients.add(client))
                log.warn("Client was in free clientIds set");
        }

    }

    @Override
    public synchronized List<SupportMessage> listClientMessages(String clientId) {
        ConsultClient client = clientIds.get(clientId);

        if (client == null) {
            log.warn("Client not found");
            return Collections.emptyList();
        }

        List<SupportMessage> messages = this.clientMessages.get(client);
        return Collections.unmodifiableList(messages);

    }

    @Override
    public synchronized void acceptSupporterAnswer(ConsultSupporter supporter, String to, String msg) {
        SupportMessage message = new SupportMessage(msg, true);
        ConsultClient client = clientIds.get(to);
        if (client == null) {
            log.warn("Supporter sent message to non-existent client");
        } else {
            NewAnswerNotification notification = new NewAnswerNotification(message);
            clientMessages.put(client, message);
            try {
                client.sendNotification(notification);
            } catch (IOException e) {
                log.warn("Can't sendNotification client about new message", e);
            }
        }

    }

    @Override
    public synchronized void disconnectSupporter(ConsultSupporter supporter) {
        Collection<ConsultClient> clients = consultClients.removeAll(supporter);
        freeClients.addAll(clients);
        for (ConsultClient client : clients) {
            NewClientNotification newClientNotification = new NewClientNotification(client.getId());
            if (supporter != clientConsulter.remove(client))
                log.warn("Client wasn't assigned to supporter");
            for(ConsultSupporter s : supporters)
                try {
                    s.sendNotification(newClientNotification);
                } catch (IOException e) {
                    log.warn("Can't notify about new client", e);
                }
        }
        if (!supporters.remove(supporter))
            log.warn("Supporter wasn't connected");

    }
}
