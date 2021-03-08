window.equo = window.equo || {};

(function (equo) {

    let webSocket;
    let userEventCallbacks = {};

    const openSocket = function () {
        // Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
            Logger.debug('WebSocket is already opened.');
            return;
        }
        let wsPort = '%d';
        // Create a new instance of the websocket
        webSocket = new WebSocket('ws://127.0.0.1:' + wsPort);
        /**
         * Binds functions to the listeners for the websocket.
         */
        webSocket.onopen = function (event) {
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if (event.data === undefined)
                return;

            Logger.debug('event.data is...', event.data);
        };

        webSocket.onmessage = function(event){
            Logger.debug('event.data is...', event.data);
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
            } catch (err) {

            }
        };

        webSocket.onclose = function(event){
            Logger.debug('event.data is...', event.data);
        };
    }();

    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    equo.sendToWebSocketServer = function (action, browserParams) {
        // Wait until the state of the socket is not ready and send the message when it is...
        waitForSocketConnection(webSocket, function () {
            webSocket.send(JSON.stringify({
                action: action,
                params: browserParams
            }));
        });
    };

    equo.send = function (actionId) {
        equo.sendToWebSocketServer(actionId);
    };

    equo.send = function (actionId, payload) {
        equo.sendToWebSocketServer(actionId, payload);
    };

    equo.on = function (userEvent, callback) {
        userEventCallbacks[userEvent] = callback;
    };

    equo.executeCommand = function (callback, commandId, filePath = null, content = null) {
        let responseId = (Math.random() + 1).toString(36).substring(7);
        equo.on(responseId, callback);
        equo.sendToWebSocketServer("_executeEclipseCommand", {
            commandId: commandId + ".command",
            responseId: responseId,
            filePath: filePath,
            content: content
        });
    };

    // Make the function wait until the connection is made...
    let waitForSocketConnection = function (socket, callback) {
        setTimeout(
            function () {
                if (socket.readyState === 1) {
                    Logger.debug('Connection is made');
                    if(callback != null){
                        callback();
                    }
                    return;

                } else {
		            try{
		                openSocket();
                    }catch(err){}
                    Logger.debug('wait for connection...')
                    waitForSocketConnection(socket, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };

}(equo));
