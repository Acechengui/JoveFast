<template>
    <el-select v-model="currentValue" :multiple="multiple" :filterable="filterable" remote reserve-keyword
        :placeholder="placeholder" :remote-method="remoteMethod" :loading="loading">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
    </el-select>
</template>
  
<script>
import { getToken } from "@/utils/auth";
export default {
    name: 'TsUniversalSelect',
    props: {
        value: { // 双向绑定至
            required: true
        },
        requestApi: { // 请求API
            type: String,
            default: ''
        },
        requestType: { // 请求类型
            type: String,
            default: 'post'
        },
        requestKey: { // 请求参数
            type: String,
            default: ''
        },
        multiple: { // 是否多选
            type: Boolean,
            default: false
        },
        filterable: { // 是否可搜索
            type: Boolean,
            default: true
        },
        placeholder: { // 占位符
            type: String,
            default: '请输入关键词'
        },
        label: { // 选项的标签
            type: String,
            default: ''
        },
        field: { // 选项的值
            type: String,
            default: ''
        }
    },
    data() {
        return {
            currentValue: '',
            options: [],
            list: [],
            loading: false,
            cache: []
        }
    },
    watch: {
        value: {
            handler(val) {
                if (val !== this.currentValue) {
                    if (this.multiple) {
                        if (Object.prototype.toString.call(val) === '[object String]') {
                            try {
                                this.currentValue = JSON.parse(val)
                            } catch (e) {
                                this.currentValue = val.split(',')
                            }
                        }
                        if (Object.prototype.toString.call(val) === '[object Array]') {
                            this.currentValue = val
                        }
                    } else {
                        this.currentValue = val
                    }
                }
            },
            immediate: true,
            deep: true
        },
        currentValue() {
            this.$emit('input', this.currentValue)
            this.$emit('change', this.currentValue)
        }
    },
    methods: {
        remoteMethod(query) {
            if (query !== '') {
                this.loading = true
                setTimeout(() => {
                    this.getDate(query)
                }, 1000)
            } else {
                this.options = this.cache
            }
        },
        getDate(query) {
            if (this.requestType === 'GET' || this.requestType === 'get') {
                this.$axios.get(`${this.requestApi}?${this.requestKey}=${query}`, {
                    headers: {
                        authorization: `Bearer ${getToken()}`,
                    },
                }).then(req => {
                    if (req.data.code === 200) {
                        this.options = req.data.data.map(item => ({ label: item[this.label], value: item[this.field] }))
                    } else {
                        this.options = []
                    }
                    this.loading = false
                })
            } else {
                this.$axios.post(this.requestApi, { [this.requestKey]: query }, {
                    headers: {
                        authorization: `Bearer ${getToken()}`,
                    },
                }).then(req => {
                    if (req.data.code === 200) {
                        this.options = req.data.data.map(item => ({ label: item[this.label], value: item[this.field] }))
                    } else {
                        this.options = []
                    }
                    this.loading = false
                })
            }
        }
    }
}
</script>