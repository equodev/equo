window.equo = window.equo || {};

(function(equo) {

	equo.LOG_LEVEL_OFF = 'OFF';
	equo.LOG_LEVEL_ERROR = 'ERROR';
	equo.LOG_LEVEL_WARN = 'WARN';
	equo.LOG_LEVEL_INFO = 'INFO';
	equo.LOG_LEVEL_DEBUG = 'DEBUG';
	equo.LOG_LEVEL_TRACE = 'TRACE';
	equo.LOG_LEVEL_ALL = 'ALL';

	// Level use to disable special logger level for javascript and use the global level
	equo.LOG_LEVEL_NOT_CONFIGURED = 'NOT CONFIGURED';

	let sendLog = function(message, type) {
		let payload = {}
		payload.message = message;
		payload.type = type;
		equo.sendToWebSocketServer('loggingEvent', payload);
	}

	let returnResponse = function(callback) {
		equo.on('loggingResponseEvent', callback);
	}

	equo.logInfo = function(message) {
		sendLog(message, 'info');
	};

	equo.logError = function(message) {
		sendLog(message, 'error');
	};

	equo.logWarn = function(message) {
		sendLog(message, 'warning');
	};

	equo.logDebug = function(message) {
		sendLog(message, 'debug');
	};

	equo.logTrace = function(message) {
		sendLog(message, 'trace');
	};

	equo.getJsLoggerLevel = function(callback) {
		returnResponse(callback);
		sendLog('', 'getLevel');
	}

	equo.setJsLoggerLevel = function(level) {
		sendLog(level, 'setLevel');
	}

	equo.getGlobalLoggerLevel = function(callback) {
		returnResponse(callback);
		sendLog('', 'getGlobalLevel');
	}

	equo.setGlobalLoggerLevel = function(level) {
		sendLog(level, 'setGlobalLevel');
	}

}(equo));
