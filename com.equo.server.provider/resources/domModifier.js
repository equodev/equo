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
