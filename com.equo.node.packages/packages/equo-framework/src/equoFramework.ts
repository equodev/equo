/* eslint-disable @typescript-eslint/no-namespace */
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

import { EquoComm, EquoCommService } from "@equo/comm";
/**
 * @namespace
 * @description Equo Framework Javascript API.
 * Use the ***framework*** package for basic browser management.
 */
export namespace EquoFramework {
    const comm: EquoComm = EquoCommService.get();

    // eslint-disable-next-line no-shadow
    const enum EQUO_EVENTS {
        OPEN_BROWSER = "openBrowser",
        UPDATE_BROWSER = "updateBrowser",
        ADD_SHORTCUT = "_addShortcut",
        REMOVE_SHORTCUT = "_removeShortcut",
    }

    /**
     * Initializes and opens a browser.
     * @function
     * @name openBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
    export function openBrowser(browserParams: BrowserParams): void {
        comm.send(EQUO_EVENTS.OPEN_BROWSER, browserParams);
    }
    /**
     * Updates a browser.
     * @function
     * @name updateBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
    export function updateBrowser(browserParams: BrowserParams): void {
        comm.send(EQUO_EVENTS.UPDATE_BROWSER, browserParams);
    }

    /**
     * Adds a global shortcut binded to the given callback
     * @function
     * @name addShortcut
     * @param {string} shortcut 
     * @param {Function} callback 
     * @returns {void}
     */
    export function addShortcut(shortcut: string, callback: Function): void {
		const payload = {
            shortcut,
		    event: `_exec_shotcut_${shortcut}`
        }
        comm.on(payload.event, callback);
        comm.send(EQUO_EVENTS.ADD_SHORTCUT, payload);
    }

    /**
     * Adds a global shortcut binded to a comm event
     * @function
     * @name addShortcutToEvent
     * @param {string} shortcut 
     * @param {string} event 
     * @returns {void}
     */
    export function addShortcutToEvent(shortcut: string, event: string): void {
		const payload = {
            shortcut,
		    event
        }
        comm.send(EQUO_EVENTS.ADD_SHORTCUT, payload);
    }

    /**
     * Removes a global shortcut
     * @function
     * @name removeShortcut
     * @param {string} shortcut
     * @returns {void}
     */
    export function removeShortcut(shortcut: string): void {
		const payload = {
            shortcut
        }
        comm.send(EQUO_EVENTS.REMOVE_SHORTCUT, payload);
    }
}

export class BrowserParams {
    private url: string;
    private name: string;
    private position: string;

    constructor(url: string, name: string, position: string) {
      this.url = url;
      this.name = name;
      this.position = position;
    }

    public getUrl(): string {
        return this.url;
    }

    public getName(): string {
        return this.name;
    }

    public getPosition(): string {
        return this.position;
    }
}
