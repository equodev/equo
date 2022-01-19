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

package com.equo.contribution.api.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.equo.contribution.api.tests.util.IContributionTest1;
import com.equo.contribution.api.tests.util.IContributionTest2;
import com.equo.contribution.api.tests.util.IContributionTest3;
import com.equo.contribution.api.tests.util.IContributionTest4;
import com.equo.contribution.api.tests.util.IContributionTest5;
import com.equo.testing.common.util.EquoRule;
import com.equo.contribution.api.IEquoContributionManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EquoContributionTest {

  @Inject
  private IEquoContributionManager equoContributionManager;

  @Inject
  private IContributionTest1 contributionTest1;

  @Inject
  private IContributionTest2 contributionTest2;

  @Inject
  private IContributionTest3 contributionTest3;

  @Inject
  private IContributionTest4 contributionTest4;

  @Inject
  private IContributionTest5 contributionTest5;

  @Rule
  public EquoRule rule = new EquoRule(this).runInNonUiThread();

  @Test
  public void testContributionLoadedOrder() {
    contributionTest1.load();
    contributionTest2.load();

    List<String> equoContributions = equoContributionManager.getContributions();

    assertThat(equoContributions).contains("equoproxy", "equologging", "equocomm",
        "contributiontest1", "contributiontest2");
  }

  @Test
  public void testUnsatisfiedDependency() {
    contributionTest3.load();

    List<String> equoContributions = equoContributionManager.getContributions();

    assertThat(equoContributions).doesNotContain("contributiontest3");
  }

  @Test
  public void testCyclicDependencies() {
    contributionTest4.load();
    contributionTest5.load();

    List<String> equoContributions = equoContributionManager.getContributions();

    assertThat(equoContributions).doesNotContain("contributiontest4", "contributiontest5");
  }

  // Check the list of pending contributions after the tests
  @Test
  public void test_noLoadedContributions() {
    List<String> equoContributions = equoContributionManager.getPendingContributions();
    assertThat(equoContributions).contains("contributiontest4", "contributiontest5",
        "contributiontest3");
  }
}
