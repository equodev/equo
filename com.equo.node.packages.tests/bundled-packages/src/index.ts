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

// @ts-expect-error
import { EquoLoggingService } from '@equo/logging'
// @ts-expect-error
import { EquoCommService, EquoComm } from '@equo/comm'
// @ts-expect-error
import { Menu, MenuBuilder } from '@equo/equo-application-menu'

const comm: EquoComm = EquoCommService.get()

comm.on('_makeLogs', () => {
  EquoLoggingService.logInfo('testInfo')
  EquoLoggingService.logWarn('testWarn')
  EquoLoggingService.logError('testError')
  EquoLoggingService.logDebug('testDebug')
  EquoLoggingService.logTrace('testTrace')

  EquoLoggingService.getJsLoggerLevel((response: string) => {
    EquoLoggingService.logInfo(`Current is ${response}`)

    EquoLoggingService.setJsLoggerLevel(EquoLoggingService.LOG_LEVEL_DEBUG)
    EquoLoggingService.getJsLoggerLevel((response: string) => {
      EquoLoggingService.logInfo(`Current is ${response}`)

      EquoLoggingService.setGlobalLoggerLevel(EquoLoggingService.LOG_LEVEL_TRACE)
      EquoLoggingService.getGlobalLoggerLevel((response: string) => {
        EquoLoggingService.logInfo(`Global is ${response}`)

        EquoLoggingService.setGlobalLoggerLevel(EquoLoggingService.LOG_LEVEL_INFO)
        EquoLoggingService.getGlobalLoggerLevel((response: string) => {
          EquoLoggingService.logInfo(`Global is ${response}`)
        })
      })
    })
  })
})

function initMenu (elementMenu: MenuBuilder, func?: Function): void {
  elementMenu.withMainMenu('Menu1')
    .addMenuItem('SubMenu11').onClick('_test').withShortcut('M1+W')
    .addMenu('SubMenu12')
    .addMenuItem('SubMenu121')

    .withMainMenu('Menu2')
    .addMenuItem('SubMenu21').onClick('_test')
    .addMenuSeparator()
    .addMenu('SubMenu22')
    .addMenuItem('SubMenu221').onClick('_test').withShortcut('M1+G')
    .addMenu('SubMenu222')
    .addMenuItem('SubMenu2221')

    .setApplicationMenu(func)
}

comm.on('_createMenuImpl', () => {
  const menu1 = Menu.create()
  initMenu(menu1, (comm: EquoComm, json: JSON) => { comm.send('_testSetMenu1', json) })
})

comm.on('_createMenu', () => {
  comm.send('_createMenuImpl')
})

comm.on('_appendMenuItem', () => {
  const menu2 = Menu.create()
  initMenu(menu2)
  menu2.appendMenuItem('Menu1', 0, 'SubMenu10').onClick('_test').withShortcut('M1+L')
    .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu2', json) })
})

comm.on('_appendMenu', () => {
  const menu3 = Menu.create()
  initMenu(menu3)
  menu3.appendMenu('Menu2/SubMenu22', 1, 'SubMenu223').addMenuItem('SubMenu2231').onClick('_test').withShortcut('M1+K')
    .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu3', json) })
})

comm.on('_removeMenuElement', () => {
  const menu4 = Menu.create()
  initMenu(menu4)
  menu4.removeMenuElementByPath('Menu2/SubMenu22/SubMenu222').setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu4', json) })
})

comm.on('_appendMenuAtTheEnd', () => {
  const menu5 = Menu.create()
  initMenu(menu5)
  menu5.appendMenuAtTheEnd('Menu1/SubMenu12', 'SubMenu122').addMenuItem('SubMenu1221')
    .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu5', json) })
})

comm.on('_appendMenuItemAtTheEnd', () => {
  const menu6 = Menu.create()
  initMenu(menu6)
  menu6.appendMenuItemAtTheEnd('Menu1/SubMenu12', 'SubMenu123').setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu6', json) })
})

