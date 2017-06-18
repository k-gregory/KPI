package io.github.k_gregory.insurance.service.consult.data;

public class NewClientNotification extends ConsultNotification{
    public final String clientId;

    public NewClientNotification(String clientId) {
        super("new-client");
        this.clientId = clientId;
    }
}
