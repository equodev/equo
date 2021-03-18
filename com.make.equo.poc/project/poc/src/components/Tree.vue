<template>
    <div class="tree-container" oncontextmenu= "return false;">
        <div class=tree-title><p>{{title}}</p></div>
        <div class="tree-root">{{path}}</div>
        <sl-vue-tree
            ref= "sltree"
            class="tree-model"
            :value="nodes"
            :allowMultiselect="false"
            @nodeclick="nodeClick"
            @nodedblclick="node2Click"
            @nodecontextmenu="nodeContextMenu"
            @toggle="toggle"
            @externaldrop.prevent="onExternalDropHandler">
        <template slot="title" slot-scope="{ node }">
          <span class="item-icon">
            <font-awesome-icon icon="folder" v-if="!node.isLeaf"></font-awesome-icon>
            <template v-for="extIcon in extensionicons"  
              >
              <font-awesome-icon :style="{color : extIcon.color}"  :key="extIcon.extension" v-if="getExtension(node.title)===extIcon.extension" :icon="extIcon.icon">
              </font-awesome-icon> 
            </template>
            
            <font-awesome-icon icon="file" v-if="node.isLeaf && getExtension(node.title)===undefined" ></font-awesome-icon>
            {{ node.title }}
          </span>
          
        </template>
        <template slot="toggle" slot-scope="{ node }">
          <span class="item-icon" v-if="!node.isLeaf">
            <font-awesome-icon v-if="node.isExpanded" icon="chevron-down"></font-awesome-icon>
            <font-awesome-icon v-if="!node.isExpanded" icon="chevron-right"></font-awesome-icon>
          </span>
        </template>
      </sl-vue-tree>
      <aside class="menu contextmenu" 
              ref="contextmenu" 
              v-show="contextMenuIsVisible">
        <div v-for="option in menuoptions"  :key="option.title"
              @click="selectOption(option.eventHandler)">
          {{option.title}} <span>{{option.shortcut}}</span> 
         </div>
      </aside> 
    </div>
</template>

<script>
import SlVueTree from './slTree/sl-vue-tree.js'
import './slTree/sl-vue-tree-dark.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons';
import { fab } from '@fortawesome/free-brands-svg-icons';

library.add(
    fab,
    fas,
    far
)


export default {
    name: 'equo-treeview',
    props:{
      title: {
        type : String,
        default: "TREE-DEFAULT-TITLE"
      },
      nodes: {
        type: Array,
        default(){ return [] } 
      },
      path: {
        type: String,
        default: "/"
      },
      menuoptions:{
        type: Array,
        default(){return []}
      },
      extensionicons:{
        type: Array,
        default(){return []}
      }
    },
    components: { SlVueTree, FontAwesomeIcon},
    created() {
        if (typeof window !== 'undefined') {
            document.addEventListener('click', this.clickedOutside)
        }
    },
    data(){ return {
                    contextMenuIsVisible: false,
                    nodeInspected : null,
                    color: "green",
                    selectedNode: undefined,
                    cutSelection: false
            }
    },
   methods: {
     clickedOutside(event){
       if (!this.isInWhiteList(event.target)) this.contextMenuIsVisible = false;
     },
     isInWhiteList() { return false; },
     nodeClick(node, event){
       console.log("Click",node.path, event);
     },
     node2Click(node){
       if (node.isLeaf){
        this.$emit('openEditor', node.data.path);
       }
     },
     getNode(nodes,path){
        if (path.length === 1) return nodes[path[0]];
        return this.getNode(nodes[path[0]].children, path.slice(1));
     },
     
     toggle(expandedNode){
        var tree = this;
        var originalTree = this.nodes;
        expandedNode.isExpanded=!expandedNode.isExpanded; 
        if(expandedNode.data.wasExpandedBefore === false){
          /*eslint-disable*/
          equo.fileInfo(expandedNode.data.path,function(response){
            tree.$refs.sltree.getNode(expandedNode.path).data.wasExpandedBefore = true;
            tree.$emit('placeResponseInModel', response, originalTree, tree, expandedNode);
          });
          /*eslint-enable*/
        }
     },

     nodeContextMenu(node, event){
       if(this.menuoptions.length!== 0){
        this.contextMenuIsVisible = true;
        const $contextMenu = this.$refs.contextmenu;
        $contextMenu.style.left = event.clientX + 'px';
        $contextMenu.style.top = event.clientY + 'px';
        this.nodeInspected = node;
       }
       else{
         console.error("ContextMenu have to be at least one element defined in the Tree Component");
       }
     },
     selectOption(event){
       const node = this.nodeInspected;
       event(node, this);
       this.nodeInspected = null;
     },
     getExtension(file){
       let re = /(?:\.([^.]+))?$/;
       let ext = re.exec(file)[1];
       return(ext);
     }
  },
}
</script>

<style>

    @font-face {
      font-family: 'Circular-Std';
      src: url('../fonts/circular/CircularStd-Medium.ttf');
    }

  .tree-container{
      width: 100%;
      height: 100%;
  }

  .tree-title{
    font-family: 'Circular-Std';
    height: 35px;
    padding-left: 3%;
    padding-top: 3px;
    padding-bottom: 3px;
    background-color: rgb(9, 22, 29);
    color: white;
    font-weight: bolder;
    overflow: hidden;
  }

  .tree-root{
    min-height: 35px;
    background-color: rgb(27, 37, 43);
    color: whitesmoke;
    padding-top: 3px;
    padding-bottom: 3px;
    padding-left: 3%;
    font-weight: bold;
    border-bottom: 2px solid black ;
    font-family: Circular-Std;
    overflow: hidden;
  }

  .tree-model{
    padding-left: 2%;
    font-family: Circular-Std;
  }

.item-icon{
  white-space: nowrap;
}

  .contextmenu {
    padding-top: 5px;
    padding-bottom: 5px;
    width: 15%;
    position: absolute;
    background-color: rgba(63, 63, 63, 0.575);
    color: white;
    border-radius: 2px;
    cursor: pointer;
  }

  .contextmenu > div {
      padding: 2px;
      padding-left: 5%;
  }

  .contextmenu > div:hover {
      background-color: rgba(153, 153, 153, 0.3);
      color: black;
      border-radius: 2px;
  }

  .contextmenu > div >  span {
        float: right;
        padding-right: 5%;
  }

  .sl-vue-tree-selected > .sl-vue-tree-node-item > .sl-vue-tree-title > .item-icon > svg{
    background-color: #13242d;
    color: rgb(255, 255, 255,0.5)
}

</style>
