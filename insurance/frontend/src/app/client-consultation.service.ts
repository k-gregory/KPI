import { Injectable } from '@angular/core';
import {webSocketUrl} from 'util/urls'
import {Observable, Subject} from '@reactivex/rxjs';
import {BufferSendableState, ConnectionContext, WebSocketSendableState} from '../websocket/ConnectionContext';
import {NewAnswerNotification, SupportMessage} from '../consultation/ConsultationMessage';
import {JsonMessageFactory} from '../consultation/MessageFactory';

@Injectable()
export class ClientConsultationService {
  private wsUrl = webSocketUrl('/ws/consult/client');
  private ws: WebSocket;
  private wsCtx = new ConnectionContext();
  wsObservable: Observable<SupportMessage>;
  messages: SupportMessage[] = [];

  constructor() {
    this.wsCtx.setState(new BufferSendableState());

    const messageFactory = new JsonMessageFactory();

    this.ws = new WebSocket(this.wsUrl);
    this.ws.onopen = ev=>this.wsCtx.setState(new WebSocketSendableState(this.ws));

    this.wsObservable = Observable.create(observer=>{
      this.ws.onmessage = ev=>{console.log(ev.data);observer.next(ev.data);};
    }).map(msg=> (messageFactory.create(msg) as NewAnswerNotification).message);

    this.wsObservable.subscribe(msg=>this.messages.push(msg))
  }

  writeQuestion(msg: string){
    const consultationMessage = new SupportMessage(msg, false);
    this.messages.push(consultationMessage);
    this.wsCtx.write(msg);
  }
}
