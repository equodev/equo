<template>
  <v-app>
    <v-app-bar height="35" app color="primary">
      <v-tooltip  v-for="item in toolitems"  :key="item.icon" :to="item.tooltip"  bottom>
                <template v-slot:activator="{ on }">
                  <v-btn v-on:click="clickEvent(item.eventHandler)" icon text dark v-on="on">
                    <font-awesome-icon :icon=item.icon />
                  </v-btn>
                </template>
                <span>{{item.tooltip}}</span>
      </v-tooltip>
      <v-spacer></v-spacer>
      <v-btn dark text>
      <font-awesome-icon :icon="['fas','times']" />
    </v-btn>
    </v-app-bar>

  </v-app>
</template>

<script>
export default {
  name: 'App',

  data: () => ({
      toolitems: [
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
