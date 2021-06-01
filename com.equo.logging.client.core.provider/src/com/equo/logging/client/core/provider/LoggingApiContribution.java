package com.equo.logging.client.core.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

/**
 * Contribution for the Logging js API.
 */
@Component
public class LoggingApiContribution {

  private static final String LOGGING_CONTRIBUTION_NAME = "equologging";
  private static final String LOGGING_JS_API = "loggingApi.js";

  @Reference
  private EquoContributionBuilder builder;

  @Activate
  protected void activate() {
    builder.withContributionName(LOGGING_CONTRIBUTION_NAME).withScriptFile(LOGGING_JS_API)
        .withUrlResolver(new EquoGenericUrlResolver(LoggingApiContribution.class.getClassLoader()))
        .build();
  }
}
