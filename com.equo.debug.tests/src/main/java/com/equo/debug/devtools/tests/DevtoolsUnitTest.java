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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.equo.debug.api.DevtoolsDebugApi;
import com.equo.debug.devtools.model.DevtoolsJson;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.equo.testing.common.util.EquoRule;
import com.google.gson.Gson;

public class DevtoolsUnitTest {

  @Inject
  private DevtoolsDebugApi devTool;

  private static final String DEBUG_PROPERTY = "org.eclipse.swt.chromium.remote-debugging-port";

  private Logger mockLogger;

  @Rule
  public EquoRule rule = new EquoRule(this).runInNonUiThread();

  @Before
  public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {
    mockLogger = mock(LoggerFactory.getLogger(devTool.getClass()).getClass());
    Field privateLoggerField = devTool.getClass().getDeclaredField("logger");
    // Set the accessibility as true
    privateLoggerField.setAccessible(true);
    // Set the value of private field with the mock
    privateLoggerField.set(devTool, mockLogger);
  }

  @Test
  public void whenDebugModeIsOnThenShouldOpenDevtoolsBrowser() throws IOException {
    String msg = "[ {\n" + "         \"description\": \"\",\n"
        + "        \"devtoolsFrontendUrl\": \"/devtools/inspector.html?ws=localhost:8888/devtools/page/EAB83668195A09BA76BB0F1FAFF4F248\",\n"
        + "        \"id\": \"EAB83668195A09BA76BB0F1FAFF4F248\",\n"
        + "        \"title\": \"plainequoapp/?equocommport=43009\",\n"
        + "        \"type\": \"page\",\n"
        + "        \"url\": \"http://plainequoapp/?equocommport=43009\",\n"
        + "        \"webSocketDebuggerUrl\": \"ws://localhost:8888/devtools/page/EAB83668195A09BA76BB0F1FAFF4F248\"\n"
        + "     } ]";

    DevtoolsDebugApi spyDevTool = spy(this.devTool);
    DevtoolsJson[] json = new Gson().fromJson(msg, DevtoolsJson[].class);
    doReturn(json).when(spyDevTool).getDevtoolsJson("http://localhost:8888");
    List<String> url = spyDevTool.getDevtoolsUrl("8888");

    checkUrlExists(url.get(0));
  }

  private void checkUrlExists(String url) throws IOException {
    assertThat(url).isEqualTo(
        "http://localhost:8888/devtools/inspector.html?ws=localhost:8888/devtools/page/EAB83668195A09BA76BB0F1FAFF4F248");
  }

  @Test
  public void whenDebugModeIsOnThenShouldOpenAllDevtoolsBrowser() throws IOException {
    String msg = "[ {\n" + "   \"description\": \"\",\n"
        + "   \"devtoolsFrontendUrl\": \"/devtools/inspector.html?ws=localhost:8888/devtools/page/0BD9A9DCBCC9A4CFD6D8EF692A31EA27\",\n"
        + "   \"id\": \"0BD9A9DCBCC9A4CFD6D8EF692A31EA27\",\n"
        + "   \"title\": \"plainequoapp/?equocommport=57311\",\n" + "   \"type\": \"page\",\n"
        + "   \"url\": \"http://plainequoapp/?equocommport=57311\",\n"
        + "   \"webSocketDebuggerUrl\": \"ws://localhost:8888/devtools/page/0BD9A9DCBCC9A4CFD6D8EF692A31EA27\"\n"
        + "}, {\n" + "   \"description\": \"\",\n"
        + "   \"devtoolsFrontendUrl\": \"/devtools/inspector.html?ws=localhost:8888/devtools/page/FBDE590D73B04D13528D929FB6137DE0\",\n"
        + "   \"id\": \"FBDE590D73B04D13528D929FB6137DE0\",\n" + "   \"title\": \"YouTube\",\n"
        + "   \"type\": \"page\",\n" + "   \"url\": \"https://www.youtube.com/\",\n"
        + "   \"webSocketDebuggerUrl\": \"ws://localhost:8888/devtools/page/FBDE590D73B04D13528D929FB6137DE0\"\n"
        + "}, {\n" + "   \"description\": \"\",\n"
        + "   \"devtoolsFrontendUrl\": \"/devtools/inspector.html?ws=localhost:8888/devtools/page/92E6B0509B3C72C07C246B49D020B073\",\n"
        + "   \"id\": \"92E6B0509B3C72C07C246B49D020B073\",\n"
        + "   \"title\": \"plainequoapp/?equocommport=57311\",\n" + "   \"type\": \"page\",\n"
        + "   \"url\": \"http://plainequoapp/?equocommport=57311\",\n"
        + "   \"webSocketDebuggerUrl\": \"ws://localhost:8888/devtools/page/92E6B0509B3C72C07C246B49D020B073\"\n"
        + "} ]";

    DevtoolsDebugApi spyDevTool = spy(this.devTool);
    DevtoolsJson[] json = new Gson().fromJson(msg, DevtoolsJson[].class);
    doReturn(json).when(spyDevTool).getDevtoolsJson("http://localhost:8888");
    List<String> urls = spyDevTool.getDevtoolsUrl("8888");

    checkAllUrlExists(urls);
  }

  private void checkAllUrlExists(List<String> urls) {
    assertThat(urls.get(0)).isEqualTo(
        "http://localhost:8888/devtools/inspector.html?ws=localhost:8888/devtools/page/0BD9A9DCBCC9A4CFD6D8EF692A31EA27");
    assertThat(urls.get(1)).isEqualTo(
        "http://localhost:8888/devtools/inspector.html?ws=localhost:8888/devtools/page/FBDE590D73B04D13528D929FB6137DE0");
    assertThat(urls.get(2)).isEqualTo(
        "http://localhost:8888/devtools/inspector.html?ws=localhost:8888/devtools/page/92E6B0509B3C72C07C246B49D020B073");
  }

  @Test
  public void checkAvailablePortIsSetCorrectly() {
    devTool.setDebugProperty("6753");
    String stringPort = System.getProperty(DEBUG_PROPERTY);
    int port = Integer.valueOf(stringPort);
    assertThat(port).isEqualTo(6753);
  }

  @Test
  public void setRandomPortCorrectly() {
    devTool.setDebugProperty("auto");
    String stringPort = System.getProperty(DEBUG_PROPERTY);
    int port = Integer.valueOf(stringPort);
    assertThat(port).isLessThan(65535).isGreaterThan(1023);
  }

  @Test
  public void whenAnInvalidPortIsSentShouldLogAnError() {
    shouldLogAnError("");
    shouldLogAnError("asd");
    shouldLogAnError("123d");
    shouldLogAnError("1023");
    shouldLogAnError("65535");
    shouldLogAnError("null");
    shouldLogAnError("2da2");
  }

  private void shouldLogAnError(String value) {
    ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
    devTool.setDebugProperty(value);
    // atLeast(0): Was called any number of times. Useful with captors.
    verify(mockLogger, Mockito.atLeast(0)).error(errorCaptor.capture());
    assertThat(errorCaptor.getValue())
        .isEqualTo("The port \"" + value + "\" is not an available port.");
  }

  @Test
  public void shouldLogAnErrorWhenAPortIsAlreadyUsed() throws IOException {
    ServerSocket sc = new ServerSocket(4565);
    ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
    devTool.setDebugProperty("4565");
    verify(mockLogger).error(errorCaptor.capture());
    assertThat(errorCaptor.getValue()).isEqualTo("The port \"4565\" is already used.");
  }

}
