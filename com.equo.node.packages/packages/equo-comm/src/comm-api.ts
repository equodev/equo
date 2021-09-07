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

import { EquoService } from '@equo/service-manager'
import { UUID } from './util'

type Payload = any
type Args = any

export interface CallbackArgs {
  once: boolean
};

export interface UserEvent {
  actionId: string
  payload?: Payload
  args?: any
};

export interface UserEventCallback {
  onSuccess: Function
  onError?: Function
  args?: CallbackArgs
};

interface FrameworkArgs {
  callerId?: string
  success?: boolean
}

export class EquoComm {
  private readonly HOST = '127.0.0.1'
  private readonly port: number
  private readonly userEventCallbacks: Map<string, UserEventCallback> = new Map()
  private ws?: WebSocket

  /**
       * @name EquoComm
       * @class
       */
  constructor(port: number) {
    this.port = port
    this.openComm()
  }

  private openComm(): void {
    if (this.ws !== undefined && this.ws.readyState !== WebSocket.CLOSED) {
      return
    }
    this.ws = new WebSocket(`ws://${this.HOST}:${this.port}`)

    // Binds functions to the listeners for the comm.
    this.ws.onopen = (event: any): void => {
      // For reasons I can't determine, onopen gets called twice
      // and the first time event.data is undefined.
      // Leave a comment if you know the answer.
      if (event.data === undefined) { }
    }

    this.ws.onmessage = (event: any): void => {
      var message: UserEvent & FrameworkArgs | null = this.processMessage(event.data)
      if (message) {
        var actionId: string = message.actionId
        if (this.userEventCallbacks.has(actionId)) {
          var callback: UserEventCallback | undefined = this.userEventCallbacks.get(message.actionId)
          if (callback?.args?.once) {
            this.userEventCallbacks.delete(actionId)
          }
          if (typeof message.success === 'undefined' || message.success) {
            callback?.onSuccess(message.payload)
          } else if (callback?.onError) {
            callback.onError(message.payload)
          }
        }
      }
    }

    this.ws.onclose = (): void => { }
  }

  private processMessage(event: string): UserEvent & FrameworkArgs | null {
    if (typeof event === 'undefined') {
      return null
    }
    try {
      return JSON.parse(event)
    } catch (err) {
    }
    return null
  }

  // Expose the below methods via the equo interface while
  // hiding the implementation of the method within the
  // function() block
  private sendToCommServer(userEvent: UserEvent, frameworkData?: FrameworkArgs): void {
    // Wait until the state of the comm is not ready and send the message when it is...
    this.waitForCommConnection(this, () => {
      var event: string = JSON.stringify({
        actionId: userEvent.actionId,
        payload: userEvent.payload,
        args: userEvent.args,
        callerId: frameworkData?.callerId
      })
      this.ws?.send(event)
    })
  }

  // Make the function wait until the connection is made...
  private waitForCommConnection(comm: EquoComm, callback: Function): void {
    setTimeout(
      () => {
        if (this.ws !== undefined && this.ws.readyState === WebSocket.OPEN) {
          if (callback != null) {
            callback()
          }
        } else {
          try {
            this.openComm()
          } catch (err) { }
          comm.waitForCommConnection(comm, callback)
        }
      }, 5) // wait 5 milisecond for the connection...
  };

  /**
     * Sends an action to execute in Framework.
     * @param {string} actionId
     * @param {Payload} [payload] - Optional
     * @param {Args} [args] Extra framework arguments - Optional
     * @returns {void}
     */
  public send(actionId: string, payload?: Payload, args?: Args): void {
    var userEvent: UserEvent = { actionId: actionId, payload: payload, args: args }
    this.sendToCommServer(userEvent)
  };

  /**
     * Listens for an event with the given name.
     * @param {string} userEventId
     * @param {UserEventCallback} callback
     * @returns {void}
     */
  public on(userEventActionId: string, onSuccessCallback: Function, onErrorCallback?: Function, args?: CallbackArgs): void {
    var callback: UserEventCallback = { onSuccess: onSuccessCallback, onError: onErrorCallback, args: args }
    this.userEventCallbacks.set(userEventActionId, callback)
  };

  /**
     * Sends a user event to the Framework. Returns a promise that resolves with the response's payload as argument.
     * @param userEvent
     * @returns {Promise<T | void>}
     */
  public async call<T>(userEvent: UserEvent, callbackArgs: CallbackArgs = { once: true }): Promise<T | undefined> {
    return await new Promise<T | undefined>((resolve, reject) => {
      var callerUuid = UUID.getUuid()
      this.on(callerUuid, resolve, reject, { once: callbackArgs.once })
      this.sendToCommServer(userEvent, { callerId: callerUuid })
    })
  }
}
/**
 * @namespace
 * @description Comm API for usage within the Equo Framework.
 *
 * This document specifies the usage methods for equo-comm.
 */
export namespace EquoCommService {
  const COMM_SERVICE_ID = 'equo-comm'
  const queryParams: URLSearchParams = new URLSearchParams(
    window.location.search
  )
  const portS: string | null = queryParams.get('equocommport')
  const port: number = portS === null ? 0 : +portS

  if (port === 0) {
    throw new Error('Comm port could not be found.')
  }
  /**
       * Creates an EquoCommService instance.
       * @function
       * @name create
       * @returns {EquoService<EquoComm>}
       */
  export function create(): EquoService<EquoComm> {
    return {
      id: COMM_SERVICE_ID,
      service: new EquoComm(port)
    }
  }
  /**
       * Obtains existing EquoComm service instance or else create a new one.
       * @function
       * @name get
       * @returns {EquoComm}
       */
  export function get(): EquoComm {
    return EquoService.get<EquoComm>(COMM_SERVICE_ID, create)
  }
}
