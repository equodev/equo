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

export interface EquoService<T> {
  id: string
  service: T
}

export namespace EquoService {
  const global = window as any

  export function get<T> (id: string, create?: () => EquoService<T>): T {
    const globalService: T = global[id] as T
    if (!globalService && (create != null)) {
      const newService: EquoService<T> = create()
      if (!newService) {
        throw new Error(`${id} couldn't be created`)
      }
      install<T>(newService)
      return newService.service
    } else if (globalService) {
      return globalService
    }
    throw new Error(`${id} has not been installed`)
  }

  export function install<T> (service: EquoService<T>): void {
    if (global[service.id]) {
      return
    }
    global[service.id] = service.service
  }
}
