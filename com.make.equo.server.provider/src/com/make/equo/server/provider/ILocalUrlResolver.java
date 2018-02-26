package com.make.equo.server.provider;

import java.net.URL;

public interface ILocalUrlResolver {

	String getProtocol();

	URL resolve(String pathToResolve);

}
