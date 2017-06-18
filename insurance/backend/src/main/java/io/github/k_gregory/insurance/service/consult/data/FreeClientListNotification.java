package io.github.k_gregory.insurance.service.consult.data;

import java.util.Collection;

public class FreeClientListNotification extends ConsultNotification {
    public final Collection<String> clients;

    public FreeClientListNotification(Collection<String> clients) {
        super("client-list");
        this.clients = clients;
    }
}
