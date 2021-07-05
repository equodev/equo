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

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.equo.comm.api.IEquoEventHandler;
import com.equo.comm.api.IEquoRunnable;
import com.equo.comm.api.IEquoWebSocketService;
import com.equo.comm.api.JsonPayloadEquoRunnable;
import com.equo.comm.api.JsonRunnableParser;
import com.equo.comm.api.ObjectPayloadParser;
import com.equo.comm.api.StringPayloadEquoRunnable;
import com.equo.comm.api.StringPayloadParser;

/**
 * Implements the handler actions for send and received websocket events using
 * equoWebSocketService instance.
 */
@Component
public class EquoEventHandler implements IEquoEventHandler {

  private IEquoWebSocketService equoWebSocketService;

  @Override
  public void send(String userEvent) {
    this.send(userEvent, null);
  }

  @Override
  public void send(String userEvent, Object payload) {
    equoWebSocketService.send(userEvent, payload);
  }

  @Override
  public void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable) {
    equoWebSocketService.addEventHandler(eventId, new JsonRunnableParser(jsonPayloadEquoRunnable));
  }

  @Override
  public void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable) {
    equoWebSocketService.addEventHandler(eventId,
        new StringPayloadParser(stringPayloadEquoRunnable));
  }

  @Override
  public <T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable) {
    equoWebSocketService.addEventHandler(eventId,
        new ObjectPayloadParser<T>(objectPayloadEquoRunnable));
  }

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  void setWebsocketService(IEquoWebSocketService equoWebSocketService) {
    this.equoWebSocketService = equoWebSocketService;
  }

  void unsetWebsocketService(IEquoWebSocketService equoWebSocketService) {
    this.equoWebSocketService = null;
  }
}
