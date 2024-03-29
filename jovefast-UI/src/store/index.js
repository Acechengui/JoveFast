import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import dict from './modules/dict'
import user from './modules/user'
import tagsView from './modules/tagsView'
import permission from './modules/permission'
import settings from './modules/settings'
import getters from './getters'
import { $dataRoomStore } from '@gcpaas/data-room-ui'
import { $dashboardStore } from '@gcpaas/dash-board-ui'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    dict,
    user,
    tagsView,
    permission,
    settings,
    // 注册大屏设计器的store
    bigScreen: $dataRoomStore,
    // 注册仪表盘设计器的store
    dashboard: $dashboardStore
  },
  getters
})

export default store



// 重置state数据
export function clearState (state, initData) {
  Object.keys(initData).forEach(key => {
    state[key] = initData[key]
  })
}
// 清除store
export function clearStore (module) {
  if (!store) return
  const modules = ['CONTRACT']
  if (module) {
    store.commit('RESET_' + module)
  } else {
    modules.forEach(item => {
      store.commit('RESET_' + item)
    })
  }
}
