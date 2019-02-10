$(document).ready(function () {
    const namespace = getUrlParameter('namespace').toString()

    function getUrlParameter(sParam) {
        var url = new URL(window.location.href);
        var namespaceValue = url.searchParams.get(sParam);
        return namespaceValue;
    }

    equo.on(namespace + "_icons", values => {
        vm.$data.parts = values;
        console.log('values= ', values)
    });

    function receiveToolBarModel(vm) {
        equo.on(namespace + "_icons", values => {
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
            receiveToolBarModel(vm);
        }

    });
});
