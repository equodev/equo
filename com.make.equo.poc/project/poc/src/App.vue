<template>
  <v-app id="app">
    <equo-toolbar>
      <!-- equo-toolitem icon use FontAwesome to its definition. take a look at www.fontawesome.com/icons to choose a icon -->
      <equo-toolitem tooltip="Open Folder" icon='folder-open' :eventHandler="this.openFolder"/>
      <equo-toolitem tooltip="Search" icon='search' :eventHandler="this.find"/>
      <equo-toolitem tooltip="Copy" icon='copy' :eventHandler="this.openFolder"/>
      <equo-toolitem tooltip="Save" icon='save' :eventHandler="this.save"/>
      <equo-toolitem tooltip="Run" icon='play' :eventHandler="this.openFolder"/>
      <equo-toolitem tooltip="Debug" icon='bug' :eventHandler="this.openFolder"/>  
    </equo-toolbar>

    <div class="contentDiv">
      <div  class="treeDiv"><equo-treeview ref="tree" title="Explorer" v-bind:extensionicons="extensionIcons" :menuoptions="contextMenuOptions" :path="path" v-bind:nodes="nodes" /></div>
      <div  class="editorShellDiv">
        <div id="editor" class="editor"></div>
        <equo-shell class="shellDiv"/>
    </div>
    </div>
</v-app>
</template>

<script>
import EquoTreeview from './components/Tree.vue'
import EquoToolbar from './components/Toolbar.vue'
import EquoToolitem from './components/Toolitem.vue'
import EquoShell from './components/shell/Shell.vue'
import vuetify from './plugins/vuetify'
import { EquoMonaco } from '@equo/equo-monaco-editor'

export default {
  name: 'App',
  vuetify,
  components: {
    EquoTreeview,
    EquoToolbar,
    EquoToolitem,
    EquoShell
  },
  
    data() { return {
              /* nodesData representa la informacion que se va trayendo de Java a medida que se va pidiendo con el expand de las folders 
                 (es hardcode hasta que se pueda interactuar con java, despues ya no existe mas) */
              nodesData : [
                    {title: 'Item1.js', isLeaf: true, data: { path: "js" }},
                    {title: 'Item2.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder1', isExpanded:false},
                    {
                    title: 'Folder2', isExpanded: false, children: [
                        {title: 'Item1-2.json', isLeaf: true},
                        {title: 'Item2-2', isLeaf: true},
                        {title: 'Folder7', isExpanded: false, children: [
                          {title: 'Item1-7-2', isLeaf: true},
                          {title: 'Item2-7-2', isLeaf: true}
                          ]
                        }
                    ]},
                    {title: 'Item3', isLeaf: true},
                    {title: 'Item4.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder3',isExpanded : false},
                    {
                    title: 'Folder4', isExpanded: false, children: [
                        {title: 'Item1-4', isLeaf: true},
                        {title: 'Item2-4', isLeaf: true}
                        ]
                    },
                    {title: 'Item5', isLeaf: true},
                    {title: 'Item6.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder5', isExpanded: false},
                    {
                    title: 'Folder6', isExpanded: false, children: [
                        {title: 'Item1-6', isLeaf: true},
                        {title: 'Item2-6', isLeaf: true}
                        ]
                    }
                    ],
                    //nodes es la informacion actual que se tiene sobre las folders y subfolders del directorio ( se actualiza con los expands)
                    nodes: [
                    {title: 'Item1.js', isLeaf: true, data: { path: "js" }},
                    {title: 'Item2.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder1', isExpanded:false},
                    {
                    title: 'Folder2', isExpanded: false},
                    {title: 'Item3.html', isLeaf: true},
                    {title: 'Item4.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder3', isExpanded: false},
                    {
                    title: 'Folder4', isExpanded: false},
                    {title: 'Item5.gitignore', isLeaf: true},
                    {title: 'Item6.vue', isLeaf: true, data: { path: "vue" }},
                    {title: 'Folder5', isExpanded: false},
                    {
                    title: 'Folder6', isExpanded: false}
                    ],
              extensionIcons:[
                  {icon:['fab','vuejs' ],extension:"vue",color:'lightgreen'},
                  {icon:['fab','js' ],extension:"js"},
                  {icon:'code' ,extension:"html", color:'orange'},
                  {icon:['fas','cog' ],extension:"json"},
                  {icon:['fas','code-branch' ],extension:"gitignore"},
              ],
              path: "/home/PoC",
                    //items de la toolbar

              contextMenuOptions:[
                      {title: "Open",eventHandler:function(path, tree){
                        if (tree.editor)
                          tree.editor.dispose();
                        tree.editor = EquoMonaco.create(document.getElementById('editor'), path);
                      }},
                      {title: "Cut", shortcut: "Ctrl + X",eventHandler:function(){console.log("Cutting...")}},
                      {title: "Copy", shortcut: "Ctrl + C",eventHandler:function(){console.log("Copying...")}},
                      {title: "Remove", shortcut: "Supr",eventHandler:function(){console.log("Removing...")}},
                      {title: "Rename",eventHandler:function(){console.log("Renaming...")}}
              ],
              editor: undefined
              }
    },
    /* eslint-disable */
    methods:{
      openFolder(){
        var treeData = this;
        equo.openFolder(function(response){
          if (!response.err){
            for(let i = 0; i < response.children.length;i++){
              response.children[i].data = {path: response.children[i].path};
              if(response.children[i].isDirectory){
                response.children[i].isExpanded = false;
                response.children[i].data.wasExpandedBefore = false;
                response.children[i].children = [];
              }
              response.children[i].isLeaf = !response.children[i].isDirectory;
              response.children[i].title = response.children[i].name;
            }
            response.isLeaf = !response.isDirectory;
            response.title = response.name;
            treeData.nodes.splice(0,treeData.nodes.length);
            Array.prototype.push.apply(treeData.nodes,response.children);
            treeData.path = response.path;
          }
        });
      },
      find(){
        equo.find();
      },
      save(){
        equo.save(function(response){
          console.log(JSON.stringify(response));
        });
      }
    }
        /* eslint-enable */


}

</script>

<style>
    #app{
      height: 100%;
    }

    .editor{
      height: calc(100% - 270px - 35px);
      background-color: cornflowerblue;
      overflow: hidden;
    }

    .contentDiv{
      display: flex;
      overflow: hidden;
      flex-wrap: unset;
      flex: 1 1 auto;
    }

    .treeDiv{
      flex: 0 0 25%;
      max-width: 25%;
    }

    .editorShellDiv{
      flex: 0 0 75%;
      max-width: 75%;
    }

    .shellDiv{
      height: 300px;
    }

</style>
