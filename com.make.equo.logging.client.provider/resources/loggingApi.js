window.equo = window.equo || {};

(function (equo) {

  equo.logInfo = function(payload) {
  	payload.type = 'info';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logError = function(payload) {
    payload.type = 'error';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logWarn = function(payload) {
    payload.type = 'warning';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

}(equo));
