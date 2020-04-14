package com.make.equo.testing.common.util;

import org.eclipse.core.commands.CommandManager;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.internal.CommandServiceImpl;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.osgi.framework.FrameworkUtil;

import com.make.equo.renderers.EclipseWebRendererFactory;


public class EquoRule implements TestRule {

	private Object caseTest;
	
	private IEclipseContext eclipseContext;
	
	private EclipseWebRendererFactory rendererFactory;

	public EquoRule(Object equoTestingInjector) {
		this.caseTest = equoTestingInjector;
	}

	public Statement apply(Statement base, Description description) {
		return statement(base);
	}

	private Statement statement(final Statement base) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				inject(caseTest);
				try {
					base.evaluate();
				} finally {
				}
			}
		};
	}

	private void inject(Object caseTest) {
		IEclipseContext eclipseContext = EclipseContextFactory
				.getServiceContext(FrameworkUtil.getBundle(caseTest.getClass()).getBundleContext());
		ContextInjectionFactory.inject(caseTest, eclipseContext);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	public EquoRule withApplicationContext() {
		rendererFactory = new EclipseWebRendererFactory();
		eclipseContext = EclipseContextFactory
		.getServiceContext(FrameworkUtil.getBundle(this.getClass()).getBundleContext());
		
		rendererFactory.init(eclipseContext);
	
		IResourceUtilities resources = new ResourceUtility();
		eclipseContext.set(IResourceUtilities.class, resources);
			
		Logger logger = Mockito.mock(Logger.class);

		eclipseContext.set(Logger.class, logger);
		
		MApplication app = MApplicationFactory.INSTANCE.createApplication();
		app.setContext(eclipseContext);
		eclipseContext.set(MApplication.class, app);
		
		EModelService modelService = new ModelServiceImpl(eclipseContext);
		eclipseContext.set(EModelService.class, modelService);
		
		CommandManager commandManager = new CommandManager();
		
		CommandServiceImpl commandService = new CommandServiceImpl();
		commandService.setManager(commandManager);
		eclipseContext.set(ECommandService.class, commandService);
		
		UISynchronize sync = Mockito.mock(UISynchronize.class);
				
		eclipseContext.set(UISynchronize.class, sync);
		
		Display d = new Display();
		Shell shell = new Shell(d);
		Composite parent = new Composite(shell,SWT.NONE);
		
		return this;
	}

	public IEclipseContext getEclipseContext() {
		return eclipseContext;
	}

	public EclipseWebRendererFactory getRendererFactory() {
		return rendererFactory;
	}

	
}
