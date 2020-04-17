package com.make.equo.testing.common.util;

import static org.mockito.Mockito.mock;

import org.eclipse.core.commands.CommandManager;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.commands.internal.CommandServiceImpl;
import org.eclipse.e4.core.commands.internal.HandlerServiceImpl;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory;
import org.eclipse.e4.ui.internal.workbench.addons.CommandProcessingAddon;
import org.eclipse.e4.ui.internal.workbench.addons.HandlerProcessingAddon;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.FrameworkUtil;

import com.make.equo.renderers.EclipseWebRendererFactory;


public class EquoRule implements TestRule {

	private Object caseTest;
	
	private IEclipseContext eclipseContext;
	
	private EclipseWebRendererFactory rendererFactory;

	private CommandProcessingAddon commandAddon;

	private HandlerProcessingAddon handlerAddon;

	private ModelServiceImpl modelService;
	
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
	public EquoRule withApplicationContext(MApplication app) {
		rendererFactory = new EclipseWebRendererFactory();
		eclipseContext = EclipseContextFactory
		.getServiceContext(FrameworkUtil.getBundle(this.getClass()).getBundleContext());
		
		rendererFactory.init(eclipseContext);
	
		IContributionFactory factory = new ReflectionContributionFactory();
		eclipseContext.set(IContributionFactory.class, factory);

		IResourceUtilities resources = new ResourceUtility();
		eclipseContext.set(IResourceUtilities.class, resources);
			
		Logger logger = mock(Logger.class);

		eclipseContext.set(Logger.class, logger);
		
		app.setContext(eclipseContext);
		eclipseContext.set(MApplication.class, app);
		
		modelService = new ModelServiceImpl(eclipseContext);
		eclipseContext.set(EModelService.class, modelService);
		
		CommandServiceImpl commandService = new CommandServiceImpl();
		CommandManager commandManager = new CommandManager();
		commandService.setManager(commandManager);
		eclipseContext.set(ECommandService.class, commandService);
				
		Display display = new Display();
		UISynchronize sync =  new UISynchronize() {

			@Override
			public void syncExec(Runnable runnable) {
					runnable.run();
			}

			@Override
			public void asyncExec(Runnable runnable) {
					runnable.run();
				
			}
		};		
		eclipseContext.set(UISynchronize.class, sync);
		
		IEventBroker eventBroker = mock(IEventBroker.class);
		eclipseContext.set(IEventBroker.class, eventBroker);
		

		EHandlerService handlerService = new  HandlerServiceImpl();
		eclipseContext.set(EHandlerService.class, handlerService);
		
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell,SWT.NONE);
		
		IEclipseContext addonStaticContext = EclipseContextFactory.create();
		for (MAddon addon : app.getAddons()) {
			addonStaticContext.set(MAddon.class, addon);
			Object obj = factory.create(addon.getContributionURI(), eclipseContext, addonStaticContext);
			addon.setObject(obj);
			if (addon.getElementId().equals("org.eclipse.e4.ui.workbench.commands.model")) {
				commandAddon = (CommandProcessingAddon) obj;
			}
			if (addon.getElementId().equals("org.eclipse.e4.ui.workbench.handler.model")) {
				handlerAddon = (HandlerProcessingAddon) obj;
			}
		}
		
		return this;
	}

	public IEclipseContext getEclipseContext() {
		return eclipseContext;
	}

	public EclipseWebRendererFactory getRendererFactory() {
		return rendererFactory;
	}

	public CommandProcessingAddon getCommandAddon() {
		return commandAddon;
	}

	public HandlerProcessingAddon getHandlerAddon() {
		return handlerAddon;
	}

	public ModelServiceImpl getModelService() {
		return modelService;
	}
	
}
