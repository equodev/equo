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

package com.equo.application.handlers;

/**
 * Encapsulation of parameters required to open a new browser.
 */
public class BrowserParams {

  private String url;
  private String name;
  private String position;
  private int style;

  public BrowserParams() {

  }

  public BrowserParams(String url) {
    this.url = url;
  }

  public BrowserParams(String url, String name) {
    this(url);
    this.name = name;
  }

  /**
   * BrowserParams constructor.
   * @param: url      url to open.
   * @param: name     browser name.
   * @param: position position to open browser.
   * @param: style    to set frameless or normal window.
   */
  public BrowserParams(String url, String name, String position, int style) {
    this.url = url;
    this.name = name;
    this.position = position;
    this.style = style;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public String getUrl() {
    return url;
  }

  public String getName() {
    return name;
  }

  public String getPosition() {
    return position;
  }

  public int getStyle() {
    return style;
  }

}
