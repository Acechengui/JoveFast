/**
 * 按钮防止重复提交指令  
 * 1.main.js中使用
 * import { preventReClick } from './assets/js/preventReClick'
 * 2.按钮上添加 v-prevent-re-click
 * <el-button size="small" type="primary" @click="submit" v-prevent-re-click>确 定</el-button>
 */
 import Vue from 'vue'

 // 防止重复提交指令
 const preventReClick = Vue.directive('preventReClick', {
     inserted (el, binding) {
         el.addEventListener('click', () => {
             if (!el.disabled) {
                 el.disabled = true
                 setTimeout(() => {
                     el.disabled = false
                 }, binding.value || 3000)
             }
         })
     }
 })
 
 export { preventReClick }
 