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

package com.equo.ws.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.equo.comm.api.IEquoCommService;
import com.equo.comm.api.IEquoRunnableParser;
import com.equo.comm.api.NamedActionMessage;
import com.equo.comm.api.actions.IActionHandler;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.GsonBuilder;

/**
 * Websocket service implementation. Manages the websocket server lifecycle and
 * all the event listeners.
 */
@Component
public class EquoWebSocketServiceImpl implements IEquoCommService {
  protected static final Logger logger = LoggerFactory.getLogger(EquoWebSocketServiceImpl.class);

  @SuppressWarnings("rawtypes")
  private Map<String, IActionHandler> actionHandlers = new HashMap<>();
  private Map<String, IEquoRunnableParser<?>> eventHandlers = new HashMap<>();
  private EquoWebSocketServer equoWebSocketServer;

  /**
   * Starts websocket server when the service is activated.
   */
  @Activate
  public void start() {
    logger.info("Initializing Equo websocket server...");
    equoWebSocketServer = new EquoWebSocketServer(eventHandlers, actionHandlers);
    equoWebSocketServer.start();
  }

  /**
   * Stops websocket server when the service is deactivated.
   */
  @Deactivate
  public void stop() {
    logger.info("Stopping Equo websocket server... ");
    eventHandlers.clear();
    try {
      equoWebSocketServer.stop();
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void addEventHandler(String eventId, IEquoRunnableParser<?> equoRunnableParser) {
    eventHandlers.put(eventId, equoRunnableParser);
  }

  @Override
  public void send(String userEvent, Object payload) {
    GsonBuilder gsonBuilder = new GsonBuilder();
    NamedActionMessage namedActionMessage = new NamedActionMessage(userEvent, payload);
    String messageAsJson = gsonBuilder.create().toJson(namedActionMessage);
    equoWebSocketServer.broadcast(messageAsJson);
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
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC,
      policyOption = ReferencePolicyOption.GREEDY)
  public void setActionHandler(IActionHandler actionHandler) {
    this.actionHandlers.put(actionHandler.getEventName(), actionHandler);
    Map<String, IActionHandler> events = actionHandler.getExtraEvents();
    for (Map.Entry<String, IActionHandler> extraEvent : events.entrySet()) {
      this.actionHandlers.put(extraEvent.getKey(), extraEvent.getValue());
    }
  }

  public void unsetActionHandler(@SuppressWarnings("rawtypes") IActionHandler actionHandler) {
    this.actionHandlers.clear();
  }

}
