$(document).ready(function () {

    let app = {
        template: `
      <div class="app">
        <el-tabs type="card" closable @tab-remove="removeTab" @tab-click="handleClick">
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
        mounted() {
            equo.on(this.namespace + "_addTab", tab => {
                let bool = true;
                for (existingTab in this.e4Model) {
                    if (existingTab.id === tab.id) {
                        bool = false;
                    }
                }
                if (bool) {
                    this.e4Model.push(tab);
                    tab.isSelected = 'true';
                    let previousTab = this.getSelectedTab();
                    previousTab.isSelected = 'false';
                    this.callE4Command(tab.id);
                }
            });
        },
        methods: {
            getSelectedTab(){
                for (tab in this.e4Model) {
                    if (tab.isSelected === 'true') {
                        return tab;
                    }
                }
            },
            handleClick(tab, event) {
                this.callE4Command(tab.$attrs.id);
            },
            removeTab(toDelete) {
                let tabs = this.e4Model;
                let activeTab = this.getSelectedTab();
                if (activeTab !== undefined && activeTab.name === toDelete) {
                    activeName.isSelected = false;
                    tabs.forEach((tab, index) => {
                        if (tab.name === toDelete) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                this.callE4Command(nextTab.id);
                                nextTab.isSelected = true;
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
