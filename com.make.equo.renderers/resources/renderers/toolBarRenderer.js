$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
            <span v-for="item in e4Model">
                <span v-if="toolItemsIdsToData[item.id] !== null && toolItemsIdsToData[item.id] !== undefined && toolItemsIdsToData[item.id] !== 'undefined'">
                    <md-button class="md-icon-button md-primary" @click="callE4Command(item.id, toolItemsIdsToData[item.id].commandId, toolItemsIdsToData[item.id].userEvent, toolItemsIdsToData[item.id].isAnEquoModelElement)">
                        <md-icon>{{toolItemsIdsToData[item.id].icon}}</md-icon>
                        <md-tooltip>{{toolItemsIdsToData[item.id].tooltip}}</md-tooltip>
                    </md-button>
                </span>
            </span>
      </div>
      `,
        props: {
            namespace: {
                type: String
            },
            toolItemsIdsToData: {
                type: Object
            },
            e4Model: {
                type: Array,
                required: true
            }
        },
        mounted() {},
        methods: {
            callE4Command(toolBarElementId, commandId, userEvent, isAnEquoModelElement) {
                equo.send(this.namespace + '_itemClicked', {
                    toolBarElementId,
                    commandId,
                    userEvent,
                    isAnEquoModelElement
                });
            }
        },
        style: `
        `
    };
    Vue.component("app-comp", app)
    Vue.use(VueMaterial.default)

    let toolItemsIdsToData
    const setToolBarContributions = function (namespace, modelContributions) {
        toolItemsIdsToData = modelContributions
    }

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
    equo.getModelContributions(setToolBarContributions);
    equo.getE4Model(createToolbar);
});
