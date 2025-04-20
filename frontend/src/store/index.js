import { store } from 'quasar/wrappers'
import { createStore } from 'vuex'

import user from './user'
import {establishSockJs} from "../utils/WebsocketUtils"

/*
 * If not building with SSR mode, you can
 * directly export the Store instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Store instance.
 */

export default store(function (/* { ssrContext } */) {
  const Store = createStore({

    state: {
      binanceClient: undefined,
      realtimeAssetsMap: new Map()
    },

    getters: {
      isBinanceClientConnected(state) {
        return state.binanceClient !== undefined && state.binanceClient.connected
      },
      getRealtimeAssetsMap(state) {
        return state.realtimeAssetsMap
      }
    },

    actions: {
      connectBinanceStream({commit, getters}, args) {
        if (!getters.isBinanceClientConnected)
          commit('setBinanceClient', establishSockJs(
            args.url,
            args.destination,
            (frame) => console.log("Binance Connection Frame: ", frame),
            (message) => {
              const assetJson = JSON.parse(message.body)
              commit('putAssetInMap', assetJson)
            },
            (error) => console.log("Binance Connection Error: ", error))
          )
      }
    },

    mutations: {
      setBinanceClient(state, binanceClient) {
        state.binanceClient = binanceClient
      },
      putAssetInMap(state, asset) {
        state.realtimeAssetsMap.set(asset.s, asset)
      }
    },

    modules: {
      user,
    },

    // enable strict mode (adds overhead!)
    // for dev mode and --debug builds only
    strict: process.env.DEBUGGING
  })

  return Store
})
