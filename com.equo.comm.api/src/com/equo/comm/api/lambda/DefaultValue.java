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

import java.util.HashMap;
import java.util.Map;

/**
 * Default value for each data type.
 */
public class DefaultValue {
  private static Map<Class<?>, Object> defaultValues = new HashMap<>();

  static {
    defaultValues.put(int.class, 0);
    defaultValues.put(Integer.class, 0);
    defaultValues.put(boolean.class, false);
    defaultValues.put(Boolean.class, false);
    defaultValues.put(byte.class, (byte) 0);
    defaultValues.put(Byte.class, 0);
    defaultValues.put(char.class, ' ');
    defaultValues.put(Character.class, ' ');
    defaultValues.put(short.class, (short) 0.0);
    defaultValues.put(Short.class, (short) 0.0);
    defaultValues.put(long.class, 0L);
    defaultValues.put(Long.class, 0L);
    defaultValues.put(float.class, 0.0f);
    defaultValues.put(Float.class, 0.0f);
    defaultValues.put(double.class, 0.0d);
    defaultValues.put(Double.class, 0.0d);
  }

  @SuppressWarnings("unchecked")
  public static <T> T ofType(Class<?> type) {
    return (T) defaultValues.getOrDefault(type, null);
  }
}
