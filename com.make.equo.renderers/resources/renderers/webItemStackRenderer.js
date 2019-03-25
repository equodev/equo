$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
        <el-tabs v-model="selectedTab" type="card" closable @tab-remove="removeTab" @tab-click="handleClick">
            <el-tab-pane v-for="item in e4Model" :id="item.id" :key="item.id" :name="item.id" :label="item.label"></el-tab-pane>
        </el-tabs>
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
        computed: {
            selectedTab: {
                get() {
                    return this.getSelectedTab().id;
                },
                set(elem) {
                    console.log("CALLING SETTER" + elem);
                    this.e4Model.forEach((tab) => {
                        if (tab.id === elem) {
                            tab.isSelected = 'true';
                        }
                    });                }
            }
        },
        mounted() {
            equo.on(this.namespace + "_addTab", tab => {
                let bool = true;
                currentElem = this.getSelectedTab();
                this.e4Model.forEach(modelElem => {
                    if (modelElem.id === tab.id) {
                        bool = false;
                    }
                });
                if (bool) {
                    this.e4Model.push(tab);
                    let previousTab = this.getSelectedTab();
                    previousTab.isSelected = 'false';
                    this.callE4Command(tab.id);
                } else if (tab.id !== currentElem.id) {
                    currentElem.isSelected = 'false';
                    this.callE4Command(tab.id);
                }
            });
        },
        methods: {
            getSelectedTab(){
                this.e4Model.forEach(tab => {
                    if (tab.isSelected === 'true') {
                        return tab;
                    }
                });
                this.e4Model[0].isSelected = 'true';
                return this.e4Model[0];
            },
            handleClick(tab, event) {
                this.callE4Command(tab.$attrs.id);
            },
            removeTab(toDelete) {
                let tabs = this.e4Model;
                let activeTab = this.getSelectedTab();
                if (activeTab !== undefined && activeTab.name === toDelete) {
                    activeTab.isSelected = 'false';
                    tabs.forEach((tab, index) => {
                        if (tab.name === toDelete) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                this.callE4Command(nextTab.id);
                                nextTab.isSelected = 'true';
                            }
                        }
                    });
                }

                this.e4Model = tabs.filter(tab => tab.id !== toDelete);

                equo.send(this.namespace + '_tabClicked', {
                    partId: toDelete,
                    namespace: this.namespace,
                    close: true
                });
            },
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
