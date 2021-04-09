package com.make.equo.node.packages.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.CustomDeserializer;
import com.make.equo.application.model.EquoMenuItem;
import com.make.equo.application.model.EquoMenuItemSeparator;
import com.make.equo.application.model.Menu;
import com.make.equo.logging.client.api.Logger;
import com.make.equo.node.packages.tests.common.ChromiumSetup;
import com.make.equo.node.packages.tests.mocks.LoggingServiceMock;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.testing.common.util.EquoRule;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.IEquoWebSocketService;
import com.make.equo.ws.api.JsonPayloadEquoRunnable;

public class PackagesIntegrationTest {

	@Inject
	protected IEquoApplication equoApp;

	@Inject
	protected IEquoServer equoServer;

	@Inject
	protected Logger loggingServiceMock;

	@Inject
	protected IEquoWebSocketService websocketService;

	@Inject
	protected IEquoEventHandler handler;

	protected Browser chromium;

	private Display display;
	private static Gson gson;

	@Rule
	public EquoRule rule = new EquoRule(this).runInNonUIThread();

	@BeforeClass
	public static void inicialize() {
		CustomDeserializer deserializer = new CustomDeserializer();
		deserializer.registerMenuType(EquoMenuItem.CLASSNAME, EquoMenuItem.class);
		deserializer.registerMenuType(EquoMenuItemSeparator.CLASSNAME, EquoMenuItemSeparator.class);

		gson = new GsonBuilder().registerTypeAdapter(Menu.class, deserializer).create();

		new ChromiumSetup();
	}

	@Before
	public void before() {
		AtomicBoolean start = new AtomicBoolean(false);
		handler.on("_ready", (IEquoRunnable<Void>) runnable -> {
			start.set(true);
		});

		display = rule.getDisplay();
		display.syncExec(() -> {
			Shell shell = rule.createShell();
			chromium = new Browser(shell, SWT.NONE);
			GridData data = new GridData();
			data.minimumWidth = 1;
			data.minimumHeight = 1;
			data.horizontalAlignment = SWT.FILL;
			data.verticalAlignment = SWT.FILL;
			data.grabExcessHorizontalSpace = true;
			data.grabExcessVerticalSpace = true;
			chromium.setLayoutData(data);
			chromium.setUrl("http://testbundles/" + String.format("?equowsport=%d", websocketService.getPort()));
			shell.open();
			forceBrowserToStart();
		});

		await().untilTrue(start);
	}

	private void forceBrowserToStart() {
		Listener[] listeners = chromium.getListeners(SWT.Paint);
		assertThat(listeners.length > 0);
		Event event = new Event();
		event.type = SWT.Paint;
		event.display = display;
		event.widget = chromium;
		listeners[0].handleEvent(event);
	}

	private void testMenuTemplate(String userActionOn, String userActionSend, String json) {
		AtomicReference<Boolean> wasCorrectly = new AtomicReference<>(false);
		handler.on(userActionOn, (JsonPayloadEquoRunnable) payload -> {
			if (payload.get("code") != null) {
				wasCorrectly.set(payload.toString().contains(json));
			} else {
				wasCorrectly.set(gson.fromJson(payload, Menu.class).serialize().toString().equals(json));
			}
		});
		handler.send(userActionSend);
		await().until(() -> wasCorrectly.get());
	}

	private Menu createTestMenuModel() {
		Menu equoMenuModel = Menu.create();
		equoMenuModel.withMainMenu("Menu1")
			.addMenuItem("SubMenu11").withShortcut("M1+W").onClick("_test")
			.addMenuItem("NeedToBeRemoved")
		.withMainMenu("Menu2")
			.addMenuItem("SubMenu21").onClick("_test")
			.addMenuItemSeparator()
			.addMenu("SubMenu22")
				.addMenuItem("SubMenu221").onClick("_test").withShortcut("M1+G")
		.withMainMenu("MenuToBeRemoved")
			.addMenuItem("ItemThatWillBeRemoved");

		equoMenuModel.appendMenuAtTheEnd("Menu2/SubMenu22", "SubMenu222")
			.addMenuItem("SubMenu2221");

		equoMenuModel.removeMenuElementByPath("Menu1/NeedToBeRemoved");
		equoMenuModel.removeMenuElementByPath("MenuToBeRemoved");

		return equoMenuModel;
	}

