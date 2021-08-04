package com.equo.contribution.api.tests.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

@Component
public class ContributionTest1 implements IContributionTest1 {
  @Reference
  private EquoContributionBuilder builder;

  @Override
  public void load() {
    builder //
        .withContributionName("contributionTest1") //
        .withBaseHtmlResource("index.html") //
        .withPathWithScript("", "index.bundle.js") //
        .withDependencies("equocomm", "equologging")
        .withUrlResolver(new EquoGenericUrlResolver(ContributionTest1.class.getClassLoader())) //
        .build();
  }
}
