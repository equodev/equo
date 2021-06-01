package com.equo.contribution.api.resolvers;

import java.net.URI;
import java.net.URL;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Interface for URL resolver.
 */
public interface IEquoContributionUrlResolver {

  URL resolve(String pathToResolve);

  /**
   * Determines if the URI is accepted by current resolver.
   * @return true if accepted, false otherwise
   */
  default boolean accepts(HttpRequest request, URI requestUri) {
    if (requestUri.getPath() != null && requestUri.getPath().matches(".*\\..+$")) {
      return true;
    }
    return false;
  }

}
