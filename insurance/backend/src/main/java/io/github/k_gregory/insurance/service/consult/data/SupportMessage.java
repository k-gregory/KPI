package io.github.k_gregory.insurance.service.consult.data;

public class SupportMessage extends ConsultNotification{
    public final String text;
    public final boolean fromSupport;

    public SupportMessage(String text, boolean fromSupport) {
        super("message");
        this.text = text;
        this.fromSupport = fromSupport;
    }
}
