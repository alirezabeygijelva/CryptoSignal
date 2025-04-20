import {boot} from "quasar/wrappers";
import VueCookies from 'vue-cookies'

export default boot(({app}) => {

  // Set cookies to vue app
  app.use(VueCookies)
})
