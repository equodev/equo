$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
        <md-tabs md-sync-route>
            <md-tab v-for="item in e4Model" :id="item.id" :md-label="item.label" @click="callE4Command(item.id)"></md-tab>
        </md-tabs>
      </div>
      `,
        props: {
            namespace: {
                type: String
            },
            e4Model: {
                type: Array,
                required: true
            }
        },
        mounted() {},
        methods: {
            callE4Command(tabId) {
                equo.send(this.namespace + '_tabClicked', {
                    partId: tabId,
                    namespace: this.namespace
                });
            }
        },
        style: `
        `
    };
    Vue.component("tabs-comp", app);
    Vue.use(VueMaterial.default)

    // Add web stack elements to the html body
    const createWebItemStack = function (namespace, e4Model) {
        console.log('The e4 model is ', e4Model);
        let webStackApp = Vue.extend(app);
        let component = new webStackApp({
            propsData: {
                namespace,
                e4Model
            }
        }).$mount()

        $('body').append(component.$el)

    }

    equo.getE4Model(createWebItemStack);

});