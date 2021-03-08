
<template>
    <v-tooltip data-app bottom>
            <template v-slot:activator="{ on }">
                <v-btn v-on:click="eventhandlerEval" icon text dark v-on="on">
                  <font-awesome-icon :icon="['fas',icon]" />
                </v-btn>
            </template>
            <span>{{tooltip}}</span>
    </v-tooltip>
</template>


<script>
import { VBtn, VTooltip } from 'vuetify/lib'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(
  fas
)

  export default {
    name: "equo-toolitem",
    components: { VBtn, VTooltip, FontAwesomeIcon },
        props:{
        eventhandler:{
            type: String,
            default: (()=>{console.warn("On click event Listener by default, please define an EventHandler for each Equo ToolItem");}).toString(),
        },
        tooltip:{
            type: String,
            default: "Tooltip"
        },
        icon:{
            type: String,
            default: "info-circle"
        }
    },
    methods: {
      eventhandlerEval(){
        var f = new Function("return " + this.eventhandler.toString())();
        f();
      } 
    }
  }
</script>
<style>
.v-tooltip{
  display: block;
}

@import url("./../styles/vuetify.css");
@import url("./../styles/styles.css");
</style>