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
            icon: 'create_new_folder',
            commandId: 'org.eclipse.ui.newWizard'
        },
        'new.group': {
            icon: 'arrow_drop_down',
            commandId: 'org.eclipse.ui.newWizard'
        },
        print: {
            icon: 'print',
            commandId: 'org.eclipse.ui.file.print'
        },
        debug: {
            icon: 'bug_report',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.DebugDropDownAction'
        },
        'org.eclipse.debug.internal.ui.actions.DebugDropDownAction': {
            icon: 'bug_report',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.DebugDropDownAction'
        },
        'org.eclipse.debug.internal.ui.actions.RunDropDownAction': {
            icon: 'play_circle_filled',
            commandId: 'AUTOGEN:::org.eclipse.debug.ui.launchActionSet/org.eclipse.debug.internal.ui.actions.RunDropDownAction'
        },
        'org.eclipse.jdt.ui.actions.OpenProjectWizard': {
            icon: 'folder_open',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.OpenProjectWizard'
        },
        'org.eclipse.jdt.ui.actions.OpenPackageWizard': {
            icon: 'library_books',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.OpenPackageWizard'
        },
        'org.eclipse.jdt.ui.actions.NewTypeDropDown': {
            icon: 'library_add',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.JavaElementCreationActionSet/org.eclipse.jdt.ui.actions.NewTypeDropDown'
        },
        'org.eclipse.search.OpenSearchDialogPage': {
            icon: 'image_search',
            commandId: 'AUTOGEN:::org.eclipse.search.searchActionSet/org.eclipse.search.OpenSearchDialogPage'
        },
        openType: {
            icon: 'open_in_new',
            commandId: 'AUTOGEN:::org.eclipse.jdt.ui.SearchActionSet/org.eclipse.jdt.ui.actions.OpenJavaSearchPage'
        },
        backardHistory: {
            icon: 'arrow_back',
            commandId: 'org.eclipse.ui.navigate.backwardHistory'
        },
        forwardHistory: {
            icon: 'arrow_forward',
            commandId: 'org.eclipse.ui.navigate.forwardHistory'
        },
        'org.eclipse.ui.edit.text.gotoNextAnnotation': {
            icon: 'arrow_downward',
            commandId: 'org.eclipse.ui.edit.text.actionSet.annotationNavigation'
        },
        'org.eclipse.ui.edit.text.gotoPreviousAnnotation': {
            icon: 'arrow_upward',
            commandId: 'org.eclipse.ui.edit.text.actionSet.annotationNavigation'
        },
        'org.eclipse.ui.edit.text.gotoLastEditPosition': {
            icon: 'edit_location',
            commandId: 'org.eclipse.ui.edit.text.gotoLastEditPosition'
        },
        // // the following is an addon, should be handled differently
        // // 'PerspectiveSpacer': {
        // //     icon: '',
        // //     commandId: 'org.eclipse.ui.navigate.forwardHistory'
        // // },
        // // the following is an addon, should be handled differently
        // // 'PerspectiveSwitcher': {
        // //     icon: '',
        // //     commandId: 'org.eclipse.ui.navigate.forwardHistory'
        // // },
        // unknown icon, it's used for ids that are not found here in this mapping table.
        'unknownElement': {
            icon: 'help_outline',
            commandId: 'unknownEquoCommand'
        }
    }

    let app = {
        template: `
      <div class="app">
            <span v-for="item in e4Model">
                <span v-if="toolItemsIdsToData[item.id] !== null && toolItemsIdsToData[item.id] !== undefined && toolItemsIdsToData[item.id] !== 'undefined'">
                    <md-button class="md-icon-button md-primary" @click="callE4Command(item.id, toolItemsIdsToData[item.id].commandId)">
                        <md-icon>{{toolItemsIdsToData[item.id].icon}}</md-icon>
                    </md-button>
                </span>
                <span v-else>
                    <md-button class="md-icon-button md-primary" @click="callE4Command(item.id, toolItemsIdsToData['unknownElement'].commandId)">
                        <md-icon>{{toolItemsIdsToData['unknownElement'].icon}}</md-icon>
                    </md-button>
                </span>
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
            callE4Command(toolBarElementId, commandId) {
                equo.send(this.namespace + '_itemClicked', {
                    toolBarElementId,
                    commandId
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