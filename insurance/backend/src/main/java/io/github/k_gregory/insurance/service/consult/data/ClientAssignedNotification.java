package io.github.k_gregory.insurance.service.consult.data;

public class ClientAssignedNotification extends ConsultNotification {
    public final String clientId;
    public final boolean toRecv;

    public ClientAssignedNotification(String clientId, boolean toRecv) {
        super("client-assigned");
        this.clientId = clientId;
        this.toRecv = toRecv;
    }
}
