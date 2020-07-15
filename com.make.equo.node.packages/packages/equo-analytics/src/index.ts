import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';

export namespace EquoAnalyticsService {
  var websocket: EquoWebSocket = EquoWebSocketService.get();

  export function registerEvent(payload: any): void {
    websocket.send('customEvent', payload);
  };

}
