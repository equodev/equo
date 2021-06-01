package com.equo.contribution.api.handler;

import java.util.List;

import org.littleshoot.proxy.HttpFilters;

import com.equo.contribution.api.ResolvedContribution;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Interface for request handlers.
 */
public interface IEquoContributionRequestHandler {

  HttpFilters handle(HttpRequest request);

  HttpFilters handleOffline(HttpRequest request);

  ResolvedContribution getResolvedContributions();

  List<String> getContributionProxiedUris();

}
