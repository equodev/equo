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

package com.equo.debug.devtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

import com.equo.debug.api.DevtoolsDebugApi;
import com.equo.debug.devtools.model.DevtoolsJson;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.gson.Gson;

/**
 * Class that opens devtools for debug mode.
 */
@Component
public class DevtoolsDebug implements DevtoolsDebugApi {
  private static final String WINDOWS_CHROME_DEFAULT_PATH_X86 =
      "C:\\Program Files (x86)\\Google\\Chrome\\Application\\";
  private static final String WINDOWS_CHROME_DEFAULT_PATH =
      "C:\\Program Files\\Google\\Chrome\\Application\\";

  private static final int MAX_PORT_AVAILABLE = 65535;

  private static final int MIN_PORT_AVAILABLE = 1023;

  private static final String DEBUG_PROPERTY = "org.eclipse.swt.chromium.remote-debugging-port";

  private Logger logger = LoggerFactory.getLogger(DevtoolsDebug.class);

  //necessary to prevent opening existing browsers
  private static Set<String> allUrls = new HashSet<>();

  @Override
  public List<String> getDevtoolsUrl(String debugPort) {
    String baseUrl = "http://localhost:" + debugPort;
    DevtoolsJson[] json = getDevtoolsJson(baseUrl);
    return getValidUrlFromJson(json, baseUrl);
  }

  @Override
  public DevtoolsJson[] getDevtoolsJson(String baseUrl) {
    String jsonUrl = baseUrl + "/json";
    try {
      URL url = new URL(jsonUrl);

      StringBuilder json = new StringBuilder();
      try (InputStream input = url.openStream();
          InputStreamReader isr = new InputStreamReader(input);
          BufferedReader reader = new BufferedReader(isr)) {
        int c;
        while ((c = reader.read()) != -1) {
          json.append((char) c);
        }
        DevtoolsJson[] jsonArray = new Gson().fromJson(json.toString(), DevtoolsJson[].class);

        return jsonArray;
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    // should not return null
    return null;
  }

  private List<String> getValidUrlFromJson(DevtoolsJson[] json, String baseUrl) {
    if (json == null) {
      return Arrays.asList(baseUrl);
    }

    List<String> urls = new ArrayList<>();
    for (DevtoolsJson devtoolsJson : json) {
      if (!allUrls.contains(baseUrl + devtoolsJson.getDevtoolsFrontendUrl())) {
        allUrls.add(baseUrl + devtoolsJson.getDevtoolsFrontendUrl());
        urls.add(baseUrl + devtoolsJson.getDevtoolsFrontendUrl());
      }
    }
    if (!urls.isEmpty()) {
      return urls;
    } else {
      // should not return baseUrl
      return Arrays.asList(baseUrl);
    }
  }

  @Override
  public void startDebug(String debugPort) {
    List<String> urls = getDevtoolsUrl(debugPort);
    openExternalBrowsers(urls);
  }

  @Override
  public boolean openExternalBrowsers(List<String> urls) {
    String operatingSystemName = System.getProperty("os.name").toLowerCase();
    // Open all browsers in debug mode
    boolean openBrowserCorrectly = false;
    for (String url : urls) {
      if (operatingSystemName.contains("linux")) {
        // Linux commands
        openBrowserCorrectly = executeCommands(
            new ArrayList<String[]>(Arrays.asList(new String[] { "google-chrome", url },
                new String[] { "chromium-browser", url }, new String[] { "xdg-open", url })));
      } else if (operatingSystemName.contains("win")) {
        // Windows commands
        openBrowserCorrectly = executeCommands(new ArrayList<String[]>(Arrays.asList(
            new String[] { WINDOWS_CHROME_DEFAULT_PATH_X86 + "chrome.exe", url },
            new String[] { WINDOWS_CHROME_DEFAULT_PATH + "chrome.exe", url },
            new String[] { "MicrosoftEdge", url }, new String[] { "cmd", "/c", "start", url })));
      } else if (operatingSystemName.contains("mac")) {
        // Mac commands
        openBrowserCorrectly = executeCommands(new ArrayList<String[]>(Arrays.asList(
            new String[] { "open", "-a", "Google Chrome", url },
            new String[] { "open", "-a", "Chromium", url }, new String[] { "open", "-a", url })));
      }
      if (!openBrowserCorrectly) {
        return false;
      }
    }
    return openBrowserCorrectly;
  }

  private boolean executeCommands(List<String[]> commands) {
    for (String[] command : commands) {
      boolean outputValue = executeCommand(command);
      if (outputValue) {
        return true;
      }
    }
    return false;
  }

  private boolean executeCommand(String[] commands) {
    try {
      Process process = new ProcessBuilder(commands).start();
      return process.waitFor() == 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public void setDebugProperty(String debugPort) {
    // check if it is a valid port, otherwise set a random port
    if (!debugPort.isEmpty() && debugPort.matches("[0-9]*")
        && Integer.parseInt(debugPort) > MIN_PORT_AVAILABLE
        && Integer.parseInt(debugPort) < MAX_PORT_AVAILABLE) {
      if (checkIfIsAnAvailablePort(debugPort)) {
        System.setProperty(DEBUG_PROPERTY, debugPort);
      }
    } else if ("auto".equals(debugPort)) {
      System.setProperty(DEBUG_PROPERTY, getValidPort());
    } else {
      logger.error("The port \"" + debugPort + "\" is not an available port.");
    }
  }

  private boolean checkIfIsAnAvailablePort(String debugPort) {
    try (ServerSocket sc = new ServerSocket(Integer.valueOf(debugPort))) {
      sc.close();
      return true;
    } catch (IOException e) {
      logger.error("The port \"" + debugPort + "\" is already used.");
      return false;
    }
  }

  private String getValidPort() {
    try (ServerSocket sc = new ServerSocket(0)) {
      int port = sc.getLocalPort();
      sc.close();
      String randomPort = String.valueOf(port);
      return randomPort;
    } catch (IOException e) {
      // should not return null
      logger.error("Could not get a valid port.");
      return null;
    }

  }
}