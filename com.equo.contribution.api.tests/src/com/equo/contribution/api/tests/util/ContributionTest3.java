package com.equo.contribution.api.tests.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

@Component
public class ContributionTest3 implements IContributionTest3 {
  @Reference
  private EquoContributionBuilder builder;

  @Override
  public void load() {
    builder //
        .withContributionName("contributionTest3") //
        .withBaseHtmlResource("index.html") //
        .withPathWithScript("", "index.bundle.js") //
        .withDependencies("contributiontest10")
        .withUrlResolver(new EquoGenericUrlResolver(ContributionTest3.class.getClassLoader())) //
        .build();
  }
}
