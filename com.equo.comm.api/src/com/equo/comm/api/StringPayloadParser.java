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
 * Parser for comm event payload into an Equo runnable of String type.
 */
public class StringPayloadParser implements IEquoRunnableParser<String> {

  private IEquoRunnable<String> stringPayloadEquoRunnable;
  private Gson gson;

  /**
   * Creates the payload parser for object of type String.
   * @param stringPayloadEquoRunnable the runnable of type String.
   */
  public StringPayloadParser(IEquoRunnable<String> stringPayloadEquoRunnable) {
    this.stringPayloadEquoRunnable = stringPayloadEquoRunnable;
    this.gson = new Gson();
  }

  @Override
  public String parsePayload(Object payload) {
    String result = gson.toJson(payload);
    return result;
  }

  @Override
  public IEquoRunnable<String> getEquoRunnable() {
    return stringPayloadEquoRunnable;
  }

}
