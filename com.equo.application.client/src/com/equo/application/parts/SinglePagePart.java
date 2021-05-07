package com.equo.application.parts;

import static com.equo.application.util.IConstants.MAIN_URL_KEY;
import static com.equo.application.util.IConstants.MAIN_URL_WS_PORT;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

public class SinglePagePart {

	@Inject
	private MPart thisPart;

	private Browser browser;

	@Inject
	public SinglePagePart(Composite parent) {
//	    Chromium.setCommandLine(new String[][] {
//	            new String[] {"proxy-server", "localhost:9896"},
//	            new String[] {"ignore-certificate-errors", null},
//	            new String[] {"allow-file-access-from-files", null},
//	            new String[] {"disable-web-security", null},
//	            new String[] {"enable-widevine-cdm", null},
//	            new String[] {"proxy-bypass-list", "127.0.0.1"}
//	    });
	}

	@PostConstruct
	public void createControls(Composite parent) {
		String equoAppUrl = thisPart.getProperties().get(MAIN_URL_KEY);
		String equoWsPort = thisPart.getProperties().get(MAIN_URL_WS_PORT);
		if (equoAppUrl != null) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(GridLayoutFactory.fillDefaults().create());
			browser = new Browser(composite, SWT.NONE);
			browser.setUrl(equoAppUrl + String.format("?equowsport=%s", equoWsPort));
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		}
	}

	public void loadUrl(String newUrl) {
		browser.setUrl(newUrl);
	}

}
