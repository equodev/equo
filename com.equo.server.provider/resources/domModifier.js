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
import $ from "../../node_modules/@types/jquery";

window.equo = window.equo || {};

(function (equo) {
    const domModifiersCallbacks = [];

    equo.onNativeDomChanged = function (callback) {
        domModifiersCallbacks.push(callback);
    };

    $(document).ready(() => {
        const observeDOM = (function () {
            const MutationObserver =
                    window.MutationObserver || window.WebKitMutationObserver,
                eventListenerSupported = window.addEventListener;

            return function (obj, callback) {
                if (MutationObserver) {
                    // define a new observer
                    // 'observer' is declared but its value is never read.
                    const obs = new MutationObserver((mutations/* , observer */) => {
                        if (
                            mutations[0].addedNodes.length ||
                            mutations[0].removedNodes.length
                        ) {
                            callback(mutations);
                        }
                    });
                    obs.observe(obj, {
                        childList: true,
                        subtree: true,
                    });
                } else if (eventListenerSupported) {
                    obj.addEventListener("DOMNodeInserted", callback, false);
                    obj.addEventListener("DOMNodeRemoved", callback, false);
                }
            };
        })();

        // Observe the body
        const targetNode = document.body;
        observeDOM(targetNode, (mutations) => {
            for (let i = 0; i < mutations.length; i++) {
                const mutation = mutations[i];
                if (mutation.addedNodes.length) {
                    const addedNode = $(mutation.addedNodes[0]);
                    for (const callback of domModifiersCallbacks) {
                        callback(addedNode);
                    }
                }
            }
        });
    });
})(window.equo);