	@Test
	public void createEquoMenu() {
		testMenuTemplate("_testSetMenu1", "_createMenu",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\""
						+ "type\":\"EquoMenuItem\",\"title\":\"SubMenu121\"}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu21\",\"action\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\""
						+ "children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\""
						+ "title\":\"SubMenu222\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu2221\"}]}]}]}]}");
	}

	@Test
	public void appendMenuItem() {
		testMenuTemplate("_testSetMenu2", "_appendMenuItem",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu10\",\"shortcut\":\"M1+L\",\"action\":\"_test\"},{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu11\",\"shortcut\":\""
						+ "M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\""
						+ "SubMenu121\"}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu21\",\"action\""
						+ ":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu222\",\"children\":[{\""
						+ "type\":\"EquoMenuItem\",\"title\":\"SubMenu2221\"}]}]}]}]}");
	}

	@Test
	public void appendMenu() {
		testMenuTemplate("_testSetMenu3", "_appendMenu",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuItem\",\""
						+ "title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\"type\":\""
						+ "EquoMenuItem\",\"title\":\"SubMenu121\"}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\""
						+ ":\"SubMenu21\",\"action\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\""
						+ "type\":\"EquoMenuItem\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu223\""
						+ ",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu2231\",\"shortcut\":\"M1+K\",\"action\":\"_test\"}]},{\"type\":\"EquoMenu\",\""
						+ "title\":\"SubMenu222\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu2221\"}]}]}]}]}");
	}

