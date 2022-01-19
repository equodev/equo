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

package com.equo.node.packages.tests.handlers;

public class TestPayload {

  private String testParam1;
  private int testParam2;

  public TestPayload(String testParam1, int testParam2) {
    this.testParam1 = testParam1;
    this.testParam2 = testParam2;
  }

  public String getTestParam1() {
    return this.testParam1;
  }

  public int getTestParam2() {
    return testParam2;
  }
}
