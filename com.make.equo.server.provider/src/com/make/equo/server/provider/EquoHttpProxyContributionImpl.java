package com.make.equo.server.provider;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.make.equo.contribution.api.IEquoContribution;

@Component(name = "equoProxyServerContribution", property = { "type=equoProxyServerContribution",
		"service.ranking:Integer=9999" })
public class EquoHttpProxyContributionImpl implements IEquoContribution {

	private static final String equoFrameworkJsApi = "equoFramework.js";
	private static final String domModifierJsApi = "domModifier.js";
	private static final String jqueryJsApi = "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";

	@Override
	public URL getJavascriptAPIResource(String name) {
		return this.getClass().getClassLoader().getResource(name);
	}

	// TODO check for the properties of the equo app to see if it change the html
	// content or not. Inject the Equo app in this component to check it. See #140
	// and #142.
	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public List<String> getJavascriptFileNames() {
		return Arrays.asList(jqueryJsApi, equoFrameworkJsApi, domModifierJsApi);
	}

}
