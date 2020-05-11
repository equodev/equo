<template>
    <div class="tree-container" oncontextmenu= "return false;">
        <div class=tree-title><p>{{title}}</p></div>
        <div class="tree-root">{{path}}</div>
        <sl-vue-tree
            class="tree-model"
            :value="nodes"
            :allowMultiselect="false"
            @nodeclick="nodeClick"
            @nodedblclick="node2Click"
            @nodecontextmenu="nodeContextMenu"
            @externaldrop.prevent="onExternalDropHandler">
        <template slot="title" slot-scope="{ node }">
          <span class="item-icon">
            <font-awesome-icon icon="folder" v-if="!node.isLeaf"></font-awesome-icon>
            <font-awesome-icon :icon="[ 'fab', 'vuejs' ]" v-else-if="node.data.path==='VUE'"></font-awesome-icon> 
            <font-awesome-icon icon="file" v-else-if="node.isLeaf" ></font-awesome-icon>
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
        <div @click="open">Open </div>
        <div @click="cut">Cut <span>Ctrl + X</span></div>
        <div @click="copy">Copy <span>Ctrl + C </span></div>
        <div @click="remove">Delete <span> Del </span></div>
        <div @click="rename">Rename</div>
      </aside> 
    </div>
</template>

<script>
import vuetify from 'vuetify'
import SlVueTree from './slTree/sl-vue-tree.js'
import './slTree/sl-vue-tree-dark.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import { faChevronDown, faChevronRight, faFile, faFolder } from '@fortawesome/free-solid-svg-icons'
import { faJs, faVuejs,faCss3 } from '@fortawesome/free-brands-svg-icons';

library.add(
    faChevronRight,
    faChevronDown,
    faFile,
    faFolder,
    faJs,
    faVuejs,
    faCss3
)

export default {
    vuetify,
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
            }
    },
   methods: {
     clickedOutside(event){
       if (!this.isInWhiteList(event.target)) this.contextMenuIsVisible = false;
     },
     isInWhiteList() { return false; },
     nodeClick(){
       console.log(this.maxWidth)
     },
     node2Click(){
       console.log("2click")
     },
     nodeContextMenu(node, event){
        this.contextMenuIsVisible = true;
        const $contextMenu = this.$refs.contextmenu;
        $contextMenu.style.left = event.clientX + 'px';
        $contextMenu.style.top = event.clientY + 'px';
        //console.log(node);
        this.nodeInspected = node;
     },
     open(){
        console.log(this.nodeInspected);
        this.node = null;
     },
     copy(){
        console.log(this.nodeInspected);
        this.node = null;     
     },
     cut(){
        console.log(this.nodeInspected);
        this.node = null;  
     },
     remove(){
        console.log(this.nodeInspected);
        this.node = null;  
     },
     rename(){
        console.log(this.nodeInspected);
        this.node = null;  
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
      width: 25%;
      height: 100%;
      overflow-y: auto;
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
  }

  .tree-root{
    height: 35px;
    background-color: rgb(27, 37, 43);
    color: whitesmoke;
    padding-top: 3px;
    padding-bottom: 3px;
    padding-left: 3%;
    font-weight: bold;
    border-bottom: 2px solid black ;
    font-family: Circular-Std;
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

</style>
