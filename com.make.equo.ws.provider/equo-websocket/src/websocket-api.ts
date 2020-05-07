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

    private sendToWebSocketServer(action: any, browserParams?: any): void {
        // Wait until the state of the socket is not ready and send the message when it is...
        this.waitForSocketConnection(this, () => {
            this.send(JSON.stringify({
                action: action,
                params: browserParams
            }));
        });
    };

    // Make the function wait until the connection is made...
    private waitForSocketConnection(socket: EquoWebSocket, callback: Function) {
        setTimeout(
            () => {
                if (socket.readyState === socket.OPEN) {
                    console.log('Connection is made');
                    if (callback != null) {
                        callback();
                    }
                    return;
                } else {
                    this.waitForSocketConnection(socket, callback);
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

export interface EquoWebSocketService extends EquoService<EquoWebSocket> {
    name: string,
    service: EquoWebSocket
}

export namespace EquoWebSocketService {
    const WebsocketServiceName: string = 'equo-websocket';
    const EquoWebSocketService: EquoWebSocketService = {
        name: WebsocketServiceName,
        service: new EquoWebSocket()
    };
    export function get(): EquoWebSocketService {
        EquoService.install(EquoWebSocketService);
        return EquoService.get(WebsocketServiceName) as EquoWebSocketService;
    }
}
