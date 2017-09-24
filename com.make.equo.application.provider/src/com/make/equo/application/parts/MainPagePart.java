package com.make.equo.application.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MainPagePart {
	
	public static final String MAIN_URL_KEY = "mainUrl";

	public static final String ID = "com.make.equo.part.main";

//	private Chromium browser;

	@Inject
	private MPart thisPart;

//	private Chromium browser;
	
	@Inject
	public MainPagePart(Composite parent) {
		
	}
	
	@PostConstruct
    public void createControls(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(GridLayoutFactory.fillDefaults().create());
		composite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		String url = thisPart.getProperties().get(MAIN_URL_KEY);
		if (url != null) {
			System.out.println("the url is " + url);
			Label label = new Label(composite, SWT.NONE);
			label.setText(url);
		}
//		browser = new Chromium(composite, SWT.NONE);
//		 browser = new Chromium(parent, SWT.NONE);
//		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		setUrl("netflix.com");
	}
	
	public void setUrl(String url) {
//		browser.setUrl(url);
	}
}
