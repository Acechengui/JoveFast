import Vue from 'vue'

import Cookies from 'js-cookie'
import Print from 'vue-print-nb'//打印功能

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/jove.scss' // jove css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import { download } from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime,resetForm,addDateRange,addMultipleDateRange,addMultipleDateRangeThree,selectDictLabel,selectDictLabels,handleTree } from "@/utils/jove";

import elDragDialog from '@/utils/el-drag-dialog' // 引入移动事件
Vue.directive('el-drag-dialog', elDragDialog);//添加标签事件绑定 可以放大移动弹窗
//弹窗默认点击遮罩改为不关闭 为了防止和拖拽冲突 ，这句需要放在use ElementUI之前（也可以不加这句，测试区别）
Element.Dialog.props.closeOnClickModal.default = false;

// 右击菜单组件
import Contextmenu from "vue-contextmenujs"
// 分页组件
import Pagination from "@/components/Pagination"
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
import WangEditor from "@/components/WangEditor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 字典数据组件
import DictData from '@/components/DictData'

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.addMultipleDateRange = addMultipleDateRange
Vue.prototype.addMultipleDateRangeThree = addMultipleDateRangeThree
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('WangEditor', WangEditor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)

Vue.use(Print)
Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(Contextmenu)
DictData.install()

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false
Vue.config.silent = true //关闭浏览器vue warn

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
