import Vue from 'vue'

import Cookies from 'js-cookie'
import Print from 'vue-print-nb'//打印功能
import i18n from './lang' //国际化

import Element from 'element-ui'
import './assets/styles/element-variables.scss'
import { getToken } from '@/utils/auth'
import axios from 'axios'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/jove.scss' // jove css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import { download } from '@/utils/request'
import * as $dataroomhttpaxios from '@/utils/dataroom-http.js'

import './assets/icons' // icon
import './permission' // permission control
import { getDicts } from "@/api/system/dict/data";
import { getConfigKey } from "@/api/system/config";
import { parseTime,formatDateC,resetForm,addDateRange,addMultipleDateRange,addMultipleDateRangeThree,selectDictLabel,selectDictLabels,handleTree } from "@/utils/jove";

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
//自定义组件-el-table横向滚动条固定在窗口底部
import horizontalScroll from 'el-table-horizontal-scroll'
//大屏设计组件
import { registerConfig as registerConfigDataRoom, $dataRoomAxios } from '@gcpaas/data-room-ui'
//仪表盘设计组件
import { registerConfig as registerConfigDashboard,$dashboardAxios} from '@gcpaas/dash-board-ui'

//引入ng-form-element表单设计器
import NgForm  from 'ng-form-element'
import 'ng-form-element/lib/ng-form-element.css'

//引入自定义组件
import NgComponents from '@/views/dynamicForms/customComponents/index.js' 

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.formatDateC = formatDateC
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.addMultipleDateRange = addMultipleDateRange
Vue.prototype.addMultipleDateRangeThree = addMultipleDateRangeThree
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.prototype.$axios = axios

// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('WangEditor', WangEditor)
Vue.component('FileUpload', FileUpload)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)
Vue.use(horizontalScroll)
Vue.use(NgForm, {components: NgComponents}) 
Vue.use(Print)
Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(Contextmenu)
Vue.use(Element, {
  i18n: (key, value) => i18n.t(key, value)
})
DictData.install()
// 使用大屏提供的方法，进行后端服务地址注册
registerConfigDataRoom({
  routers: {
    // 大屏列表页面
    pageListUrl: '/dataroom/largeScreen/bigScreenManagement',
    // 大屏设计页面
    designUrl: '/dataroom/largeScreen/bigScreenDesign',
    // 大屏预览页面
    previewUrl: '/dataroom/largeScreen/bigScreenRun',
    // 数据源管理页面
    dataSourceUrl: '/dataroom/largeScreen/dataSourceManagement',
    // 组件库页面
    componentUrl: '/dataroom/largeScreen/bigScreenComponents',
    // 数据集管理页面
    dataSetUrl: '/dataroom/largeScreen/dataSetManagement',
    // 资源库页面 
    //SourceUrl: '/dataroom/largeScreen/bigScreenSource',
    // 地图管理页面
    //mapData: '/dataroom/largeScreen/bigScreenMapManagement',
    // 指定回退的路由
    pageManagementUrl:'/dataroom/bigScreenManagement',
    // 业务组件路由
    bizComponentPreviewUrl: '/big-screen-biz-component-preview',
    bizComponentDesignUrl: '/big-screen-biz-component-design'
  },
  httpConfigs: {
    baseURL: process.env.VUE_APP_BASE_API + '/dataroom',
    // 这里是大屏文件的访问前缀，一般和后端配置的gc.starter.file.urlPrefix保持一致即可
    fileUrlPrefix: 'http://192.168.20.98:9000', 
    headers: {
      Authorization: 'Bearer ' + getToken()
    }
  },
  // 资源库允许上传的文件类型
  sourceExtends:['jpg','gif','bmp','svg','png','ico'],
  // 自定义title和logo等属性 
  starter: {
    title: '大屏设计器',
    logo: 'http://192.168.20.98:9000/dataroom/img/logo.png'
  }
// 此处第二个参数为自己项目中的路由实例对象
}, router)

// 将仪表盘的aixos实例挂载到Vue上
Vue.prototype.$dataRoomAxios = $dataroomhttpaxios

// 使用仪表盘提供的方法，进行后端服务地址注册
registerConfigDashboard({
  routers: {
    // 仪表盘列表页面
    pageListUrl: '/dataroom/dashboard/dashboardManagement',
    // 仪表盘设计页面
    designUrl: '/dataroom/dashboard/dashboardDesign',
    // 仪表盘预览页面
    previewUrl: '/dataroom/dashboard/dashboardRun',
    // 数据源管理页面
    dataSourceUrl: '/dataroom/dashboard/dataSourceManagement',
    // 组件库页面
    componentUrl: '/dataroom/dashboard/dashboardComponents',
    // 数据集管理页面
    dataSetUrl: '/dataroom/dashboard/dataSetManagement',
    // 资源库页面 
    //SourceUrl: '/dataroom/dashboard/bigScreenSource',
    // 地图管理页面
    //mapData: '/dataroom/dashboard/bigScreenMapManagement',
    //指定回退的路由
    pageManagementUrl:'/dataroom/dashboard/dashboardManagement' 
  },
  httpConfigs: {
    baseURL: process.env.VUE_APP_BASE_API + '/dataroom',
  },
  // 资源库允许上传的文件类型
  sourceExtends:['jpg','gif','bmp','svg','png','ico'],
  // 自定义title和logo等属性 
  starter: {
    title: '仪表盘设计器',
    logo: 'http://192.168.20.98:9000/dataroom/img/logo.png'
  }
// 此处第二个参数为自己项目中的路由实例对象
}, router)

// 将仪表盘的aixos实例挂载到Vue上
Vue.prototype.$dashboardAxios = $dataroomhttpaxios

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
  i18n,
  render: h => h(App)
})
