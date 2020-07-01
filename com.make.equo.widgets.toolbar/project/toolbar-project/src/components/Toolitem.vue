
<template>
    <v-tooltip bottom>
            <template v-slot:activator="{ on }">
                <v-btn class="e-btn"  v-on:click="eventHandler" icon text dark v-on="on">
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
    name: "equo-toolbar",
    components: { VBtn, VTooltip, FontAwesomeIcon },
        props:{
        eventHandler:{
            type: Function,
            default: () => 1,
        },
        tooltip:{
            type: String,
            default: "Tooltip"
        },
        icon:{
            type: String,
            default: ""
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