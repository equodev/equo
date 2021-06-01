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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Equo contributions model.
 */
public class EquoContribution {

  private final IEquoContributionManager manager;
  private final IEquoContributionUrlResolver urlResolver;
  private final IFiltersAdapterHandler filtersAdapterHandler;

  private final String contributedHtmlName;
  private final String contributionName;

  private final List<String> proxiedUris;
  private final List<String> contributedScripts;
  private final List<String> contributedStyles;
  private final List<String> excludedResources;

  private final Map<String, String> pathsToScripts;
  private final Map<String, String> pathsToStyles;

  private final IHttpRequestFilter filter;

  private final Runnable runnableAtStart;

  /**
   * Constructor with all the contributions elements. Used by the Contribution Builder.
   */
  public EquoContribution(IEquoContributionManager manager,
      IEquoContributionUrlResolver urlResolver, IFiltersAdapterHandler filtersAdapterHandler,
      String contributedHtmlName, String contributionName, List<String> proxiedUris,
      List<String> contributedScripts, List<String> contributedStyles,
      List<String> excludedResources, Map<String, String> pathsToScripts,
      Map<String, String> pathsToStyles, IHttpRequestFilter filter, Runnable runnableAtStart) {
    this.manager = manager;
    this.urlResolver = urlResolver;
    this.filtersAdapterHandler = filtersAdapterHandler;
    this.contributedHtmlName = contributedHtmlName;
    this.contributionName = contributionName.toLowerCase();
    this.proxiedUris = proxiedUris;
    this.contributedScripts = contributedScripts;
    this.contributedStyles = contributedStyles;
    this.excludedResources = excludedResources;
    this.pathsToScripts = pathsToScripts;
    this.pathsToStyles = pathsToStyles;
    this.filter = filter;
    this.runnableAtStart = runnableAtStart;
    addToManager();
  }

  public String getContributedResourceName() {
    return contributedHtmlName;
  }

  public String getContributionName() {
    return contributionName;
  }

  public List<String> getProxiedUris() {
    return new ArrayList<String>(proxiedUris);
  }

  public List<String> getContributedScripts() {
    return new ArrayList<String>(contributedScripts);
  }

  public List<String> getContributedStyles() {
    return new ArrayList<String>(contributedStyles);
  }

  public List<String> getExcludedResources() {
    return new ArrayList<String>(excludedResources);
  }

  public Map<String, String> getPathsToScripts() {
    return new HashMap<String, String>(pathsToScripts);
  }

  public Map<String, String> getPathsToStyles() {
    return new HashMap<String, String>(pathsToStyles);
  }

  public IHttpRequestFilter getFilter() {
    return filter;
  }

  public IEquoContributionUrlResolver getUrlResolver() {
    return urlResolver;
  }

  public boolean hasCustomFiltersAdapter() {
    return filtersAdapterHandler != null;
  }

  /**
   * Gets a filter adapter for the given requests.
   * @param  request the request for which the filter will be returned
   * @return         a contributed filter for the request if it exists, or a
   *                 default one that does not perform any special operation
   */
  public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
    if (filtersAdapterHandler == null) {
      return HttpFiltersAdapter.NOOP_FILTER;
    }
    return filtersAdapterHandler.getFiltersAdapter(request);
  }

  public boolean accepts(HttpRequest request, URI requestUri) {
    return urlResolver.accepts(request, requestUri);
  }

  private void addToManager() {
    if (manager != null) {
      manager.addContribution(this);
    }
  }

  public Runnable getRunnableAtStart() {
    return runnableAtStart;
  }

}
