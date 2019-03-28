$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
        <el-tabs v-model="selectedTab" type="card" @tab-remove="removeTab" @tab-click="handleClick">
            <template v-for='item in e4Model'>
                <el-tab-pane v-if="item.closeable === 'true'" closable :id="item.id" :key="item.name" :name="item.name" :label="item.label"></el-tab-pane>
                <el-tab-pane v-else :id="item.id" :key="item.name" :name="item.name" :label="item.label"></el-tab-pane>
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
                    return tab !== undefined ? tab.name : undefined;
                },
                set(tabName) {
                    if (tabName !== undefined) {
                        this.e4Model.forEach((tab) => {
                            if (tab.isSelected === 'true') {
                                tab.isSelected === 'false';
                            }
                            if (tab.name === tabName) {
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
                if (this.e4Model.length === 0) {
                    this.e4Model.push(tab);
                    this.callE4Command(tab.name);
                } else {
                    currentElem = this.getSelectedTab();
                    if (currentElem !== undefined) {
                        this.e4Model.forEach(modelElem => {
                            if (modelElem.name === tab.name) {
                                bool = false;
                                return;
                            }
                        });
                        if (bool) {
                            this.e4Model.push(tab);
                            this.callE4Command(tab.name);
                        } else if (tab.name !== currentElem.name) {
                            this.callE4Command(tab.name);
                        }
                    }
                }
            });
            equo.on(this.namespace + "_updateTab", tab => {
                this.e4Model.forEach(modelElem => {
                    if (modelElem.name === tab.name) {
                        for (property in tab) {
                            modelElem[property] = tab[property];
                        }
                        return;
                    }
                })
            })
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
            removeTab(tabName) {
                let tabs = this.e4Model;

                let activeTab = this.getSelectedTab();

                equo.on(this.namespace + '_proceedClose', () => {
                    if (activeTab !== undefined && activeTab.name === tabName) {
                        activeTab.isSelected = 'false';
                        tabs.forEach((tab, index) => {
                            if (tab.name === tabName) {
                                let nextTab = tabs[index + 1] || tabs[index - 1];
                                if (nextTab) {
                                    this.callE4Command(nextTab.name);
                                }
                            }
                        });
                    }

                    this.e4Model = tabs.filter(tab => {
                        return tab.name !== tabName;
                    });
                });
                equo.send(this.namespace + '_tabClicked', {
                    partName: tabName,
                    namespace: this.namespace,
                    close: true
                });

            },
            callE4Command(tabName) {
                equo.send(this.namespace + '_tabClicked', {
                    partName: tabName,
                    namespace: this.namespace
                });

                this.selectedTab = tabName;
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
