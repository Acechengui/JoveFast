<template>
  <div v-if="activeData.__config__.tag==='ts-sub-form'">
    <el-divider>子表单设置</el-divider>
    <el-form size="small" label-width="90px">
      <el-form-item v-if="activeData.addButton !== undefined" label="添加按钮">
        <el-switch v-model="activeData.addButton" />
      </el-form-item>
      <el-form-item v-if="activeData.deleteButton !== undefined" label="删除按钮">
        <el-switch v-model="activeData.deleteButton" />
      </el-form-item>
      <el-form-item v-if="activeData.canEdit !== undefined" label="可编辑">
        <el-switch v-model="activeData.canEdit" />
      </el-form-item>
    </el-form>
    <el-button
      style="padding-bottom: 0"
      icon="el-icon-circle-plus-outline"
      type="text"
      @click="addSubFormData"
    >
      添加演示数据
    </el-button>
    <el-button
      style="padding-bottom: 0"
      icon="el-icon-circle-plus-outline"
      type="text"
      @click="settingSubFormData"
    >设置子表单
    </el-button>
    <el-divider />

    <el-dialog
      title="添加演示数据,效果仅在运行后有效"
      :visible.sync="openAddSubFormData"
      :before-close="handleClose"
    >
      <el-table
        :data="activeData.__config__.defaultValue"
        border
        :destroy-on-close="true"
        style="width: 100%"
      >
        <el-table-column
          v-for="(column, index) in activeData.__config__.children"
          :key="index"
          :label="column.__config__.label"
          align="center"
          :prop="column.__vModel__"
        >
          <template slot-scope="scope">
            <el-input v-model="activeData.__config__.defaultValue[scope.$index][column.__vModel__]" size="small" placeholder="请输入" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120px">
          <template slot-scope="scope">
            <el-button size="mini" :plain="true" type="danger" icon="el-icon-delete-solid" :data="scope.row" @click="activeData.__config__.defaultValue.splice(scope.$index, 1)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px">
        <el-button size="small" icon="el-icon-plus" style="float: left;" @click="ts_add_rows(activeData.__config__.defaultValue)">
          添加
        </el-button>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="handleClose">{{ $t('common.cancel') }}</el-button>
        </span>
    </el-dialog>
    <el-dialog
      title="设置子表单"
      :visible.sync="openSettingSubFormData"
      :before-close="handleCloseSetting"
    >
      <div style="display: flex;flex-direction: row;align-items: center;">
        <div v-for="(column, index) in activeData.__config__.children" :key="index" style="margin-right: 10px;">
          <div>第{{index + 1}}列</div>
          <el-input v-model="activeData.__config__.children[index].width" size="small" placeholder="请输入" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { isNumberStr } from '@/utils'

export default {
  name: 'TsCollapseItem',
  props: ['activeData'],
  data() {
    return {
      openAddSubFormData: false,
      openSettingSubFormData: false
    }
  },
  methods: {
    addSubFormData() {
      this.openAddSubFormData = true
    },
    handleClose() {
      this.openAddSubFormData = false
    },
    settingSubFormData() {
      this.openSettingSubFormData = true
    },
    handleCloseSetting() {
      this.openSettingSubFormData = false
    },
    ts_add_rows(data) {
      data.push({})
    },
    setOptionValue(item, val) {
      item.value = isNumberStr(val) ? +val : val
    }
  }
}
</script>
