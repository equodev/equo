/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

window.equo = window.equo || {};

(function (equo) {

    let comm;
    let userEventCallbacks = {};

    let receiveMessage = function(event){
        if(event === undefined) {
            return;
        }
        try {
            let parsedPayload = JSON.parse(event);
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

        }
    };

    const openComm = function() {
        // Ensures only one connection is open at a time
        if(comm !== undefined && comm.readyState !== WebSocket.CLOSED){
            //equo.logDebug('comm is already opened.');
            return;
        }
        let wsPort = '%d';
        // Create a new instance of the comm
        comm = new WebSocket('ws://127.0.0.1:' + wsPort);
        /**
         * Binds functions to the listeners for the comm.
         */
        comm.onopen = function(event){
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if(event.data === undefined)
                return;
        };

        comm.onmessage = function(event){
            receiveMessage(event.data);
        };

        comm.onclose = function(event){
        };
    }();

    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block

    equo.sendToCommServer = function(action, browserParams) {
        // Wait until the state of the comm is not ready and send the message when it is...
        waitForCommConnection(comm, function(){
            let event = JSON.stringify({
                action: action,
                params: browserParams
            });
            comm.send(event);
            receiveMessage(event);
        });
    };

    equo.send = function(actionId) {
        equo.sendToCommServer(actionId);
    };

    equo.send = function(actionId, payload) {
        equo.sendToCommServer(actionId, payload);
    };

    equo.on = function(userEvent, callback) {
        userEventCallbacks[userEvent] = callback;
    };

    // Make the function wait until the connection is made...
    let waitForCommConnection = function(comm, callback){
        setTimeout(
            function () {
                if (comm.readyState === 1) {
                    if(callback != null){
                        callback();
                    }
                    return;

                } else {
		            try{
		                openComm();
                    }catch(err){}
                    waitForCommConnection(comm, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };

}(equo));
