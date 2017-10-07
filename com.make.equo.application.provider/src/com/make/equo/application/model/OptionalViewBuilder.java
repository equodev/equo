package com.make.equo.application.model;

public class OptionalViewBuilder extends OptionalFieldBuilder {

	private UrlMandatoryBuilder urlMandatoryBuilder;

	public OptionalViewBuilder(UrlMandatoryBuilder urlMandatoryBuilder) {
		super(urlMandatoryBuilder.getEquoApplicationBuilder());
		this.urlMandatoryBuilder = urlMandatoryBuilder;
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable) {
		EquoApplicationBuilder equoAppBuilder = this.urlMandatoryBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.urlMandatoryBuilder.getPart().getElementId(), runnable)
				.addGlobalShortcut(keySequence);
		return this;
	}

}
