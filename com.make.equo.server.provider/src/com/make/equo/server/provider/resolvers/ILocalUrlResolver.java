package com.make.equo.server.provider.resolvers;

import java.net.URL;

public interface ILocalUrlResolver {

	String getProtocol();

	URL resolve(String pathToResolve);

}
