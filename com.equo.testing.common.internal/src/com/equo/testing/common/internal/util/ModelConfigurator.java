package com.equo.testing.common.internal.util;

import org.eclipse.e4.ui.model.application.MApplication;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;
import com.equo.application.model.EquoApplicationBuilderConfigurator;
import com.equo.application.model.EquoApplicationModel;

/**
 * Utility methods to be used in testing for setting up an E4 model from an
 * IEquoApplication instance.
 */
public class ModelConfigurator {

  MApplication app;

  public ModelConfigurator(MApplication app) {
    this.app = app;
  }

  /**
   * Receives instances of IEquoApplication and EquoApplicationBuilder, builds a
   * MApplication instance and configures the OptionalViewBuilder with them.
   * @param equoApplicationBuilder to use
   * @param equoApp                to use
   */
  public void configure(EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
    EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
    equoApplicationModel.setMainApplication(app);
    EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator =
        new EquoApplicationBuilderConfigurator(equoApplicationModel, equoApplicationBuilder,
            equoApp);
    equoApplicationBuilderConfigurator.configure();
  }

}
