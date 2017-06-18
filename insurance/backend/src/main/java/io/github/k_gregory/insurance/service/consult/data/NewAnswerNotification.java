package io.github.k_gregory.insurance.service.consult.data;

public class NewAnswerNotification extends ConsultNotification{
    public final SupportMessage message;

    public NewAnswerNotification(SupportMessage message) {
        super("new-answer");
        this.message = message;
    }
}
