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

package com.equo.contribution.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The resolution of a contribution which provides the HTML code to inject the
 * contributed scripts and styles to the app.
 */
public class ResolvedContribution {

  private List<String> scripts;
  private List<String> styles;
  private Map<String, String> urlsToScripts;
  private Map<String, String> urlsToStyles;

  /**
   * Parameterized constructor.
   */
  public ResolvedContribution(List<String> scripts, List<String> styles,
      Map<String, String> urlsToScripts, Map<String, String> urlsToStyles) {
    this.scripts = scripts;
    this.styles = styles;
    this.urlsToScripts = urlsToScripts;
    this.urlsToStyles = urlsToStyles;
  }

  public List<String> getScripts() {
    return new ArrayList<String>(scripts);
  }

  public List<String> getStyles() {
    return new ArrayList<String>(styles);
  }

  public Map<String, String> getUrlsToScripts() {
    return new HashMap<String, String>(urlsToScripts);
  }

  public Map<String, String> getUrlsToStyles() {
    return new HashMap<String, String>(urlsToStyles);
  }

  public String getCustomScripts(String url) {
    return getResource(urlsToScripts, url);
  }

  public String getCustomStyles(String url) {
    return getResource(urlsToStyles, url);
  }

  private String getResource(Map<String, String> map, String url) {
    if (map.containsKey(url)) {
      return map.get(url);
    }
    if (map.containsKey(url + "/")) {
      return map.get(url + "/");
    }
    URI uri = URI.create(url);
    String key = (uri.getScheme() + "://" + uri.getAuthority() + uri.getPath()).toLowerCase();
    if (!map.containsKey(key)) {
      key = (uri.getAuthority() + uri.getPath()).toLowerCase();
    }
    return resourcesOrEmptyString(map.get(key));
  }

  private String resourcesOrEmptyString(String resources) {
    if (resources != null) {
      return resources;
    }
    return "";
  }

}
