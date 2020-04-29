import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify';
import { library } from '@fortawesome/fontawesome-svg-core'
import { faChild, faSearch, faCircle, faArchive, faPlay, faBug, faCopy,faSave,faTimes} from '@fortawesome/free-solid-svg-icons'
import { faComment} from '@fortawesome/free-regular-svg-icons'
import { FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

library.add(
  faChild,
  faCircle,
  faArchive,
  faSearch,
  faComment,
  faBug,
  faPlay,
  faCopy,
  faSave,
  faTimes
)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.config.productionTip = false

new Vue({
  vuetify,
  render: h => h(App)
}).$mount('#app')
