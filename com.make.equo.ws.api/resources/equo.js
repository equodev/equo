window.equo = window.equo || {};

(function (equo) {

    let webSocket;
    let userEventCallbacks = {};

    var openSocket = function() {
        // Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
            console.log('WebSocket is already opened.');
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket('ws://127.0.0.1:9895');
         
        /**
         * Binds functions to the listeners for the websocket.
         */
        webSocket.onopen = function(event){
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if(event.data === undefined)
                return;

            console.log('event.data is...', event.data);
        };

        webSocket.onmessage = function(event){
            console.log('event.data is...', event.data);
            if(event.data === undefined) {
                return;
            }
            try {
                let parsedPayload = JSON.parse(event.data);
                let actionId = parsedPayload.action;
                if (actionId in userEventCallbacks) {
                    let params = parsedPayload.params;
                    if (parsedPayload.params) {
                        userEventCallbacks[actionId](params);
                    } else {
                        userEventCallbacks[actionId]();
                    }
                }
            } catch(err) {
                console.log('Error while trying to parse ', event.data, '. The error is ', err);
            }
        };

        webSocket.onclose = function(event){
            console.log('event.data is...', event.data);
        };
    }();

    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    var sendToWebSocketServer = function(action, browserParams) {
        // Wait until the state of the socket is not ready and send the message when it is...
        waitForSocketConnection(webSocket, function(){
            webSocket.send(JSON.stringify({
                action: action,
                params: browserParams
            }));
        });
    };

    equo.openBrowser = function(browserParams) {
        sendToWebSocketServer('openBrowser', browserParams);
    };

    equo.send = function(actionId) {
        sendToWebSocketServer(actionId);
    };

    equo.send = function(actionId, payload) {
        sendToWebSocketServer(actionId, payload);
    };

    equo.updateBrowser = function(browserParams) {
        sendToWebSocketServer('updateBrowser', browserParams);
    };

    // Make the function wait until the connection is made...
    var waitForSocketConnection = function(socket, callback){
        setTimeout(
            function () {
                if (socket.readyState === 1) {
                    console.log('Connection is made');
                    if(callback != null){
                        callback();
                    }
                    return;

                } else {
                    openSocket();
                    console.log('wait for connection...')
                    waitForSocketConnection(socket, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };

    equo.on = function(userEvent, callback) {
        userEventCallbacks[userEvent] = callback;
    };

}(equo));
