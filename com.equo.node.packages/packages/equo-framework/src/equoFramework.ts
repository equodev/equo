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

}