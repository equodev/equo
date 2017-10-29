package com.make.equo.application.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;

public class ScriptHandler {

	private MPart mPart;

	public ScriptHandler(MPart mPart) {
		this.mPart = mPart;
	}

	public void saveScript(String scriptPath) {
		Map<String, String> properties = mPart.getProperties();
		int index = 1;
		String scriptId = IConstants.CUSTOM_SCRIPTS + index;
		while (properties.containsKey(scriptId)) {
			index++;
			scriptId = IConstants.CUSTOM_SCRIPTS + index;
		}
		properties.put(scriptId, scriptPath);
	}

	public List<String> getScripts() {
		List<String> scriptPaths = new ArrayList<>();
		Map<String, String> properties = mPart.getProperties();
		int index = 1;
		String scriptId = IConstants.CUSTOM_SCRIPTS + index;
		while (properties.containsKey(scriptId)) {
			scriptPaths.add(properties.get(scriptId));
			index++;
			scriptId = IConstants.CUSTOM_SCRIPTS + index;
		}
		return scriptPaths;
	}

}
