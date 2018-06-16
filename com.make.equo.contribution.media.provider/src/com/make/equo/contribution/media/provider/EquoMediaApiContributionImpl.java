package com.make.equo.contribution.media.provider;

import java.net.URL;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.make.equo.contribution.api.IEquoContribution;

@Component(name = "equoMediaContribution", property = { "type=equoMediaContribution" })
public class EquoMediaApiContributionImpl implements IEquoContribution {

	private static final String mediaJsApi = "media.js";

	@Override
	public URL getJavascriptAPIResource() {
		return this.getClass().getClassLoader().getResource(mediaJsApi);
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public boolean containsJavascriptApi() {
		return true;
	}

}
