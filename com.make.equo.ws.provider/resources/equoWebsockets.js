window.equo = window.equo || {};

(function (equo) {

    let webSocket;
    let userEventCallbacks = {};

    const openSocket = function () {
        // Ensures only one connection is open at a time
        if (webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED) {
            console.log('WebSocket is already opened.');
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

            console.log('event.data is...', event.data);
        };

        webSocket.onmessage = function (event) {
            console.log('event.data is...', event.data);
            if (event.data === undefined) {
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

        webSocket.onclose = function (event) {
            console.log('event.data is...', event.data);
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

    equo.executeCommandWithCallback = function (callback, commandId, filePath = null, content = null) {
        let responseId = (Math.random() + 1).toString(36).substring(7);
        equo.on(responseId, callback);
        equo.sendToWebSocketServer("_executeEclipseCommand", {
            commandId: commandId,
            responseId: responseId,
            filePath: filePath,
            content: content
        });
    };

    equo.saveFile = function (filePath, content, callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.file.saveAs", filePath, content);
    };

    equo.renameFile = function (filePath, newName, callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.edit.rename", filePath, newName);
    };

    equo.moveFile = function (filePath, directoryDest, callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.edit.move", filePath, directoryDest);
    };

    equo.newFile = function () {
        equo.sendToWebSocketServer("_executeEclipseCommand", { commandId: "org.eclipse.ui.file.new" });
    };

    equo.openFile = function (callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.file.open");
    };

    equo.deleteFile = function (filePath, callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.file.delete", filePath);
    };

    equo.fileInfo = function (filePath, callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.file.properties", filePath);
    };

    equo.openProject = function (callback) {
        equo.executeCommandWithCallback(callback, "org.eclipse.ui.project.openProject");
    };

    equo.newEditor = function () {
        equo.sendToWebSocketServer("_executeEclipseCommand", { commandId: "org.eclipse.ui.window.newEditor" });
    };

    // Make the function wait until the connection is made...
    let waitForSocketConnection = function (socket, callback) {
        setTimeout(
            function () {
                if (socket.readyState === 1) {
                    console.log('Connection is made');
                    if (callback != null) {
                        callback();
                    }
                    return;

                } else {
                    try {
                        openSocket();
                    } catch (err) { }
                    console.log('wait for connection...')
                    waitForSocketConnection(socket, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };

}(equo));
