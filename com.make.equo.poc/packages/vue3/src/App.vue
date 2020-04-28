<template>
  <v-app>
    <v-app-bar app color="primary">
      <v-tooltip v-for="item in toolitems"  :key="item.icon" :to="item.tooltip"  bottom>
                <template v-slot:activator="{ on }">
                  <v-btn text dark v-on="on">
                    <font-awesome-icon :icon=item.icon size="2x" />
                  </v-btn>
                </template>
                <span>{{item.tooltip}}</span>
      </v-tooltip>
      <v-spacer></v-spacer>
        <v-img src="https://www.equoplatform.com/assets/img/white_logo_navbar.png"  aspect-ratio=15 contain> </v-img>
      <v-btn dark text>
      <font-awesome-icon :icon="['fas','times']" size="2x" />
    </v-btn>
    </v-app-bar>

  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
      toolitems: [
        { icon: ['fas', 'search'], tooltip: 'Search' },
        { icon: ['fas', 'copy'], tooltip: 'Copy' },
        { icon: ['fas', 'save'], tooltip: 'Save' },
        { icon: ['fas', 'play'], tooltip: 'Run' },
        { icon: ['fas', 'bug'], tooltip: 'Debug' }
      ]
  }),
};
</script>
