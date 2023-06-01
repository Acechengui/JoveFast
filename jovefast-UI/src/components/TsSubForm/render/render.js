const tsComponentChild = {}
const tsSlotsFiles = require.context('./slots', false, /\.js$/)
const tsKeys = tsSlotsFiles.keys() || []
tsKeys.forEach(key => {
  const tsTag = key.replace(/^\.\/(.*)\.\w+$/, '$1')
  const tsValue = tsSlotsFiles(key).default
  tsComponentChild[tsTag] = tsValue
})

function _tsWayBind_(tsDataObject, tsValue) {
  tsDataObject.props.value = tsValue
  tsDataObject.on.input = val => {
    this.$emit('input', val)
    this.$emit('update:syncData', this.syncData)
  }
}

function tsMountSlotFiles(h, tsRawData, tsChildren) {
  const childObjs = tsComponentChild[tsRawData.config.tag]
  if (childObjs) {
    Object.keys(childObjs).forEach(key => {
      const childFunc = childObjs[key]
      if (tsRawData.__slot__ && tsRawData.__slot__[key]) {
        tsChildren.push(childFunc(h, tsRawData, key))
      }
    })
  }
}

function deepClone(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

function tsEmitEvents(tsRawData) {
  ['on', 'nativeOn'].forEach(attr => {
    const eventKeyList = Object.keys(tsRawData[attr] || {})
    eventKeyList.forEach(key => {
      const val = tsRawData[attr][key]
      if (typeof val === 'string') {
        tsRawData[attr][key] = event => this.$emit(val, event)
      }
    })
  })
}
function tsBuildDataObject(tsRawData, tsDataObject) {
  Object.keys(tsRawData).forEach(key => {
    const val = tsRawData[key]
    if (key === '_ts_way_bind_') {
      _tsWayBind_.call(this, tsDataObject, this.syncData[this.syncDataKey])
    } else if (tsDataObject[key] !== undefined) {
      if (tsDataObject[key] === null
        || tsDataObject[key] instanceof RegExp
        || ['boolean', 'string', 'number', 'function'].includes(typeof tsDataObject[key])) {
        tsDataObject[key] = val
      } else if (Array.isArray(tsDataObject[key])) {
        tsDataObject[key] = [...tsDataObject[key], ...val]
      } else {
        tsDataObject[key] = { ...tsDataObject[key], ...val }
      }
    } else {
      tsDataObject.attrs[key] = val
    }
  })

  // 清理属性
  tsClearAttrs(tsDataObject)
}

function tsClearAttrs(tsDataObject) {
  delete tsDataObject.attrs.config
  delete tsDataObject.attrs.__slot__
}

function tsPrototypeData() {
  return {
    class: {},
    attrs: {},
    props: {},
    domProps: {},
    nativeOn: {},
    on: {},
    style: {},
    directives: [],
    scopedSlots: {},
    slot: null,
    key: null,
    ref: null,
    refInFor: true
  }
}

export default {
  props: {
    data: {
      type: Object,
      required: true
    },
    syncData: {
      type: Object,
      required: true
    },
    syncDataKey: {
      type: String
    }
  },
  render(h) {
    const tsDataObject = tsPrototypeData()
    const tsRawData = deepClone(this.data)
    const tsChildren = this.$slots.default || []
    tsMountSlotFiles.call(this, h, tsRawData, tsChildren)
    tsEmitEvents.call(this, tsRawData)
    tsBuildDataObject.call(this, tsRawData, tsDataObject)
    return h(this.data.config.tag, tsDataObject, tsChildren)
  }
}