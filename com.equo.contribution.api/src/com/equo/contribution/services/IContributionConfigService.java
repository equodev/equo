package com.equo.contribution.services;

import java.util.List;

import org.osgi.framework.Bundle;

import com.google.gson.JsonObject;
import com.equo.contribution.api.EquoContribution;

public interface IContributionConfigService {
	
	public List<EquoContribution> defineContributions(JsonObject configJson, Bundle bundle);

}
