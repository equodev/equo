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

package com.equo.ws.api.lambda;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

import static java.util.Arrays.asList;

public interface MethodFinder extends Serializable {
  default SerializedLambda serialized() {
    try {
      Method replaceMethod = getClass().getDeclaredMethod("writeReplace");
      replaceMethod.setAccessible(true);
      return (SerializedLambda) replaceMethod.invoke(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  default Class<?> getContainingClass() {
    try {
      String className = serialized().getImplClass().replaceAll("/", ".");
      return Class.forName(className);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  default Method method() {
    SerializedLambda lambda = serialized();
    Class<?> containingClass = getContainingClass();
    return asList(containingClass.getDeclaredMethods()).stream()
        .filter(method -> Objects.equals(method.getName(), lambda.getImplMethodName())).findFirst()
        .orElseThrow(UnableToGuessMethodException::new);
  }

  default Parameter parameter(int n) {
    return method().getParameters()[n];
  }

  default Object defaultValueForParameter(int n) {
    return DefaultValue.ofType(parameter(n).getType());
  }

  @SuppressWarnings("serial")
  class UnableToGuessMethodException extends RuntimeException {
  }
}
