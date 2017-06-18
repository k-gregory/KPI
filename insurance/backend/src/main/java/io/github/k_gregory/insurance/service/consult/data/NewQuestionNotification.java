package io.github.k_gregory.insurance.service.consult.data;

public class NewQuestionNotification extends ConsultNotification{
    public final String clientId;
    public final SupportMessage message;

    public NewQuestionNotification(String clientId, SupportMessage message) {
        super("new-question");
        this.clientId = clientId;
        this.message = message;
    }
}
