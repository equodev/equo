package com.make.equo.renderers;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import com.make.equo.renderers.contributions.EquoRenderersContribution;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.equo.ws.api.StringPayloadEquoRunnable;
import com.make.swtcef.Chromium;

public interface IEquoRenderer {

	static final String EQUO_RENDERERS_URL = "http://equo_renderers";

	/**
	 * The initial point to start an Equo render process. It must be call after the
	 * creation of a Chromium browser.
	 * 
	 * @param parent
	 */
	default void configureAndStartRenderProcess(Composite parent) {
		Chromium browser = createBrowserComponent(parent);
		getEquoProxyServer().addUrl(EQUO_RENDERERS_URL);

		String renderersContributionPath = getEquoProxyServer().getEquoContributionPath()
				+ EquoRenderersContribution.TYPE + "/";

		List<String> jsScriptsFilesForRendering = getJsFileNamesForRendering();

		for (String fileName : jsScriptsFilesForRendering) {
			String e4ElemmentContributionUri = renderersContributionPath + fileName;
			getEquoProxyServer().addCustomScript(EQUO_RENDERERS_URL, e4ElemmentContributionUri);
		}

		String renderersUri = EQUO_RENDERERS_URL + "/" + getEquoProxyServer().getEquoContributionPath()
				+ EquoRenderersContribution.TYPE;

		String namespace = getNamespace();
		browser.setUrl(renderersUri + "?" + "namespace=" + namespace);

		List<Map<String, String>> e4Model = getEclipse4Model();
		sendEclipse4Model(e4Model);
		onActionPerformedOnElement();
	}

	default void sendEclipse4Model(List<Map<String, String>> e4Model) {
		String namespace = getNamespace();
		EquoEventHandler equoEventHandler = getEquoEventHandler();
		equoEventHandler.on(namespace + "getModel", (StringPayloadEquoRunnable stringPayloadEquoRunnable) -> {
			equoEventHandler.send(namespace + "model", e4Model);
		});
	};

	/**
	 * Receives a message when an action is performed on an element in the
	 * Javascript side(i.e. click on a toolbar item).
	 */
	void onActionPerformedOnElement();

	EquoEventHandler getEquoEventHandler();

	List<Map<String, String>> getEclipse4Model();

	/**
	 * Returns a list of custom Javascript file names which are used to render an
	 * element. These scripts will be processed by the Equo Proxy and added to the
	 * base Equo Renderer file.
	 * 
	 * @return a list of Javascript files
	 */
	List<String> getJsFileNamesForRendering();

	/**
	 * Returns a unique namespace for the element to be rendered.
	 * 
	 * @return a namespace
	 */
	String getNamespace();

	IEquoServer getEquoProxyServer();

	Chromium createBrowserComponent(Composite parent);
}
