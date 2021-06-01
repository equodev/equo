package com.equo.server.offline.api.resolvers;

import java.net.URL;

/**
 * Interface for URL resolver in offline mode.
 */
public interface ILocalUrlResolver {

  String getProtocol();

  URL resolve(String pathToResolve);

}
