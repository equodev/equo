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

package com.equo.comm.api.util;

import java.util.Optional;

import com.equo.comm.api.annotations.EventName;

/**
 * Helper class containing common methods.
 */
public class ActionHelper {

  /**
   * Returns the event name if defined or an empty Optional if not.
   * @param  event annotation to get the event name from
   * @return       an Optional containing the event name or an empty Optional
   */
  public static Optional<String> getEventName(EventName event) {
    if (event != null) {
      String eventName = event.value();
      if (eventName != null && !eventName.isEmpty()) {
        return Optional.of(eventName);
      }
    }
    return Optional.empty();
  }

}
