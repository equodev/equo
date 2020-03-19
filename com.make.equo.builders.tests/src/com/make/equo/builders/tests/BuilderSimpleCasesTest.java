package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.extractProperty;

import java.net.URISyntaxException;



import org.junit.Ignore;
import org.junit.Test;

import com.make.equo.application.model.MenuItemBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;


public class BuilderSimpleCasesTest extends EquoInjectableTest {
	
	
	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip);
		assertThat(toolItemBuilder).isNotNull();
		assertThat(toolItemBuilder).extracting("item").isNotNull();
		assertThat(toolItemBuilder).extracting("text").containsExactly(tooltip);
		assertThat(toolItemBuilder).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolItemBuilder).extracting("toolbarBuilder").extracting("toolbar").extractingResultOf("getChildren").contains(extractProperty("item").from(new ToolbarItemBuilder[]{toolItemBuilder}));
	}
	
	@Ignore
	@Test
	public void menuSimpleTest() throws URISyntaxException {
		String label = "item1";
		MenuItemBuilder menuItem = appBuilder.plainApp("/").withMainMenu("Menu").addMenuItem(label);
		assertThat(menuItem).isNotNull();
		assertThat(menuItem).extracting("item").isNotNull();
		//assertThat(menuItem.getLabel()).isEqualTo(label);
		assertThat(menuItem).extracting("item").extractingResultOf("getLabel").containsExactly(label);
	}
	
	@Ignore
	@Test public void menuAndToolbarTest() throws URISyntaxException{
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu").addMenuItem("item1").withToolbar().addToolItem("text", "text")).isNotNull();
	}
	
	@Ignore
	@Test public void toolbarAndmenuTest() throws URISyntaxException{
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").withMainMenu("Menu").addMenuItem("item1")).isNotNull();
	}
	
}
