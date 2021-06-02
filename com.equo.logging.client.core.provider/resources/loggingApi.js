/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

let oldOnError = window.onerror;
let console=(function(oldCons){
    return {
        log: function(text){
            oldCons.log(text);
            equo.logDebug(text);
        },
        info: function (text) {
            oldCons.info(text);
            equo.logInfo(text);
        },
        warn: function (text) {
            oldCons.warn(text);
            equo.logWarn(text);
        },
        error: function (text) {
            oldCons.error(text);
            equo.logError(text);
        }
    };
}(window.console));


window.console = console;
window.onerror = function (msg, url, lineNo, columnNo, error) {
	equo.error(msg, url, lineNo, columnNo, error);
	if (oldOnError) {
		return oldOnError(msg, url, lineNo, columnNo, error);
	}
	return false;
}

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

	equo.error = function (msg, url, lineNo, columnNo, error) {
		sendLog({ "msg": msg, "url": url, "lineNo": lineNo, "columnNo": columnNo, "error": error }, 'jserror');
	}

}(equo));
