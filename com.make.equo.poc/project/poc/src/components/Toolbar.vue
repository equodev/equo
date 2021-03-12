<template>
    <div class="toolbar-container">
        <v-toolbar height="35px" color="primary">
          <slot></slot>
          <v-spacer></v-spacer>
          <v-btn v-if="shouldShowClose" dark icon text @click="this.closeEditorFunction" v->
            <font-awesome-icon :icon="['fas','times']" />
          </v-btn>
        </v-toolbar>
    </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(
  fas
)

export default {
  name: 'equo-toolbar',
  components: { FontAwesomeIcon },
  props:{
    toolitems:{
      type: Array,
      default(){ return []} 
      },
    closeEditorFunction: {
      type: Function
    },
    shouldShowClose:{
      type: Boolean,
      default() { return false }
    }
  },
  data: () => ({
     
  }),
    methods: {
    }

  }
</script>
<style>
.toolbar-container{
  height: 35px;
}

</style>
