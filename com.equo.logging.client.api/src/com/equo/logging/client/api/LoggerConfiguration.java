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

package com.equo.logging.client.api;

import org.slf4j.LoggerFactory;

/**
 * Class that allows to make global logger configurations.
 */
public class LoggerConfiguration {
  private static Level DEFAULT_LEVEL = Level.INFO;

  /**
   * Gets a global log level.
   * @return a global log level.
   */
  public static Level getGlobalLevel() {
    ch.qos.logback.classic.Logger root =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    return Level.toLevel(root.getLevel().toString(), DEFAULT_LEVEL);
  }

  /**
   * Sets a global log level.
   * @param level log level.
   */
  public static void setGlobalLevel(Level level) {
    if (level == null) {
      return;
    }
    ch.qos.logback.classic.Logger root =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    root.setLevel(ch.qos.logback.classic.Level.toLevel(level.toString()));
  }
}
