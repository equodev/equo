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

package com.equo.aer.internal.api;

import com.google.gson.JsonObject;

/**
 * API for internal error reporting service.
 *
 */
public interface IEquoCrashReporter {

  /**
   * Logs an unexpected crash caught by Equo's status reporter which is an
   * extension of eclipse's.
   * 
   * @param segmentation tags to append to the log
   */

  public void logCrash(JsonObject segmentation);

  /**
   * Logs a point with data from logs done by the eclipse logger.
   * 
   * @param segmentation tags to append to the log
   */

  public void logEclipse(JsonObject segmentation);

}
