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
     * @param {string[]} browserParams - url, name, position.
     * @returns {void}
     */
    export function openBrowser(browserParams: any): void {
        websocket.send('openBrowser', browserParams);
    };
    /**
     * Updates a browser.
     * @function
     * @name updateBrowser
     * @param {string[]} browserParams - url, name.
     * @returns {void}
     */
    export function updateBrowser(browserParams: any): void {
        websocket.send('updateBrowser', browserParams);
    };

    /**
     * Adds a global shortcut binded to the given callback
     * @param shortcut 
     * @param callback 
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
     * @param shortcut 
     * @param event 
     */
    export function addShortcutToEvent(shortcut: string, event: string) {
		let payload = {
            shortcut: shortcut,
		    event: event
        }
        websocket.send('_addShortcut', payload);
    };
}