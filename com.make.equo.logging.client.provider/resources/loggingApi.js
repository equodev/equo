window.equo = window.equo || {};

(function (equo) {


  equo.logInfo = function(payload) {
    if(payload === 'string'){
      let message = payload;
      let payload = {}
      payload.message = message;
    }
    payload.type = 'info';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logError = function(payload) {
    if(payload === 'string'){
      let message = payload;
      let payload = {}
      payload.message = message;
    }
    payload.type = 'error';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logWarn = function(payload) {
    if(payload === 'string'){
      let message = payload;
      let payload = {}
      payload.message = message;
    }
    payload.type = 'warning';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

/*  equo.logInfoMessage = function(message) {
    let payload = {}
    payload.message = message;
    payload.type = 'info';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logErrorMessage = function(message) {
    let payload = {}
    payload.message = message;
    payload.type = 'error';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };

  equo.logWarnMessage = function(message) {
    let payload = {}
    payload.message = message;
    payload.type = 'warning';
    equo.sendToWebSocketServer('loggingEvent', payload);
  };
  */

}(equo));
