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

package com.equo.comm.api;

import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Implements the handler actions for send and received web-socket events using
 * equoCommService instance.
 */
@Component(immediate = true)
public class EquoEventHandler implements IEquoEventHandler {

  private IEquoCommService equoCommService;

  public void send(String userEvent) {
    equoCommService.send(userEvent, null);
  }

  public void send(String userEvent, Object payload) {
    equoCommService.send(userEvent, payload);
  }

  @Override
  public <T> Future<T> send(String userEvent, Class<T> responseTypeClass) {
    return equoCommService.send(userEvent, null, responseTypeClass);
  }

  @Override
  public <T> Future<T> send(String userEvent, Object payload, Class<T> responseTypeClass) {
    return equoCommService.send(userEvent, payload, responseTypeClass);
  }

  @Override
  public <T, R> void on(String actionId, Class<T> payloadClass, Function<T, R> actionHandler) {
    equoCommService.addEventHandler(actionId, actionHandler, payloadClass);
  }

  @Override
  public <R> void on(String actionId, Function<String, R> actionHandler) {
    equoCommService.addEventHandler(actionId, actionHandler, String.class);
  }

  @Override
  public <T> void on(String actionId, Class<T> payloadClass, Consumer<T> actionHandler) {
    equoCommService.addEventHandler(actionId, actionHandler, payloadClass);
  }

  @Override
  public void on(String actionId, Consumer<String> actionHandler) {
    equoCommService.addEventHandler(actionId, actionHandler, String.class);
  }

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  void setWebsocketService(IEquoCommService equoWebSocketService) {
    this.equoCommService = equoWebSocketService;
  }

  void unsetWebsocketService(IEquoCommService equoWebSocketService) {
    this.equoCommService = null;
  }

}
