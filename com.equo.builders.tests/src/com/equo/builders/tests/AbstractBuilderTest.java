package com.equo.builders.tests;

import static com.equo.application.util.IConstants.DEFAULT_BINDING_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.junit.Before;
import org.junit.Rule;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;
import com.equo.application.model.MenuItemBuilder;
import com.equo.application.model.ToolbarItemBuilder;
import com.equo.testing.common.util.EquoRule;
import com.equo.builders.tests.util.ModelConfigurator;

public abstract class AbstractBuilderTest {

	@Inject
	protected EquoApplicationBuilder appBuilder;
	
	@Inject
	protected IEquoApplication equoApp; 
	
	protected ModelConfigurator modelTestingConfigurator = new ModelConfigurator();
	
	@Rule
	public EquoRule injector = new EquoRule(this);

	@Before
	public void before() {
		modelTestingConfigurator.configure(appBuilder,equoApp);
		assertThat(appBuilder).isNotNull();
	}
	
	protected void assertCheckToolItemTooltip(String tooltip, ToolbarItemBuilder toolItemBuilder) {
		assertThat(toolItemBuilder).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(list(MHandledItem.class)).extracting(MHandledItem::getTooltip).contains(tooltip);
	}
	
	protected void assertCheckMenuItemLabel(String label, MenuItemBuilder menuItemBuilder) {
		assertThat(menuItemBuilder)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder)
			.extracting("menuBuilder.menu").asInstanceOf(type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(list(MMenuElement.class)).extracting(MMenuElement::getLabel).contains(label);
	}
	
	@SuppressWarnings("restriction")
	protected void assertCheckItemShortcut(String shortcut) {
		assertThat(appBuilder)
			.extracting("mApplication", type(MApplication.class)).extracting(MApplication::getBindingTables).asInstanceOf(list(MBindingTable.class))
			.satisfies( list -> {
				MBindingTable bindingTable = null;
				for (MBindingTable mBindingTable  :  list) {
					if (mBindingTable.getElementId().equals(DEFAULT_BINDING_TABLE)) {
						bindingTable = mBindingTable;
						break;
					}
				}
				assertThat(bindingTable).isNotNull();
				assertThat(bindingTable.getBindings()).extracting(key -> key.getKeySequence()).contains(shortcut);
			});
	}
	
	protected <T> Object getValueFromField(Class<T> clazz, Object O, String fieldName)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);    
		  field.setAccessible(true);
		  Object value = field.get(O);
		return value;
	}

}