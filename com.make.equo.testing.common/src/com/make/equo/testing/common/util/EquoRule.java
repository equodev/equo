package com.make.equo.testing.common.util;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.CommandManager;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.commands.internal.CommandServiceImpl;
import org.eclipse.e4.core.commands.internal.HandlerServiceImpl;
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
import com.make.equo.testing.common.statements.InjectStatement;
import com.make.equo.testing.common.statements.RunInThreadStatement;

public class EquoRule implements TestRule {

	private Object testCase;

	private IEclipseContext eclipseContext;

	private EclipseWebRendererFactory rendererFactory;

	private CommandProcessingAddon commandAddon;

	private HandlerProcessingAddon handlerAddon;

	private ModelServiceImpl modelService;

	private boolean runInNonUIThread;
	private boolean displayOwner;
	private Display display;

	private List<Shell> preExistingShells;

	public EquoRule(Object testCase) {
		this.testCase = testCase;
		this.runInNonUIThread = false;
		preExistingShells = Arrays.asList(captureShells());
	}

	public Statement apply(Statement base, Description description) {
		Statement tempResult = new InjectStatement(base, testCase);
		if (runInNonUIThread) {
			tempResult = new RunInThreadStatement(tempResult, getDisplay());
		}

		final Statement result = tempResult;
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				try {
					result.evaluate();
				} finally {
					dispose();
				}
			}
		};
	}

	public Shell createShell() {
		return createShell(SWT.SHELL_TRIM);
	}

	public Shell createShell(int style) {
		return new Shell(getDisplay(), style);
	}

	public EquoRule runInNonUIThread() {
		runInNonUIThread = true;
		return this;
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
		UISynchronize sync = new UISynchronize() {

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

		EHandlerService handlerService = new HandlerServiceImpl();
		eclipseContext.set(EHandlerService.class, handlerService);

		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.NONE);

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

	public Display getDisplay() {
		if (display == null) {
			displayOwner = Display.getCurrent() == null;
			display = Display.getDefault();
		}
		return display;
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

	public void dispose() {
		if (runInNonUIThread) {
			flushPendingEvents();
		}
		disposeNewShells();
		disposeDisplay();
	}

	private static Shell[] captureShells() {
		Shell[] result = new Shell[0];
		Display currentDisplay = Display.getCurrent();
		if (currentDisplay != null) {
			result = currentDisplay.getShells();
		}
		return result;
	}

	public List<Shell> getNewShells() {
		List<Shell> newShells = new ArrayList<>();
		Shell[] shells = captureShells();
		for (Shell shell : shells) {
			if (!preExistingShells.contains(shell)) {
				newShells.add(shell);
			}
		}
		return newShells;
	}

	private void disposeNewShells() {
		List<Shell> newShells = getNewShells();
		for (Shell shell : newShells) {
			shell.dispose();
		}
	}

	private void disposeDisplay() {
		if (display != null && displayOwner) {
			if (display.isDisposed()) {
				display.dispose();
			}
			display = null;
		}
	}

	public void flushPendingEvents() {
		while (Display.getCurrent() != null && !Display.getCurrent().isDisposed()
				&& Display.getCurrent().readAndDispatch()) {
		}
	}

}
