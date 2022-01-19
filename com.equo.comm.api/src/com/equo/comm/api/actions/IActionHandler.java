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

package com.equo.comm.api.actions;

/**
 * <p>
 * Action handler interface. A class implementing this interface is declared as
 * a comm event listener.
 * </p>
 * <p>
 * By default, the methods will be called on an event named with its name. This
 * can be changed using {@link com.equo.comm.api.annotations.EventName}
 * annotation on the method.
 * </p>
 */
public interface IActionHandler {
}
