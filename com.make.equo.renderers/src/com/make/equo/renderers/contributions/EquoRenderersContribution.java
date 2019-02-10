package com.make.equo.renderers.contributions;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.google.common.collect.Lists;
import com.make.equo.contribution.api.IEquoContribution;

@Component(name = "equoRenderersContribution", property = { "type=renderersContribution" })
public class EquoRenderersContribution implements IEquoContribution {

	public static final String TYPE = "renderersContribution";

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
		return Lists.newArrayList();
	}
}
