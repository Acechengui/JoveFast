<!-- 运行态页面 -->
<template>
    <div class="app-container">
      <div class="inner-container">
        <BigScreenRun
          :config="config"
        />
      </div>
    </div>
</template>
<script>
import { BigScreenRun } from '@gcpaas/data-room-ui'
export default {
components: {
    BigScreenRun
},
data () {
    return {
    config: {
        // 大屏运行编码
        code: '',
        // fitSelector 为您需要自适应的外部盒子的选择器
        fitSelector: '.inner-container',
        // 自适应模式 无(none) 、自动(auto)、宽度铺满(fitWidth)、高度铺满(fitHeight)和 双向铺满（cover） 5 种自适应模式
        fitMode: 'auto'
    }
    }
}
}
</script>
