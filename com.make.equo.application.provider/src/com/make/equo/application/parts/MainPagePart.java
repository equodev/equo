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

import com.make.equo.application.server.EquoHttpProxy;
import com.make.equo.application.util.IConstants;
import com.make.equo.application.util.ScriptHandler;
import com.make.swtcef.Chromium;

public class MainPagePart {

	@Inject
	private MPart thisPart;

	private Chromium browser;

	private EquoHttpProxy equoHttpProxy;

	@Inject
	public MainPagePart(Composite parent) {

	}

	@PostConstruct
	public void createControls(Composite parent) {
		String url = thisPart.getProperties().get(IConstants.MAIN_URL_KEY);
		if (url != null) {
			String equoServerAddress = startEquoServer(url);
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(GridLayoutFactory.fillDefaults().create());
			composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			browser = new Chromium(composite, SWT.NONE);
			browser.setUrl(equoServerAddress);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		}
	}

	private String startEquoServer(String url) {
		// TODO use proper logging...
		System.out.println("Creating Equo server proxy...");
		ScriptHandler scriptHandler = new ScriptHandler(thisPart);
		List<String> customScripts = scriptHandler.getScripts();
		equoHttpProxy = new EquoHttpProxy(url);
		if (!customScripts.isEmpty()) {
			equoHttpProxy.addScripts(customScripts);
		}
		try {
			equoHttpProxy.startProxy();
			while (!equoHttpProxy.isStarted()) {
				// wait until the Equo server is started
				System.out.println("Equo Server is being started");
			}
			String address = equoHttpProxy.getAddress();
			// TODO use proper logging...
			System.out.println("Equo proxy started with address " + address);
			return address;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@PreDestroy
	public void preDestroy() {
		if (equoHttpProxy != null) {
			equoHttpProxy.stop();
		}
	}
}
