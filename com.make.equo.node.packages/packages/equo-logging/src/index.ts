import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';

export interface LogPayload {
	message: string;
	type: string;
}

export namespace EquoLoggingService {
	var websocket: EquoWebSocket = EquoWebSocketService.get();

	export const LOG_LEVEL_OFF = 'OFF';
	export const LOG_LEVEL_ERROR = 'ERROR';
	export const LOG_LEVEL_WARN = 'WARN';
	export const LOG_LEVEL_INFO = 'INFO';
	export const LOG_LEVEL_DEBUG = 'DEBUG';
	export const LOG_LEVEL_TRACE = 'TRACE';
	export const LOG_LEVEL_ALL = 'ALL';

	// Level use to disable special logger level for javascript and use the global level
	export const LOG_LEVEL_NOT_CONFIGURED = 'NOT CONFIGURED';

	function sendLog(message: string, type: string) {
		var payload: LogPayload = {
			message: message,
			type: type
		};
		websocket.send('loggingEvent', payload);
	}

	function returnResponse(callback: Function) {
		websocket.on('loggingResponseEvent', callback);
	}

	export function logInfo(message: string): void {
		sendLog(message, 'info');
	};

	export function logError(message: string): void {
		sendLog(message, 'error');
	};

	export function logWarn(message: string): void {
		sendLog(message, 'warning');
	};

	export function logDebug(message: string): void {
		sendLog(message, 'debug');
	};

	export function logTrace(message: string): void {
		sendLog(message, 'trace');
	};

	export function getJsLoggerLevel(callback: Function) {
		returnResponse(callback);
		sendLog('', 'getLevel');
	};

	export function setJsLoggerLevel(level: string) {
		sendLog(level, 'setLevel');
	};

	export function getGlobalLoggerLevel(callback: Function) {
		returnResponse(callback);
		sendLog('', 'getGlobalLevel');
	};

	export function setGlobalLoggerLevel(level: string) {
		sendLog(level, 'setGlobalLevel');
	};
}
