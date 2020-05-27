package com.make.equo.contribution.api.handler;

import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.commands.ParameterValuesException;

public class CommandParameter implements IParameter {
	private String id;
	private String name;

	public CommandParameter(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IParameterValues getValues() throws ParameterValuesException {
		return null;
	}

	@Override
	public boolean isOptional() {
		return true;
	}

}
