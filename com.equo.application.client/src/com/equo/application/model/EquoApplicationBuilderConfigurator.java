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
