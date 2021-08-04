package com.equo.server.tests;

import static com.equo.comm.api.EquoCommContribution.COMM_CONTRIBUTION_NAME;
import static com.equo.server.api.EquoServerContribution.SERVER_CONTRIBUTION_NAME;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

@Component
public class ServerTestContribution {

  @Reference
  private EquoContributionBuilder builder;

  @Activate
  protected void activate() {
    builder //
        .withContributionName("servertest") //
        .withBaseHtmlResource("index.html") //
        .withScriptFile("testSimpleInjection.js") //
        .withDependencies(COMM_CONTRIBUTION_NAME, SERVER_CONTRIBUTION_NAME) //
        .withPathWithScript("xmlrequest.html", "testXmlRequest.js") //
        .withUrlResolver(new EquoGenericUrlResolver(ServerTestContribution.class.getClassLoader())) //
        .build();
  }

}
