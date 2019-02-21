window.equo = window.equo || {};

(function (equo) {

  equo.getE4ElementNamespace = function (sParam) {
    var url = new URL(window.location.href);
    var namespaceValue = url.searchParams.get(sParam);
    return namespaceValue;
  }

  const namespace = equo.getE4ElementNamespace('namespace').toString()

  equo.getE4Model = function (renderE4Model) {
    equo.send(namespace + '_getModel');
    equo.on(namespace + "_model", values => {
      renderE4Model(namespace, values)
    });
  }

}(equo));
