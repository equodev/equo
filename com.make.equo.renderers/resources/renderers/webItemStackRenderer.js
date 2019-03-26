$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
        <el-tabs v-model="selectedTab" type="card" @tab-remove="removeTab" @tab-click="handleClick">
            <template v-for='item in e4Model'>
                <el-tab-pane v-if="item.closeable === 'true'" closable :id="item.id" :key="item.label" :name="item.id + '_' + item.label" :label="item.label"></el-tab-pane>
                <el-tab-pane v-else :id="item.id" :key="item.label" :name="item.id + '_' + item.label" :label="item.label"></el-tab-pane>
            </template>
        </el-tabs>
      </div>
      `,
        props: {
            namespace: {
                type: String,
                required: true
            },
            e4Model: {
                type: Array,
                required: true
            }
        },
        computed: {
            selectedTab: {
                get() {
                    let tab = this.getSelectedTab();
                    return tab !== undefined ? tab.id+'_'+tab.label : undefined;
                },
                set(elem) {
                    if (elem !== undefined) {
                        let elemIdAndLabel = elem.split('_');
                        let elemId = elemIdAndLabel[0];
                        let elemLabel = elemIdAndLabel[1];

                        this.e4Model.forEach((tab) => {
                            if (tab.isSelected === 'true') {
                                tab.isSelected === 'false';
                            }
                            if (tab.id === elemId && tab.label === elemLabel) {
                                tab.isSelected = 'true';
                            }
                        });
                    }
                }
            }
        },
        mounted() {
            equo.on(this.namespace + "_addTab", tab => {
                let bool = true;
                currentElem = this.getSelectedTab();
                if (currentElem !== undefined) {
                    this.e4Model.forEach(modelElem => {
                        if (modelElem.id === tab.id && modelElem.label === tab.label) {
                            bool = false;
                        }
                    });
                    if (bool) {
                        this.e4Model.push(tab);
                        this.callE4Command(tab.id+'_'+tab.label);
                    } else if (tab.id !== currentElem.id) {
                        this.callE4Command(tab.id+'_'+tab.label);
                    }
                }
            });
        },
        methods: {
            getSelectedTab() {
                let selectedTab = this.e4Model[0];
                this.e4Model.forEach(tab => {
                    if (tab.isSelected === 'true') {
                        selectedTab = tab;
                    }
                });
                if (selectedTab !== undefined) {
                    selectedTab.isSelected = 'true';
                }
                return selectedTab;
            },
            handleClick(tab, event) {
                this.callE4Command(tab.name);
            },
            removeTab(tabToDelete) {
                let tabs = this.e4Model;

                let idAndLabel = tabToDelete.split('_');
                let toDeleteId = idAndLabel[0];
                let toDeleteLabel = idAndLabel[1];

                let activeTab = this.getSelectedTab();

                equo.on(this.namespace + '_proceed', () => {
                    if (activeTab !== undefined && activeTab.id === toDeleteId && activeTab.label === toDeleteLabel) {
                        activeTab.isSelected = 'false';
                        tabs.forEach((tab, index) => {
                            if (tab.id === toDeleteId) {
                                let nextTab = tabs[index + 1] || tabs[index - 1];
                                if (nextTab) {
                                    this.callE4Command(nextTab.id+'_'+nextTab.label);
                                }
                            }
                        });
                    }

                    this.e4Model = tabs.filter(tab => {
                        return tab.id !== toDeleteId || tab.label !== toDeleteLabel;
                    });
                });
                equo.send(this.namespace + '_tabClicked', {
                    partId: toDeleteId,
                    partLabel: toDeleteLabel,
                    namespace: this.namespace,
                    close: true
                });

            },
            callE4Command(tab) {
                let tabIdAndLabel = tab.split('_');
                let tabId = tabIdAndLabel[0];
                let tabLabel = tabIdAndLabel[1];

                equo.send(this.namespace + '_tabClicked', {
                    partId: tabId,
                    partLabel: tabLabel,
                    namespace: this.namespace
                });

                this.selectedTab = tab;
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
