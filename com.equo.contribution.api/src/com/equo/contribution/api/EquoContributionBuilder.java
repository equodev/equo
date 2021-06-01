package com.equo.contribution.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.equo.server.offline.api.filters.IHttpRequestFilter;

/**
 * Serves as a builder for any external EquoContribution. It is recommended to
 * use OSGi Declarative Services to get an instance of this class to build their
 * own contribution.
 */
@Component(scope = ServiceScope.PROTOTYPE, service = EquoContributionBuilder.class)
public class EquoContributionBuilder {

  @Reference
  private IEquoContributionManager manager;

  private IEquoContributionUrlResolver urlResolver;
  private IFiltersAdapterHandler filtersAdapterHandler;

  private String contributedHtmlName;
  private String contributionName;

  private List<String> proxiedUris;
  private List<String> contributedScripts;
  private List<String> contributedStyles;
  private List<String> excludedResources;

  private Map<String, String> pathsToScripts;
  private Map<String, String> pathsToStyles;

  private IHttpRequestFilter filter;

  private Runnable runnableAtStart;

  /**
   * Builder constructor.
   */
  public EquoContributionBuilder() {
    this.proxiedUris = new ArrayList<String>();
    this.contributedScripts = new ArrayList<String>();
    this.contributedStyles = new ArrayList<String>();
    this.excludedResources = new ArrayList<String>();
    this.pathsToScripts = new HashMap<String, String>();
    this.pathsToStyles = new HashMap<String, String>();
    this.filter = ((originalRequest) -> {
      return originalRequest;
    });
    this.contributionName = "";
    this.contributedHtmlName = "";
    this.runnableAtStart = null;
  }

  public EquoContributionBuilder withManager(IEquoContributionManager manager) {
    this.manager = manager;
    return this;
  }

  /**
   * Defines the base URI of the contribution. Used to resolve resource locations
   * for this contribution. If not assigned a default value will be used.
   * @param  contributionName Name for this contribution.
   * @return                  this
   */
  public EquoContributionBuilder withContributionName(String contributionName) {
    this.contributionName = contributionName.toLowerCase();
    return this;
  }

  /**
   * Adds an URI to the contribution. These URIs will be proxied by the EquoServer
   * when accessed, adding all contributed scripts to it.
   * @param  proxiedUri URI to be proxied
   * @return            this
   */
  public EquoContributionBuilder withProxiedUri(String proxiedUri) {
    this.proxiedUris.add(proxiedUri);
    return this;
  }

  /**
   * Adds a script to a specific path to be handled by the contribution.
   * @param  path   Path for the script to be added to. Will be treated as
   *                relative to the contribution's name.
   * @param  script Name of the script file to be added.
   * @return        this
   */
  public EquoContributionBuilder withPathWithScript(String path, String script) {
    this.pathsToScripts.put(path, script);
    return this;
  }

  /**
   * Adds a style to a specific path to be handled by the contribution.
   * @param  path  Path for the style to be added to. Will be treated as relative
   *               to the contribution's name.
   * @param  style Name of the style file to be added.
   * @return       this
   */
  public EquoContributionBuilder withPathWithStyle(String path, String style) {
    this.pathsToStyles.put(path, style);
    return this;
  }

  /**
   * Defines an HTML resource to proxy for the contribution. A browser accessing
   * this contribution's base URI will be proxied using the file defined here as a
   * base.
   * @param  contributedHtmlName Name of the html file to be proxied.
   * @return                     this
   */
  public EquoContributionBuilder withBaseHtmlResource(String contributedHtmlName) {
    this.contributedHtmlName = contributedHtmlName;
    return this;
  }

  /**
   * Adds a script to the contribution. These scripts are global and can be used
   * by other contributions of the framework.
   * @param  script Name of the script file to be added.
   * @return        this
   */
  public EquoContributionBuilder withScriptFile(String script) {
    this.contributedScripts.add(script);
    return this;
  }

  /**
   * Adds a list of scripts to the contribution. These scripts are global and can
   * be used by other contributions of the framework.
   * @param  scripts List of names of the script files to be added.
   * @return         this
   */
  public EquoContributionBuilder withScriptFiles(List<String> scripts) {
    this.contributedScripts.addAll(scripts);
    return this;
  }

  /**
   * Adds a style to the contribution. These styles are global and can be used by
   * other contributions of the framework.
   * @param  style Name of the style file to be added.
   * @return       this
   */
  public EquoContributionBuilder withStyleFile(String style) {
    this.contributedStyles.add(style);
    return this;
  }

  /**
   * Adds a list of styles to the contribution. These styles are global and can be
   * used by other contributions of the framework.
   * @param  styles List of names of the style files to be added.
   * @return        this
   */
  public EquoContributionBuilder withStyleFiles(List<String> styles) {
    this.contributedStyles.addAll(styles);
    return this;
  }

  /**
   * Changes the URL resolver of the contribution. The URL resolver is required by
   * the framework to process file requests to the contribution.
   * @param  urlResolver the new URL resolver
   * @return             this
   */
  public EquoContributionBuilder withUrlResolver(IEquoContributionUrlResolver urlResolver) {
    this.urlResolver = urlResolver;
    return this;
  }

  /**
   * Changes the custom filter of the contribution. Default is a no-op filter.
   * This filter is called before any resource handling is attempted by the
   * framework.
   * @param  filter the new request filter
   * @return        this
   */
  public EquoContributionBuilder withFilter(IHttpRequestFilter filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Changes the adapter handler of the contribution. The handler is called by the
   * framework with the original request to be proxied and the request's response
   * is delegated to the adapter returned by this handler.
   * @param  filtersAdapterHandler the new adapter handler
   * @return                       this
   */
  public EquoContributionBuilder
      withFiltersAdapterHandler(IFiltersAdapterHandler filtersAdapterHandler) {
    this.filtersAdapterHandler = filtersAdapterHandler;
    return this;
  }

  /**
   * Adds a procedure that will run when the application starts.
   * @param  startProcedure the new procedure to run at application start
   * @return                this
   */
  public EquoContributionBuilder withStartProcedure(Runnable startProcedure) {
    this.runnableAtStart = startProcedure;
    return this;
  }

  /**
   * Builds the contribution defined by this builder.
   * @return an EquoContribution instance.
   */
  public EquoContribution build() {
    return new EquoContribution(manager, urlResolver, filtersAdapterHandler, contributedHtmlName,
        contributionName, proxiedUris, contributedScripts, contributedStyles, excludedResources,
        pathsToScripts, pathsToStyles, filter, runnableAtStart);
  }

}
