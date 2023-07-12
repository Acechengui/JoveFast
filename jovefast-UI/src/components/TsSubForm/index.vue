<template>
  <div>
    <el-table
      v-if="ts_table_execute_down()"
      ref="singleTable"
      :data="ts_local_data"
      :border="true"
      style="width: 100%"
      @cell-mouse-enter="ts_mouse_enter_rows"
      @cell-mouse-leave="ts_mouse_leave_rows"
    >
      <el-table-column
        type="index"
        label="#"
        align="center"
        width="50px"
      >
        <template slot-scope="scope">
          <i v-if="canEdit && deleteButton && scope.row.ts_delete_mark && scope.row.ts_delete_mark != null"
             class="el-icon-remove" style="color: red;cursor:pointer" @click="ts_delete_rows(scope)"
          />
          <span v-else>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        v-for="(col,index) in tableData"
        :key="index"
        :label="col.config.label"
        :prop="col.config.prop"
        :width="col.width"
      />
    </el-table>
    <div style="margin-top: 10px">
      <el-button v-if="canEdit && addButton && tableData.length > 0"
                 size="small" icon="el-icon-plus" style="float: left;" @click="ts_add_rows"
      >
        添加
      </el-button>
      <render style="display: none" />
    </div>
  </div>
</template>

<script>
import render from './ts-render'

export default {
  name: 'TsSubForm',
  components: {
    render
  },
  props: {
    tableData: {
      type: Array,
      default() {
        return []
      }
    },
    value: {
      type: Array,
      default() {
        return []
      }
    },
    addButton: {
      type: Boolean,
      default() {
        return true
      }
    },
    deleteButton: {
      type: Boolean,
      default() {
        return true
      }
    },
    canEdit: {
      type: Boolean,
      default() {
        return true
      }
    }
  },
  data() {
    return {
      ts_sustain_type: ['el-input', 'el-radio-group', 'el-checkbox-group',
        'el-input-number', 'el-select', 'el-cascader', 'el-switch',
        'el-slider', 'el-time-select', 'el-date-picker', 'el-upload', 'el-rate', 'el-color-picker','ts-universal-select'],
      ts_get_data_type: ['el-radio-group', 'el-select', 'el-checkbox-group', 'el-cascader'],
      ts_local_data: [],
      ts_current_row: null,
      ts_current_rows: [],
      syncData: [],
      ts_whether_add: false
    }
  },
  watch: {
    value: {
      handler(val) {
        this.syncData = val
        this.ts_render_rows()
      },
      deep: true
    },
    syncData: {
      handler(val) {
        this.$emit('input', val)
      },
      deep: true
    }
  },
  created() {
    this.syncData = this.value
    this.ts_render_rows()
  },
  methods: {
    ts_render_rows() {
      if (this.ts_execute_down() && !this.ts_whether_add) {
        const _that = this
        const _columns = this.deepClone(this.tableData)
        this.ts_current_rows.push(_columns)
        this.syncData.forEach(_data => {
          const _rows = {
            ts_delete_mark: false
          }
          _that.ts_cycle_rows(_columns, _data, _rows, false)
          _that.ts_local_data.push(_rows)
        })
      }
    },
    ts_add_rows() {
      this.ts_whether_add = true
      const _rows = {
        ts_delete_mark: false
      }
      const _columns = this.deepClone(this.tableData)
      this.ts_current_rows.push(_columns)
      const _data = {}
      this.ts_cycle_rows(_columns, _data, _rows, true)
      this.syncData.push(_data)
      this.ts_local_data.push(_rows)
    },
    ts_cycle_rows(_columns, _data, _rows, _add) {
      this.ts_whether_add = true
      const _that = this
      _columns.forEach(item => {
        if (this.contains(_that.ts_sustain_type, item.config.tag)) {
          _that.ts_get_parent_data(item)
          _add && (_data[item.config.prop] = '')
          !_that.canEdit && (item.disabled = true)
          item.config.tag === 'el-checkbox-group' && (_data[item.config.prop] = [])
          item._ts_way_bind_ = item.config.prop
          _rows[item.config.prop] = <render
            syncData={_data} syncDataKey={item.config.prop} current-data={item}>
          </render>
        } else {
          _rows[item.config.prop] = <span>{ item.config.label }</span>
        }
      })
    },
    ts_mouse_enter_rows(_row) {
      _row.ts_delete_mark = true
    },
    ts_mouse_leave_rows(_row) {
      _row.ts_delete_mark = false
    },
    ts_delete_rows(_row) {
      this.ts_whether_add = true
      // eslint-disable-next-line camelcase
      const ts_finally_data = this.deepClone(this.syncData)
      this.ts_local_data.splice(_row.$index, 1)
      ts_finally_data.splice(_row.$index, 1)
      this.syncData.splice(_row.$index, 1)
      this.$emit('update:value', ts_finally_data)
    },
    ts_get_parent_data(_item) {
      this.ts_whether_add = true
      if (this.isNotNull(_item.__slot__)
        && this.contains(this.ts_get_data_type, _item.config.tag)
        && _item.config.tag !== 'el-cascader') {
        _item.__slot__.options = this.$parent[_item.config.data] != null
          ? this.$parent[_item.config.data] : _item.__slot__.options
      } else if (this.isNotNull(_item.options) && _item.config.tag === 'el-cascader') {
        _item.options = this.$parent[_item.config.data] != null ? this.$parent[_item.config.data] : _item.options
      }
    },
    ts_execute_down() {
      return this.syncData != null
        && this.syncData.length > 0
        && this.tableData != null
        && this.tableData.length > 0
    },
    ts_table_execute_down() {
      return this.tableData != null && this.tableData.length > 0
    },
    deepClone(source) {
      if (!source && typeof source !== 'object') {
        throw new Error('error arguments', 'deepClone')
      }
      const targetObj = source.constructor === Array ? [] : {}
      Object.keys(source).forEach(keys => {
        if (source[keys] && typeof source[keys] === 'object') {
          targetObj[keys] = this.deepClone(source[keys])
        } else {
          targetObj[keys] = source[keys]
        }
      })
      return targetObj
    },
    contains(array, value) {
      for (let i = 0; i < array.length; i++) {
        if (array[i] === value) {
          return true
        }
      }
      return false
    },
    isNotNull(value) {
      return !this.isNull(value)
    },
    isNull(value) {
      return Object.is(value, '') || Object.is(value, null) || Object.is(value, undefined)
    }
  }
}
</script>
