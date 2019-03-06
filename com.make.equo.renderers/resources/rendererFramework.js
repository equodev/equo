window.equo = window.equo || {};

(function (equo) {

  equo.getE4ElementNamespace = function (sParam) {
    var url = new URL(window.location.href);
    var namespaceValue = url.searchParams.get(sParam);
    return namespaceValue;
  }

  const namespace = equo.getE4ElementNamespace('namespace').toString()

  equo.getE4Model = function (renderE4Model) {
    equo.on(namespace + "_model", values => {
      renderE4Model(namespace, values)
    });
    equo.send(namespace + '_getModel');
  }

  equo.getModelContributions = function (setModelContributions) {
    equo.on(namespace + "_modelContributions", modelContributionFileIds => {
      setModelContributions(namespace, modelContributionFileIds)
    });
    equo.send(namespace + '_getModelContributions');
  }
}(equo));
