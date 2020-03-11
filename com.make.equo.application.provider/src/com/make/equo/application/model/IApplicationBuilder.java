package com.make.equo.application.model;

import java.net.URISyntaxException;

public interface IApplicationBuilder {

	OptionalViewBuilder plainApp(String baseHtmlFile) throws URISyntaxException;

}