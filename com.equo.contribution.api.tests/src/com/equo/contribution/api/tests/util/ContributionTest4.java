package com.equo.contribution.api.tests.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

@Component
public class ContributionTest4 implements IContributionTest4 {
  @Reference
  private EquoContributionBuilder builder;

  @Override
  public void load() {
    builder //
        .withContributionName("contributionTest4") //
        .withBaseHtmlResource("index.html") //
        .withPathWithScript("", "index.bundle.js") //
        .withDependencies("contributiontest5")
        .withUrlResolver(new EquoGenericUrlResolver(ContributionTest4.class.getClassLoader())) //
        .build();
  }
}
