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

  let domModifiersCallbacks = [];

  equo.onNativeDomChanged = function (callback) {
    domModifiersCallbacks.push(callback);
  };

  $(document).ready(function () {
    const observeDOM = (function () {
      let MutationObserver = window.MutationObserver || window.WebKitMutationObserver,
        eventListenerSupported = window.addEventListener;

      return function (obj, callback) {
        if (MutationObserver) {
          // define a new observer
          let obs = new MutationObserver(function (mutations, observer) {
            if (mutations[0].addedNodes.length || mutations[0].removedNodes.length)
              callback(mutations);
          });
          obs.observe(obj, {
            childList: true,
            subtree: true
          });
        } else if (eventListenerSupported) {
          obj.addEventListener('DOMNodeInserted', callback, false);
          obj.addEventListener('DOMNodeRemoved', callback, false);
        }
      };
    })();

    // Observe the body
    let targetNode = document.body;
    observeDOM(targetNode, function (mutations) {
      for (let i = 0; i < mutations.length; i++) {
        let mutation = mutations[i];
        if (mutation.addedNodes.length) {
          let addedNode = $(mutation.addedNodes[0]);
          for (let callback of domModifiersCallbacks) {
            callback(addedNode);
          }
        }
      }
    });
  });
}(equo));
