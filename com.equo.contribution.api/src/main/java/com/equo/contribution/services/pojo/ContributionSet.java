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

package com.equo.contribution.services.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO for JSON config file.
 */
public class ContributionSet {

  String limitedConnectionPagePath;

  List<ConfigContribution> contributions = new ArrayList<>();

  public String getLimitedConnectionPagePath() {
    return limitedConnectionPagePath;
  }

  public void setLimitedConnectionPagePath(String limitedConnectionPagePath) {
    this.limitedConnectionPagePath = limitedConnectionPagePath;
  }

  public List<ConfigContribution> getContributions() {
    return contributions;
  }

  public void setContributions(List<ConfigContribution> contributions) {
    this.contributions = contributions;
  }

}
