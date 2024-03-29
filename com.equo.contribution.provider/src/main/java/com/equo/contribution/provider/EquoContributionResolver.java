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

package com.equo.contribution.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.ResolvedContribution;
import com.equo.contribution.provider.cache.ResolvedContributionsCache;

/**
 * Contributions resolver. Generates the HTML code to include the contributed
 * files into the app.
 */
@Component(service = EquoContributionResolver.class)
public class EquoContributionResolver {

  private static final String URL_PATH = "urlPath";
  private static final String PATH_TO_STRING_REG = "PATHTOSTRING";
  private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
  private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"PATHTOSTRING\"></script>";
  private static final String URL_CSS_SENTENCE =
      "<link type=\"text/css\" rel=\"stylesheet\" href=\"urlPath\">";
  private static final String LOCAL_CSS_SENTENCE =
      "<link type=\"text/css\" rel=\"stylesheet\" href=\"PATHTOSTRING\">";

  private List<String> scripts;
  private List<String> styles;
  private Map<String, String> urlsToScripts;
  private Map<String, String> urlsToStyles;

  private List<String> proxiedUris;

  @Reference
  private ResolvedContributionsCache cache;

  /**
   * Parameterized constructor.
   */
  public EquoContributionResolver() {
    this.scripts = new ArrayList<String>();
    this.styles = new ArrayList<String>();
    this.proxiedUris = new ArrayList<String>();
    this.urlsToScripts = new HashMap<String, String>();
    this.urlsToStyles = new HashMap<String, String>();
  }

  /**
   * Resolves the contribution given by parameter.
   * @return the resolved contribution
   */
  public ResolvedContribution resolve(EquoContribution contribution) {
    ResolvedContribution resolvedContribution = cache.getCached(contribution);
    if (resolvedContribution == null) {
      List<String> scripts = new ArrayList<String>();
      List<String> styles = new ArrayList<String>();
      Map<String, String> urlsToScripts = new HashMap<String, String>();
      Map<String, String> urlsToStyles = new HashMap<String, String>();

      resolvedContribution = doResolve(contribution, scripts, styles, urlsToScripts, urlsToStyles);
      addResolvedContribution(resolvedContribution);
      cache.put(contribution, resolvedContribution);
    }
    return resolvedContribution;
  }

  protected ResolvedContribution doResolve(EquoContribution contribution, List<String> scripts,
      List<String> styles, Map<String, String> urlsToScripts, Map<String, String> urlsToStyles) {
    addEquoContributionJsAndCss(contribution, scripts, styles);
    addEquoContributionProxiedUris(contribution);
    addEquoContributionCustomScripts(contribution, urlsToScripts);
    addEquoContributionCustomStyles(contribution, urlsToStyles);
    return new ResolvedContribution(scripts, styles, urlsToScripts, urlsToStyles);
  }

  public ResolvedContribution getResolvedContributions() {
    return new ResolvedContribution(scripts, styles, urlsToScripts, urlsToStyles);
  }

  public List<String> getContributionProxiedUris() {
    return new ArrayList<String>(proxiedUris);
  }

  private void handleResourceAdd(String url, String generatedResourceSentence,
      Map<String, String> existingResourceMap) {

    if (!existingResourceMap.containsKey(url)) {
      existingResourceMap.put(url, generatedResourceSentence);
    } else {
      String existingResources = existingResourceMap.get(url);
      if (!existingResources.contains(generatedResourceSentence)) {
        existingResourceMap.put(url, appendToExistingOnes(existingResources,
            generatedResourceSentence, existingResourceMap));
      }
    }
  }

  private String generateSentence(String scriptPath, String baseSentence, String localSentence) {
    try {
      URL url = new URL(scriptPath);
      String scriptSentence = baseSentence.replaceAll(URL_PATH, url.toString());
      return scriptSentence;
    } catch (MalformedURLException e) {
      return createLocalSentence(scriptPath, localSentence);
    }
  }

  private String createLocalSentence(String scriptPath, String localSentence) {
    String scriptSentence = localSentence.replaceAll(PATH_TO_STRING_REG, scriptPath);
    return scriptSentence;
  }

