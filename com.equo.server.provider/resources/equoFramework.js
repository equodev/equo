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

}(equo));
