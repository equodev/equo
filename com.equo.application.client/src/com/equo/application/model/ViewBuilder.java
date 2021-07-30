/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application.model;

import static com.equo.application.util.IConstants.DEFAULT_BINDING_TABLE;
import static com.equo.application.util.IConstants.DEV_APP_URL;
import static com.equo.application.util.IConstants.MAIN_PART_ID;
import static com.equo.application.util.IConstants.MAIN_URL_KEY;
import static com.equo.contribution.api.IEquoContributionConstants.OFFLINE_SUPPORT_CONTRIBUTION_NAME;

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.equo.analytics.internal.api.AnalyticsService;
import com.equo.application.api.IEquoApplication;
import com.equo.application.model.util.MenuModelHelper;
import com.equo.application.util.IConstants;
import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.IEquoContributionManager;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;
import com.equo.server.api.IEquoServer;

/**
 * Builder for Equo apps view.
 */
@Component(service = ViewBuilder.class)
public class ViewBuilder {

  private EquoApplicationBuilder equoAppBuilder;
  private MBindingTable mainPartBindingTable;
  private MPart part;
  private String url;

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  private IEquoContributionManager equoContributionManager;

  private EquoContributionBuilder mainAppBuilder = new EquoContributionBuilder();

  private EquoContributionBuilder offlineSupportBuilder = new EquoContributionBuilder();

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  private IEquoServer equoServer;

  @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
  private volatile AnalyticsService analyticsService;

  private OptionalViewBuilder optionalViewBuilder;

  OptionalViewBuilder withSingleView(String url) {
    mainAppBuilder.withContributionName("webwrapper");
    this.url = normalizeUrl(url);
    addUrlToProxyServer(this.url);
    equoServer.addUrl(this.url);
    part.getProperties().put(MAIN_URL_KEY, this.url);
    return optionalViewBuilder;
  }

  OptionalViewBuilder withBaseHtml(String baseHtmlFile) {
    mainAppBuilder.withContributionName("plainequoapp");
    this.url = "http://plainequoapp/";
    String developmentUrl = System.getProperty(DEV_APP_URL);
    if (developmentUrl != null) {
      this.url = normalizeUrl("http://" + developmentUrl);
      addUrlToProxyServer(this.url);
    }
    part.getProperties().put(IConstants.MAIN_URL_KEY, this.url);
    return optionalViewBuilder.withBaseHtml(baseHtmlFile);
  }

  OptionalViewBuilder configureViewPart(EquoApplicationBuilder equoApplicationBuilder,
      IEquoApplication equoApp, String appId) {
    this.equoAppBuilder = equoApplicationBuilder;
    createMainPartIfNotPresent();

    EquoGenericUrlResolver equoAppUrlResolver =
        new EquoGenericUrlResolver(equoApp.getClass().getClassLoader());
    mainAppBuilder.withManager(equoContributionManager).withUrlResolver(equoAppUrlResolver);
    offlineSupportBuilder.withManager(equoContributionManager);
    offlineSupportBuilder.withContributionName(OFFLINE_SUPPORT_CONTRIBUTION_NAME);
    offlineSupportBuilder.withUrlResolver(equoAppUrlResolver);
    Optional<String> contributedLimitedConnectionPage =
        equoContributionManager.getContributedLimitedConnectionPage();
    if (contributedLimitedConnectionPage.isPresent()) {
      offlineSupportBuilder.withBaseHtmlResource(contributedLimitedConnectionPage.get());
    }
    optionalViewBuilder = new OptionalViewBuilder(this, equoServer, analyticsService,
        mainAppBuilder, offlineSupportBuilder, equoApp);

    resetMenu(appId);

    return optionalViewBuilder;
  }

  OptionalViewBuilder setSslTrust(boolean trustAllServers) {
    equoServer.setTrust(trustAllServers);
    return optionalViewBuilder;
  }

  private void addUrlToProxyServer(String url) {
    mainAppBuilder.withProxiedUri(url);
  }

  private void resetMenu(String appId) {
    if (equoAppBuilder.getmWindow().getMainMenu() == null) {
      MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
      mainMenu.setElementId(appId + "." + "mainmenu");
      equoAppBuilder.getmWindow().setMainMenu(mainMenu);
    } else {
      MenuModelHelper.getInstance().removeRecursively(equoAppBuilder.getmWindow().getMainMenu(),
          optionalViewBuilder);
    }
  }

  private void createMainPartIfNotPresent() {
    MTrimmedWindow mainWindow = equoAppBuilder.getmWindow();
    List<MWindowElement> windowElements = mainWindow.getChildren();
    MPart mainPart = null;
    Optional<MWindowElement> optionalMainPart =
        windowElements.stream().filter((elem) -> MAIN_PART_ID.equals(elem.getElementId()))
            .filter((elem) -> elem instanceof MPart).findFirst();
    if (optionalMainPart.isPresent()) {
      mainPart = (MPart) optionalMainPart.get();
    } else {
      Optional<List<MStackElement>> optionalMainStackPart = mainWindow.getChildren().stream()
          .filter((elem) -> elem instanceof MPartSashContainer)
          .map((sash) -> ((MPartSashContainer) sash).getChildren())
          .filter((sashElem) -> sashElem instanceof MPartStack)
          .map((partStack) -> ((MPartStack) partStack).getChildren())
          .filter((stackElem) -> MAIN_PART_ID.equals(((MStackElement) stackElem).getElementId()))
          .filter((part) -> part instanceof MPart).findFirst();
      if (optionalMainStackPart.isPresent()) {
        mainPart = (MPart) optionalMainStackPart.get();
      }
    }
    if (mainPart != null) {
      part = mainPart;
      return;
    }

    part = MBasicFactory.INSTANCE.createPart();
    part.setElementId(IConstants.MAIN_PART_ID);
    part.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);

    // Get the Window binding context.
    MBindingContext mBindingContext =
        equoAppBuilder.getmApplication().getRootContext().get(0).getChildren().get(0);
    part.getBindingContexts().add(mBindingContext);

    mainPartBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
    mainPartBindingTable.setBindingContext(mBindingContext);
    mainPartBindingTable.setElementId(DEFAULT_BINDING_TABLE);
    equoAppBuilder.getmApplication().getBindingTables().add(mainPartBindingTable);

    equoAppBuilder.getmWindow().getChildren().add(part);
  }

  private String normalizeUrl(String url) {
    String protocolURl = url.substring(0, 4).toLowerCase();
    if (!protocolURl.equals("http")) {
      url = "http://" + url;
    }
    // if there is no connection, convert the url from https to http
    if (!equoServer.isAddressReachable(url) && url.startsWith("https")) {
      url = url.replace("https", "http");
    }
    if (url.endsWith("/")) {
      return url;
    } else {
      return url + "/";
    }
  }

  MBindingTable getBindingTable() {
    return mainPartBindingTable;
  }

  EquoApplicationBuilder getEquoApplicationBuilder() {
    return equoAppBuilder;
  }

  MPart getPart() {
    return part;
  }

  String getUrl() {
    return url;
  }

  /**
   * Launch app.
   */
  public EquoApplicationBuilder start() {
    if (analyticsService != null) {
      analyticsService.registerLaunchApp();
    }
    offlineSupportBuilder.build();
    mainAppBuilder.build();
    return this.equoAppBuilder;
  }

}
