package com.equo.contribution.provider;

import static com.equo.contribution.api.IEquoContributionConstants.OFFLINE_SUPPORT_CONTRIBUTION_NAME;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.littleshoot.proxy.HttpFilters;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.IEquoContributionManager;
import com.equo.contribution.api.ResolvedContribution;
import com.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;
import com.equo.contribution.provider.filter.ContributionFileRequestFiltersAdapter;
import com.equo.contribution.provider.filter.DefaultContributionRequestFiltersAdapter;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Default request handler.
 */
@Component
public class DefaultEquoContributionRequestHandler implements IEquoContributionRequestHandler {
  protected static final Logger logger =
      LoggerFactory.getLogger(DefaultEquoContributionRequestHandler.class);

  private static final String limitedConnectionGenericPageFilePath = "/limitedConnectionPage.html";

  @Reference
  private IEquoContributionManager manager;

  @Override
  public HttpFilters handle(HttpRequest request) {
    try {
      URI uri = URI.create(request.getUri());
      Optional<String> key = getContributionKeyIfPresent(uri);
      if (key.isPresent()) {
        EquoContribution contribution = manager.getContribution(key.get());
        if (contribution.hasCustomFiltersAdapter()) {
          return contribution.getFiltersAdapter(request);
        } else {
          IHttpRequestFilter filter = contribution.getFilter();
          if (filter != null) {
            request = filter.applyFilter(request);
          }

          if (contribution.accepts(request, uri)) {
            return new ContributionFileRequestFiltersAdapter(request, contribution.getUrlResolver(),
                contribution.getContributionName());
          }

          return new DefaultContributionRequestFiltersAdapter(request, getResolvedContributions(),
              contribution.getUrlResolver(), contribution.getContributedResourceName());
        }
      }
    } catch (IllegalArgumentException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  @Override
  public ResolvedContribution getResolvedContributions() {
    return manager.getResolvedContributions();
  }

  @Override
  public List<String> getContributionProxiedUris() {
    return manager.getContributionProxiedUris();
  }

  @Override
  public HttpFilters handleOffline(HttpRequest request) {
    EquoContribution offlineContribution =
        manager.getContribution(OFFLINE_SUPPORT_CONTRIBUTION_NAME);
    if (offlineContribution != null) {
      return new DefaultContributionRequestFiltersAdapter(request,
          manager.resolve(offlineContribution), offlineContribution.getUrlResolver(),
          offlineContribution.getContributedResourceName());
    } else {
      return new DefaultContributionRequestFiltersAdapter(request,
          new ResolvedContribution(null, null, null, null),
          new EquoGenericUrlResolver(DefaultEquoContributionRequestHandler.class.getClassLoader()),
          limitedConnectionGenericPageFilePath);
    }

  }

  private Optional<String> getContributionKeyIfPresent(URI uri) {
    String[] path = uri.getPath().split("/");
    for (String s : path) {
      if (manager.contains(s)) {
        return Optional.of(s);
      }
    }
    if (manager.contains(uri.getAuthority())) {
      return Optional.of(uri.getAuthority());
    }
    return Optional.empty();
  }

}
