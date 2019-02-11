$(document).ready(function () {
    const namespace = getUrlParameter('namespace').toString()
    const knownToolItems = {
        'com.make.equo.application.provider.handledtoolitem.save': {
            label: 'Save',
            icon: 'an-icon-identifier'
        }
    }

    let model;

    function getUrlParameter(sParam) {
        var url = new URL(window.location.href);
        console.log('url is ', url)
        var namespaceValue = url.searchParams.get(sParam);
        console.log('namespace is ', namespaceValue)
        return namespaceValue;
    }

    function getToolBarModel(vm) {
        equo.send(namespace + '_getModel');
        equo.on(namespace + "_model", values => {
            console.log('iconos son ')
            model = values;
        });
    }

    new Vue({
        el: '#app',
        data: {
            active: null,
            parts: []
        },
        methods: {
            send_onclick: function (accion) {
                equo.send(namespace + '_itemClicked', {
                    command: accion
                });
            },

        },
        created: function () {
            let vm = this;
            getToolBarModel(vm);
        }

    });
});