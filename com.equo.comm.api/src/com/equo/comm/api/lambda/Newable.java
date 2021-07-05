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

package com.equo.comm.api.lambda;

/**
 * Interface for all the Equo payload handlers that can be created with lambda
 * functions.
 */
public interface Newable<T> extends MethodFinder {
  @SuppressWarnings("unchecked")
  default Class<T> type() {
    return (Class<T>) parameter(0).getType();
  }

  /**
   * Gets a new instance of the payload data class.
   */
  default T newInstance() {
    try {
      return type().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
