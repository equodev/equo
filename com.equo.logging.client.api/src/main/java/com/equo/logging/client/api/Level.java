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

package com.equo.logging.client.api;

/**
 * Level of logging.
 */
public final class Level {
  /**
   * The <code>OFF</code> is used to turn off logging.
   */
  public static final Level OFF = new Level("OFF");

  /**
   * The <code>ERROR</code> level designates error events which may or not be
   * fatal to the application.
   */
  public static final Level ERROR = new Level("ERROR");

  /**
   * The <code>WARN</code> level designates potentially harmful situations.
   */
  public static final Level WARN = new Level("WARN");

  /**
   * The <code>INFO</code> level designates informational messages highlighting
   * overall progress of the application.
   */
  public static final Level INFO = new Level("INFO");

  /**
   * The <code>DEBUG</code> level designates informational events of lower
   * importance.
   */
  public static final Level DEBUG = new Level("DEBUG");

  /**
   * The <code>TRACE</code> level designates informational events of very low
   * importance.
   */
  public static final Level TRACE = new Level("TRACE");

  /**
   * The <code>ALL</code> is used to turn on all logging.
   */
  public static final Level ALL = new Level("ALL");

  private final String levelStr;

  /**
   * Instantiate a Level object.
   */
  private Level(String levelStr) {
    this.levelStr = levelStr;
  }

  /**
   * Returns the string representation of this Level.
   */
  public String toString() {
    return levelStr;
  }

  /**
   * Convert the string passed as argument to a Level. If the conversion fails,
   * then this method returns the value of <code>defaultLevel</code>.
   */
  public static Level toLevel(final String sArg, Level defaultLevel) {
    if (sArg == null) {
      return defaultLevel;
    }

    final String in = sArg.trim();

    if (in.equalsIgnoreCase("ALL")) {
      return Level.ALL;
    }
    if (in.equalsIgnoreCase("TRACE")) {
      return Level.TRACE;
    }
    if (in.equalsIgnoreCase("DEBUG")) {
      return Level.DEBUG;
    }
    if (in.equalsIgnoreCase("INFO")) {
      return Level.INFO;
    }
    if (in.equalsIgnoreCase("WARN")) {
      return Level.WARN;
    }
    if (in.equalsIgnoreCase("ERROR")) {
      return Level.ERROR;
    }
    if (in.equalsIgnoreCase("OFF")) {
      return Level.OFF;
    }
    return defaultLevel;
  }
}
