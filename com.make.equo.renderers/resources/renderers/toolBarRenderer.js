$(document).ready(function () {

    // Add toolbar elements to the html body
    const createToolbar = function (e4Model) {
        console.log('The e4 model is ', e4Model);
    }

    equo.getE4Model(createToolbar);

    const sendOnclick = function (accion) {
        equo.send(namespace + '_itemClicked', {
            command: accion
        });
    }
});
