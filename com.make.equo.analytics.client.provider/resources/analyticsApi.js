window.equo = window.equo || {};

(function (equo) {

  equo.registerEvent = function(payload) {
    equo.sendToWebSocketServer('customEvent', payload);
  };

}(equo));
