$(document).ready(function () {
    const namespace = getUrlParameter('namespace').toString()

    function getUrlParameter(sParam) {
        var url = new URL(window.location.href);
        console.log('url is ', url)
        var namespaceValue = url.searchParams.get(sParam);
        console.log('namespace is ', namespaceValue)
        return namespaceValue;
    }

    function getToolBarModel(vm) {
        equo.send(namespace + 'getModel');
        equo.on(namespace + "model", values => {
            console.log('iconos son ')
            vm.$data.parts = values;
            console.log('values= ', values)
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
                equo.send(namespace + '_iconClicked', {
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
