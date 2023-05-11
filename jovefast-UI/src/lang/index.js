import Vue from 'vue'
import VueI18n from 'vue-i18n'
import Cookies from 'js-cookie'
import elementEnLocale from 'element-ui/lib/locale/lang/en' // element-ui lang
import elementThLocale from 'element-ui/lib/locale/lang/th' // element-ui lang
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN'// element-ui lang
import enLocale from './en'
import zhLocale from './zh'
import thLocale from './th'

Vue.use(VueI18n)

const messages = {
  en_US: {
    ...enLocale,
    ...elementEnLocale
  },
  zh_CN: {
    ...zhLocale,
    ...elementZhLocale
  },
  //泰语
  th_TH: {
    ...thLocale,
    ...elementThLocale
  }
}

const i18n = new VueI18n({
  // 设置语言 选项 en_US | zh_CN | th_TH
  locale: Cookies.get('language') || 'zh_CN' || 'en_US'  || 'th_TH',
  // 设置文本内容
  messages
})

export default i18n