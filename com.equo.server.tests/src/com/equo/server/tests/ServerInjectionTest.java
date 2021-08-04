/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.server.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.equo.comm.api.IEquoCommService;
import com.equo.comm.api.IEquoEventHandler;
import com.equo.comm.api.IEquoRunnable;
import com.equo.comm.api.StringPayloadEquoRunnable;
import com.equo.server.api.IEquoServer;
import com.equo.testing.common.util.EquoRule;

public class ServerInjectionTest {

  private static final String TEST_URL = "http://servertest";

  @Inject
  protected IEquoCommService commService;

  @Inject
  protected IEquoServer equoServer;

  @Inject
  protected IEquoEventHandler handler;

  protected Browser chromium;

  private Display display;

  @Rule
  public EquoRule rule = new EquoRule(this).runInNonUiThread();

  private void forceBrowserToStart() {
    Listener[] listeners = chromium.getListeners(SWT.Paint);
    assertThat(listeners.length > 0);
    Event event = new Event();
    event.type = SWT.Paint;
    event.display = display;
    event.widget = chromium;
    listeners[0].handleEvent(event);
  }

  private void goToUrl(String url) {
    AtomicBoolean start = new AtomicBoolean(false);
    handler.on("_ready", (IEquoRunnable<Void>) runnable -> {
      start.set(true);
    });
    goToUrlWithoutWaitForLoad(url + String.format("?equocommport=%d", commService.getPort()));
    await().untilTrue(start);
  }

  private void goToUrlWithoutWaitForLoad(String url) {
    display.syncExec(() -> {
      chromium.setUrl(url);
    });
  }

  @Before
  public void setup() {
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
      shell.open();
      forceBrowserToStart();
    });
  }

  @Test
  public void scriptShouldBeInjectedInBaseUrl() {
    goToUrl(TEST_URL);
  }

  @Test
  public void scriptShouldBeInjectedInIndexHtml() {
    goToUrl(TEST_URL + "/index.html");
  }

  @Test
  public void scriptShouldNotBeInjectedInXmlFile() throws IOException, URISyntaxException {
    AtomicBoolean textReceived = new AtomicBoolean(false);
    StringBuilder stringBuilder = receiveXmlFileResponseContent(textReceived);
    goToUrlWithoutWaitForLoad(TEST_URL + "/xmlrequest.html");
    await().untilTrue(textReceived);

    String browserText = stringBuilder.toString();
    String fileText = getXmlFileContent();

    assertEquals("File content has been modified in the request", fileText, browserText);
  }

  private String getXmlFileContent() throws IOException {
    InputStream fis = getClass().getClassLoader().getResourceAsStream("pom.xml");
    String fileText = IOUtils.toString(fis, "UTF-8");
    return fileText;
  }

  private StringBuilder receiveXmlFileResponseContent(AtomicBoolean textReceived) {
    StringBuilder stringBuilder = new StringBuilder();
    handler.on("_sendText", (StringPayloadEquoRunnable) s -> {
      stringBuilder.append(s);
      textReceived.set(true);
    });
    return stringBuilder;
  }
}
