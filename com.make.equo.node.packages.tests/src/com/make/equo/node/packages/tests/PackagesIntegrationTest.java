package com.make.equo.node.packages.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.aer.client.api.ILoggingApi;
import com.make.equo.analytics.client.api.IAnalyticsApi;
import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.node.packages.tests.mocks.AnalyticsServiceMock;
import com.make.equo.node.packages.tests.mocks.LoggingServiceMock;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.testing.common.util.EquoRule;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.JsonPayloadEquoRunnable;

public class PackagesIntegrationTest {

	@Inject
	protected IAnalyticsApi analyticsApi;

	@Inject
	protected ILoggingApi loggingApi;

	@Inject
	protected IEquoApplication equoApp;

	@Inject
	protected IEquoServer equoServer;

	@Inject
	protected AnalyticsService analyticsServiceMock;

	@Inject
	protected IEquoLoggingService loggingServiceMock;

	@Inject
	protected IEquoEventHandler handler;

	protected Browser chromium;

	private Display display;

	@Rule
	public EquoRule rule = new EquoRule(this).runInNonUIThread();

	@Before
	public void before() {
		System.setProperty("swt.chromium.debug", "true");
		System.setProperty("swt.chromium.args",
				"--proxy-server=localhost:9896;--ignore-certificate-errors;--allow-file-access-from-files;--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=127.0.0.1");
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
			chromium.setUrl("http://testbundles");
			shell.open();
		});
	}

	@Test
	public void analyticsAreReceivedCorrectlyByTheService() {
		await().untilAsserted(() -> {
			assertThat(analyticsServiceMock).isInstanceOf(AnalyticsServiceMock.class).extracting("receivedMessages")
					.asInstanceOf(list(String.class)).contains("testEvent-{\"testKey\":\"testValue\"}");
		});
	}

	@Test
	public void loggingMessagesAreReceivedCorrectlyByTheService() {
		await().untilAsserted(() -> {
			assertThat(loggingServiceMock).isInstanceOf(LoggingServiceMock.class).extracting("receivedMessages")
					.asInstanceOf(list(String.class)).hasSize(3).contains("testInfo", "testWarn", "testError");
		});
	}

	@Test
	public void monacoEditorIsCreatedCorrectly() {
		AtomicReference<String> dom = new AtomicReference<>();
		handler.on("_doGetDom", (JsonPayloadEquoRunnable) payload -> {
			dom.set(payload.toString());
		});
		await().untilAsserted(() -> {
			handler.send("_getDom");
			assertThat(dom.get()).isNotNull().contains("<div class=\\\"monaco-editor");
		});
	}

}
