<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表单名称" prop="formName">
        <el-input v-model="queryParams.formName" placeholder="请输入表单名称" clearable size="small"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search')
        }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['flowable:form:add']">{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['flowable:form:del']">{{ $t('common.del') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['flowable:form:export']">{{ $t('common.export') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="formList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="表单ID" align="center" prop="formId" />
      <el-table-column label="表单名称" align="center" prop="formName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)"
            v-hasPermi="['flowable:form:list']">详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['flowable:form:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['flowable:form:del']">{{ $t('common.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!--表单配置详情-->
    <el-dialog :title="formTitle" :visible.sync="formConfOpen">
      <div>
        <ng-form-build ref="formBuild" :preview="true" :formTemplate="formConf" :config="formBuildConfig"
          :custom-components="customComponents" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listForm, delForm } from "@/api/flowable/form";
import { getToken } from '@/utils/auth'
//自定义
import BackgroundImageComponent from '../../../ngform/customComponents/backgroundImage/index.vue'
import BackgroundImagePropertie from '../../../ngform/customComponents/backgroundImage/properties.vue'
export default {
  name: "Form",
  components: {
    BackgroundImageComponent, BackgroundImagePropertie
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程表单表格数据
      formList: [],
      formConf: null,
      formConfOpen: false,
      formTitle: "",
      formBuildConfig: {
        httpConfig: (config) => {
          config.headers['Authorization'] = 'Bearer ' + getToken()
          return config
        }
      },
      customComponents: [
        /**
         {
            type: '类型', // 唯一，不能和已有组件冲突
            label: '组件名称', // 唯一，不能和已有组件冲突
            component: 组件实际的渲染文件 ,// .vue
            properties: 组件的属性配置面板 , // .vue
            icon: 组件显示的图标 // base64
            ..... // 其他配置项
        } */
        {
          type: 'ngImage',
          label: '自定义组件',
          component: BackgroundImageComponent,
          properties: BackgroundImagePropertie,
          options: {
            style: null,
            imgurl: null
          },
          icon: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDgiIGhlaWdodD0iNDgiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHJlY3QgeD0iNS44IiB5PSIxMC44IiB3aWR0aD0iMzYuNCIgaGVpZ2h0PSIyNi40IiByeD0iMy4yIiBmaWxsPSIjZmZmIiBzdHJva2U9IiM3NTc1NzUiIHN0cm9rZS13aWR0aD0iMS42Ii8+PGNpcmNsZSBjeD0iMTMuNSIgY3k9IjE4LjUiIHI9IjMuNSIgZmlsbD0iI0VFQ0E4NiIvPjxwYXRoIGQ9Ik0yNy45MjMgMTguMzY2YTEgMSAwIDAgMSAxLjY5Ni0uMDE4bDguMzk1IDEzLjExM0ExIDEgMCAwIDEgMzcuMTcyIDMzSDIwLjc4MWExIDEgMCAwIDEtLjg1NC0xLjUybDcuOTk2LTEzLjExNFoiIGZpbGw9IiM4MkJGOTkiLz48cGF0aCBkPSJNMTYuNjc2IDI2LjE5OWExIDEgMCAwIDEgMS42NDggMGwzLjU5OSA1LjIzNEExIDEgMCAwIDEgMjEuMDk5IDMzSDEzLjlhMSAxIDAgMCAxLS44MjQtMS41NjdsMy41OTktNS4yMzRaIiBmaWxsPSIjODJCRjk5Ii8+PC9zdmc+Cg==',
        }
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        formName: null,
        formContent: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程表单列表 */
    getList() {
      this.loading = true;
      listForm(this.queryParams).then(response => {
        this.formList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.formId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 表单配置信息 */
    handleDetail(row) {
      this.formConfOpen = true;
      this.formTitle = "流程表单详细";
      this.formConf = JSON.parse(row.formContent)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({ path: '/ngform/ngformDesign', query: { formId: null } })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$router.push({ path: '/ngform/ngformDesign', query: { formId: row.formId } })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const formIds = row.formId || this.ids;
      this.$confirm('是否确认删除流程表单编号为"' + formIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delForm(formIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "/flowable/form/export",
        {
          ...this.queryParams
        },
        `流程表单列表_${new Date().getTime()}.xlsx`
      );

    }
  }
};
</script>
