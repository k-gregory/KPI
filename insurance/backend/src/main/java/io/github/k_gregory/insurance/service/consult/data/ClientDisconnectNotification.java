package io.github.k_gregory.insurance.service.consult.data;

public class ClientDisconnectNotification extends ConsultNotification{
    public final String clientId;

    public ClientDisconnectNotification(String clientId) {
        super("client-disconnected");
        this.clientId = clientId;
    }
}
