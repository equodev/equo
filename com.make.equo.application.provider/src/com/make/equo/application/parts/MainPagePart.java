package com.make.equo.application.parts;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.make.equo.application.server.EquoHttpProxyServer;
import com.make.equo.application.util.FrameworkUtils;
import com.make.equo.application.util.IConstants;
import com.make.equo.application.util.ScriptHandler;
import com.make.swtcef.Chromium;

public class MainPagePart {

	@Inject
	private MPart thisPart;

	private Chromium browser;

	private EquoHttpProxyServer equoHttpProxyServer;

	@Inject
	public MainPagePart(Composite parent) {

	}

	@PostConstruct
	public void createControls(Composite parent) {
		String url = thisPart.getProperties().get(IConstants.MAIN_URL_KEY);
		if (url != null) {
			startEquoServer(url);
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(GridLayoutFactory.fillDefaults().create());
			composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			browser = new Chromium(composite, SWT.NONE);
			browser.setUrl(url);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		}
	}

	private void startEquoServer(String url) {
		System.out.println("Creating Equo server proxy...");
		ScriptHandler scriptHandler = new ScriptHandler(thisPart);
		List<String> customScripts = scriptHandler.getScripts();
		equoHttpProxyServer = new EquoHttpProxyServer(url, FrameworkUtils.INSTANCE.getAppBundlePath(), FrameworkUtils.INSTANCE.getFrameworkName());
		if (!customScripts.isEmpty()) {
			equoHttpProxyServer.addScripts(customScripts);
		}
		equoHttpProxyServer.startProxy();
	}
	
	@PreDestroy
	public void stopServer() {
		if (equoHttpProxyServer != null) {
			equoHttpProxyServer.stop();
		}
	}
}
