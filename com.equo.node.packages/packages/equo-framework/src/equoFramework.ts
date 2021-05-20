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