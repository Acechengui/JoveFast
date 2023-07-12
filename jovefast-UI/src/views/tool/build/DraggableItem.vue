<script>
import draggable from 'vuedraggable'
import render from '@/components/render/render'

const components = {
  itemBtns(h, currentItem, index, list) {
    const { copyItem, deleteItem } = this.$listeners
    return [
      <span class="drawing-item-copy" title="复制" onClick={event => {
        copyItem(currentItem, list); event.stopPropagation()
      }}>
        <i class="el-icon-copy-document" />
      </span>,
      <span class="drawing-item-delete" title="删除" onClick={event => {
        deleteItem(index, list); event.stopPropagation()
      }}>
        <i class="el-icon-delete" />
      </span>
    ]
  }
}
const layouts = {
  colFormItem(h, currentItem, index, list) {
    const { activeItem } = this.$listeners
    const config = currentItem.__config__
    const child = renderChildren.apply(this, arguments)
    let className = this.activeId === config.formId ? 'drawing-item active-from-item' : 'drawing-item'
    if (this.formConf.unFocusedComponentBorder) className += ' unfocus-bordered'
    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    if (config.showLabel === false) labelWidth = '0'
    return (
      <el-col span={config.span} class={className}
              nativeOnClick={event => { activeItem(currentItem); event.stopPropagation() }}>
        <el-form-item label-width={labelWidth}
                      label={config.showLabel ? config.label : ''} required={config.required}>
          <render key={config.renderKey} conf={currentItem} onInput={ event => {
            this.$set(config, 'defaultValue', event)
          }}>
            {child}
          </render>
        </el-form-item>
        {components.itemBtns.apply(this, arguments)}
      </el-col>
    )
  },
  rowFormItem(h, currentItem, index, list) {
    const { activeItem } = this.$listeners
    const config = currentItem.__config__
    const className = this.activeId === config.formId
      ? 'drawing-row-item active-from-item'
      : 'drawing-row-item'
    let child = renderChildren.apply(this, arguments)
    if (currentItem.type === 'flex') {
      child = <el-row type={currentItem.type} justify={currentItem.justify} align={currentItem.align}>
        {child}
      </el-row>
    }
    return (
      <el-col span={config.span}>
        <el-row gutter={config.gutter} class={className}
                nativeOnClick={event => { activeItem(currentItem); event.stopPropagation() }}>
          <span class="component-name">{config.componentName}</span>
          <draggable list={config.children || []} animation={340}
                     group="componentsGroup" class="drag-wrapper">
            {child}
          </draggable>
          {components.itemBtns.apply(this, arguments)}
        </el-row>
      </el-col>
    )
  },
  raw(h, currentItem, index, list) {
    const config = currentItem.__config__
    const child = renderChildren.apply(this, arguments)
    return <render key={config.renderKey} conf={currentItem} onInput={ event => {
      this.$set(config, 'defaultValue', event)
    }}>
      {child}
    </render>
  },
  tsSubform(h, currentItem, index, list) {
    const { activeItem } = this.$listeners
    const config = currentItem.__config__
    let className = this.activeId === config.formId ? 'drawing-item active-from-item' : 'drawing-item'
    if (this.formConf.unFocusedComponentBorder) className += ' unfocus-bordered'
    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    if (config.showLabel === false) labelWidth = '0'
    const tableData = []
    const tableDataColumns = []
    const tableDataProp = {}
    const child = tsSubformChildren.apply(this, arguments)
    tableDataColumns.push(<el-table-column label="将元素拖拽到下方" minWidth="100%" prop="columns1"></el-table-column>)
    // eslint-disable-next-line max-len
    tableDataProp.columns1 = <draggable list={config.children || []} animation={340} group="componentsGroup" class="drag-wrapper" style="display: flex;flex-direction: row">
      {child}
      <div style="clear:both;height:20px"></div>
    </draggable>
    tableData.push(tableDataProp)
    return (
      <el-col class={className}
              nativeOnClick={event => { activeItem(currentItem); event.stopPropagation() }}>
        <el-form-item label-width={labelWidth} label={config.showLabel ? config.label : ''} required={config.required}>
          <el-table
            data={tableData}
            border={true}
            style="width: 100%">
            <el-table-column
              type="index"
              label="#"
              align="center"
              width="30px"
            />
            {tableDataColumns}
          </el-table>
        </el-form-item>
        {components.itemBtns.apply(this, arguments)}
      </el-col>
    )
  }
}

function tsSubformChildren(h, currentItem, index, list, formId) {
  const config = currentItem.__config__
  if (!Array.isArray(config.children)) return null
  return config.children.map((el, i) => {
    const layout = layouts[el.__config__.layout]
    el.__config__.showLabel = false
    if (layout) {
      const style = `width:${el.__config__.tag === 'el-input-number'
        ? '240px' : el.width || '200px'};border:1px solid rgb(199 199 199)`
      return <div style={style}>
        <div style="width:100%;padding-left:20px">{el.__config__.label}</div>
        {layout.call(this, h, el, i, config.children, formId)}
      </div>
    }
    return <div>
      <div style="width:100%;padding-left:20px">标题1</div>
      {layoutIsNotFound.call(this)}
    </div>
  })
}


function renderChildren(h, currentItem, index, list) {
  const config = currentItem.__config__
  if (!Array.isArray(config.children)) return null
  return config.children.map((el, i) => {
    const layout = layouts[el.__config__.layout]
    if (layout) {
      return layout.call(this, h, el, i, config.children)
    }
    return layoutIsNotFound.call(this)
  })
}

function layoutIsNotFound() {
  throw new Error(`没有与${this.currentItem.__config__.layout}匹配的layout`)
}

export default {
  components: {
    render,
    draggable
  },
  props: [
    'currentItem',
    'index',
    'drawingList',
    'activeId',
    'formConf'
  ],
  render(h) {
    const layout = layouts[this.currentItem.__config__.layout]

    if (layout) {
      return layout.call(this, h, this.currentItem, this.index, this.drawingList)
    }
    return layoutIsNotFound.call(this)
  }
}
</script>
