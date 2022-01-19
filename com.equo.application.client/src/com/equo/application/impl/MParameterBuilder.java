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

package com.equo.application.impl;

import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;

/**
 * Builder to create a new command parameter.
 */
public interface MParameterBuilder {

  /**
   * Creates a new parameter.
   * @param  id    parameter id
   * @param  value parameter value
   * @return       new created parameter
   */
  default MParameter createMParameter(String id, String value) {
    MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
    // set the identifier for the corresponding command parameter
    parameter.setName(id);
    parameter.setValue(value);
    return parameter;
  }

}
