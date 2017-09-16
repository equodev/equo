package com.make.equo.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.make.swtcef.Chromium;

public class MainPagePart {

	private Chromium browser;

	@Inject
	public MainPagePart(Composite parent) {
		
	}
	
	@PostConstruct
    public void createControls(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().create());
		composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		browser = new Chromium(composite, SWT.NONE);
		// browser = new Chromium(parent, SWT.NONE);
		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		setUrl("netflix.com");
	}
	
	public void setUrl(String url) {
		browser.setUrl(url);
	}
}
