export interface WsNotificationVisitor{
  visitNewQuestionNotification(c: NewQuestionNotification);
  visitNewClientNotification(c: NewClientNotification);
  visitNewAnswerNotification(c: NewAnswerNotification);
  visitClientDisconnectNotification(c: ClientDisconnectNotification);
  visitFreeClientListNotification(c: FreeClientListNotification);
  visitClientAssignedNotification(c: ClientAssignedNotification);
  visitMessageListNotification(c: MessageListNotification);
}

export interface WsNotification{
  accept(visitor: WsNotificationVisitor)
}

export class SupportMessage{
  text: string;
  fromSupport: boolean;

  constructor(text: string, fromSupport: boolean) {
    this.text = text;
    this.fromSupport = fromSupport;
  }
}

export class NewQuestionNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitNewQuestionNotification(this)
  }

  message: SupportMessage;
  clientId: string;

  constructor(message: SupportMessage, clientId: string) {
    this.message = message;
    this.clientId = clientId;
  }
}

export class NewClientNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitNewClientNotification(this)
  }
  clientId: string;

  constructor(clientId: string) {
    this.clientId = clientId;
  }
}

export class NewAnswerNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitNewAnswerNotification(this)
  }

  message: SupportMessage;

  constructor(message: SupportMessage) {
    this.message = message;
  }
}

export class ClientDisconnectNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitClientDisconnectNotification(this)
  }

  clientId: string;

  constructor(clientId: string) {
    this.clientId = clientId;
  }
}

export class FreeClientListNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitFreeClientListNotification(this)
  }

  clients: string[];

  constructor(clients: string[]) {
    this.clients = clients;
  }
}

export class ClientAssignedNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitClientAssignedNotification(this)
  }

  clientId: string;
  toRecv: boolean;

  constructor(clientId: string, toRecv:boolean) {
    this.clientId = clientId;
    this.toRecv = toRecv;
  }
}

export class MessageListNotification implements WsNotification{
  accept(visitor: WsNotificationVisitor) {
    visitor.visitMessageListNotification(this);
  }

  clientId: string;
  messages: SupportMessage[];

  constructor(clientId: string, messages: SupportMessage[]) {
    this.clientId = clientId;
    this.messages = messages;
  }
}

export class WsRequest{
  tag: String;

  constructor(tag: String) {
    this.tag = tag;
  }
}

export class FreeClientListRequest extends WsRequest{
  constructor(){
    super('do-list-free');
  }
}

export class AssignClientRequest extends WsRequest{
  constructor(clientId: string) {
    super('do-assign-client');
    this.clientId = clientId;
  }
  clientId: string;
}

export class AnswerClientRequest extends WsRequest{
  clientId: string;
  message: string;

  constructor(clientId: string, message: string) {
    super('do-answer');
    this.clientId = clientId;
    this.message = message;
  }
}

export class ListClientMessagesRequest extends WsRequest{
  clientId: string;

  constructor(clientId: string) {
    super('do-list-client-messages');
    this.clientId = clientId;
  }
}

export class UnassignClientRequest extends WsRequest{
  clientId: string;
  constructor(cliendId: string){
    super("do-unassign-client");
    this.clientId = cliendId;
  }
}
