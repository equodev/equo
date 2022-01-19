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

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface of the Comm service.
 */
public interface IEquoCommService {
  /**
   * Adds a Function event handler.
   * @param actionId      the action ID.
   * @param actionHandler the action handler.
   * @param paramTypes    types for the handler parameters.
   */
  public <T, R> void addEventHandler(String actionId, Function<T, R> actionHandler,
      Class<?>... paramTypes);

  /**
   * Adds a Consumer event handler.
   * @param actionId      the action ID.
   * @param actionHandler the action handler.
   * @param paramTypes    types for the handler parameters.
   */
  public <T> void addEventHandler(String actionId, Consumer<T> actionHandler,
      Class<?>... paramTypes);

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
