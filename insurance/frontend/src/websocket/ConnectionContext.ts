export interface SendableState{
  writeMessage(msg:any);
  replace(s: SendableState)
}

export class BufferSendableState implements SendableState{
  replace(s: SendableState) {
    this.buffer.forEach(msg=>s.writeMessage(msg))
  }

  buffer: any[] = [];

  writeMessage(msg: any) {
    this.buffer.push(msg)
  }
}

export class WebSocketSendableState implements SendableState{
  replace(s: SendableState) {
  }

  constructor(private ws: WebSocket){
    setInterval(()=>ws.send("ping"), 1000)
  }

  writeMessage(msg: any) {
    this.ws.send(msg)
  }

}

export class ConnectionContext{
  state: SendableState;

  setState(s: SendableState){
    if(this.state != null) this.state.replace(s);
    this.state = s;
  }

  write(msg: any){
    this.state.writeMessage(JSON.stringify(msg));
  }
}
