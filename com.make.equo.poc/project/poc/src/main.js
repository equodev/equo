import Vue from 'vue'
import App from './App.vue'
import vClickOutside from "v-click-outside"
import Buefy from 'buefy'

Vue.config.productionTip = false
Vue.use(vClickOutside)
Vue.use(Buefy)

new Vue({
  render: h => h(App),
}).$mount('#app')
