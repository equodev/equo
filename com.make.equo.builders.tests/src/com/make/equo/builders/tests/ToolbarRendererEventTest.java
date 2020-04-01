package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.core.commands.CommandManager;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.internal.CommandServiceImpl;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MApplicationFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import com.make.equo.application.model.ToolbarBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;
import com.make.equo.builders.mocks.EmptyClient;
import com.make.equo.renderers.EclipseWebRendererFactory;
import com.make.equo.renderers.ToolBarRenderer;
import com.make.equo.ws.api.IEquoWebSocketService;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;

public class ToolbarRendererEventTest extends AbstractBuilderTest {
	
	
	
	@Inject
	private IEquoWebSocketService messageService;

	private EclipseWebRendererFactory rendererFactory;
	
	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder);
		assertThat(appBuilder).isNotNull();
		Assertions.setAllowExtractingPrivateFields(true);
		
	}
	
	private IEclipseContext eclipseContext;
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void toolbarRenderer_Should_Store_ClickEventHandler_in_WSocket_EventsHash() throws Exception {
		
		
		String tooltip = "text1";
		
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip).onClick(() -> System.out.println("toolitem event 1"));
				
		configureContext();

		//obtaining Item Model via reflection
		ToolbarBuilder toolbarBuilder = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder, "toolbarBuilder");
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(), toolItemBuilder, "item");
				
		ToolBarRenderer renderer = (ToolBarRenderer) rendererFactory.getRenderer(toolbar, null); 

		renderer.setToolbarContext(toolbar);
		renderer.onActionPerformedOnElement();
		
		assertThat(messageService).isNotNull()
		.extracting("eventHandlers").asInstanceOf(InstanceOfAssertFactories.type(Map.class)).isNotNull()
		.satisfies( map -> {
							Set<String> s = map.keySet();
							List<String> l = new ArrayList<String>(s);
							assertThat(l).contains("toolbar" + Integer.toHexString(toolbar.hashCode())+ "_itemclicked");
							});

		// Testing invoke action from WebSocketClient
		EmptyClient client = new EmptyClient(new URI("ws://localhost:" + messageService.getPort()));
		client.setMessage("{\"action\":\""+"Toolbar" + Integer.toHexString(toolbar.hashCode())+ "_itemClicked"
		+ "\",\"params\":{\"toolBarElementId\":\""+toolItem.getElementId()+"\",\"commandId\":\""+ toolItem.getCommand().getElementId()+"\",\"isAnEquoModelElement\":\"true\"}}");
		client.connect();	
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void configureContext() {
		rendererFactory = new EclipseWebRendererFactory();
		eclipseContext = EclipseContextFactory
		.getServiceContext(FrameworkUtil.getBundle(this.getClass()).getBundleContext());
		
		rendererFactory.init(eclipseContext);
		
	

		IResourceUtilities resources = new ResourceUtility();
		eclipseContext.set(IResourceUtilities.class, resources);
		
		Logger logger = new Logger() {
			
			@Override
			public void warn(Throwable t, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void trace(Throwable t, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isWarnEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isTraceEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isInfoEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isErrorEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isDebugEnabled() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void info(Throwable t, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(Throwable t, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void debug(Throwable t, String message) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void debug(Throwable t) {
				// TODO Auto-generated method stub
				
			}
		};	
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
		
		UISynchronize sync = new UISynchronize() {
			
			@Override
			public void syncExec(Runnable runnable) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void asyncExec(Runnable runnable) {
				// TODO Auto-generated method stub
				
			}
		};
		eclipseContext.set(UISynchronize.class, sync);
		
		Display d = new Display();
		Shell shell = new Shell(d);
		Composite parent = new Composite(shell,SWT.NONE);
	}

}
