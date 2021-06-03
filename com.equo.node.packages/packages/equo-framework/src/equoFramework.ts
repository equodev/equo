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

import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';
/**
 * @namespace
 * @description Equo Framework Javascript API.
 * Use the ***framework*** package for basic browser management.
 */
export namespace EquoFramework {
    var websocket: EquoWebSocket = EquoWebSocketService.get();
    /**
     * Initializes and opens a browser.
     * @function
     * @name openBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
    export function openBrowser(browserParams: BrowserParams): void {
        websocket.send('openBrowser', browserParams);
    };
    /**
     * Updates a browser.
     * @function
     * @name updateBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
    export function updateBrowser(browserParams: BrowserParams): void {
        websocket.send('updateBrowser', browserParams);
    };

    /**
     * Adds a global shortcut binded to the given callback
     * @function
     * @name addShortcut
     * @param {string} shortcut 
     * @param {Function} callback 
     * @returns {void}
     */
    export function addShortcut(shortcut: string, callback: Function) {
		let payload = {
            shortcut: shortcut,
		    event: "_exec_shotcut_" + shortcut
        }
        websocket.on(payload.event, callback);
        websocket.send('_addShortcut', payload);
    };

    /**
     * Adds a global shortcut binded to a websocket event
     * @function
     * @name addShortcutToEvent
     * @param {string} shortcut 
     * @param {string} event 
     * @returns {void}
     */
    export function addShortcutToEvent(shortcut: string, event: string) {
		let payload = {
            shortcut: shortcut,
		    event: event
        }
        websocket.send('_addShortcut', payload);
    };

    /**
     * Removes a global shortcut
     * @function
     * @name removeShortcut
     * @param {string} shortcut
     * @returns {void}
     */
    export function removeShortcut(shortcut: string) {
		let payload = {
            shortcut: shortcut
        }
        websocket.send('_removeShortcut', payload);
    };
}

export class BrowserParams {
    private url: string;
    private name: string;
    private position: string;

    constructor(url: string, name: string, position: string) {
      this.url = url;
      this.name = name;
      this.position = position;
    };

    public getUrl(): string {
        return this.url;
    };

    public getName(): string {
        return this.name;
    }

    public getPosition(): string {
        return this.position;
    }
}
