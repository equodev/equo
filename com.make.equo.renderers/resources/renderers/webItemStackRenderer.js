$(document).ready(function () {

    let tabs = {
        template: `
      <div class="app2">
      <span v-for="item in e4Model">
        <el-tabs  type="card" @tab-click="callE4Command(item.id)>
            <el-tab-pane
                v-for="(item, index) in parts"
                key="item.id"
                label="item.label"
                name="item.label">
                {{item.label}}
             </el-tab-pane>
        </el-tabs>
        </span>
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
        	 callE4Command(commandId) {
        		 console.log(this.activeName);
        		 equo.send(this.namespace + '_itemClicked', {
        			 command: commandId
                 });
             }

        },
        style: `
        `
    };
    Vue.component("tabs-comp", tabs);
  
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