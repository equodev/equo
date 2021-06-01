package com.equo.contribution.media.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

/**
 * A contribution for the Media API.
 */
@Component
public class EquoMediaApiContribution {

  private static final String MEDIA_CONTRIBUTION_NAME = "equomedia";
  private static final String MEDIA_JS_API = "media.js";

  private EquoContributionBuilder builder;

  @Activate
  protected void activate() {
    builder.withContributionName(MEDIA_CONTRIBUTION_NAME).withScriptFile(MEDIA_JS_API)
        .withUrlResolver(
            new EquoGenericUrlResolver(EquoMediaApiContribution.class.getClassLoader()))
        .build();
  }

  @Reference
  void setEquoBuilder(EquoContributionBuilder builder) {
    this.builder = builder;
  }

  void unsetEquoBuilder(EquoContributionBuilder builder) {
    this.builder = null;
  }

}
