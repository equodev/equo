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

package com.equo.comm.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Annotation to define the name of the event to be listened to in the annotated
 * class/method.
 * </p>
 * <p>
 * If a class implementing {@link com.equo.comm.api.actions.IActionHandler} is
 * annotated, then the
 * {@link com.equo.comm.api.actions.IActionHandler#call(Object) call} method will
 * be executed when this event is sent.
 * </p>
 * <p>
 * If a method of a class implementing
 * {@link com.equo.comm.api.actions.IActionHandler} is annotated, then that method
 * will be executed when this event is sent.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface EventName {
  /** Gets the event name. */
  String value();
}
