package com.make.equo.contribution.provider;

import static com.make.equo.contribution.api.IEquoContributionConstants.OFFLINE_SUPPORT_CONTRIBUTION_NAME;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.littleshoot.proxy.HttpFilters;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContribution;
import com.make.equo.contribution.api.IEquoContributionManager;
import com.make.equo.contribution.api.ResolvedContribution;
import com.make.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.contribution.provider.filter.ContributionFileRequestFiltersAdapter;
import com.make.equo.contribution.provider.filter.DefaultContributionRequestFiltersAdapter;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpRequest;

@Component
public class DefaultEquoContributionRequestHandler implements IEquoContributionRequestHandler {

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
			System.out.println(e.getMessage());
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
		EquoContribution offlineContribution = manager.getContribution(OFFLINE_SUPPORT_CONTRIBUTION_NAME);
		if (offlineContribution != null) {
			return new DefaultContributionRequestFiltersAdapter(request, manager.resolve(offlineContribution),
					offlineContribution.getUrlResolver(), offlineContribution.getContributedResourceName());
		} else {
			return new DefaultContributionRequestFiltersAdapter(request,
					new ResolvedContribution(null, null, null, null),
					new EquoGenericURLResolver(DefaultEquoContributionRequestHandler.class.getClassLoader()),
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
