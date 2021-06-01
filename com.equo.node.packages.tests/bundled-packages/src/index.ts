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

// @ts-ignore
import { EquoLoggingService } from '@equo/logging';
// @ts-ignore
import { EquoFramework } from '@equo/framework';
// @ts-ignore
import { EquoWebSocketService, EquoWebSocket } from '@equo/websocket';
// @ts-ignore
import { Menu, MenuBuilder } from '@equo/equo-application-menu';

var websocket: EquoWebSocket = EquoWebSocketService.get();

EquoFramework.openBrowser('');

websocket.on("_makeLogs", () => {
    EquoLoggingService.logInfo('testInfo');
    EquoLoggingService.logWarn('testWarn');
    EquoLoggingService.logError('testError');
	EquoLoggingService.logDebug('testDebug');
	EquoLoggingService.logTrace('testTrace');

	EquoLoggingService.getJsLoggerLevel((response: any) => {
		EquoLoggingService.logInfo('Current is ' + response);

		EquoLoggingService.setJsLoggerLevel(EquoLoggingService.LOG_LEVEL_DEBUG);
		EquoLoggingService.getJsLoggerLevel((response: any) => {
			EquoLoggingService.logInfo('Current is ' + response);

			EquoLoggingService.setGlobalLoggerLevel(EquoLoggingService.LOG_LEVEL_TRACE);
			EquoLoggingService.getGlobalLoggerLevel((response: any) => {
				EquoLoggingService.logInfo('Global is ' + response);

				EquoLoggingService.setGlobalLoggerLevel(EquoLoggingService.LOG_LEVEL_INFO);
				EquoLoggingService.getGlobalLoggerLevel((response: any) => {
					EquoLoggingService.logInfo('Global is ' + response);
				});
			});
		});
	});
});

function inicMenu(elementMenu : MenuBuilder, func?: Function) {
    elementMenu.withMainMenu("Menu1")
    .addMenuItem("SubMenu11").onClick("_test").addShortcut("M1+W")
    .addMenu("SubMenu12")
        .addMenuItem("SubMenu121")

    .withMainMenu("Menu2")
    .addMenuItem("SubMenu21").onClick("_test")
    .addMenuSeparator()
    .addMenu("SubMenu22")
        .addMenuItem("SubMenu221").onClick("_test").addShortcut("M1+G")
            .addMenu("SubMenu222")
                .addMenuItem("SubMenu2221")

    .setApplicationMenu(func);
}

websocket.on("_createMenu", () => {
    let menu1 = Menu.create();
    inicMenu(menu1, (ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu1", json); });
});

websocket.on("_appendMenuItem", () => {
	let menu2 = Menu.create();
    inicMenu(menu2);
    menu2.appendMenuItem("Menu1", 0, "SubMenu10").onClick("_test").addShortcut("M1+L")
        .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu2", json); });
});

websocket.on("_appendMenu", () => {
	let menu3 = Menu.create();
    inicMenu(menu3);
    menu3.appendMenu("Menu2/SubMenu22", 1, "SubMenu223").addMenuItem("SubMenu2231").onClick("_test").addShortcut("M1+K")
        .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu3", json); });
});

websocket.on("_removeMenuElement", () => {
	let menu4 = Menu.create();
    inicMenu(menu4);
    menu4.removeMenuElementByPath("Menu2/SubMenu22/SubMenu222").setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu4", json); });
});

websocket.on("_appendMenuAtTheEnd", () => {
	let menu5 = Menu.create();
    inicMenu(menu5);
    menu5.appendMenuAtTheEnd("Menu1/SubMenu12", "SubMenu122").addMenuItem("SubMenu1221")
        .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu5", json); });
});

websocket.on("_appendMenuItemAtTheEnd", () => {
	let menu6 = Menu.create();
    inicMenu(menu6);
    menu6.appendMenuItemAtTheEnd("Menu1/SubMenu12", "SubMenu123").setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu6", json); });
});

websocket.on("_appendMenuRepeated", () => {
	let menu7 = Menu.create();
    inicMenu(menu7);
    menu7.appendMenuItemAtTheEnd("Menu1/SubMenu12", "SubMenu123").addMenuItem("SubMenu1221").setApplicationMenu();

    try {
        menu7.appendMenuItemAtTheEnd("Menu1/SubMenu12", "SubMenu123").addMenuItem("SubMenu1221").setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu7", json); });
    } catch (Exception) {
        websocket.send("_testSetMenu7", JSON.parse(JSON.stringify({"code":450,"error":"The menu SubMenu123 already exist in Menu1/SubMenu12"})))
    }
});

websocket.on("_appendMenuItemRepeated", () => {
	let menu8 = Menu.create();
    inicMenu(menu8);
    menu8.appendMenuAtTheEnd("Menu1/SubMenu12", "SubMenu122").addMenuItem("SubMenu1221").setApplicationMenu();

    try {
        menu8.appendMenuAtTheEnd("Menu1/SubMenu12", "SubMenu122").addMenuItem("SubMenu1221").setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu8", json); });
    } catch (Exception) {
        websocket.send("_testSetMenu8", JSON.parse(JSON.stringify({"code":450,"error":"The menu SubMenu122 already exist in Menu1/SubMenu12"})))
    }
});

websocket.on("_createMenuWithRepeatedMenus", () => {
    Menu.create().withMainMenu("Menu1")
    .addMenuItem("SubMenu11").onClick("_test").addShortcut("M1+W")
    .addMenu("SubMenu12")
        .addMenuItem("SubMenu121")

    .withMainMenu("Menu2")
    .addMenuItem("SubMenu21").onClick("_test")
    .addMenuSeparator()
    .addMenu("SubMenu22")
        .addMenuItem("SubMenu221").onClick("_test").addShortcut("M1+G")
            .addMenu("SubMenu222")
                .addMenuItem("SubMenu2221")
                .addMenu("SubMenu2221")
                    .addMenuItem("newMenu")
        
    .withMainMenu("Menu1")
        .addMenuItem("SubMenu14").onClick("_test").addShortcut("M1+W")

    .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu9", json); });
});

websocket.on("_buildWithCurrentModel", () => {
    Menu.getCurrentModel((builder: MenuBuilder) => {builder
        .withMainMenu("Menu3")
            .addMenuItem("subMenu31").onClick("_test").addShortcut("M1+W")
            .addMenu("subMenu32")
                .addMenuItem("subMenu321")
        
        .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testSetMenu10", json); })
    })
});

websocket.on("_buildWithCurrentModelWithRepeatedMenus", () => {

    Menu.getCurrentModel((builder: MenuBuilder) => {
        try {
            builder.withMainMenu("Menu2")
                .addMenuItem("SubMenu22").onClick("_test").addShortcut("M1+W")
                .addMenu("subMenu23")
                .addMenuItem("subMenu231")
        
                .setApplicationMenu();
        } catch{
            websocket.send("_testSetMenu11", JSON.parse(JSON.stringify({"code":450,"error":"The menu SubMenu22 already exist in Menu2 and your type is Menu"})))
        }
    })    
});

websocket.on("_customActionOnClick", () => {
    Menu.create().withMainMenu("Menu1")
    .addMenuItem("SubMenu11").onClick(()=> {websocket.send("_customActionOnClickResponse")}).addShortcut("M1+W")

    .setApplicationMenu((ws: EquoWebSocket, json: JSON) => { ws.send("_testCustomOnClick", json); });
});

websocket.send("_ready");
