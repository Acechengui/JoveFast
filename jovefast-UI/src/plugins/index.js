import tab from './tab'
import auth from './auth'
import cache from './cache'
import modal from './modal'
import download from './download'
import preventReClick from './preventReClick'

export default {
  install(Vue) {
    // 页签操作
    Vue.prototype.$tab = tab
    // 认证对象
    Vue.prototype.$auth = auth
    // 缓存对象
    Vue.prototype.$cache = cache
    // 模态框对象
    Vue.prototype.$modal = modal
    // 下载文件
    Vue.prototype.$download = download
    // 按钮防止重复提交
    Vue.prototype.$preventReClick = preventReClick
  }
}
