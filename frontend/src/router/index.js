import {route} from 'quasar/wrappers'
import {createRouter, createMemoryHistory, createWebHistory, createWebHashHistory} from 'vue-router'
import routes from './routes'
import {useStore} from "vuex";

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default route(function (/* { store, ssrContext } */) {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory)

  const Router = createRouter({
    scrollBehavior: () => ({left: 0, top: 0}),
    routes,

    // Leave this as is and make changes in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    history: createHistory(process.env.VUE_ROUTER_BASE)
  })

  // Global Navigation Guards
  Router.beforeEach(async function (to, from, next) {

    const accessToken = $cookies.get("access_token")
    if (to.meta.requiresAuth && !accessToken) {
      next({path: '/login'});
    } else if (to.meta.requiresAuth && accessToken) {
      const store = useStore();
      if (!store.getters.isUserAccountAlreadyLoad) {
        await store.dispatch("getAccount", accessToken);
      }

      if (to.meta.roles !== undefined) {
        const isAuthorized = await store.dispatch('isUserAuthorized', to.meta.roles)
        if (!isAuthorized) {
          next({path: '/404'})
        }
      }
      next();
    } else {
      next();
    }
  });

  return Router
})
