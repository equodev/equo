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

package com.equo.ws.api;

/**
 * Interface of the websocket service.
 */
public interface IEquoWebSocketService {
  /**
   * Adds an event handler. Asings a instance of IEquoRunnableParser<?> to
   * determinated action.
   * @param actionId           the action ID.
   * @param equoRunnableParser the parser instance.
   */
  public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);

  /**
   * Sends the specified data to later be transmitted using the userEvent as ID.
   * @param userEvent the event ID.
   * @param payload   the data to send.
   */
  public void send(String userEvent, Object payload);

  /**
   * Gets the port number that this server listens on.
   * @return the port number.
   */
  public int getPort();

}
