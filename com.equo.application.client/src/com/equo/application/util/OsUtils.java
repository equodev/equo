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

package com.equo.application.util;

/**
 * Utilities to find in which OS the app is running.
 */
public class OsUtils {

  private static String OS = System.getProperty("os.name").toLowerCase();

  public static boolean isWindows() {
    return (OS.contains("win"));
  }

  public static boolean isMac() {
    return (OS.contains("mac"));
  }

  public static boolean isUnix() {
    return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
  }
}
