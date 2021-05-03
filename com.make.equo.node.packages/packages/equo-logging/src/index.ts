import { EquoWebSocket, EquoWebSocketService } from '@equo/websocket';

export interface LogPayload {
	message: string;
	type: string;
}
/**
 * @namespace
 * @description Equo Framework Javascript API.
 * Configure logs levels using ***equo-logging***
 */
export namespace EquoLoggingService {
	var websocket: EquoWebSocket = EquoWebSocketService.get();

	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_OFF = 'OFF';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_ERROR = 'ERROR';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_WARN = 'WARN';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_INFO = 'INFO';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_DEBUG = 'DEBUG';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_TRACE = 'TRACE';
	/**
	 * @constant
	 * @type {string}
	 * @default
	 */
	export const LOG_LEVEL_ALL = 'ALL';
	/**
	 * Level use to disable special logger level for javascript and use the global level
	 * @constant
	 * @type {string}
	 * @default
	 */
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

	/**
   * Log a message with ***info*** level.
   * @function
   * @name logInfo
	 * @param {string} message
   * @returns {void}
   */
	export function logInfo(message: string): void {
		sendLog(message, 'info');
	};
	/**
   * Log a message with ***error*** level.
   * @function
   * @name logError
	 * @param {string} message
   * @returns {void}
   */
	export function logError(message: string): void {
		sendLog(message, 'error');
	};
	/**
   * Log a message with ***warn*** level.
   * @function
   * @name logWarn
	 * @param {string} message
   * @returns {void}
   */
	export function logWarn(message: string): void {
		sendLog(message, 'warning');
	};
	/**
   * Log a message with ***debug*** level.
   * @function
   * @name logDebug
	 * @param {string} message
   * @returns {void}
   */
	export function logDebug(message: string): void {
		sendLog(message, 'debug');
	};
	/**
   * Log a message with ***trace*** level.
   * @function
   * @name logTrace
	 * @param {string} message
   * @returns {void}
   */
	export function logTrace(message: string): void {
		sendLog(message, 'trace');
	};
	/**
   * Gets a custom level for javascript logs.
   * @function
   * @name getJsLoggerLevel
	 * @param {Function} callback
   * @returns {void}
   */
	export function getJsLoggerLevel(callback: Function) {
		returnResponse(callback);
		sendLog('', 'getLevel');
	};
	/**
   * Sets a custom level for javascript logs.
	 * Javascrips logs will use this level instead the global one. Disabled by default.
	 * @default LOG_LEVEL_NOT_CONFIGURED
   * @function
   * @name setJsLoggerLevel
	 * @param {string} level - Use constant log level.
   * @returns {void}
   */
	export function setJsLoggerLevel(level: string) {
		sendLog(level, 'setLevel');
	};
	/**
   * Gets a global log level.
   * @function
   * @name getGlobalLoggerLevel
	 * @param {Function} callback
   * @returns {void}
   */
	export function getGlobalLoggerLevel(callback: Function) {
		returnResponse(callback);
		sendLog('', 'getGlobalLevel');
	};
	/**
   * Sets a global log level.
   * @function
   * @name setGlobalLoggerLevel
	 * @param {string} level - Use constant log level.
   * @returns {void}
   */
	export function setGlobalLoggerLevel(level: string) {
		sendLog(level, 'setGlobalLevel');
	};
}
