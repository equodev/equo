package com.equo.ws.api;

public abstract class ActionMessage {

	private String action;

	public ActionMessage(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

}
