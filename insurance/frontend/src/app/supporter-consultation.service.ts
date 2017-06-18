import {Injectable} from '@angular/core';
import {webSocketUrl} from '../util/urls';
import {BufferSendableState, ConnectionContext, WebSocketSendableState} from '../websocket/ConnectionContext';
import {Observable} from '@reactivex/rxjs';
import {
  AnswerClientRequest,
  AssignClientRequest,
  ClientAssignedNotification,
  ClientDisconnectNotification, FreeClientListNotification, FreeClientListRequest, ListClientMessagesRequest, MessageListNotification,
  NewAnswerNotification, NewClientNotification, NewQuestionNotification, SupportMessage, UnassignClientRequest, WsNotification,
  WsNotificationVisitor
} from '../consultation/ConsultationMessage';
import {JsonMessageFactory} from '../consultation/MessageFactory';
import {isUndefined} from 'util';

export class ClientInfo{

  constructor(id: string) {
    this.id = id;
  }

  id: string;
  messages: SupportMessage[] = [];
  hasNewMessages: boolean = false;
  assigned: boolean = false;
  assignedToMe : boolean = false;
}

class SupporterVisitor implements WsNotificationVisitor{
  visitMessageListNotification(c: MessageListNotification) {
    this.s.clients[c.clientId].messages = c.messages;
  }

  constructor(private s: SupporterConsultationService){}

  visitNewQuestionNotification(c: NewQuestionNotification) {
    this.s.clients[c.clientId].messages.push(c.message);
    this.s.clients[c.clientId].hasNewMessages = true;
  }

  visitNewClientNotification(c: NewClientNotification) {
    this.s.clients[c.clientId] = new ClientInfo(c.clientId);
  }

  visitNewAnswerNotification(c: NewAnswerNotification) {
    throw new Error('Can\'t receive answers');
  }

  visitClientDisconnectNotification(c: ClientDisconnectNotification) {
    delete this.s.clients[c.clientId];
  }

  visitFreeClientListNotification(c: FreeClientListNotification) {
    c.clients.forEach(freeClientId=>{
      if(!isUndefined(this.s.clients[freeClientId])) {
        this.s.clients[freeClientId].assigned = false;
        this.s.clients[freeClientId].assignedToMe = false;
      } else this.s.clients[freeClientId] = new ClientInfo(freeClientId);
    })
  }

  visitClientAssignedNotification(c: ClientAssignedNotification) {
    this.s.clients[c.clientId].assigned = true;
    this.s.clients[c.clientId].assignedToMe = c.toRecv;
  }

}

@Injectable()
export class SupporterConsultationService {


  private wsUrl = webSocketUrl('/ws/consult/support');
  private wsCtx = new ConnectionContext();
  wsObservable: Observable<WsNotification>;
  clients: { [id: string]: ClientInfo} = {};
  selectedClient: string;

  private visitor = new SupporterVisitor(this);

  constructor() {
    this.wsCtx.setState(new BufferSendableState());

    const messageFactory = new JsonMessageFactory();

    const ws = new WebSocket(this.wsUrl);
    ws.onopen = ev=>this.wsCtx.setState(new WebSocketSendableState(ws));

    this.wsObservable = Observable.create(observer=>{
      ws.onmessage = ev=>observer.next(ev.data);
    }).map(msg=> messageFactory.create(msg));

    this.wsObservable.subscribe(n=>n.accept(this.visitor));
  }

  refreshFree(){
    this.wsCtx.write(new FreeClientListRequest);
  }

  selectClient(clientId: string){
    this.selectedClient = clientId;
    this.wsCtx.write(new ListClientMessagesRequest(clientId))
  }

  assignClient(clientId: string){
    this.selectedClient = clientId;
    this.wsCtx.write(new AssignClientRequest(clientId))
  }

  unassignClient(clientId: string){
    this.selectedClient = undefined;
    this.wsCtx.write(new UnassignClientRequest(clientId))
  }

  writeAnswer(message: string){
    const clientId = this.selectedClient;
    const supportMessage = new SupportMessage(message, true);
    this.clients[clientId].messages.push(supportMessage);
    this.wsCtx.write(new AnswerClientRequest(clientId, message));
  }
}
