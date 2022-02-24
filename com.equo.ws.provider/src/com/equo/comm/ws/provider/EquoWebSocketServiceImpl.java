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

package com.equo.comm.ws.provider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.equo.comm.api.IEquoCommService;
import com.equo.comm.api.NamedActionMessage;
import com.equo.comm.api.actions.IActionHandler;
import com.equo.comm.api.annotations.EventName;
import com.equo.comm.api.util.ActionHelper;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.Gson;

/**
 * Websocket service implementation. Manages the websocket server lifecycle and
 * all the event listeners.
 */
@Component
public class EquoWebSocketServiceImpl implements IEquoCommService {
  protected static final Logger logger = LoggerFactory.getLogger(EquoWebSocketServiceImpl.class);

  private Gson gsonParser = new Gson();

  private Map<String, Function<?, ?>> functionActionHandlers = new HashMap<>();
  private Map<String, Consumer<?>> consumerActionHandlers = new HashMap<>();
  private Map<String, CompletableFuture<Object>> responseActionHandlers = new HashMap<>();
  private Map<String, Class<?>> actionParamTypes = new HashMap<>();

  private EquoWebSocketServer equoWebSocketServer;

  /**
   * Starts websocket server when the service is activated.
   */
  @Activate
  public void start() {
    logger.info("Initializing Equo websocket server...");
    equoWebSocketServer = new EquoWebSocketServer(functionActionHandlers, consumerActionHandlers,
        responseActionHandlers, actionParamTypes, gsonParser);
    equoWebSocketServer.start();
  }

  /**
   * Stops websocket server when the service is deactivated.
   */
  @Deactivate
  public void stop() {
    logger.info("Stopping Equo websocket server... ");
    try {
      equoWebSocketServer.stop();
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public <T> void addEventHandler(String eventId, Consumer<T> actionHandler,
      Class<?>... paramTypes) {
    consumerActionHandlers.put(eventId, actionHandler);
    if (paramTypes != null && paramTypes.length == 1) {
      actionParamTypes.put(eventId, paramTypes[0]);
    }
  }

  @Override
  public <T, R> void addEventHandler(String eventId, Function<T, R> actionHandler,
      Class<?>... paramTypes) {
    functionActionHandlers.put(eventId, actionHandler);
    if (paramTypes != null && paramTypes.length == 1) {
      actionParamTypes.put(eventId, paramTypes[0]);
    }
  }

  @Override
  public void send(String userEvent, Object payload) {
    NamedActionMessage namedActionMessage = new NamedActionMessage(userEvent, payload);
    String messageAsJson = gsonParser.toJson(namedActionMessage);
    equoWebSocketServer.broadcast(messageAsJson);
  }

  @Override
  public <T> Future<T> send(String userEvent, Object payload, Class<T> responseTypeClass) {
    return CompletableFuture.supplyAsync(() -> {
      try {
        String uuid = UUID.randomUUID().toString();
        NamedActionMessage namedActionMessage = new NamedActionMessage(userEvent, payload, uuid);
        String messageAsJson = gsonParser.toJson(namedActionMessage);
        equoWebSocketServer.broadcast(messageAsJson);
        CompletableFuture<Object> future = new CompletableFuture<>();
        responseActionHandlers.put(uuid, future);
        while (!future.isDone()) {
          // Sleep?
        }
        return (T) gsonParser.fromJson(future.get().toString(), responseTypeClass);
      } catch (InterruptedException e) {
        logger.debug("Thread was interrupted");
        return null;
      } catch (ExecutionException e) {
        logger.debug(
            "An unexpected exception was thrown when retrieving the response from javascript.");
        return null;
      } catch (Exception e) {
        logger.debug("Unexpected exception");
        return null;
      }
    });
  }

  @Override
  public int getPort() {
    // TODO implement a timeout.
    while (!equoWebSocketServer.isStarted()) {
    }
    return equoWebSocketServer.getPort();
  }

  /**
   * Method used to add all the Action Handler implementations.
   */
  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      policyOption = ReferencePolicyOption.GREEDY)
  public void setFunctionActionHandler(IActionHandler actionHandler) {
    for (Method method : actionHandler.getClass().getDeclaredMethods()) {
      final String actionHandlerName =
          ActionHelper.getEventName(method.getAnnotation(EventName.class)).orElse(method.getName());
      final Class<?> parameterType;
      Type[] types = method.getGenericParameterTypes();
      if (types.length == 1) {
        parameterType = (Class<?>) types[0];
        actionParamTypes.put(actionHandlerName, parameterType);
      } else {
        parameterType = Object.class;
      }
      Class<?> rt = method.getReturnType();
      if (Void.TYPE.equals(rt)) {
        Consumer<?> cons = (param) -> {
          try {
            method.invoke(actionHandler, parameterType.cast(param));
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
              | ClassCastException e1) {
            try {
              method.invoke(actionHandler);
            } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e2) {
              logger.error("Error invoking action handler " + actionHandlerName, e2);
            }
          }
        };
        consumerActionHandlers.put(actionHandlerName, cons);
      } else {
        Function<?, ?> func = (param) -> {
          try {
            return method.invoke(actionHandler, parameterType.cast(param));
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
              | ClassCastException e1) {
            try {
              return method.invoke(actionHandler);
            } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e2) {
              logger.error("Error invoking action handler " + actionHandlerName, e2);
              return null;
            }
          }
        };
        functionActionHandlers.put(actionHandlerName, func);
      }
    }
  }

  /**
   * Method to release all actions defined in this action handler.
   */
  public void unsetFunctionActionHandler(IActionHandler actionHandler) {
    for (Method method : actionHandler.getClass().getDeclaredMethods()) {
      final String actionHandlerName =
          ActionHelper.getEventName(method.getAnnotation(EventName.class)).orElse(method.getName());
      functionActionHandlers.remove(actionHandlerName);
      consumerActionHandlers.remove(actionHandlerName);
      actionParamTypes.remove(actionHandlerName);
    }
  }

}
