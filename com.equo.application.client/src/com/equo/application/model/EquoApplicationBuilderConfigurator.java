package com.equo.application.model;

import com.equo.application.api.IEquoApplication;

/**
 * Configurator class to set things up in the app life cycle.
 */
public class EquoApplicationBuilderConfigurator {

  private EquoApplicationBuilder equoApplicationBuilder;
  private EquoApplicationModel equoApplicationModel;
  private IEquoApplication equoApp;

  /**
   * Configurator constructor.
   */
  public EquoApplicationBuilderConfigurator(EquoApplicationModel equoApplicationModel,
      EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
    this.equoApplicationModel = equoApplicationModel;
    this.equoApplicationBuilder = equoApplicationBuilder;
    this.equoApp = equoApp;
  }

  public OptionalViewBuilder configure() {
    return this.equoApplicationBuilder.configure(equoApplicationModel, equoApp);
  }
}
