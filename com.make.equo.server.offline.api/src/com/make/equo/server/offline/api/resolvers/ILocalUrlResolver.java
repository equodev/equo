package com.make.equo.server.offline.api.resolvers;

import java.net.URL;

public interface ILocalUrlResolver {

	default String getProtocol() {
		return "";
	};

	URL resolve(String pathToResolve);

}
