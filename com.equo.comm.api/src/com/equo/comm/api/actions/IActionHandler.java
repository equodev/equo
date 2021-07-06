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

package com.equo.comm.api.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.equo.comm.api.annotations.EventName;
import com.equo.logging.client.api.LoggerFactory;

/**
 * <p>
 * Action handler interface. A class implementing this interface its declared as
 * a comm event listener.
 * </p>
 * <p>
 * By default, the {@link #call(Object) call} method will be called on an event
 * named with the class name in lowercase. This can be changed using
 * {@link com.equo.comm.api.annotations.EventName} annotation.
 * </p>
 */
public interface IActionHandler<T> extends IEquoCallable<T> {

  /**
   * Gets the generic type of the current class.
   */
  @SuppressWarnings("rawtypes")
  public default Class getGenericInterfaceType() {
    Class c = getClass();
    ParameterizedType parameterizedType = (ParameterizedType) c.getGenericInterfaces()[0];
    Type[] typeArguments = parameterizedType.getActualTypeArguments();
    Class<?> typeArgument = (Class<?>) typeArguments[0];
    return typeArgument;
  }

  /**
   * Gets the event name associated to the current class. If there is no
   * {@link com.equo.comm.api.annotations.EventName} annotation, it is by default
   * the class name in lowercase.
   */
  public default String getEventName() {
    Class<?> clazz = getClass();
    EventName equoHandler = clazz.getAnnotation(EventName.class);
    if (equoHandler != null) {
      String eventName = equoHandler.value();
      if (eventName != null && !eventName.isEmpty()) {
        return eventName;
      }
    }
    return clazz.getSimpleName().toLowerCase();
  }

  /**
   * Gets all the extra event listeners the current class may have. That is, all
   * other methods annotated with {@link com.equo.comm.api.annotations.EventName
   * EventName}.
   * @return a map with event name as key, and an action handler executing the
   *         annotated method as value.
   */
  public default Map<String, IActionHandler<T>> getExtraEvents() {
    IActionHandler<T> original = this;
    Map<String, IActionHandler<T>> result = new HashMap<>();
    Class<?> clazz = getClass();
    final Class<?> type = getGenericInterfaceType();
    for (Method method : clazz.getDeclaredMethods()) {
      EventName equoHandler = method.getAnnotation(EventName.class);
      if (equoHandler != null) {
        String eventName = equoHandler.value();
        if (eventName != null && !eventName.isEmpty()) {
          result.put(eventName, new IActionHandler<T>() {
            @Override
            public Object call(T payload) {
              try {
                return method.invoke(original);
              } catch (IllegalAccessException | IllegalArgumentException
                  | InvocationTargetException e) {
                try {
                  return method.invoke(original, payload);
                } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e1) {
                  LoggerFactory.getLogger(clazz)
                      .error("Error calling handler for " + eventName + " event", e1);
                }
              }
              return null;
            }
            
            @Override
            public Class getGenericInterfaceType() {
              return type;
            }
          });
        }
      }
    }
    return result;
  }

  public Object call(T payload);
}
