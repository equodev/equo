package com.make.equo.monaco;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

@Component
public class EquoMonacoStandaloneEditor extends EquoMonacoEditor {

	public EquoMonacoStandaloneEditor() {
		super();
	}

	@Activate
	public void activate() {
		equoEventHandler.on("_createEditor", (IEquoRunnable<Void>) runnable -> handleCreateEditor("", ""));
	}

	@Reference
	public void setEquoEventHandler(IEquoEventHandler handler) {
		equoEventHandler = handler;
	}

	public void unsetEquoEventHandler(IEquoEventHandler handler) {
		equoEventHandler = null;
	}

}
