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

package com.equo.application.model.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.fail;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;
import com.equo.application.model.WindowManager;
import com.equo.testing.common.internal.util.ModelConfigurator;
import com.equo.testing.common.util.EquoRule;

public class WindowManagerTest {

  protected WindowManager windowManager;

  @Inject
  protected EquoApplicationBuilder equoApplicationBuilder;

  @Inject
  protected IEquoApplication equoApp;

  private Display display;

  @Before
  public void setupModelAndStartShell() throws InterruptedException {
    ModelConfigurator modelConfigurator = new ModelConfigurator(rule.getMApplication());
    modelConfigurator.configure(equoApplicationBuilder, equoApp);

    MApplication mApp = rule.getEclipseContext().get(MApplication.class);
    Bundle bundle = FrameworkUtil.getBundle(MApplication.class);
    BundleContext bundleContext = bundle.getBundleContext();
    bundleContext.registerService(MApplication.class, mApp, null);

    display = rule.getDisplay();

    windowManager = WindowManager.getInstance();

    if (windowManager == null || windowManager.getClass().getName().endsWith("LazyWindowManager")) {
      fail();
    }
    while (rule.getMApplication().getChildren().get(0) != null
        && rule.getMApplication().getChildren().get(0).getWidget() == null) {
      Thread.sleep(500);
    }
  }

  @Rule
  public EquoRule rule = new EquoRule(this)
      .runWithE4Model("../com.equo.application.client/resources/Application.e4xmi");

  @Test
  public void topLevelWindowIsReturnedCorrectly() {
    MWindow topLevelWindow = rule.getMApplication().getChildren().get(0);
    assertThat(windowManager.getTopLevelWindow()).isEqualTo(topLevelWindow);
  }

  @Test
  public void windowResizesCorrectly() throws Exception {
    MWindow window = rule.getMApplication().getChildren().get(0);
    // Add 100 to avoid hitting the minimum width and height of some desktop environments.
    int width = (int) (Math.random() * 100) + 100;
    int height = (int) (Math.random() * 100) + 100;
    windowManager.resizeWindow(window, width, height);
    await().untilAsserted(() -> {
      Shell widget = (Shell) window.getWidget();
      final Rectangle bounds = new Rectangle(0, 0, 0, 0);
      display.syncExec(() -> {
        Rectangle actualBounds = widget.getBounds();
        bounds.height = actualBounds.height;
        bounds.width = actualBounds.width;
      });
      assertThat(widget).isNotNull();
      assertThat(widget).isInstanceOf(Shell.class);
      assertThat(window.getWidth()).isEqualTo(width);
      assertThat(window.getHeight()).isEqualTo(height);
      assertThat(bounds).extracting("width").isEqualTo(width);
      assertThat(bounds).extracting("height").isEqualTo(height);
    });
  }

  @Test
  public void windowPositionIsSetCorrectly() {
    MWindow window = rule.getMApplication().getChildren().get(0);
    // Add 100 to avoid hitting the minimum width and height of some desktop environments.
    int x = (int) (Math.random() * 100) + 100;
    int y = (int) (Math.random() * 100) + 100;
    windowManager.moveWindow(window, x, y);
    await().untilAsserted(() -> {
      Shell widget = (Shell) window.getWidget();
      final Rectangle bounds = new Rectangle(0, 0, 0, 0);
      display.syncExec(() -> {
        Rectangle actualBounds = widget.getBounds();
        Rectangle displayBounds = display.getBounds();
        bounds.x = actualBounds.x - displayBounds.x;
        bounds.y = actualBounds.y - displayBounds.y;
      });
      assertThat(widget).isNotNull();
      assertThat(widget).isInstanceOf(Shell.class);
      assertThat(window.getX()).isEqualTo(x);
      assertThat(window.getY()).isEqualTo(y);
      assertThat(bounds).extracting("x").isEqualTo(x);
      assertThat(bounds).extracting("y").isEqualTo(y);
    });
  }

  // Can't run this test without running an EventBroker.
  //  @Test
  //  public void newWindowIsCreatedCorrectly() {
  //    MWindow createdWindow = windowManager.createWindow("testWindow", "testUrl");
  //    await().untilAsserted(() -> {
  //      assertThat(rule.getMApplication().getChildren().contains(createdWindow));
  //      assertThat(createdWindow.getWidget()).isNotNull().isInstanceOf(Shell.class);
  //    });
  //  }

}
