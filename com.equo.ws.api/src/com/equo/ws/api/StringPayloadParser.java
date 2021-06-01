package com.equo.ws.api;

import com.google.gson.Gson;

/**
 * Parser for ws event payload into an Equo runnable of String type.
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
