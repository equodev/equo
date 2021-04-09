import { EquoService } from '@equo/service-manager'

export class EquoWebSocket extends WebSocket {

    private userEventCallbacks: any = {};

    constructor(port: number) {
        super(`ws://127.0.0.1:${port}`);
    }

    private receiveMessage(event: any): void {
        if (event === undefined) {
            return;
        }
        try {
            let parsedPayload = JSON.parse(event);
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
    }

    onmessage = (event: any): void => {
        this.receiveMessage(event.data);
    };

    onclose = (event: any): void => {
    };


    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    private sendToWebSocketServer(actionId: any, browserParams?: any): void {
        // Wait until the state of the socket is not ready and send the message when it is...
        let receiveMessage = this.receiveMessage;
        this.waitForSocketConnection(this, () => {
            let event = JSON.stringify({
                action: actionId,
                params: browserParams
            });
            super.send(event);
            receiveMessage(event);
        });
    };

    // Make the function wait until the connection is made...
    private waitForSocketConnection(socket: EquoWebSocket, callback: Function): void {
        setTimeout(
            () => {
                if (socket.readyState === socket.OPEN) {
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

export namespace EquoWebSocketService {
    const WebsocketServiceId: string = 'equo-websocket';
    const queryParams: URLSearchParams = new URLSearchParams(window.location.search);
    const portS: string | null = queryParams.get("equowsport");
    const port: number = portS === null ? 0 : +portS;

    if (port === 0) {
        throw new Error("WebSocket port could not be found.");
    }

    export function create(): EquoService<EquoWebSocket> {
        return {
            id: WebsocketServiceId,
            service: new EquoWebSocket(port)
        };
    }
    export function get(): EquoWebSocket {
        return EquoService.get<EquoWebSocket>(WebsocketServiceId, create);
    }
}
