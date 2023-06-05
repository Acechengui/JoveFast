<template>
  <div>
    <el-dialog
      v-bind="$attrs"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      v-on="$listeners"
      @open="onOpen"
      @close="onClose"
    >
      <div>
        <el-table
          :data="tableData"
          border
          style="width: 100%"
        >
          <template v-for="(item, index) in tableChildren">
            <el-table-column :key="index"
              v-if="item.type != 'selection'"
              :align="item.align"
              :label="item.label"
            >
              <template slot-scope="scope">
                <el-input v-model="scope.row[item.prop]" class="require_des" />
              </template>
            </el-table-column>
          </template>
          <el-table-column
            fixed="right"
            align="center"
            label="操作"
            width="100"
          >
            <template slot-scope="scope">
              <i style="color: red;" class="el-icon-delete" @click="deleteRow(scope)" />
            </template>
          </el-table-column>
        </el-table>
        <el-button
          style="padding-bottom: 0"
          icon="el-icon-circle-plus-outline"
          type="text"
          @click="addRow"
        >
          添加数据
        </el-button>
      </div>

      <div slot="footer">
        <el-button @click="close">
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  inheritAttrs: false,
  props: ['current'],
  data() {
    return {
      tableData: [],
      tableChildren: []
    }
  },
  methods: {
    onOpen() {
      this.tableData = this.current.data
      this.tableChildren = this.current.__config__.children
    },
    onClose() {},
    selectRow(val) {
      this.selectlistRow = val
    },
    addRow() {
      this.tableData.push({})
    },
    deleteRow(row) {
      this.tableData.splice(row.$index, 1)
    },
    close() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
