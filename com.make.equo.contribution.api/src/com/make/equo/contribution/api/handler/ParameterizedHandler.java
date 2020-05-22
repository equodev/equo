package com.make.equo.contribution.api.handler;

import org.eclipse.core.commands.IParameter;
import org.osgi.framework.FrameworkUtil;

public abstract class ParameterizedHandler {
	public abstract String getCommandId();

	public abstract IParameter[] getParameters();

	public String getShortcut() {
		return "";
	}

	public String getContributionUri() {
		return "bundleclass://" + FrameworkUtil.getBundle(this.getClass()).getSymbolicName() + "/"
				+ this.getClass().getName();
	}
}