	@Test
	public void removeMenuElement() {
		testMenuTemplate("_testSetMenu4", "_removeMenuElement",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\"type\""
						+ ":\"EquoMenuItem\",\"title\":\"SubMenu121\"}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\""
						+ ":\"SubMenu21\",\"action\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\""
						+ ":\"EquoMenuItem\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"}]}]}]}");
	}

	@Test
	public void appendMenuAtTheEnd() {
		testMenuTemplate("_testSetMenu5", "_appendMenuAtTheEnd",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuItem\""
						+ ",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\"type\":\""
						+ "EquoMenuItem\",\"title\":\"SubMenu121\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu122\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\""
						+ "SubMenu1221\"}]}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu21\",\"action\":\""
						+ "_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\""
						+ ":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu222\",\"children\":[{\"type\":\""
						+ "EquoMenuItem\",\"title\":\"SubMenu2221\"}]}]}]}]}");
	}

	@Test
	public void appendMenuItemAtTheEnd() {
		testMenuTemplate("_testSetMenu6", "_appendMenuItemAtTheEnd",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\"EquoMenuI"
						+ "tem\",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"children\":[{\"typ"
						+ "e\":\"EquoMenuItem\",\"title\":\"SubMenu121\"},{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu123\"}]}]},{\"type\":\"EquoMenu\",\"title\":\"Me"
						+ "nu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu21\",\"action\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":"
						+ "\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_te"
						+ "st\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu222\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu2221\"}]}]}]}]}");
	}

	@Test
	public void appendMenuRepeated() {
		testMenuTemplate("_testSetMenu7", "_appendMenuRepeated",
				"The menu SubMenu123 already exist in Menu1/SubMenu12");
	}

	@Test
	public void appendMenuItemRepeated() {
		testMenuTemplate("_testSetMenu8", "_appendMenuItemRepeated",
				"The menu SubMenu122 already exist in Menu1/SubMenu12");
	}

	@Test
	public void createMenuWithRepeatedMenus() {
		testMenuTemplate("_testSetMenu9", "_createMenuWithRepeatedMenus",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\":\""
						+ "EquoMenuItem\",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu12\",\"child"
						+ "ren\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu121\"}]},{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu14\",\"shortcut\":\"M1+W\",\""
						+ "action\":\"_test\"}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu21\",\"actio"
						+ "n\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\":\"EquoMenuItem"
						+ "\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu222\",\"children\":[{\""
						+ "type\":\"EquoMenuItem\",\"title\":\"SubMenu2221\"},{\"type\":\"EquoMenuItem\",\"title\":\"newMenu\"}]}]}]}]}");
	}

	@Test
	public void buildWithCurrentModel() {
		handler.on("_getMenu", (JsonPayloadEquoRunnable) payload -> {
			handler.send("_doGetMenu", createTestMenuModel().serialize());
		});

		testMenuTemplate("_testSetMenu10", "_buildWithCurrentModel",
				"{\"menus\":[{\"type\":\"EquoMenu\",\"title\":\"Menu1\",\"children\":[{\"type\""
						+ ":\"EquoMenuItem\",\"title\":\"SubMenu11\",\"shortcut\":\"M1+W\",\"action\":\"_test\"}]},{\"type\":\"EquoMenu\",\"title\":\"Menu2\""
						+ ",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu21\",\"action\":\"_test\"},{\"type\":\"EquoMenuItemSeparator\"},{\"ty"
						+ "pe\":\"EquoMenu\",\"title\":\"SubMenu22\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMenu221\",\"shortcut\":\"M1+G\""
						+ ",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"SubMenu222\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"SubMen"
						+ "u2221\"}]}]}]},{\"type\":\"EquoMenu\",\"title\":\"Menu3\",\"children\":[{\"type\":\"EquoMenuItem\",\"title\":\"subMenu31\",\"short"
						+ "cut\":\"M1+W\",\"action\":\"_test\"},{\"type\":\"EquoMenu\",\"title\":\"subMenu32\",\"children\":[{\"type\":\"EquoMenuItem\",\"tit"
						+ "le\":\"subMenu321\"}]}]}]}");
	}

	@Test
	public void buildWithCurrentModelWithRepeatedMenus() {
		handler.on("_getMenu", (JsonPayloadEquoRunnable) payload -> {
			handler.send("_doGetMenu", createTestMenuModel().serialize());
		});

		testMenuTemplate("_testSetMenu11", "_buildWithCurrentModelWithRepeatedMenus",
				"The menu SubMenu22 already exist in Menu2 and your type is Menu");
	}
	
	@Test
	public void customActionOnClick() {
		AtomicReference<Boolean> wasCorrectly = new AtomicReference<>(false);
		handler.on("_customActionOnClickResponse", (IEquoRunnable<Void>) runnable -> {
			wasCorrectly.set(true);
		});
		handler.on("_testCustomOnClick", (JsonPayloadEquoRunnable) payload -> {
			JsonArray menus = ((JsonObject)payload.getAsJsonObject()).get("menus").getAsJsonArray();
			String action = ((JsonObject)((JsonArray)((JsonObject)menus.get(0)).get("children")).get(0)).get("action").getAsString();
			handler.send(action);
		});
		handler.send("_customActionOnClick");
		await().until(() -> wasCorrectly.get());
	}

	@Test
	public void loggingMessagesAreReceivedCorrectlyByTheService() {
		handler.send("_makeLogs");
		await().untilAsserted(() -> {
			assertThat(loggingServiceMock).isInstanceOf(LoggingServiceMock.class).extracting("receivedMessages")
					.asInstanceOf(list(String.class)).contains("testInfo", "testWarn", "testError",
							"testTrace", "testDebug", "Current is NOT CONFIGURED", "Changed to DEBUG",
							"Current is DEBUG", "Global is INFO", "Global is TRACE", "Equo Contribution added: equologging");
		});
	}
}
