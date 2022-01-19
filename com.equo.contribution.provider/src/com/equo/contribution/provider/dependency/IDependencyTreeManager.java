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

package com.equo.contribution.provider.dependency;

import java.util.List;

import com.equo.contribution.api.EquoContribution;

/**
 * DependencyTree administrator interface. Provides the methods for using
 * DependencyTree.
 */
public interface IDependencyTreeManager {

  /**
   * Adds contribution to the dependency tree.
   * @param equoContribution Contribution to add.
   */
  void addContribution(EquoContribution equoContribution);

  /**
   * Gets and extract a list of contributions that are ready to be uploaded. This implies that
   * the contributions are discarded from the dependency tree.
   * @param  dependency Satisfied dependence
   * @return            List of contributions.
   */
  List<EquoContribution> pullSatisfiedContributions(String dependency);

  /**
   * Removes dependency key to dependency tree.
   * @param dependency Dependency to remove.
   */
  void removeDependency(String dependency);

  /**
   * Gets a list of pending contributions.
   * @return List of pending contributions.
   */
  List<String> getPendingContributions();
}
