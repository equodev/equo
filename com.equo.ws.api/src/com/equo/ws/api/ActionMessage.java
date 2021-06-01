package com.equo.ws.api;

/**
 * Websocket message that executes an action.
 */
public abstract class ActionMessage {

  private String action;

  public ActionMessage(String action) {
    this.action = action;
  }

  public String getAction() {
    return action;
  }

}
