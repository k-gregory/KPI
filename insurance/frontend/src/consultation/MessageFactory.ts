import {
  ClientAssignedNotification,
  ClientDisconnectNotification, FreeClientListNotification, MessageListNotification, NewAnswerNotification, NewClientNotification,
  NewQuestionNotification,
  SupportMessage,
  WsNotification
} from './ConsultationMessage';
export interface MessageFactory{
  create(msg: string): WsNotification
}

export class JsonMessageFactory implements MessageFactory{
  create(msg: string): WsNotification {
    function getMessage(data: any): SupportMessage {
      return new SupportMessage(data.text, data.fromSupport);
    }
    const data = JSON.parse(msg);
    switch(data.tag){
      case "new-question":
        return new NewQuestionNotification(getMessage(data.message), data.clientId);
      case "new-client":
        return new NewClientNotification(data.clientId);
      case "new-answer":
        return new NewAnswerNotification(getMessage(data.message));
      case "client-disconnected":
        return new ClientDisconnectNotification(data.clientId);
      case "client-assigned":
        return new ClientAssignedNotification(data.clientId, data.toRecv);
      case "client-list":
        return new FreeClientListNotification(data.clients);
      case "list-client-messages":
        return new MessageListNotification(data.clientId, data.messages.map(getMessage));
    }
  }

}
