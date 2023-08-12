import { isArray } from 'util'
import { exportDefault, titleCase, deepClone } from '@/utils/index'
import ruleTrigger from './ruleTrigger'

const units = {
  KB: '1024',
  MB: '1024 / 1024',
  GB: '1024 / 1024 / 1024'
}
let confGlobal
const inheritAttrs = {
  file: '',
  dialog: 'inheritAttrs: false,'
}

/**
 * 组装js 【入口函数】
 * @param {Object} formConfig 整个表单配置
 * @param {String} type 生成类型，文件或弹窗等
 */
export function makeUpJs(formConfig, type) {
  confGlobal = formConfig = deepClone(formConfig)
  const dataList = []
  const ruleList = []
  const optionsList = []
  const propsList = []
  const methodList = mixinMethod(type)
  const uploadVarList = []
  const created = []
  const other = []

  formConfig.fields.forEach(el => {
    buildAttributes(el, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, created, other)
  })

  const script = buildexport(
    formConfig,
    type,
    dataList.join('\n'),
    ruleList.join('\n'),
    optionsList.join('\n'),
    uploadVarList.join('\n'),
    propsList.join('\n'),
    methodList.join('\n'),
    created.join('\n'),
    other.join('\n')
  )
  confGlobal = null
  return script
}

// 构建组件属性
function buildAttributes(scheme, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, created, other) {
  const config = scheme.__config__
  const slot = scheme.__slot__
  buildData(scheme, dataList, other)
  buildRules(scheme, ruleList)

  // 特殊处理options属性
  if (scheme.options || (slot && slot.options && slot.options.length)) {
    buildOptions(scheme, optionsList)
    if (config.dataType === 'dynamic') {
      const model = `${scheme.__vModel__}Options`
      const options = titleCase(model)
      const methodName = `get${options}`
      buildOptionMethod(methodName, model, methodList, scheme)
      callInCreated(methodName, created)
    }
  }

  // 处理props
  if (scheme.props && scheme.props.props) {
    buildProps(scheme, propsList)
  }

  // 处理el-upload的action
  if (scheme.action && config.tag === 'el-upload') {
    uploadVarList.push(
      `${scheme.__vModel__}Action: '${scheme.action}',
      `
    )
    methodList.push(buildBeforeUpload(scheme))
    // 非自动上传时，生成手动上传的函数
    if (!scheme['auto-upload']) {
      methodList.push(buildSubmitUpload(scheme))
    }
  }

  // 构建子级组件属性
  if (config.children) {
    config.children.forEach(item => {
      buildAttributes(item, dataList, ruleList, optionsList, methodList, propsList, uploadVarList, created)
    })
  }
}

// 在Created调用函数
function callInCreated(methodName, created) {
  created.push(`this.${methodName}()`)
}

// 混入处理函数
function mixinMethod(type) {
  const list = []; const
    minxins = {
      file: confGlobal.formBtns ? {
        submitForm: `submitForm() {
        this.$refs['${confGlobal.formRef}'].validate(valid => {
          if(!valid) return
          // TODO 提交表单
        })
      },`,
        resetForm: `resetForm() {
        this.$refs['${confGlobal.formRef}'].resetFields()
      },`
      } : null,
      dialog: {
        onOpen: 'onOpen() {},',
        onClose: `onClose() {
        this.$refs['${confGlobal.formRef}'].resetFields()
      },`,
        close: `close() {
        this.$emit('update:visible', false)
      },`,
        handelConfirm: `handelConfirm() {
        this.$refs['${confGlobal.formRef}'].validate(valid => {
          if(!valid) return
          this.close()
        })
      },`
      }
    }

  const methods = minxins[type]
  if (methods) {
    Object.keys(methods).forEach(key => {
      list.push(methods[key])
    })
  }

  return list
}

// 构建data
function buildData(scheme, dataList, other) {
  const config = scheme.__config__
  if (config.tag === 'el-table') {
    dataList.push(`${scheme.__vModel__}: ${JSON.stringify(scheme.data)},`)
    return
  }
  if (config.tag === 'ts-sub-form') {
    const subformData = []
    if (scheme.__config__.children.length > 0) {
      scheme.__config__.children.forEach(item => {
        const basicsData = JSON.parse(JSON.stringify(item))
        basicsData.config = {}
        basicsData.config.label = item.__config__.label
        basicsData.config.formId = item.__config__.formId
        basicsData.config.align = item.__config__.align
        basicsData.config.optionType = item.__config__.optionType
        basicsData.config.width = item.__config__.width
        basicsData.config.tag = item.__config__.tag
        basicsData.config.prop = item.__vModel__
        basicsData.placeholder = item.placeholder
        basicsData.disabled = item.disabled
        basicsData.readonly = item.readonly
        delete basicsData.__config__
        delete basicsData.__vModel__
        subformData.push(basicsData)
      })
    }
    other.push(`subForm${config.formId}: ${JSON.stringify(subformData)},`)
    other.push(`subForm${config.formId}Data: ${JSON.stringify(config.defaultValue)},`)
    other.push(`addButton${config.formId}: ${JSON.stringify(scheme.addButton)},`)
    other.push(`canEdit${config.formId}: ${JSON.stringify(scheme.canEdit)},`)
    other.push(`deleteButton${config.formId}: ${JSON.stringify(scheme.deleteButton)},`)
    return
  }
  if (scheme.__vModel__ === undefined) return

  const defaultValue = JSON.stringify(config.defaultValue)
  dataList.push(`${scheme.__vModel__}: ${defaultValue},`)
}

