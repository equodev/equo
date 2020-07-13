import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';

export namespace EquoFramework {
    var websocket: EquoWebSocket = EquoWebSocketService.get();

    export function openBrowser(browserParams: any): void {
        websocket.send('openBrowser', browserParams);
    };

    export function updateBrowser(browserParams: any): void {
        websocket.send('updateBrowser', browserParams);
    };

}