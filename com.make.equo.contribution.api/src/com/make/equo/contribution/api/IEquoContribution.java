package com.make.equo.contribution.api;

import java.net.URL;
import java.util.Map;

public interface IEquoContribution {

	/**
	 * Returns the Javascript API that this Equo contribution provides.
	 * 
	 * @return the URL to the Javascript API resource
	 */
	URL getJavascriptAPIResource();

	/**
	 * Return the properties of this Equo contribution that might be needed by a
	 * service consumer.
	 * 
	 * @return the properties of this Equo contribution.
	 */
	Map<String, Object> getProperties();

}
