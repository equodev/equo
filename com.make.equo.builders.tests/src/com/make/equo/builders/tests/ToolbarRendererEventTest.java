package com.make.equo.builders.tests;

import static java.lang.Integer.toHexString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.net.URI;

import javax.inject.Inject;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.junit.Before;
import org.junit.Test;

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
	public void settingContext() {
		this.injector = injector.withApplicationContext(modelTestingConfigurator.getMainApplication(), new EclipseWebRendererFactory());
	}
	
	private Boolean executed;
	
	@Test
	public void toolbarRenderer_Should_Store_ClickEventHandler_in_WSocket_EventsHash() throws Exception {
		
		executed = false;
		String tooltip = "text1";
		
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip).onClick(() -> 
		executed = true);
				
		//obtaining Item Model via reflection
		ToolbarBuilder toolbarBuilder = toolItemBuilder.getToolbarBuilder();
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(), toolItemBuilder, "item");
		
		
		createToolbarRenderer(toolbar);
		initializeCommandAndHandlerAddons();
		
		
		assertThat(messageService).isNotNull()
			.extracting("eventHandlers").asInstanceOf(InstanceOfAssertFactories.map(String.class, String.class)).isNotNull()
			.containsKey("toolbar" + toHexString(toolbar.hashCode())+ "_itemclicked");
		
		// Testing invoke action from WebSocketClient
		EmptyClient.create(new URI("ws://localhost:" + messageService.getPort()))
			.singleMessage("{\"action\":\""+"Toolbar" + toHexString(toolbar.hashCode())+ "_itemClicked"
					+ "\",\"params\":{\"toolBarElementId\":\""+toolItem.getElementId()+"\",\"commandId\":\""+ toolItem.getCommand().getElementId()+"\",\"isAnEquoModelElement\":\"true\"}}")
			.connect();	
		
		await().untilAsserted(() -> assertThat(executed).isTrue());

	}

	private void initializeCommandAndHandlerAddons() {
		this.injector.getCommandAddon().init(); 	
		this.injector.getHandlerAddon().postConstruct(modelTestingConfigurator.getMainApplication(), this.injector.getModelService());
	}

	private void createToolbarRenderer(MToolBar toolbar) {	
		ToolBarRenderer renderer = (ToolBarRenderer) injector.getRendererFactory().getRenderer(toolbar, null); 

		renderer.setToolbarContext(toolbar);
		renderer.onActionPerformedOnElement();
	}
	

}