comm.on('_appendMenuRepeated', () => {
  const menu7 = Menu.create()
  initMenu(menu7)
  menu7.appendMenuItemAtTheEnd('Menu1/SubMenu12', 'SubMenu123').addMenuItem('SubMenu1221').setApplicationMenu()

  try {
    menu7.appendMenuItemAtTheEnd('Menu1/SubMenu12', 'SubMenu123').addMenuItem('SubMenu1221').setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu7', json) })
  } catch (Exception) {
    comm.send('_testSetMenu7', JSON.parse(JSON.stringify({ code: 450, error: 'The menu SubMenu123 already exist in Menu1/SubMenu12' })))
  }
})

comm.on('_appendMenuItemRepeated', () => {
  const menu8 = Menu.create()
  initMenu(menu8)
  menu8.appendMenuAtTheEnd('Menu1/SubMenu12', 'SubMenu122').addMenuItem('SubMenu1221').setApplicationMenu()

  try {
    menu8.appendMenuAtTheEnd('Menu1/SubMenu12', 'SubMenu122').addMenuItem('SubMenu1221').setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu8', json) })
  } catch (Exception) {
    comm.send('_testSetMenu8', JSON.parse(JSON.stringify({ code: 450, error: 'The menu SubMenu122 already exist in Menu1/SubMenu12' })))
  }
})

comm.on('_createMenuWithRepeatedMenus', () => {
  Menu.create().withMainMenu('Menu1')
    .addMenuItem('SubMenu11').onClick('_test').withShortcut('M1+W')
    .addMenu('SubMenu12')
    .addMenuItem('SubMenu121')

    .withMainMenu('Menu2')
    .addMenuItem('SubMenu21').onClick('_test')
    .addMenuSeparator()
    .addMenu('SubMenu22')
    .addMenuItem('SubMenu221').onClick('_test').withShortcut('M1+G')
    .addMenu('SubMenu222')
    .addMenuItem('SubMenu2221')
    .addMenu('SubMenu2221')
    .addMenuItem('newMenu')

    .withMainMenu('Menu1')
    .addMenuItem('SubMenu14').onClick('_test').withShortcut('M1+W')

    .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu9', json) })
})

comm.on('_buildWithCurrentModel', () => {
  Menu.getCurrentModel((builder: MenuBuilder) => {
    builder
      .withMainMenu('Menu3')
      .addMenuItem('subMenu31').onClick('_test').withShortcut('M1+W')
      .addMenu('subMenu32')
      .addMenuItem('subMenu321')

      .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testSetMenu10', json) })
  })
})

comm.on('_buildWithCurrentModelWithRepeatedMenus', () => {
  Menu.getCurrentModel((builder: MenuBuilder) => {
    try {
      builder.withMainMenu('Menu2')
        .addMenuItem('SubMenu22').onClick('_test').withShortcut('M1+W')
        .addMenu('subMenu23')
        .addMenuItem('subMenu231')

        .setApplicationMenu()
    } catch {
      comm.send('_testSetMenu11', JSON.parse(JSON.stringify({ code: 450, error: 'The menu SubMenu22 already exist in Menu2 and your type is Menu' })))
    }
  })
})

comm.on('_customActionOnClick', () => {
  Menu.create().withMainMenu('Menu1')
    .addMenuItem('SubMenu11').onClick(() => { comm.send('_customActionOnClickResponse') }).withShortcut('M1+W')

    .setApplicationMenu((comm: EquoComm, json: JSON) => { comm.send('_testCustomOnClick', json) })
})

comm.on('_resolvePromise', () => {
  comm.call<any>({ actionId: 'testPromise', payload: { testParam1: 'someString', testParam2: 256 } }).then((response: any) => {
    if (response.testParam2 === 512 && response.testParam1 === 'someStringAppended') {
      comm.send('_promiseResolved')
    }
  })
})

comm.send('_ready')
