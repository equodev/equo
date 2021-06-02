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

package com.equo.application;

import javax.inject.Inject;

import org.osgi.service.component.annotations.Component;

import com.equo.aer.internal.api.IEquoCrashReporter;

/**
 * Provides loaded implementation of the optional Equo modules.
 *
 */
@Component
public class DependencyInjector implements IDependencyInjector {
  @Inject
  private IEquoCrashReporter equoCrashReporter;

  @Override
  public IEquoCrashReporter getEquoCrashReporter() {
    return equoCrashReporter;
  }

}
