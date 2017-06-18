export function webSocketUrlRelative(s: string):string {
  let l = window.location;
  return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + l.pathname + s;
}

export function webSocketUrl(s: string):string {
  let l = window.location;
  return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + s;
}
