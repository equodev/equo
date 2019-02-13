$(document).ready(function () {

    const toolItemsData = {
        'com.make.equo.application.provider.handledtoolitem.save': 'save',
        'com.make.equo.application.provider.handledtoolitem.close': 'close'
    }

    let app = {
        template: `
      <div class="app">
        
            <span v-for="item in e4Model">
            <md-button class="md-icon-button md-primary">
                <md-icon>{{toolItemsData[item.id]}}</md-icon>
            </md-button>
            </span>
        
      </div>
      `,
        // data: function () {
        //     return {
        //         txt: 'test',
        //     };
        // },
        props: {
            toolItemsData,
            e4Model: {
                type: Array,
                required: true
            }
        },
        mounted() {},
        methods: {},
        style: `
        `
    };
    Vue.component("app-comp", app);
    Vue.use(VueMaterial.default)

    // export default app;

    // import app from "./js/component-2.js";
    // $('.create-b').on('click', function () {
    //     let toolBarApp = Vue.extend(app);
    //     let component = new toolBarApp().$mount()
    //     $('#proexemplelist').append(component.$el)
    // });

    // Add toolbar elements to the html body
    const createToolbar = function (e4Model) {
        console.log('The e4 model is ', e4Model);
        let toolBarApp = Vue.extend(app);
        let component = new toolBarApp({
            propsData: {
                toolItemsData,
                e4Model
            }
        }).$mount()

        $('body').append(component.$el)
        $('body').append(`<style lang="scss" scoped>
        small {
          display: block;
        }
      </style>`)
    }

    equo.getE4Model(createToolbar);

    const sendOnclick = function (accion) {
        equo.send(namespace + '_itemClicked', {
            command: accion
        });
    }
});