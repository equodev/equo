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

import { EquoService } from '@equo/service-manager'
import { UUID } from './util'

export type OnSuccessCallback<T> = (response: T) => any
export type OnErrorCallback = (error: SDKCommError) => any
export type Payload = any
export interface CallbackArgs {
  once: boolean
};

export interface SDKCommError {
  code?: number
  message: string
}

export interface UserEvent {
  actionId: string
  payload?: Payload
};

type SDKMessage = UserEvent & { error?: SDKCommError } & { success?: boolean, callbackId?: string } | null

interface UserEventCallback {
  id?: string
  onSuccess: OnSuccessCallback<JSON>
  onError?: OnErrorCallback
  args?: CallbackArgs
};

export class EquoComm {
  private readonly userEventCallbacks: Map<string, UserEventCallback> = new Map()
  private readonly ws?: WebSocket

  /**
       * @name EquoComm
       * @class
       */
  constructor(port?: number) {
    this.ws = this.getWebSocketIfExists(port)
    if (typeof this.ws === 'undefined') {
      // @ts-expect-error
      window.equoReceiveMessage = (event) => {
        this.receiveMessage(event)
      }
    }
  }

  private getWebSocketIfExists(port?: number): WebSocket | undefined {
    if (typeof port === 'undefined') {
      return
    }
    if (typeof this.ws !== 'undefined' && this.ws.readyState !== WebSocket.CLOSED) {
      return this.ws
    }

    const HOST: string = '127.0.0.1'

    var ws: WebSocket = new WebSocket(`ws://${HOST}:${port}`)

    // Binds functions to the listeners for the comm.
    ws.onopen = (event: any): void => {
    }

    ws.onclose = (): void => {
    }

    ws.onmessage = (event: any): void => {
      this.receiveMessage(event.data)
    }

    return ws
  }

  private receiveMessage(event: any): void {
    var message: SDKMessage = this.processMessage(event)
    if (message) {
      var actionId: string = message.actionId
      if (this.userEventCallbacks.has(actionId)) {
        var callback: UserEventCallback | undefined = this.userEventCallbacks.get(message.actionId)
        if (callback?.args?.once) {
          this.userEventCallbacks.delete(actionId)
        }
        // Success and error callbacks are not implemented yet
        if (typeof message.success === 'undefined' || message.success) {
          var response: any = callback?.onSuccess(message.payload)
          if (typeof message.callbackId !== 'undefined') {
            this.sendToJava({ actionId: message.callbackId, payload: response })
          }
        } else if (callback?.onError) {
          if (typeof message.error !== 'undefined') {
            callback.onError(message.error)
          }
        }
      }
    }
  }

  private processMessage(event: string): SDKMessage {
    if (typeof event === 'undefined') {
      return null
    }
    try {
      return JSON.parse(event)
    } catch (err) {
    }
    return null
  }

  private sendToJava(userEvent: UserEvent, callback?: UserEventCallback): void {
    var event: string = JSON.stringify({
      actionId: userEvent.actionId,
      payload: userEvent.payload,
      callbackId: callback?.id
    })
    // @ts-expect-error
    if (typeof window.equoSend !== 'undefined') {
      // @ts-expect-error
      window.equoSend({
        request: event,
        onSuccess: (response: any) => {
          var jsonResponse
          try {
            jsonResponse = JSON.parse(response)
          } catch (error) {
            jsonResponse = response
          }
          callback?.onSuccess(jsonResponse)
        },
        onFailure: (code: number, message: string) => { if (typeof callback?.onError !== 'undefined') { callback.onError({ code, message }) } },
        persistent: !callback?.args?.once
      })
    } else if (typeof this.ws !== 'undefined') {
      // Wait until the state of the comm is not ready and send the message when it is...
      this.waitForCommConnection(this, () => {
        this.ws?.send(event)
      })
    }
  }

  // Make the function wait until the connection is made...
  private waitForCommConnection(comm: EquoComm, callback: Function): void {
    setTimeout(
      () => {
        if (typeof this.ws !== 'undefined') {
          if (this.ws.readyState === WebSocket.OPEN) {
            if (callback != null) {
              callback()
            }
          } else {
            comm.waitForCommConnection(comm, callback)
          }
        }
      }, 5) // wait 5 milisecond for the connection...
  };

  /**
     * Sends an action to execute in java with the given payload.
     * @param {string} actionId
     * @param {Payload} [payload] - Optional
     * @returns {Promise<T | any>}
     */
  public async send<T>(actionId: string, payload?: Payload): Promise<T | any> {
    return await new Promise<T | any>((resolve, reject) => {
      var userEvent: UserEvent = { actionId: actionId, payload: payload }
      var callback: UserEventCallback = { onSuccess: resolve, onError: reject, args: { once: true } }
      if (typeof this.ws !== 'undefined') {
        callback.id = UUID.getUuid()
        this.on(callback.id, resolve, reject, callback.args)
      }
      this.sendToJava(userEvent, callback)
    })
  };

  /**
     * Listens for an event with the given name.
     * @param {string} userEventId
     * @param {OnSuccessCallback<any>} onSuccessCallback
     * @param {OnErrorCallback} onErrorCallback - Optional
     * @param {CallbackArgs} callbackArgs - Optional
     * @returns {void}
     */
  public on(userEventActionId: string, onSuccessCallback: OnSuccessCallback<any>, onErrorCallback?: OnErrorCallback, args?: CallbackArgs): void {
    var callback: UserEventCallback = { onSuccess: onSuccessCallback, onError: onErrorCallback, args: args }
    this.userEventCallbacks.set(userEventActionId, callback)
  };
}

const ID = 'equo-comm'

function create(): EquoService<EquoComm> {
  var port: number | undefined
  // @ts-expect-error
  if (typeof window.equoSend === 'undefined') {
    const queryParams: URLSearchParams = new URLSearchParams(
      window.location.search
    )
    const portS: string | null = queryParams.get('equocommport')
    port = portS === null ? undefined : +portS
  }

  return {
    id: ID,
    service: new EquoComm(port)
  }
}

const EquoCommService = EquoService.get<EquoComm>(ID, create)

export { EquoCommService }
