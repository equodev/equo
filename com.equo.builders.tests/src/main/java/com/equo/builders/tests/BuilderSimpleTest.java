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

package com.equo.builders.tests;

import org.junit.Test;

import com.equo.application.model.MenuItemBuilder;
import com.equo.application.model.ToolbarItemBuilder;

public class BuilderSimpleTest extends AbstractBuilderTest {

	@Test
	public void should_create_toolbar_with_an_item() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.withUI("/").withToolbar().addToolItem("text", tooltip);
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);

	}

	@Test
	public void should_Create_Menu_With_An_Item() throws Exception {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.withUI("/").withMainMenu("Menu").addMenuItem(label);

		assertCheckMenuItemLabel(label, menuItemBuilder);

	}

	@Test
	public void should_Create_MenuToolbar_With_An_Item() throws Exception {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.withUI("/").withMainMenu("Menu").addMenuItem(label);

		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem("text", tooltip);

		assertCheckMenuItemLabel(label, menuItemBuilder);

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
	}

	@Test
	public void should_Create_ToolbarMenu_With_An_Item() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.withUI("/").withToolbar().addToolItem("text", tooltip);

		String label = "item1";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu").addMenuItem(label);

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);

		assertCheckMenuItemLabel(label, menuItemBuilder);
	}



}
