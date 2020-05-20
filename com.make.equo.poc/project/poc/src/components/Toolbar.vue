<template>
    <div class="toolbar-container">
        <v-toolbar height="35px" color="primary">
          <v-tooltip  v-for="item in toolitems"  :key="item.tooltip" bottom>
                    <template v-slot:activator="{ on }">
                      <v-btn v-on:click="clickEvent(item.eventHandler)" icon text dark v-on="on">
                        <font-awesome-icon :icon=item.icon />
                      </v-btn>
                    </template>
                    <span>{{item.tooltip}}</span>
          </v-tooltip>
          <v-spacer></v-spacer>
          <v-btn dark icon text>
          <font-awesome-icon :icon="['fas','times']" />
        </v-btn>
        </v-toolbar>
    </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core'
import { faChild, faSearch, faCircle, faArchive, faPlay, faBug, faCopy,faSave,faTimes, faFolderOpen} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(
  faChild,
  faCircle,
  faArchive,
  faSearch,
  faBug,
  faPlay,
  faCopy,
  faSave,
  faTimes,
  faFolderOpen
)

export default {
  name: 'equo-toolbar',
  components: { FontAwesomeIcon },
  data: () => ({
      toolitems: [
        { icon: ['fas', 'folder-open'], tooltip: 'Open folder',eventHandler: "OpenFolderHandler"}, 
        { icon: ['fas', 'search'], tooltip: 'Search',eventHandler: "SearchHandler"}, 
        { icon: ['fas', 'copy'], tooltip: 'Copy', eventHandler: "CopyHandler"},
        { icon: ['fas', 'save'], tooltip: 'Save',eventHandler: "SaveHandler"},
        { icon: ['fas', 'play'], tooltip: 'Run',eventHandler: "RunHandler"},
        { icon: ['fas', 'bug'], tooltip: 'Debug',eventHandler: "DebugHandler"}
      ]
  }),
    methods: {
              clickEvent: function (button) {
                console.log(button);
                //equo.send(button);
                }
              }

  }
</script>
<style>
.toolbar-container{
  height: 35px;
}

</style>
