package com.make.equo.application.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.application.handlers.filesystem.*;
import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.IEquoContributionManager;
import com.make.equo.contribution.api.handler.ParameterizedHandler;

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
			browser = new Browser(composite, SWT.NONE);
			browser.setUrl(equoAppUrl);
			browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
			makeDefaultHandlers();
		}
	}

	private void makeDefaultHandlers() {
		IEclipseContext eclipseContext = thisPart.getContext();
		ECommandService commandService = eclipseContext.get(ECommandService.class);
		EHandlerService handlerService = eclipseContext.get(EHandlerService.class);

		ParameterizedHandler[] handlers = { new OpenFileHandler(), new OpenFolderHandler(), new DeleteFileHandler(),
				new SaveFileHandler(), new FileInfoHandler(), new RenameFileHandler(), new MoveFileHandler() };

		for (ParameterizedHandler handler : handlers) {
			handler.registerCommand(handlerService, commandService);
		}
		
		BundleContext bndContext = FrameworkUtil.getBundle(IEquoContributionManager.class)
				.getBundleContext();
		ServiceReference<IEquoContributionManager> svcReference = bndContext
				.getServiceReference(IEquoContributionManager.class);
		IEquoContributionManager manager = bndContext.getService(svcReference);

		for (ParameterizedHandler handler : manager.getparameterizedHandlers()) {
			handler.registerCommand(handlerService, commandService);
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
