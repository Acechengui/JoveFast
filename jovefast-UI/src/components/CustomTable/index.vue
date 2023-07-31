<template>
  <div>
    <div style="display: flex; justify-content: flex-start;padding: 0 10px 4px 0">
      <el-button size="medium" icon="el-icon-search" @click="fetchData">载入数据</el-button>
    </div>
    <el-table
      ref="singleTable"
      :data="data"
      :border="true"
      style="width: 100%"
    >
      <el-table-column
        v-for="(item, index) in children"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :width="item.width"
      />
    </el-table>
  </div>
</template>

<script>

export default {
  name: 'CustomTable',
  props: {
    url: {
      type: String,
      default() {
        return ''
      }
    },
    config: {
      type: Object,
      default() {
        return {}
      }
    },
    border: {
      type: Boolean
    }
  },
  data() {
    return {
      data: [],
      path: this.url,
      conf: this.config,
      children: this.config.children
    }
  },
  watch: {
    value: {
      handler(val) {
        this.syncData = val
        this.ts_render_rows()
      },
      deep: true
    }
  },
  created() {
    this.$nextTick(() => {
      this.fetchData()
    })
  },
  methods: {
    splicingParams(method, params) {
      const requestData = {}
      if (params?.length) {
        const data = this.getFrom(this.$parent).model
        params.forEach(re => {
          if (data && data[re]) {
            data[re] && (requestData[re] = data[re])
          }
        })
      }
      if (method === 'GET' || method === 'get') {
        let result = '?'
        // eslint-disable-next-line guard-for-in,no-restricted-syntax/*  */
        for (const key in requestData) {
          result += `${key}=${encodeURIComponent(requestData[key])}&`
        }
        return result.endsWith('&') ? result.substring(0, result.length - 1) : result
      }
      return requestData
    },
    fetchData() {
      const {
        tag, url, method, dataPath, params
      } = this.conf
      const body = this.splicingParams(method, params)
      if(body ==='?') return;
      if (tag === 'custom-table' && method && url) {
        if (method === 'GET' || method === 'get') {
          this.$axios({
            method,
            url: `${url}${body}`
          }).then(res => {
            if (res && res.data && res.data[dataPath]) {
              this.data = res.data[dataPath]
            }
          })
        } else {
          this.$axios({
            method,
            url,
            data: body
          }).then(res => {
            if (res && res.data && res.data[dataPath]) {
              this.data = res.data[dataPath]
            }
          })
        }
      }
    },
    getFrom(node) {
      if (node.$parent.$el.className.includes('el-form ')) {
        return node.$parent
      }
      return this.getFrom(node.$parent)
    }
  }
}
</script>
