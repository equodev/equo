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

export namespace UUID {
  var lookupTable: string[] = []
  for (var i = 0; i < 256; i++) {
    lookupTable[i] = (i < 16 ? '0' : '') + (i).toString(16)
  }

  /**
     * Not intended to be cryptographically secure.
     * Math.random() can be switched out in favor of window.crypto.getRandomValues() (relatively slower)
     * if collissions are ever found.
     * Adapted from https://jcward.com/UUID.js (MIT license)
     * @returns UUID
     */
  export function getUuid(): string {
    var d0 = Math.random() * 0x100000000 >>> 0
    var d1 = Math.random() * 0x100000000 >>> 0
    var d2 = Math.random() * 0x100000000 >>> 0
    var d3 = Math.random() * 0x100000000 >>> 0
    return lookupTable[d0 & 0xff] + lookupTable[d0 >> 8 & 0xff] + lookupTable[d0 >> 16 & 0xff] + lookupTable[d0 >> 24 & 0xff] + '-' +
            lookupTable[d1 & 0xff] + lookupTable[d1 >> 8 & 0xff] + '-' + lookupTable[d1 >> 16 & 0x0f | 0x40] + lookupTable[d1 >> 24 & 0xff] + '-' +
            lookupTable[d2 & 0x3f | 0x80] + lookupTable[d2 >> 8 & 0xff] + '-' + lookupTable[d2 >> 16 & 0xff] + lookupTable[d2 >> 24 & 0xff] +
            lookupTable[d3 & 0xff] + lookupTable[d3 >> 8 & 0xff] + lookupTable[d3 >> 16 & 0xff] + lookupTable[d3 >> 24 & 0xff]
  }
}
