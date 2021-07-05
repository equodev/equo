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

package com.equo.comm.api;

import com.equo.comm.api.IEquoRunnable;
import com.equo.comm.api.IEquoRunnableParser;
import com.google.gson.Gson;

/**
 * Parser for ws event payload into an generic Equo runnable.
 */
public class ObjectPayloadParser<T> implements IEquoRunnableParser<T> {

  private IEquoRunnable<T> objectPayloadEquoRunnable;
  private Gson gson;

  /**
   * Creates the payload parser for object of type T.
   * @param objectPayloadEquoRunnable the runnable of type T.
   */
  public ObjectPayloadParser(IEquoRunnable<T> objectPayloadEquoRunnable) {
    this.objectPayloadEquoRunnable = objectPayloadEquoRunnable;
    this.gson = new Gson();
  }

  @Override
  public T parsePayload(Object payload) {
    if (payload == null) {
      return null;
    }
    String jsonString = gson.toJson(payload);
    Class<T> type = getEquoRunnable().type();
    T fromJson = gson.fromJson(jsonString, type);
    return fromJson;
  }

  @Override
  public IEquoRunnable<T> getEquoRunnable() {
    return objectPayloadEquoRunnable;
  }

}
