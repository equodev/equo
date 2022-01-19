/****************************************************************************
 **
 ** Copyright (C) 2021 Equo
 **
 ** This file is part of the Equo SDK.
 **
 ** Commercial License Usage
 ** Licensees holding valid commercial Equo licenses may use this file in
 ** accordance with the commercial license agreement provided with the
 ** Software or, alternatively, in accordance with the terms contained in
 ** a written agreement between you and Equo. For licensing terms
 ** and conditions see https://www.equo.dev/terms.
 **
 ** GNU General Public License Usage
 ** Alternatively, this file may be used under the terms of the GNU
 ** General Public License version 3 as published by the Free Software
 ** Foundation. Please review the following
 ** information to ensure the GNU General Public License requirements will
 ** be met: https://www.gnu.org/licenses/gpl-3.0.html.
 **
 ****************************************************************************/

import { EquoComm, EquoCommService } from '@equo/comm'

export interface LogPayload {
  message: string
  type: string
}
/**
 * @namespace
 * @description Equo SDK Javascript API.
 * Configure logs levels using ***equo-logging***
 */

const LOG_EVENTS = {
  LOGGING_EVENT: 'loggingEvent',
  LOGGING_RESPONSE_EVENT: 'loggingResponseEvent'
}

const LOG_TYPES = {
  INFO: 'info',
  ERROR: 'error',
  WARNING: 'warning',
  DEBUG: 'debug',
  TRACE: 'trace',
  GET_LEVEL: 'getLevel',
  SET_LEVEL: 'setLevel',
  GET_GLOBAL_LEVEL: 'getGlobalLevel',
  SET_GLOBAL_LEVEL: 'setGlobalLevel'
}

export namespace EquoLoggingService {
  const comm: EquoComm = EquoCommService.get()

  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_OFF = 'OFF'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_ERROR = 'ERROR'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_WARN = 'WARN'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_INFO = 'INFO'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_DEBUG = 'DEBUG'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_TRACE = 'TRACE'
  /**
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_ALL = 'ALL'
  /**
     * Level use to disable special logger level for javascript and use the
     * global level
     * @constant
     * @type {string}
     * @default
     */
  export const LOG_LEVEL_NOT_CONFIGURED = 'NOT CONFIGURED'

  const sendLog = function (message: string, type: string): void {
    const payload: LogPayload = {
      message,
      type
    }
    comm.send(LOG_EVENTS.LOGGING_EVENT, payload)
  }

  const returnResponse = function (callback: Function): void {
    comm.on(LOG_EVENTS.LOGGING_RESPONSE_EVENT, callback)
  }

  /**
     * Log a message with ***info*** level.
     * @function
     * @name logInfo
     * @param {string} message
     * @returns {void}
     */
  export function logInfo(message: string): void {
    sendLog(message, LOG_TYPES.INFO)
  }
  /**
     * Log a message with ***error*** level.
     * @function
     * @name logError
     * @param {string} message
     * @returns {void}
     */
  export function logError(message: string): void {
    sendLog(message, LOG_TYPES.ERROR)
  }
  /**
     * Log a message with ***warn*** level.
     * @function
     * @name logWarn
     * @param {string} message
     * @returns {void}
     */
  export function logWarn(message: string): void {
    sendLog(message, LOG_TYPES.WARNING)
  }
  /**
     * Log a message with ***debug*** level.
     * @function
     * @name logDebug
     * @param {string} message
     * @returns {void}
     */
  export function logDebug(message: string): void {
    sendLog(message, LOG_TYPES.DEBUG)
  }
  /**
     * Log a message with ***trace*** level.
     * @function
     * @name logTrace
     * @param {string} message
     * @returns {void}
     */
  export function logTrace(message: string): void {
    sendLog(message, LOG_TYPES.TRACE)
  }
  /**
     * Gets a custom level for javascript logs.
     * @function
     * @name getJsLoggerLevel
     * @param {Function} callback
     * @returns {void}
     */
  export function getJsLoggerLevel(callback: Function): void {
    returnResponse(callback)
    sendLog('', LOG_TYPES.GET_LEVEL)
  }
  /**
     * Sets a custom level for javascript logs. Javascrips logs will use this
     * level instead the global one. Disabled by default.
     * @default LOG_LEVEL_NOT_CONFIGURED
     * @function
     * @name setJsLoggerLevel
     * @param {string} level - Use constant log level.
     * @returns {void}
     */
  export function setJsLoggerLevel(level: string): void {
    sendLog(level, LOG_TYPES.SET_LEVEL)
  }
  /**
     * Gets a global log level.
     * @function
     * @name getGlobalLoggerLevel
     * @param {Function} callback
     * @returns {void}
     */
  export function getGlobalLoggerLevel(callback: Function): void {
    returnResponse(callback)
    sendLog('', LOG_TYPES.GET_GLOBAL_LEVEL)
  }
  /**
     * Sets a global log level.
     * @function
     * @name setGlobalLoggerLevel
     * @param {string} level - Use constant log level.
     * @returns {void}
     */
  export function setGlobalLoggerLevel(level: string): void {
    sendLog(level, LOG_TYPES.SET_GLOBAL_LEVEL)
  }
}
