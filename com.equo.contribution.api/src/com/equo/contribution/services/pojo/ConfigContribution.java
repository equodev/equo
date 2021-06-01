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

package com.equo.contribution.services.pojo;

import java.util.List;
import java.util.Map;

/**
 * POJO for each contribution of the JSON contribution config.
 */
public class ConfigContribution {

  private String contributionName;
  private String contributionHtmlName;
  private List<String> proxiedUris;
  private List<String> contributedScripts;
  private Map<String, String> pathsWithScripts;

  public String getContributionName() {
    return contributionName;
  }

  public void setContributionName(String contributionName) {
    this.contributionName = contributionName;
  }

  public String getContributionHtmlName() {
    return contributionHtmlName;
  }

  public void setContributionHtmlName(String contributionHtmlName) {
    this.contributionHtmlName = contributionHtmlName;
  }

  public List<String> getProxiedUris() {
    return proxiedUris;
  }

  public void setProxiedUris(List<String> proxiedUris) {
    this.proxiedUris = proxiedUris;
  }

  public List<String> getContributedScripts() {
    return contributedScripts;
  }

  public void setContributedScripts(List<String> contributedScripts) {
    this.contributedScripts = contributedScripts;
  }

  public Map<String, String> getPathsWithScripts() {
    return pathsWithScripts;
  }

  public void setPathsWithScripts(Map<String, String> pathsWithScripts) {
    this.pathsWithScripts = pathsWithScripts;
  }

  /**
   * Verifies that there is content in the contribution.
   * @return true if there is no content (empty), false otherwise
   */
  public boolean isEmpty() {
    return ((this.getContributionName() == null) && (this.getContributionHtmlName() == null)
        && (this.getProxiedUris() == null) && (this.getContributedScripts() == null)
        && (this.getPathsWithScripts() == null));
  }

}
