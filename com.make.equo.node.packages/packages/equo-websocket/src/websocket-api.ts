import { EquoService } from './services'

export class EquoWebSocket extends WebSocket {

    private userEventCallbacks: any = {};

    constructor() {
        super('ws://127.0.0.1:45454');
    }

    /**
     * Binds functions to the listeners for the websocket.
     */
    onopen = (event: any): void => {
        // For reasons I can't determine, onopen gets called twice
        // and the first time event.data is undefined.
        // Leave a comment if you know the answer.
        if (event.data === undefined)
            return;

        console.log('event.data is...', event.data);
    }

    onmessage = (event: any): void => {
        console.log('event.data is...', event.data);
        if (event.data === undefined) {
            return;
        }
        try {
            let parsedPayload = JSON.parse(event.data);
            let actionId = parsedPayload.action;
            if (actionId in this.userEventCallbacks) {
                let params = parsedPayload.params;
                if (parsedPayload.params) {
                    this.userEventCallbacks[actionId](params);
                } else {
                    this.userEventCallbacks[actionId]();
                }
            }
        } catch (err) {

        }
    };

    onclose = (event: any): void => {
        console.log('event.data is...', event.data);
    };


    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    private sendToWebSocketServer(actionId: any, browserParams?: any): void {
        // Wait until the state of the socket is not ready and send the message when it is...
        this.waitForSocketConnection(this, () => {
            super.send(JSON.stringify({
                action: actionId,
                params: browserParams
            }));
        });
    };

    // Make the function wait until the connection is made...
    private waitForSocketConnection(socket: EquoWebSocket, callback: Function): void {
        setTimeout(
            () => {
                if (socket.readyState === socket.OPEN) {
                    console.log('Connection is made');
                    if (callback != null) {
                        callback();
                    }
                    return;
                } else {
                    socket.waitForSocketConnection(socket, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };

    public send(actionId: any, payload?: any): void {
        this.sendToWebSocketServer(actionId, payload);
    };

    public on(userEvent: any, callback: Function) {
        this.userEventCallbacks[userEvent] = callback;
    };

}

export interface EquoWebSocketService extends EquoService {
    symbol: symbol,
    service: EquoWebSocket
}

export namespace EquoWebSocketService {
    const WebsocketServiceSymbol: symbol = Symbol('equo-websocket');
    export function create() {
        return {
            symbol: WebsocketServiceSymbol,
            service: new EquoWebSocket()
        };
    }
    export function get(): EquoWebSocketService {
        var webSocketService!: EquoWebSocketService;
        try {
            webSocketService = EquoService.get(WebsocketServiceSymbol) as EquoWebSocketService;
        } catch (e) {
            if (!webSocketService) {
                webSocketService = create();
                EquoService.install(webSocketService);
            }
        }
        return webSocketService;
    }
}
