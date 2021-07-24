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

window.equo = window.equo || {};

(function (equo) {
  equo.openBrowser = function (browserParams) {
    equo.sendToCommServer('openBrowser', browserParams)
  }

  equo.updateBrowser = function (browserParams) {
    equo.sendToCommServer('updateBrowser', browserParams)
  }

  equo.addMenu = function (menuModel) {
    equo.sendToCommServer('_setMenu', menuModel)
  }

  equo.addShortcut = function (shortcut, callback) {
    const payload = {
      shortcut,
      event: `_exec_shotcut_${shortcut}`
    }
    equo.on(payload.event, callback)
    equo.sendToCommServer('_addShortcut', payload)
  }

  equo.addShortcutToEvent = function (shortcut, event) {
    const payload = {
      shortcut,
      event
    }
    equo.sendToCommServer('_addShortcut', payload)
  }

  // 'event' is declared but its value is never read.
  equo.removeShortcut = function (shortcut/* , event */) {
    const payload = {
      shortcut
    }
    equo.sendToCommServer('_removeShortcut', payload)
  }
})(window.equo)
