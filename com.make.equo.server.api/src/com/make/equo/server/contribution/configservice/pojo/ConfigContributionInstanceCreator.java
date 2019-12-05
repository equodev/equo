package com.make.equo.server.contribution.configservice.pojo;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ConfigContributionInstanceCreator implements InstanceCreator<ConfigContribution> {

	@Override
	public ConfigContribution createInstance(Type type) {
		return new ConfigContribution();
	}

}
