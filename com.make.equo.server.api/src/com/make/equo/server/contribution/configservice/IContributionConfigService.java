package com.make.equo.server.contribution.configservice;

import java.util.List;

import org.osgi.framework.Bundle;

import com.google.gson.JsonObject;
import com.make.equo.server.contribution.EquoContribution;

public interface IContributionConfigService {
	
	public List<EquoContribution> defineContributions(JsonObject configJson, Bundle bundle);

}
