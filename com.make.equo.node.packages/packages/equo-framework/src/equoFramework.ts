import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';
/**
 * @namespace
 * @description Equo Framework Javascript API.
 * Use el paquete ***framework***  para el manejo basico del browser.
 */
export namespace EquoFramework {
    var websocket: EquoWebSocket = EquoWebSocketService.get();
    /**
     * Initialize and open browser.
     * @function
     * @name openBrowser
     * @param {string[]} browserParams - url, name, position.
     * @returns {void}
     */
    export function openBrowser(browserParams: any): void {
        websocket.send('openBrowser', browserParams);
    };
    /**
     * Update browser.
     * @function
     * @name updateBrowser
     * @param {string[]} browserParams - url, name.
     * @returns {void}
     */
    export function updateBrowser(browserParams: any): void {
        websocket.send('updateBrowser', browserParams);
    };

}