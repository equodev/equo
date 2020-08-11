<template>
    <div data-app class="toolbar-container">
        <v-toolbar height="35px" :color="color">
          <slot></slot>
          <v-spacer></v-spacer>
          <v-btn dark icon text>
          <font-awesome-icon :icon="['fas','times']" />
        </v-btn>
        </v-toolbar>
    </div>
</template>

<script>
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import {VToolbar, VBtn, VSpacer} from 'vuetify/lib'

library.add(
  fas
)

export default {
  name: 'equo-toolbar',
  components: { FontAwesomeIcon, VToolbar, VBtn, VSpacer},
  props: {
    color: {
      type: String,
      default: "#07F"
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
.v-toolbar__content, .v-toolbar__extension {
    padding: 0px 0px;
}
@import url("./../styles/vuetify.css");
@import url("./../styles/styles.css");

</style>
