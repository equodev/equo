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

import com.equo.ws.api.lambda.Newable;

/**
 * Calls the declared run method when the payload is a Object of T type.
 * @param <T> the type of the payload.
 */
@FunctionalInterface
public interface IEquoRunnable<T> extends Newable<T> {
  /**
   * Executes the defined instructions when it receives the message from
   * websocket.
   * @param payload the data received from message from websocket.
   */
  public void run(T payload);
}