  private String appendToExistingOnes(String url, String generatedSentence,
      Map<String, String> existingResources) {
    String existingResource = existingResources.get(url);
    StringBuilder result = new StringBuilder();
    result.append(existingResource);
    result.append("\n");
    result.append(generatedSentence);
    return result.toString();
  }

  private void addEquoContributionJsAndCss(EquoContribution contribution, List<String> scripts,
      List<String> styles) {
    List<String> javascriptFilesNames = contribution.getContributedScripts();
    if (!javascriptFilesNames.isEmpty()) {
      List<String> result = javascriptFilesNames.stream().map(input -> {
        try {
          URL url = new URL(input);
          String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
          return scriptSentence;
        } catch (MalformedURLException e) {
          return createLocalSentence(contribution.getContributionName() + "/" + input,
              LOCAL_SCRIPT_SENTENCE);
        }
      }).collect(Collectors.toList());
      scripts.addAll(new ArrayList<String>(result));
    }
    List<String> cssFilesNames = contribution.getContributedStyles();
    if (!cssFilesNames.isEmpty()) {
      List<String> result = cssFilesNames.stream().map(input -> {
        try {
          URL url = new URL(input);
          String styleSentence = URL_CSS_SENTENCE.replaceAll(URL_PATH, url.toString());
          return styleSentence;
        } catch (MalformedURLException e) {
          return createLocalSentence(contribution.getContributionName() + "/" + input,
              LOCAL_CSS_SENTENCE);
        }
      }).collect(Collectors.toList());
      styles.addAll(new ArrayList<String>(result));
    }
  }

  private void addEquoContributionProxiedUris(EquoContribution contribution) {
    proxiedUris.addAll(contribution.getProxiedUris());
  }

  private void addEquoContributionCustomScripts(EquoContribution contribution,
      Map<String, String> urlsToScripts) {
    for (Entry<String, String> entry : contribution.getPathsToScripts().entrySet()) {
      proxiedUris.add(contribution.getContributionName() + "/" + entry.getKey());
      addCustomScript(contribution.getContributionName() + "/" + entry.getKey(), entry.getValue(),
          urlsToScripts);
    }
  }

  private void addEquoContributionCustomStyles(EquoContribution contribution,
      Map<String, String> urlsToStyles) {
    for (Entry<String, String> entry : contribution.getPathsToStyles().entrySet()) {
      proxiedUris.add(contribution.getContributionName() + "/" + entry.getKey());
      addCustomStyle(contribution.getContributionName() + "/" + entry.getKey(), entry.getValue(),
          urlsToStyles);
    }
  }

  public void addCustomScript(String url, String scriptUrl, Map<String, String> urlsToScripts) {
    addCustomResourceToUrl(url, scriptUrl, URL_SCRIPT_SENTENCE, LOCAL_SCRIPT_SENTENCE,
        urlsToScripts);
  }

  public void addCustomStyle(String url, String styleUrl, Map<String, String> urlsToStyles) {
    addCustomResourceToUrl(url, styleUrl, URL_CSS_SENTENCE, LOCAL_CSS_SENTENCE, urlsToStyles);
  }

  private void addCustomResourceToUrl(String url, String resourceUrl, String baseSentence,
      String localSentence, Map<String, String> resourceMap) {
    String lowerCaseUrl = url.toLowerCase();
    String generatedResourceSentence = generateSentence(resourceUrl, baseSentence, localSentence);
    handleResourceAdd(lowerCaseUrl, generatedResourceSentence, resourceMap);
  }

  protected ResolvedContribution
      addResolvedContribution(ResolvedContribution resolvedContribution) {
    scripts.addAll(resolvedContribution.getScripts());
    styles.addAll(resolvedContribution.getStyles());
    for (Map.Entry<String, String> urlToScriptEntry : resolvedContribution.getUrlsToScripts()
        .entrySet()) {
      handleResourceAdd(urlToScriptEntry.getKey(), urlToScriptEntry.getValue(), urlsToScripts);
    }
    for (Map.Entry<String, String> urlToStyleEntry : resolvedContribution.getUrlsToStyles()
        .entrySet()) {
      handleResourceAdd(urlToStyleEntry.getKey(), urlToStyleEntry.getValue(), urlsToStyles);
    }
    return new ResolvedContribution(scripts, styles, urlsToScripts, urlsToStyles);
  }
}
