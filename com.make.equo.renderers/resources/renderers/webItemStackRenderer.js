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
        data() {
            return {
                selected: ''
            };
        },
        computed: {
            selectedTab: {
                get() {
                    return this.selected !== '' ? this.selected : this.e4Model[0] !== undefined ? this.e4Model[0].name : undefined;
                },
                set(tabName) {
                    if (tabName !== undefined) {
                        this.e4Model.forEach((tab) => {
                            if (tab.isSelected === 'true') {
                                tab.isSelected = 'false';
                            }
                            if (tab.name === tabName) {
                                tab.isSelected = 'true';
                                this.selected = tab.name;
                            }
                        });
                    }

                }
            }
        },
        mounted() {
            this.e4Model.forEach(elem => {
                if (elem.isSelected === 'true') {
                    this.selected = elem.name;
                    return;
                }
            })
            equo.on(this.namespace + "_addTab", tab => {
                let tabRendered = false;
                if (this.e4Model.length === 0) {
                    this.e4Model.push(tab);
                    this.callE4Command(tab.name);
                } else {
                    if (this.selectedTab !== undefined && this.selectedTab !== '') {
                        this.e4Model.forEach(modelElem => {
                            if (modelElem.name === tab.name) {
                                tabRendered = true;
                                return;
                            }
                        });
                        if (!tabRendered) {
                            this.e4Model.push(tab);
                            this.callE4Command(tab.name);
                        } else if (tab.name !== this.selectedTab) {
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
            });
            equo.on(this.namespace + "_closeTab", tab => {
                let tabs = this.e4Model;
                let tabName = tab.name;
                this.e4Model = tabs.filter(tab => {
                    return tab.name !== tabName;
                });
            });
        },
        methods: {
            handleClick(tab, event) {
                this.callE4Command(tab.name);
            },
            removeTab(tabName) {
                let tabs = this.e4Model;

                equo.on(this.namespace + '_proceedClose', () => {
                    if (this.selectedTab !== undefined && this.selectedTab === tabName) {
                        tabs.forEach((tab, index) => {
                            if (tab.name === tabName) {
                                let nextTab = tabs[index + 1] || tabs[index - 1];
                                if (nextTab) {
                                    this.selectedTab = nextTab;
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
                this.selectedTab = tabName;
                equo.send(this.namespace + '_tabClicked', {
                    partName: tabName,
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
        const filteredE4Model = e4Model.filter(tab => {
            return tab.toBeRendered == 'true';
        });
        let webStackApp = Vue.extend(app);
        let component = new webStackApp({
            propsData: {
                namespace: namespace,
                e4Model: filteredE4Model
            }
        }).$mount()

        $('body').append(component.$el)
    }

    equo.getE4Model(createWebItemStack);

});