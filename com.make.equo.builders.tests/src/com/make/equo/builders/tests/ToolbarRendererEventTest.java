package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.ui.internal.workbench.swt.ResourceUtility;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.IResourceUtilities;
import org.eclipse.swt.widgets.Display;
import org.java_websocket.client.WebSocketClient;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.ToolbarBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;
import com.make.equo.builders.mocks.EmptyClient;
import com.make.equo.renderers.EclipseWebRendererFactory;
import com.make.equo.renderers.ToolBarRenderer;
import com.make.equo.ws.api.IEquoWebSocketService;

public class ToolbarRendererEventTest extends AbstractBuilderTest {
	
	
	
	@Inject
	private IEquoWebSocketService messageService;


	
	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder);
		assertThat(appBuilder).isNotNull();
		Assertions.setAllowExtractingPrivateFields(true);
	}
	
	@Test
	public void toolbarRenderer_Should_Store_ClickEventHandler_in_WSocket_EventsHash() throws NoSuchFieldException, IllegalAccessException, URISyntaxException {
		
		EclipseWebRendererFactory rendererFactory = new EclipseWebRendererFactory();
		rendererFactory.init(EclipseContextFactory
				.getServiceContext(FrameworkUtil.getBundle(this.getClass()).getBundleContext()));
		
		Display d = new Display();
		IResourceUtilities resources = new ResourceUtility();
		
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		});
		
		//obtaining Item Model via reflection
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(), toolItemBuilder, "item");  
		ToolbarBuilder toolbarBuilder = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder, "toolbarBuilder");
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");
		
		rendererFactory.getRenderer(toolbar, null);
		
		
		ToolBarRenderer toolbarRenderer = new ToolBarRenderer();
		toolbarRenderer.onActionPerformedOnElement();
		
		assertThat(messageService).isNotNull();
		WebSocketClient client = new EmptyClient(new URI("ws://localhost:" + messageService.getPort()));
		client.connect();
		
	}

}
