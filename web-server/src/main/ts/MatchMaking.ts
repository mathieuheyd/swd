/// <reference path="reference.ts" />

class MatchMaking {
  constructor(name: string, onGame: (gameId: string) => any) {
    let socket = new WebSocket("ws://localhost:8080/match");
    socket.onopen = (event: Event) => {
      console.log('Connected to Match Making');
      socket.send(name);
    }
    socket.onclose = (event: CloseEvent) => {
      console.log('Connection to Match Making closed');
    }
    socket.onmessage = (event: MessageEvent) => {
      console.log('New game found:', event.data);
      onGame(event.data);
      socket.close();
    }
  }
}