package com.make.equo.contribution.api;

import java.net.URL;
import java.util.List;
import java.util.Map;

public interface IEquoContribution {

	/**
	 * Returns the Javascript API that this Equo contribution provides.
	 * 
	 * @return the URL to the Javascript API resource
	 */
	URL getJavascriptAPIResource(String name);

	/**
	 * Return the properties of this Equo contribution that might be needed by a
	 * service consumer.
	 * 
	 * @return the properties of this Equo contribution.
	 */
	Map<String, Object> getProperties();

	/**
	 * Returns an ordered list of Javascript APIs files names. An ordered list means
	 * that if a Javascript file depends on another Javascript file, it should be
	 * added to the list after the file it depends on.
	 * 
	 * @return a list of Javascript files names. An empty list if this contribution
	 *         does not contribute any Javascript API.
	 */
	List<String> getJavascriptFileNames();

}
