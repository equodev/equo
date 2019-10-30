window.equo = window.equo || {};

(function (equo) {

  equo.logInfo = function(payload) {
    equo.sendToWebSocketServer('loggingInfoEvent', payload);
  };

    equo.logError = function(payload) {
    equo.sendToWebSocketServer('loggingErrorEvent', payload);
  };

    equo.logWarning = function(payload) {
    equo.sendToWebSocketServer('loggingWarningEvent', payload);
  };

}(equo));
