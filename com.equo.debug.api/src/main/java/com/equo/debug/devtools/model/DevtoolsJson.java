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

package com.equo.debug.devtools.model;

/**
 * Json format class for devtools.
 */
public class DevtoolsJson {
  private String description;
  private String devtoolsFrontendUrl;
  private String id;
  private String title;
  private String type;
  private String url;
  private String webSocketDebuggerUrl;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDevtoolsFrontendUrl() {
    return devtoolsFrontendUrl;
  }

  public void setDevtoolsFrontendUrl(String devtoolsFrontendUrl) {
    this.devtoolsFrontendUrl = devtoolsFrontendUrl;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getWebSocketDebuggerUrl() {
    return webSocketDebuggerUrl;
  }

  public void setWebSocketDebuggerUrl(String webSocketDebuggerUrl) {
    this.webSocketDebuggerUrl = webSocketDebuggerUrl;
  }
}
