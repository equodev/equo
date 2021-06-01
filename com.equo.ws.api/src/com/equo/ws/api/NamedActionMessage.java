package com.equo.ws.api;

/**
 * An action message with parameters.
 */
public class NamedActionMessage extends ActionMessage {

  private Object params;

  public NamedActionMessage(String action, Object params) {
    super(action);
    this.params = params;
  }

  public Object getParams() {
    return params;
  }
}
