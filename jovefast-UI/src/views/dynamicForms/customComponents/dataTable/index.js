// 对外输出 包含组件的对外json定义、属性配置页面、展示页面 三部分
 
import BaseIndex from './index.vue'
import BaseProperties from './properties.vue'
 
const icon = 'data:image/png;base64,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'


const obj = {}
 
obj.type = 'dataTable' // 表单类型 
obj.label = '数据表格'
obj.component = BaseIndex
obj.properties = BaseProperties
obj.icon = icon
obj.labelWidth = 0

// 序号 实际在json中删除
obj.seq = 2

// 补充配置样式
obj.options = {
    border: false, // 边框
    stripe: false , //斑马线
    showIndex: false, // 显示下标
    dbType: 1 , // 1-静态 2-api
    datasourceStatic: [] ,// 静态数据
    methodType: 'get', // API模式下请求方法类型 get post
    apiPath: '' , // API模式下请求地址
    queryData: [] , // API请求下要携带的数据
    apiDataScript: '' , // 请求到的数据转列表数组方法 自定义脚本
    columns: [] ,// 字段配置 格式: {field: '' , label: '' , width: '10%' , align: 'center'}
    page: false , // 是否分页 在分页模式下需要配置每页数据量 和当前页字段key和每页数据量字段key
    currentPageKey: 'pageIndex', // 当前页key
    pageSizeKey: 'pageSize' ,// 也没数据量KEY
    pageSize: 10 // 每页数据量
}
 

export default obj


