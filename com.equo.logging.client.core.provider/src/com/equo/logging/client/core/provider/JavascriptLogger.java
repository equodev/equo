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

package com.equo.logging.client.core.provider;

import java.util.Optional;
import java.util.function.Function;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.equo.comm.api.ICommService;
import com.equo.logging.client.api.Level;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerConfiguration;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Handles the logs made from javascript code.
 */
@Component
public class JavascriptLogger {
  protected static final Logger logger = LoggerFactory.getLogger(JavascriptLogger.class);
  private static final String LOGGING_EVENT_KEY = "loggingEvent";
  private static final String LOGGING_RESPONSE_EVENT_KEY = "loggingResponseEvent";

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  private ICommService commService;

  @Activate
  public void start() {
    commService.on(LOGGING_EVENT_KEY, JsonObject.class, new LoggingEventActionHandler());
  }

  private class LoggingEventActionHandler implements Function<JsonObject, String> {

    private static final String MESSAGE_LOG = "message";
    private static final String TYPE_LOG = "type";

    // Logging messages types
    private static final String TYPE_DEBUG = "debug";
    private static final String TYPE_INFO = "info";
    private static final String TYPE_WARNING = "warning";
    private static final String TYPE_ERROR = "error";
    private static final String TYPE_TRACE = "trace";

    private static final String TYPE_JS_ERROR = "jserror";

    // Logging configuration types
    private static final String TYPE_GET_LEVEL = "getLevel";
    private static final String TYPE_SET_LEVEL = "setLevel";
    private static final String TYPE_GET_GLOBAL_LEVEL = "getGlobalLevel";
    private static final String TYPE_SET_GLOBAL_LEVEL = "setGlobalLevel";

    @Override
    public String apply(JsonObject payload) {
      JsonElement messageJsonElement = payload.get(MESSAGE_LOG);
      JsonElement typeJsonElement = payload.get(TYPE_LOG);

      if ((typeJsonElement == null) || (messageJsonElement == null)) {
        throw new RuntimeException("A \"type\" nor \"message\" member which identified"
            + "the event name must be defined in the Logging object.");
      }
      String type = typeJsonElement.getAsString();
      String message = "";
      if (messageJsonElement.isJsonPrimitive()) {
        message = messageJsonElement.getAsString();
      }
      switch (type) {
        case TYPE_DEBUG:
          logger.debug(message);
          break;
        case TYPE_INFO:
          logger.info(message);
          break;
        case TYPE_WARNING:
          logger.warn(message);
          break;
        case TYPE_ERROR:
          logger.error(message);
          break;
        case TYPE_TRACE:
          logger.trace(message);
          break;

        case TYPE_SET_LEVEL:
          logger.setLoggerLevel(Level.toLevel(message, null));
          break;
        case TYPE_GET_LEVEL:
          String level = "NOT CONFIGURED";
          Optional<Level> loggerLevel = logger.getLoggerLevel();
          if (loggerLevel.isPresent()) {
            level = loggerLevel.get().toString();
          }
          return level;
        case TYPE_SET_GLOBAL_LEVEL:
          LoggerConfiguration.setGlobalLevel(Level.toLevel(message, null));
          break;
        case TYPE_GET_GLOBAL_LEVEL:
          return LoggerConfiguration.getGlobalLevel().toString();
        case TYPE_JS_ERROR:
          final JsonObject messageJsonObject = messageJsonElement.getAsJsonObject();
          final String msg = messageJsonObject.get("msg").getAsString();
          final String url = messageJsonObject.get("url").getAsString();
          final String lineNo = messageJsonObject.get("lineNo").getAsString();
          final String columnNo = messageJsonObject.get("columnNo").getAsString();
          logger.error(msg + ". URL: " + url + ". Line: " + lineNo + " Column: " + columnNo);
          break;
        default:
          logger.error("Incorrect log type from Equo Logging Javascript API");
      }
      return null;
    }
  }

}
