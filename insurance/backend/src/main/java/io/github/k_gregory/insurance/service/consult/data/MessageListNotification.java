package io.github.k_gregory.insurance.service.consult.data;

import java.util.Collection;

public class MessageListNotification extends ConsultNotification{
    public final String clientId;
    public final Collection<SupportMessage> messages;
    public MessageListNotification(String clientId, Collection<SupportMessage> messages) {
        super("list-client-messages");
        this.clientId = clientId;
        this.messages = messages;
    }
}
