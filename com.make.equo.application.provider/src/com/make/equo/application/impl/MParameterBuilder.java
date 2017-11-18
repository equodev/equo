package com.make.equo.application.impl;

import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;

public interface MParameterBuilder {
	
	default MParameter createMParameter(String id, String value) {
		MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
		// set the identifier for the corresponding command parameter
		parameter.setName(id);
		parameter.setValue(value);
		return parameter;
	}
	
}
