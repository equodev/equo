package com.make.equo.application.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.make.equo.application.util.IConstants;
import org.eclipse.swt.chromium.Browser;

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
		String equoAppUrl = thisPart.getProperties().get(IConstants.MAIN_URL_KEY);
		if (equoAppUrl != null) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(GridLayoutFactory.fillDefaults().create());
<<<<<<< Upstream, based on origin/master
			browser = new Browser(composite, SWT.NONE);
=======
			composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			browser = new Chromium(composite, SWT.NONE);
>>>>>>> 8af1d1e Revert fix for issue #98 to add it to its own MR for better tracking
			browser.setUrl(equoAppUrl);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		}
	}

	@PreDestroy
	public void destroy() {
		if (browser != null) {
			browser.dispose();
		}
	}

	public void loadUrl(String newUrl) {
		browser.setUrl(newUrl);
	}

}
