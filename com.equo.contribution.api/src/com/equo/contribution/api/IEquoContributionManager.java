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

package com.equo.contribution.api;

import java.util.List;

/**
 * Interface for contribution manager. The manager will concentrate all the
 * added contributions and enable them to be obtained.
 */
public interface IEquoContributionManager {

  EquoContribution getContribution(String contributionName);

  ResolvedContribution resolve(EquoContribution contribution);

  ResolvedContribution getResolvedContributions();

  List<String> getContributionProxiedUris();

  List<Runnable> getContributionStartProcedures();

  boolean contains(String contributionName);

  void addContribution(EquoContribution contribution);

}
