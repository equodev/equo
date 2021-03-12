<template>
  <v-app id="app">
    <equo-toolbar>
      <!-- equo-toolitem icon use FontAwesome to its definition. take a look at www.fontawesome.com/icons to choose a icon -->
      <equo-toolitem tooltip="Open Folder" icon='folder-open' :eventHandler="this.openFolder"/>
      <equo-toolitem tooltip="Save" icon='save' :eventHandler="this.save"/>
      <equo-toolitem tooltip="Search" icon='search' :eventHandler="this.find"/>
      <equo-toolitem tooltip="Cut" icon='cut' :eventHandler="this.editorCut"/>
      <equo-toolitem tooltip="Copy" icon='copy' :eventHandler="this.editorCopy"/>
      <equo-toolitem tooltip="Paste" icon='paste' :eventHandler="this.editorPaste"/>
    </equo-toolbar>

    <div class="contentDiv">
      <div  class="treeDiv"><equo-treeview ref="tree" title="Explorer" v-bind:extensionicons="extensionIcons" :menuoptions="contextMenuOptions" :path="path" v-bind:nodes="nodes" @openEditor="openEditor" @pasteFile="pasteFile" @removeFile="removeFile" @transformResponseToTreeData="transformResponseToTreeData" /></div>
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
              nodesData : [],
              //nodes es la informacion actual que se tiene sobre las folders y subfolders del directorio ( se actualiza con los expands)
              nodes: [],
              path: "",
                    //items de la toolbar

              contextMenuOptions:[
                      {title: "Open",eventHandler:function(node, tree){
                        tree.$emit('openEditor', node.data.path);
                      }},
                      {title: "Cut", shortcut: "Ctrl + X",eventHandler:function(node, tree){
                        tree.cutSelection = true;
                        tree.selectedNode = node.data.path;
                      }},
                      {title: "Copy", shortcut: "Ctrl + C",eventHandler:function(node, tree){
                        tree.cutSelection = false;
                        tree.selectedNode = node.data.path;
                      }},
                      {title: "Paste", shortcut: "Ctrl + V",eventHandler:function(node, tree){
                        tree.$emit('pasteFile', node, tree);
                      }},
                      {title: "Remove", shortcut: "Supr",eventHandler:function(node, tree){
                        tree.$emit('removeFile', node.data.path);
                      }}
              ],
              editor: undefined
              }
    },
    /* eslint-disable */
    methods:{
      transformResponseToTreeData(response, originalTree, originalTreeData, expandedNode){
        if (!response.err){
          for(let i =0;i < response.children.length;i++){
            response.children[i].data = {path: response.children[i].path};
            if(response.children[i].isDirectory){
              response.children[i].isExpanded = false;
              response.children[i].data.wasExpandedBefore = false;
              response.children[i].children = [];
            }
            response.children[i].isLeaf = !response.children[i].isDirectory;
            response.children[i].title = response.children[i].name;

            if (typeof expandedNode !== 'undefined'){
              expandedNode.children.splice(expandedNode.children.length - 1,0,response.children[i])
            }
          }
          response.isLeaf = !response.isDirectory;
          response.title = response.name;
          originalTree.splice(0, originalTree.length);
          if (typeof originalTreeData !== 'undefined'){
            Array.prototype.push.apply(originalTree, originalTreeData);
          } else {
            Array.prototype.push.apply(originalTree, response.children);
          }
        }
      },
      refreshTree(response){
        if (!response.err){
          this.transformResponseToTreeData(response, this.nodes);
          this.path = response.path;
        }
      },
      openFolder(){
        equo.openFolder(this.refreshTree);
      },
      openEditor(path){
        if (typeof this.editor !== 'undefined')
          this.editor.dispose();
        this.editor = EquoMonaco.create(document.getElementById('editor'), path);
        try {
          this.editor.activateShortcuts();
        } catch(err) {
          console.log(err);
        }
      },
      removeFile(path){
        var app = this;
        equo.deleteFile(path, function(response){
          equo.fileInfo(app.path, app.refreshTree);
        });
      },
      pasteFile(node, tree){
        var app = this;
        if (!node.isLeaf){
          if (tree.cutSelection){
            equo.moveFile(tree.selectedNode, node.data.path, function(response){
              equo.fileInfo(app.path, app.refreshTree);
            });
          } else {
            equo.copyFile(tree.selectedNode, node.data.path, function(response){
              equo.fileInfo(app.path, app.refreshTree);
            });
          }
        }
      },
      find(){
        if (typeof this.editor !== 'undefined'){
          this.editor.getEditor().getAction("actions.find").run();
        }
      },
      editorCut(){
        if (typeof this.editor !== 'undefined'){
          this.editor.getEditor().getAction("editor.action.clipboardCutAction").run();
        }
      },
      editorCopy(){
        if (typeof this.editor !== 'undefined'){
          this.editor.getEditor().getAction("editor.action.clipboardCopyAction").run();
        }
      },
      editorPaste(){
        if (typeof this.editor !== 'undefined'){
          this.editor.getEditor().focus();
          document.execCommand("paste");
        }
      },
      save(){
        if (typeof this.editor !== 'undefined')
          this.editor.save();
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
