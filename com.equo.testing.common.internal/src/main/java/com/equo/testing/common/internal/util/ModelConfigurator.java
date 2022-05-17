/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

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
