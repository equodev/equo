window.equo = window.equo || {};

(function (equo) {

  equo.logCrash = function(payload) {
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

}(equo));
