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
/* global EquoCommService */

window.equo = window.equo || {};

(function (equo) {
  equo.LOG_LEVEL_OFF = 'OFF'
  equo.LOG_LEVEL_ERROR = 'ERROR'
  equo.LOG_LEVEL_WARN = 'WARN'
  equo.LOG_LEVEL_INFO = 'INFO'
  equo.LOG_LEVEL_DEBUG = 'DEBUG'
  equo.LOG_LEVEL_TRACE = 'TRACE'
  equo.LOG_LEVEL_ALL = 'ALL'

  // Level use to disable special logger level for javascript and use the global level
  equo.LOG_LEVEL_NOT_CONFIGURED = 'NOT CONFIGURED'

  const sendLog = function (message, type) {
    const payload = {}
    payload.message = message
    payload.type = type
    EquoCommService.send('loggingEvent', payload)
  }

  const returnResponse = function (callback) {
    equo.on('loggingResponseEvent', callback)
  }

  equo.logInfo = function (message) {
    sendLog(message, 'info')
  }

  equo.logError = function (message) {
    sendLog(message, 'error')
  }

  equo.logWarn = function (message) {
    sendLog(message, 'warning')
  }

  equo.logDebug = function (message) {
    sendLog(message, 'debug')
  }

  equo.logTrace = function (message) {
    sendLog(message, 'trace')
  }

  equo.getJsLoggerLevel = function (callback) {
    returnResponse(callback)
    sendLog('', 'getLevel')
  }

  equo.setJsLoggerLevel = function (level) {
    sendLog(level, 'setLevel')
  }

  equo.getGlobalLoggerLevel = function (callback) {
    returnResponse(callback)
    sendLog('', 'getGlobalLevel')
  }

  equo.setGlobalLoggerLevel = function (level) {
    sendLog(level, 'setGlobalLevel')
  }

  equo.error = function ({ msg, url, lineNo, columnNo, error }) {
    sendLog(
      {
        msg,
        url,
        lineNo,
        columnNo,
        error
      },
      'jserror'
    )
  }
})(window.equo)
