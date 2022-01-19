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

package com.equo.ws.provider;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.equo.comm.api.NamedActionMessage;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.Gson;

class EquoWebSocketServer extends WebSocketServer {
  protected static final Logger logger = LoggerFactory.getLogger(EquoWebSocketServer.class);

  private Gson gsonParser;
  private Map<String, Function<?, ?>> functionActionHandlers;
  private Map<String, Consumer<?>> consumerActionHandlers;
  private Map<String, Class<?>> actionParamTypes;
  private boolean firstClientConnected = false;
  List<String> messagesToSend = new ArrayList<>();

  private volatile boolean started;

  public EquoWebSocketServer(Map<String, Function<?, ?>> functionActionHandlers,
      Map<String, Consumer<?>> consumerActionHandlers, Map<String, Class<?>> actionParamTypes) {
    super(new InetSocketAddress(0));
    this.functionActionHandlers = functionActionHandlers;
    this.consumerActionHandlers = consumerActionHandlers;
    this.actionParamTypes = actionParamTypes;
    this.gsonParser = new Gson();
    this.started = false;
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    broadcast("new connection: " + handshake.getResourceDescriptor());
    logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress()
        + " entered the Equo SDK!");
    this.firstClientConnected = true;
    synchronized (messagesToSend) {
      for (String messageToSend : messagesToSend) {
        broadcast(messageToSend);
      }
      messagesToSend.clear();
    }
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    broadcast(conn + " has left the Equo SDK!");
    logger.debug(conn + " has left the Equo SDK!");
  }

  @SuppressWarnings({ "unchecked" })
  private void receiveMessage(String message, boolean broadcast) {
    NamedActionMessage actionMessage = null;
    try {
      actionMessage = gsonParser.fromJson(message, NamedActionMessage.class);
    } catch (Exception e) {
      return;
    }

    String actionId = actionMessage.getActionId();
    if (functionActionHandlers.containsKey(actionId)
        || consumerActionHandlers.containsKey(actionId)) {
      Object parsedPayload = null;
      if (actionMessage.getPayload() != null) {
        Class<?> type = actionParamTypes.get(actionId);
        String jsonString;
        if (actionMessage.getPayload() instanceof String) {
          jsonString = actionMessage.getPayload().toString();
        } else {
          jsonString = gsonParser.toJson(actionMessage.getPayload());
        }
        try {
          if (String.class.equals(type)) {
            parsedPayload = jsonString;
          } else {
            parsedPayload = gsonParser.fromJson(jsonString, type);
          }
        } catch (Exception e) {
          parsedPayload = jsonString;
        }
      }
      Function<?, ?> function = functionActionHandlers.get(actionId);
      Object response = null;
      if (function != null) {
        response = ((Function<Object, ?>) function).apply(parsedPayload);
      } else {
        Consumer<?> consumer = consumerActionHandlers.get(actionId);
        ((Consumer<Object>) consumer).accept(parsedPayload);
      }
      if (actionMessage.getCallerUuid() != null && response != null) {
        NamedActionMessage responseMessage =
            new NamedActionMessage(actionMessage.getCallerUuid(), response);
        super.broadcast(gsonParser.toJson(responseMessage));
      }
    } else if (broadcast) {
      super.broadcast(message);
    }
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    // broadcast(message);
    logger.debug(conn + ": " + message);
    receiveMessage(message, true);
  }

  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    broadcast(message.array());
    logger.debug(conn + ": " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
    if (conn != null) {
      // some errors like port binding failed may not be assignable to a specific
      // websocket
    }
  }

  @Override
  public void onStart() {
    // TODO log web socket server started
    this.started = true;
    logger.info("Equo Websocket Server started!");
  }

  @Override
  public void broadcast(String messageAsJson) {
    if (firstClientConnected) {
      super.broadcast(messageAsJson);
      receiveMessage(messageAsJson, false);
    } else {
      synchronized (messagesToSend) {
        messagesToSend.add(messageAsJson);
      }
    }
  }

  public boolean isStarted() {
    return started;
  }

}
