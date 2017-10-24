package com.make.equo.application.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.make.equo.application.server.EquoHttpProxyExecutor;
import com.make.equo.application.util.IConstants;
import com.make.swtcef.Chromium;

public class MainPagePart {

	@Inject
	private MPart thisPart;

	private Chromium browser;

	private EquoHttpProxyExecutor equoHttpProxyExecutor;

	private Thread mainServerThread;

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
		equoHttpProxyExecutor = new EquoHttpProxyExecutor(url);
		mainServerThread = new Thread(equoHttpProxyExecutor);
		mainServerThread.start();
		while (!equoHttpProxyExecutor.isStarted()) {
			// wait until the Equo server is started
			System.out.println("Equo Server is being started");
		}
		String address = equoHttpProxyExecutor.getAddress();
		// TODO use proper logging...
		System.out.println("Equo proxy started with address " + address);
		return address;
	}

	@PreDestroy
	public void preDestroy() {
		if (mainServerThread != null) {
			equoHttpProxyExecutor.stop();
			try {
				mainServerThread.join();
			} catch (InterruptedException e) {
				// TODO use proper logging...
				e.printStackTrace();
			}
		}

	}
}
