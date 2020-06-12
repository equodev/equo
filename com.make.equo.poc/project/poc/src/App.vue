<template>
<v-app id="app">
  <equo-toolbar/>
  <equo-treeview :path="path" :nodes="nodes" title="Explorer"/>
</v-app>
</template>

<script>
import EquoTreeview from './components/Tree.vue'
import EquoToolbar from './components/Toolbar.vue'
import vuetify from './plugins/vuetify'

export default {
  name: 'App',
  vuetify,
  components: {
    EquoTreeview,
    EquoToolbar
  },
    data() { return {
      //these data must be extracted to a Json received from Java side
              nodes : [
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder1'},
                    {
                    title: 'Folder2', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'Item4', isLeaf: true},
                        {title: 'Folder7', isExpanded: true, children: [
                          {title: 'Item3', isLeaf: true},
                          {title: 'Item4', isLeaf: true}
                          ]
                        }
                    ]},
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder3'},
                    {
                    title: 'Folder4', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'Item4', isLeaf: true}
                        ]
                    },
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder5'},
                    {
                    title: 'Folder6', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'Item4', isLeaf: true}
                        ]
                    },
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder1'},
                    {
                    title: 'Folder2', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'Item4', isLeaf: true},
                        {title: 'Folder7', isExpanded: true, children: [
                          {title: 'Item3', isLeaf: true},
                          {title: 'Item4', isLeaf: true}
                          ]
                        }
                    ]},
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder3'},
                    {
                    title: 'Folder4', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'Item4', isLeaf: true}
                        ]
                    },
                    {title: 'Item1', isLeaf: true},
                    {title: 'Item2', isLeaf: true, data: { path: "VUE" }},
                    {title: 'Folder5'},
                    {
                    title: 'FolderFinal', isExpanded: true, children: [
                        {title: 'Item3', isLeaf: true},
                        {title: 'ItemFinal', isLeaf: true}
                        ]
                    }
                    ],
                    path: "/home/PoC"
              }
    }
}
</script>

<style>
    #app{
      height: 100%;
    }
</style>