// 构建校验规则
function buildRules(scheme, ruleList) {
  const config = scheme.__config__
  if (scheme.__vModel__ === undefined) return
  const rules = []
  if (ruleTrigger[config.tag]) {
    if (config.required) {
      const type = isArray(config.defaultValue) ? 'type: \'array\',' : ''
      let message = isArray(config.defaultValue) ? `请至少选择一个${config.label}` : scheme.placeholder
      if (message === undefined) message = `${config.label}不能为空`
      rules.push(`{ required: true, ${type} message: '${message}', trigger: '${ruleTrigger[config.tag]}' }`)
    }
    if (config.regList && isArray(config.regList)) {
      config.regList.forEach(item => {
        if (item.pattern) {
          rules.push(
            `{ pattern: ${eval(item.pattern)}, message: '${item.message}', trigger: '${ruleTrigger[config.tag]}' }`
          )
        }
      })
    }
    ruleList.push(`${scheme.__vModel__}: [${rules.join(',')}],`)
  }
}

// 构建options
function buildOptions(scheme, optionsList) {
  if (scheme.__vModel__ === undefined) return
  // el-cascader直接有options属性，其他组件都是定义在slot中，所以有两处判断
  let { options } = scheme
  if (!options) options = scheme.__slot__.options
  if (scheme.__config__.dataType === 'dynamic') { options = [] }
  const str = `${scheme.__vModel__}Options: ${JSON.stringify(options)},`
  optionsList.push(str)
}

function buildProps(scheme, propsList) {
  const str = `${scheme.__vModel__}Props: ${JSON.stringify(scheme.props.props)},`
  propsList.push(str)
}

// el-upload的BeforeUpload
function buildBeforeUpload(scheme) {
  const config = scheme.__config__
  const unitNum = units[config.sizeUnit]; let rightSizeCode = ''; let acceptCode = ''; const
    returnList = []
  if (config.fileSize) {
    rightSizeCode = `let isRightSize = file.size / ${unitNum} < ${config.fileSize}
    if(!isRightSize){
      this.$message.error('文件大小超过 ${config.fileSize}${config.sizeUnit}')
    }`
    returnList.push('isRightSize')
  }
  if (scheme.accept) {
    acceptCode = `let isAccept = new RegExp('${scheme.accept}').test(file.type)
    if(!isAccept){
      this.$message.error('应该选择${scheme.accept}类型的文件')
    }`
    returnList.push('isAccept')
  }
  const str = `${scheme.__vModel__}BeforeUpload(file) {
    ${rightSizeCode}
    ${acceptCode}
    return ${returnList.join('&&')}
  },`
  return returnList.length ? str : ''
}

// el-upload的submit
function buildSubmitUpload(scheme) {
  const str = `submitUpload() {
    this.$refs['${scheme.__vModel__}'].submit()
  },`
  return str
}

function buildOptionMethod(methodName, model, methodList, scheme) {
  const config = scheme.__config__
  const str = `${methodName}() {
    // 注意：this.$axios是通过Vue.prototype.$axios = axios挂载产生的
    this.$axios({
      method: '${config.method}',
      url: '${config.url}'
    }).then(resp => {
      var { data } = resp
      this.${model} = data.${config.dataPath}
    })
  },`
  methodList.push(str)
}

// js整体拼接
function buildexport(conf, type, data, rules, selectOptions, uploadVar, props, methods, created, other) {
  const str = `${exportDefault}{
  ${inheritAttrs[type]}
  components: {},
  props: [],
  data () {
    return {
      ${conf.other}: {
        ${other}
      },
      ${conf.formModel}: {
        ${data}
      },
      ${conf.formRules}: {
        ${rules}
      },
      ${uploadVar}
      ${selectOptions}
      ${props}
    }
  },
  computed: {},
  watch: {},
  created () {
    ${created}
  },
  mounted () {},
  methods: {
    ${methods}
  }
}`
  return str
}
