/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.debug.devtools.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.equo.comm.common.handler.IReceiveEventHandler;
import com.equo.comm.ws.provider.EquoWebSocketServiceImpl;
import com.equo.debug.api.DevtoolsDebugApi;
import com.equo.testing.common.util.EquoRule;

public class DevtoolsTest {

  @Inject
  private IReceiveEventHandler eventHandler;

  private Browser chromium;

  private Display display;

  private PaintListener paintListener;

  private AtomicBoolean started;

  @Inject
  private DevtoolsDebugApi devTool;

  private DevtoolsDebugApi mockDevtool;

  private static final String DEBUG_PORT_PROPERTY =
      "org.eclipse.swt.chromium.remote-debugging-port";

  @Rule
  public EquoRule rule = new EquoRule(this).runInNonUiThread();

  @BeforeClass
  public static void setupProperties() {
    System.setProperty(DEBUG_PORT_PROPERTY, "8888");
  }

  @Test
  public void whenDebugModeIsSetProperlyThenShouldExecuteStartDebug() {
    started = new AtomicBoolean(false);
    String debugPort = System.getProperty(DEBUG_PORT_PROPERTY);
    AtomicBoolean needStopPaintListener = new AtomicBoolean(false);
    mockDevtool = mock(devTool.getClass());
    StringBuilder sb = new StringBuilder();
    sb.append("http://testbundle:8888/");
    if (eventHandler instanceof EquoWebSocketServiceImpl) {
      int port = ((EquoWebSocketServiceImpl) eventHandler).getPort();
      sb.append("?equocommport=");
      sb.append(port);
    }
    display = rule.getDisplay();
    display.syncExec(() -> {
      Shell shell = rule.createShell();
      chromium = new Browser(shell, SWT.NONE);

      chromium.setUrl(sb.toString());
      shell.open();

      paintListener = new PaintListener() {
        @Override
        public void paintControl(PaintEvent paintEvent) {
          if (!needStopPaintListener.get()) {
            if (chromium == paintEvent.widget) {
              needStopPaintListener.set(true);
              mockDevtool.startDebug(debugPort);
              started.set(true);
            }
          }
          if (paintListener != null) {
            chromium.removePaintListener(paintListener);
          }
        }
      };

      forceBrowserToStart();

      chromium.addPaintListener(paintListener);
    });
    await().untilTrue(started);
    verify(mockDevtool).startDebug("8888");
  }

  private void forceBrowserToStart() {
    Listener[] listeners = chromium.getListeners(SWT.Paint);
    await().timeout(Duration.ofSeconds(5))
        .untilAsserted(() -> assertThat(listeners).hasSizeGreaterThan(0));
    Event event = new Event();
    event.type = SWT.Paint;
    event.display = display;
    event.widget = chromium;
    for (Listener listener : listeners) {
      listener.handleEvent(event);
    }
  }
}
