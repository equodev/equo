<template>
  <v-app id="app">
    <equo-toolbar :close-editor-function="closeEditor" :shouldShowClose='this.thereIsAnEditor()'>
      <!-- equo-toolitem icon use FontAwesome to its definition. take a look at www.fontawesome.com/icons to choose a icon -->
      <equo-toolitem tooltip="Open Folder" icon='folder-open' :eventHandler="this.openFolder"/>
      <equo-toolitem tooltip="Save" icon='save' :eventHandler="this.save"/>
      <equo-toolitem tooltip="Search" icon='search' :eventHandler="this.find"/>
      <equo-toolitem tooltip="Cut" icon='cut' :eventHandler="this.editorCut"/>
      <equo-toolitem tooltip="Copy" icon='copy' :eventHandler="this.editorCopy"/>
      <equo-toolitem tooltip="Paste" icon='paste' :eventHandler="this.editorPaste"/>
    </equo-toolbar>

    <div class="contentDiv">
      <div  class="treeDiv"><equo-treeview ref="tree" title="Explorer" v-bind:extensionicons="extensionIcons" :menuoptions="contextMenuOptions" :path="path" v-bind:nodes="nodes" @openEditor="openEditor" @pasteFile="pasteFile" @removeFile="removeFile" @placeResponseInModel="placeResponseInModel" /></div>
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
import { Menu } from '@equo/equo-application-menu'

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
                        tree.selectedNode = node;
                      }},
                      {title: "Copy", shortcut: "Ctrl + C",eventHandler:function(node, tree){
                        tree.cutSelection = false;
                        tree.selectedNode = node;
                      }},
                      {title: "Paste", shortcut: "Ctrl + V",eventHandler:function(node, tree){
                        tree.$emit('pasteFile', node, tree);
                      }},
                      {title: "Remove", shortcut: "Supr",eventHandler:function(node, tree){
                        tree.$emit('removeFile', node, tree);
                      }}
              ],
              editor: undefined,
              menuWithoutEditor: undefined,
              menuWithEditor: undefined
              }
    },
    /* eslint-disable */
    methods:{
      transformResponseToTreeData(response, expandedNode){
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
          response.data = {path: response.path};
          if (response.isDirectory){
            response.isExpanded = false;
            response.data.wasExpandedBefore = false;
          }
          response.isLeaf = !response.isDirectory;
          response.title = response.name;
          return response;
        }
      },
      placeResponseInModel(response, originalTreeNodes, treeComponent, expandedNode){
          this.transformResponseToTreeData(response, expandedNode);
          if (typeof treeComponent !== 'undefined'){
            response.children.forEach(function(item){
              treeComponent.$refs.sltree.insert({
                node: expandedNode,
                placement: 'inside'
              }, item);
            });
          } else {
            originalTreeNodes.splice(0, originalTreeNodes.length);
            Array.prototype.push.apply(originalTreeNodes, response.children);
          }
      },
      refreshTree(response){
        if (!response.err){
          this.placeResponseInModel(response, this.nodes);
          this.path = response.path;
        }
      },
      openFolder(){
        equo.openFolder(this.refreshTree);
      },
      thereIsAnEditor(){
        return typeof this.editor !== 'undefined';
      },
      doOpenEditor(path){
        this.editor = EquoMonaco.create(document.getElementById('editor'), path);
        this.menuWithEditor.setApplicationMenu();

        try {
          this.editor.activateShortcuts();
        } catch(err) {
          console.log(err);
        }
      },
      openEditor(path){
        if (this.thereIsAnEditor()){
          let app = this;
          this.closeExistingEditor(function(){
            app.editor.dispose();
            app.editor = undefined;
            app.doOpenEditor(path);
          });
        }else{
          this.doOpenEditor(path);
        }
      },
      closeExistingEditor(closingFunction){
        if (this.thereIsAnEditor()){
          if (this.editor.isDirty()){
            let app = this;
            equo.on("_savedialogresponse", function(response){
              if (response.close){
                if (response.save){
                  app.editor.setActionDirtyState(function(){
                    if (!app.editor.isDirty()){
                      closingFunction();
                    }
                  });
                  app.editor.save();
                }else{
                  closingFunction();
                }
              }
            });
            equo.send("savedialoghandler");
          }else{
            closingFunction();
          }
        }
      },
      closeEditor(){
        let app = this;
        this.closeExistingEditor(function(){
          app.editor.dispose();
          app.editor = undefined;
          app.menuWithoutEditor.setApplicationMenu();
        });
      },
      removeFile(node, tree){
        var app = this;
        let path = node.data.path;
        equo.on("_confirmremoveresponse", function(response){
          if (response.proceed){
            equo.deleteFile(path, function(secondResponse){
              if (!secondResponse.err){
                tree.$refs.sltree.remove([node.path]);
                if (app.thereIsAnEditor() && app.editor.getFilePath() === node.data.path){
                  app.closeEditor();
                }
              }
            });
          }
        });
        equo.send("confirmremovehandler");
      },
      pasteFile(node, tree){
        var app = this;
        if (!node.isLeaf){
          if (typeof tree.selectedNode !== 'undefined'){
            if (tree.cutSelection){
              equo.moveFile(tree.selectedNode.data.path, node.data.path, function(response){
                if (!response.err){
                  tree.$refs.sltree.remove([tree.selectedNode.path]);
                  if (app.thereIsAnEditor() && app.editor.getFilePath() === tree.selectedNode.data.path){
                    app.closeEditor();
                  }
                  tree.selectedNode = undefined;
                  if (node.data.wasExpandedBefore === true){
                    equo.fileInfo(response.content, function(secondResponse){
                      tree.$refs.sltree.insert({
                        node: node,
                        placement: 'inside'
                      }, app.transformResponseToTreeData(secondResponse));
                    });
                  }
                }
              });
            } else {
              equo.copyFile(tree.selectedNode.data.path, node.data.path, function(response){
                tree.selectedNode = undefined;
                if (!response.err && node.data.wasExpandedBefore === true){
                  equo.fileInfo(response.content, function(secondResponse){
                    tree.$refs.sltree.insert({
                      node: node,
                      placement: 'inside'
                    }, app.transformResponseToTreeData(secondResponse));
                  });
                }
              });
            }
          }
        }
      },
      find(){
        if (this.thereIsAnEditor()){
          this.editor.getEditor().getAction("actions.find").run();
        }
      },
      editorCut(){
        if (this.thereIsAnEditor()){
          this.editor.getEditor().getAction("editor.action.clipboardCutAction").run();
        }
      },
      editorCopy(){
        if (this.thereIsAnEditor()){
          this.editor.getEditor().getAction("editor.action.clipboardCopyAction").run();
        }
      },
      editorPaste(){
        if (this.thereIsAnEditor()){
          this.editor.getEditor().focus();
          document.execCommand("paste");
        }
      },
      save(){
        if (this.thereIsAnEditor())
          this.editor.save();
      }
    },
    created(){
      this.menuWithoutEditor = Menu.create()
        .withMainMenu("File")
        .addMenuItem("Open folder").addShortcut("M1+O").onClick(this.openFolder)
        .addMenuItem("Exit").addShortcut("M1+Q").onClick('exitapphandler');

      this.menuWithEditor = Menu.create()
        .withMainMenu("File")
          .addMenuItem("Open folder").addShortcut("M1+O").onClick(this.openFolder)
          .addMenuItem("Save").addShortcut("M1+S").onClick(this.save)
          .addMenuItem("Exit").addShortcut("M1+Q").onClick('exitapphandler')
        .withMainMenu("Edit")
          .addMenuItem("Cut").addShortcut("M1+X").onClick(this.editorCut)
          .addMenuItem("Copy").addShortcut("M1+C").onClick(this.editorCopy)
          .addMenuItem("Paste").addShortcut("M1+V").onClick(this.editorPaste);

      this.menuWithoutEditor.setApplicationMenu();
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
