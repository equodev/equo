$(document).ready(function () {

    const toolItemsIdsToData = {
        'save': {
            icon: 'save',
            commandId: 'org.eclipse.ui.file.save'
        },
        'saveAll': {
            icon: 'save_alt',
            commandId: 'org.eclipse.ui.file.saveAll'
        }
    }

    let app = {
        template: `
      <div class="app">
        
            <span v-for="item in e4Model">
            <md-button v-if="toolItemsIdsToData[item.id] !== undefined" class="md-icon-button md-primary" @click="callE4Command(toolItemsIdsToData[item.id].commandId)">
                <md-icon>{{toolItemsIdsToData[item.id].icon}}</md-icon>
            </md-button>
            </span>
        
      </div>
      `,
        props: {
            namespace: {
                type: String
            },
            toolItemsIdsToData,
            e4Model: {
                type: Array,
                required: true
            }
        },
        mounted() {},
        methods: {
            callE4Command(commandId) {
                equo.send(this.namespace + '_itemClicked', {
                    command: commandId
                });
            }
        },
        style: `
        `
    };
    Vue.component("app-comp", app);
    Vue.use(VueMaterial.default)

    // Add toolbar elements to the html body
    const createToolbar = function (namespace, e4Model) {
        let toolBarApp = Vue.extend(app);
        let component = new toolBarApp({
            propsData: {
                namespace,
                toolItemsIdsToData,
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
});