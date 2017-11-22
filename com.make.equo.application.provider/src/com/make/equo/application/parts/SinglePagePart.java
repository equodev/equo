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
import com.make.swtcef.Chromium;

public class SinglePagePart {

	@Inject
	private MPart thisPart;

	private Chromium browser;

	@Inject
	public SinglePagePart(Composite parent) {

	}

	@PostConstruct
	public void createControls(Composite parent) {
		String url = thisPart.getProperties().get(IConstants.MAIN_URL_KEY);
		if (url != null) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(GridLayoutFactory.fillDefaults().create());
			composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			browser = new Chromium(composite, SWT.NONE);
			browser.setUrl(url);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		}
	}
	
	@PreDestroy
	public void destroy() {
		if (browser != null) {
			browser.dispose();
		}
	}
}
