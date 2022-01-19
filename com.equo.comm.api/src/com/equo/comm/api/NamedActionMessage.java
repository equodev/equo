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

/**
 * An action message with parameters.
 */
public class NamedActionMessage extends ActionMessage implements IdentifiableActionMessage {

  private Object payload;
  private String callerId;

  public NamedActionMessage(String actionId, Object payload) {
    super(actionId);
    this.payload = payload;
  }

  /**
   * Simple constructor. Initializes this instance's attributes with the given
   * values.
   * @param actionId ID of this action
   * @param payload  parameters of this action
   * @param callerId id of this action's caller
   */
  public NamedActionMessage(String actionId, Object payload, String callerId) {
    super(actionId);
    this.payload = payload;
    this.callerId = callerId;
  }

  public Object getPayload() {
    return payload;
  }

  @Override
  public String getCallerUuid() {
    return callerId;
  }
}
