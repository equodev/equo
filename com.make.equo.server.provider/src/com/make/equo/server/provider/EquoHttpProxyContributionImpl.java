package com.make.equo.server.provider;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.contribution.api.IEquoContribution;

@Component(name = "equoProxyServerContribution", property = { "type=equoProxyServerContribution",
		"service.ranking:Integer=9999" })
public class EquoHttpProxyContributionImpl implements IEquoContribution {

	private static final String equoFrameworkJsApi = "equoFramework.js";
	private static final String domModifierJsApi = "domModifier.js";
	private static final String jqueryJsApi = "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";
	private boolean change_original_html;

	@Override
	public URL getJavascriptAPIResource(String name) {
		return this.getClass().getClassLoader().getResource(name);
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public List<String> getJavascriptFileNames() {
		List<String> javascriptNames = new ArrayList<>();
		javascriptNames.add(jqueryJsApi);
		javascriptNames.add(equoFrameworkJsApi);
		if (change_original_html) {
			javascriptNames.add(domModifierJsApi);
		}
		return javascriptNames;
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	void setEquoApplication(IEquoApplication equoApplication, Map<String, String> props) {
		String change_html = props.get("change_original_html");
		if (change_html != null && change_html.equals("true")) {
			this.change_original_html = true;
		} else {
			this.change_original_html = false;
		}
	}
}
