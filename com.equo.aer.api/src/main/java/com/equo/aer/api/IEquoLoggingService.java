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

package com.equo.aer.api;

import com.google.gson.JsonObject;

/**
 * API for error reporting service.
 *
 */
public interface IEquoLoggingService {

  /**
   * Logs an error with an associated message.
   * @param message Log message
   */
  public void logError(String message);

  /**
   * Logs an error with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logError(String message, JsonObject tags);

  /**
   * Logs info with an associated message.
   * @param message Log message
   */
  public void logInfo(String message);

  /**
   * Logs info with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logInfo(String message, JsonObject tags);

  /**
   * Logs a warning with an associated message.
   * @param message Log message
   */
  public void logWarning(String message);

  /**
   * Logs a warning with an associated message and custom tags.
   * @param message Log message
   * @param tags    JsonObject tags
   */
  public void logWarning(String message, JsonObject tags);
}
