window.equo = window.equo || {};

(function (equo) {

    equo.openBrowser = function(browserParams) {
        equo.sendToWebSocketServer('openBrowser', browserParams);
    };

    equo.updateBrowser = function(browserParams) {
        equo.sendToWebSocketServer('updateBrowser', browserParams);
    };

    equo.addMenu = function(menuModel) {
        equo.sendToWebSocketServer('_setMenu', menuModel);
    };

    equo.addShortcut = function(shortcut, callback) {
		let payload = {
            shortcut: shortcut,
		    event: "_exec_shotcut_" + shortcut
        }
        equo.on(payload.event, callback);
        equo.sendToWebSocketServer('_addShortcut', payload);
    };

    equo.addShortcutToEvent = function(shortcut, event) {
		let payload = {
            shortcut: shortcut,
		    event: event
        }
        equo.sendToWebSocketServer('_addShortcut', payload);
    };

}(equo));
