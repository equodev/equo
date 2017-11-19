window.equo = window.equo || {};

(function (equo) {

    var webSocket;

    var openSocket = function() {
        // Ensures only one connection is open at a time
        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
            console.log("WebSocket is already opened.");
            return;
        }
        // Create a new instance of the websocket
        webSocket = new WebSocket("ws://127.0.0.1:9895");
         
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
            console.log('Receive Java messages here...');
        };

        webSocket.onclose = function(event){
            console.log('event.data is...', event.data);
        };
    }();

    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    equo.openBrowser = function(url) {
        console.log('url is ' + url);
        // Wait until the state of the socket is not ready and send the message when it is...
        waitForSocketConnection(ws, function(){
            webSocket.send(JSON.stringify({
                action: 'openBrowser',
                url: url
            }));
        });
    };

    // Make the function wait until the connection is made...
    var waitForSocketConnection = function(socket, callback){
        setTimeout(
            function () {
                if (socket.readyState === 1) {
                    console.log("Connection is made");
                    if(callback != null){
                        callback();
                    }
                    return;

                } else {
                    openSocket();
                    console.log("wait for connection...")
                    waitForSocketConnection(socket, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    }

    // equo.openBrowser = function(name, url) {
    //     console.log('url is ' + url);
    //     webSocket.send(JSON.stringify({
    //         action: openBrowser,
    //         name: name,
    //         url: url
    //     }));
    // };

    equo.each = function(collection, iterator) {
        if (Array.isArray(collection)) {
            for (var i = 0; i < collection.length; i++) {
            iterator(collection[i], i, collection);
            }
        } else {
            for (var key in collection) {
            iterator(collection[key], key, collection);
            }
        }
    };

    equo.filter = function(collection, test) {
        var filtered = [];
        equo.each(collection, function(item) {
            if (test(item)) {
            filtered.push(item);
            }
        });
        return filtered;
    };

    equo.map = function(collection, iterator) {
        var mapped = [];
        globalUtils.each(collection, function(value, key, collection) {
            mapped.push(iterator(value));
        });
        return mapped;
    };

    equo.reduce = function(collection, iterator, accumulator) {
        var startingValueMissing = accumulator === undefined;

        equo.each(collection, function(item) {
            if(startingValueMissing) {
            accumulator = item;
            startingValueMissing = false;
            } else {
            accumulator = iterator(accumulator, item);
            }
        });

        return accumulator;

    };

}(equo));
