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

import { EquoService } from '@equo/service-manager'

export class EquoComm {

    private userEventCallbacks: any = {};
    private ws?: WebSocket;
    private port: number;

    /**
     * @name EquoComm
     * @class
     */
    constructor(port: number) {
        this.port = port;
        this.openComm()
    }

    private openComm() {
        if (this.ws !== undefined && this.ws.readyState !== WebSocket.CLOSED){
            return;
        }
        this.ws = new WebSocket(`ws://127.0.0.1:${this.port}`);

        // Binds functions to the listeners for the comm.
        this.ws.onopen = (event: any): void => {
            // For reasons I can't determine, onopen gets called twice
            // and the first time event.data is undefined.
            // Leave a comment if you know the answer.
            if (event.data === undefined)
                return;
        }

        this.ws.onmessage = (event: any): void => {
            this.receiveMessage(event.data);
        };

        this.ws.onclose = (event: any): void => {
        };
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

    // Expose the below methods via the equo interface while
    // hiding the implementation of the method within the 
    // function() block
    private sendToCommServer(actionId: any, browserParams?: any): void {
        // Wait until the state of the comm is not ready and send the message when it is...
        let receiveMessage = this.receiveMessage;
        this.waitForCommConnection(this, () => {
            let event = JSON.stringify({
                action: actionId,
                params: browserParams
            });
            this.ws!.send(event);
            receiveMessage(event);
        });
    };

    // Make the function wait until the connection is made...
    private waitForCommConnection(comm: EquoComm, callback: Function): void {
        setTimeout(
            () => {
                if (this.ws !== undefined && this.ws!.readyState === WebSocket.OPEN) {
                    if (callback != null) {
                        callback();
                    }
                    return;
                } else {
		            try{
		                this.openComm();
                    }catch(err){}
                    comm.waitForCommConnection(comm, callback);
                }
            }, 5); // wait 5 milisecond for the connection...
    };
    /**
     * Sends an action to execute in Framework.
     * @param {string} actionId
     * @param {JSON} [payload] - Optional
     * @returns {void}
     */
    public send(actionId: any, payload?: any): void {
        this.sendToCommServer(actionId, payload);
    };
    /**
     * Listens for an event with the given name.
     * @param {string} userEvent
     * @param {Function} callback
     * @returns {void}
     */
    public on(userEvent: any, callback: Function) {
        this.userEventCallbacks[userEvent] = callback;
    };

}
/** 
 * @namespace
 * @description Comm API for usage within the Equo Framework.
 * 
 * This document specifies the usage methods for equo-comm.
 */
export namespace EquoCommService {
    const CommServiceId: string = 'equo-comm';
    const queryParams: URLSearchParams = new URLSearchParams(window.location.search);
    const portS: string | null = queryParams.get("equocommport");
    const port: number = portS === null ? 0 : +portS;

    if (port === 0) {
        throw new Error("Comm port could not be found.");
    }
    /**
     * Creates an EquoCommService instance.
     * @function
     * @name create
     * @returns {EquoService<EquoComm>}
     */
    export function create(): EquoService<EquoComm> {
        return {
            id: CommServiceId,
            service: new EquoComm(port)
        };
    }
    /**
     * Obtains existing EquoComm service instance or else create a new one.
     * @function
     * @name get
     * @returns {EquoComm}
     */
    export function get(): EquoComm {
        return EquoService.get<EquoComm>(CommServiceId, create);
    }
}
