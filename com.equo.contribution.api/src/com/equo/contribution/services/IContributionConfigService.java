package com.equo.contribution.services;

import java.util.List;

import org.osgi.framework.Bundle;

import com.equo.contribution.api.EquoContribution;
import com.google.gson.JsonObject;

/**
 * Interface for contribution configuration services.
 */
public interface IContributionConfigService {

  public List<EquoContribution> defineContributions(JsonObject configJson, Bundle bundle);

}
