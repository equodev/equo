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

package com.equo.comm.api;

/**
 * Interface for event handlers. Allows to listen and send events.
 */
public interface IEquoEventHandler {
  /**
   * Sends a null data to later be transmitted using the userEvent as ID.
   * @param userEvent the event ID.
   */
  void send(String userEvent);

  /**
   * Sends the specified data to later be transmitted using the userEvent as ID.
   * @param userEvent the event ID.
   * @param payload   the data to send.
   */
  void send(String userEvent, Object payload);

  /**
   * Defines a custom JsonPayloadEquoRunnable for an specific event ID.
   * @param eventId                 the id event.
   * @param jsonPayloadEquoRunnable the runnable.
   */
  void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable);

  /**
   * Defines a custom StringPayloadEquoRunnable for an specific event ID.
   * @param eventId                   the id event.
   * @param stringPayloadEquoRunnable the runnable.
   */
  void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable);

  /**
   * Defines a custom objectPayloadEquoRunnable for an specific event ID.
   * @param eventId                   the event ID.
   * @param objectPayloadEquoRunnable the runnable.
   */
  <T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable);

}
