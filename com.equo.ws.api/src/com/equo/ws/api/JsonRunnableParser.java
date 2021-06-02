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

package com.equo.ws.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Parser for ws event payload into an Equo runnable of JsonObject type.
 */
public class JsonRunnableParser implements IEquoRunnableParser<JsonObject> {

  private JsonParser jsonParser;
  private IEquoRunnable<JsonObject> jsonPayloadEquoRunnable;
  private Gson gson;

  /**
   * Creates the payload parser for object of type Json.
   * @param jsonPayloadEquoRunnable the runnable of type Json.
   */
  public JsonRunnableParser(IEquoRunnable<JsonObject> jsonPayloadEquoRunnable) {
    this.jsonPayloadEquoRunnable = jsonPayloadEquoRunnable;
    this.jsonParser = new JsonParser();
    this.gson = new Gson();
  }

  @Override
  public JsonObject parsePayload(Object payload) {
    String jsonString = gson.toJson(payload);
    return jsonParser.parse(jsonString).getAsJsonObject();
  }

  @Override
  public IEquoRunnable<JsonObject> getEquoRunnable() {
    return jsonPayloadEquoRunnable;
  }

}
