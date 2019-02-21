$(document).ready(function () {

    const toolItemsIdsToData = {
        save: {
            icon: 'save',
            commandId: 'org.eclipse.ui.file.save'
        },
        saveAll: {
            icon: 'save_alt',
            commandId: 'org.eclipse.ui.file.saveAll'
        },
        newWizardDropDown: {
            icon: 'folder',
            commandId: 'org.eclipse.ui.newWizard'
        },
        print: {
            icon: 'print',
            commandId: 'org.eclipse.ui.file.print'
        },
        debug: {
            icon: 'bug',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.DebugDropDownAction'
        },
        'org.eclipse.debug.internal.ui.actions.DebugDropDownAction': {
            icon: 'bug',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.DebugDropDownAction'
        },
        'org.eclipse.debug.internal.ui.actions.RunDropDownAction': {
            icon: 'play-circle',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.RunDropDownAction'
        },
        'org.eclipse.jdt.ui.actions.OpenProjectWizard': {
            icon: 'envelope-open',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.OpenProjectWizard'
        },
        'org.eclipse.jdt.ui.actions.OpenPackageWizard': {
            icon: 'envelope-open-o',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.OpenPackageWizard'
        },
        'org.eclipse.jdt.ui.actions.NewTypeDropDown': {
            icon: 'rocket',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.NewTypeDropDown'
        },
        'org.eclipse.search.OpenSearchDialogPage': {
            icon: 'search-plus',
            commandId: 'AUTOGEN:::org.eclipse.search.searchActionSet/org.eclipse.search.OpenSearchDialogPage'
        },
        openType: {
            icon: 'folder-open',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.SearchActionSet/org.eclipse.jdt.ui.actions.OpenJavaSearchPage'
        },
        backardHistory: {
            icon: 'backward',
            commandId: 'org.eclipse.ui.navigate.backwardHistory'
        },
        forwardHistory: {
            icon: 'forward',
            commandId: 'org.eclipse.ui.navigate.forwardHistory'
        },
        'org.eclipse.ui.edit.text.gotoNextAnnotation': {
            icon: 'arrow-down',
            commandId: 'org.eclipse.ui.edit.text.actionSet.annotationNavigation'
        },
        'org.eclipse.ui.edit.text.gotoPreviousAnnotation': {
            icon: 'arrow-up',
            commandId: 'org.eclipse.ui.edit.text.actionSet.annotationNavigation'
        },
        'org.eclipse.ui.edit.text.gotoLastEditPosition': {
            icon: 'arrow-left',
            commandId: 'org.eclipse.ui.edit.text.gotoLastEditPosition'
        },
        // the following is an addon, should be handled differently
        // 'PerspectiveSpacer': {
        //     icon: '',
        //     commandId: 'org.eclipse.ui.navigate.forwardHistory'
        // },
        // the following is an addon, should be handled differently
        // 'PerspectiveSwitcher': {
        //     icon: '',
        //     commandId: 'org.eclipse.ui.navigate.forwardHistory'
        // },
        unknownElement: {
            icon: 'question-circle',
            commandId: 'unknownEquoCommand'
        }
        // unknown icon, it's used for ids that are not found here in this mapping table.

    }

    let app = {
        template: `
      <div class="app">
        
            <span v-for="item in e4Model">
            <md-button v-if="toolItemsIdsToData[item.id] !== undefined" class="md-icon-button md-primary" @click="callE4Command(toolItemsIdsToData[item.id].commandId)">
                <md-icon>{{toolItemsIdsToData[item.id].icon}}</md-icon>
            </md-button>
            <md-button v-else class="md-icon-button md-primary" @click="callE4Command(toolItemsIdsToData['unknownElement'].commandId)">
                <md-icon>{{toolItemsIdsToData['unknownElement'].icon}}</md-icon>
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