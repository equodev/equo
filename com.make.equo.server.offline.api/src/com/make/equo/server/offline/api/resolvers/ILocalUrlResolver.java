package com.make.equo.server.offline.api.resolvers;

import java.net.URL;

public interface ILocalUrlResolver {

	String getProtocol();

	URL resolve(String pathToResolve);

}
