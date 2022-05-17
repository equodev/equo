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

package com.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;

/**
 * Model of the curennt Equo app. Used to easily access to app elements.
 */
public class EquoApplicationModel {

  private static EquoApplicationModel currentModel;

  private MApplication mainApplication;

  public EquoApplicationModel() {
    EquoApplicationModel.currentModel = this;
  }

  public MApplication getMainApplication() {
    return mainApplication;
  }

  public void setMainApplication(MApplication mainApplication) {
    this.mainApplication = mainApplication;
  }

  public static EquoApplicationModel getApplicaton() {
    return currentModel;
  }

}
