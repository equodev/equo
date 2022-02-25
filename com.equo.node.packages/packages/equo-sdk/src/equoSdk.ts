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

import { EquoComm, EquoCommService, SDKCommError } from '@equo/comm'
/**
 * @namespace
 * @description JavaScript API containing functionality to manipulate the application.
 * Use the ***SDK*** package for basic browser management.
 */
export namespace EquoSDK {
  const comm: EquoComm = EquoCommService

  const EQUO_EVENTS = {
    OPEN_BROWSER: 'openBrowser',
    UPDATE_BROWSER: 'updateBrowser',
    ADD_SHORTCUT: '_addShortcut',
    REMOVE_SHORTCUT: '_removeShortcut'
  }

  /**
     * Initializes and opens a browser.
     * @function
     * @name openBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
  export function openBrowser(browserParams: BrowserParams): void {
    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    comm.send(EQUO_EVENTS.OPEN_BROWSER, browserParams)
  }
  /**
     * Updates a browser.
     * @function
     * @name updateBrowser
     * @param {BrowserParams} browserParams
     * @returns {void}
     */
  export function updateBrowser(browserParams: BrowserParams): void {
    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    comm.send(EQUO_EVENTS.UPDATE_BROWSER, browserParams)
  }

  /**
     * Adds a global shortcut binded to the given callback
     * @function
     * @name addShortcut
     * @param {string} shortcut
     * @param {Function} callback
     * @returns {void}
     */
  export function addShortcut(shortcut: string, callback: Function): void {
    const payload = {
      shortcut,
      event: `_exec_shortcut_${shortcut}`
    }
    comm.send(EQUO_EVENTS.ADD_SHORTCUT, payload)
      .then(callback as ((response: any) => any) | undefined)
      .catch((error: SDKCommError) => {
        console.error(`Error adding shortcut ${payload.shortcut}.`, error)
      })
  }

  /**
     * Adds a global shortcut binded to a comm event
     * @function
     * @name addShortcutToEvent
     * @param {string} shortcut
     * @param {string} event
     * @returns {void}
     */
  export function addShortcutToEvent(shortcut: string, event: string): void {
    const payload = {
      shortcut,
      event
    }
    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    comm.send(EQUO_EVENTS.ADD_SHORTCUT, payload)
  }

  /**
     * Removes a global shortcut
     * @function
     * @name removeShortcut
     * @param {string} shortcut
     * @returns {void}
     */
  export function removeShortcut(shortcut: string): void {
    const payload = {
      shortcut
    }
    // eslint-disable-next-line @typescript-eslint/no-floating-promises
    comm.send(EQUO_EVENTS.REMOVE_SHORTCUT, payload)
  }
}

export class BrowserParams {
  private readonly url: string
  private readonly name: string
  private readonly position: string

  constructor(url: string, name: string, position: string) {
    this.url = url
    this.name = name
    this.position = position
  }

  public getUrl(): string {
    return this.url
  }

  public getName(): string {
    return this.name
  }

  public getPosition(): string {
    return this.position
  }
}
