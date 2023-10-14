<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="procDefName">
        <el-input
          v-model="queryParams.procDefName"
          placeholder="请输入完整流程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
                        value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00', '23:59:59']"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="batchhandle"
          v-hasPermi="['flowable:task:batchhandle']"
        >批量审批</el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
        :columns="columns"
      ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="todoList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="任务编号" align="center" prop="taskId" :show-overflow-tooltip="true" v-if="columns[0].visible"/>
      <el-table-column label="流程标题" align="center" prop="processTitle" :show-overflow-tooltip="true" v-if="columns[1].visible"/>
      <el-table-column label="流程名称" align="center" prop="procDefName" v-if="columns[2].visible"/>
      <el-table-column label="任务节点" align="center" prop="taskName" v-if="columns[3].visible"/>
      <el-table-column label="办理" align="center" v-if="columns[4].visible">
        <template slot-scope="scope">
          <label v-if="scope.row.assigneeName">{{scope.row.assigneeName}} <el-tag type="info" size="mini" v-if="scope.row.deptName">{{scope.row.deptName}}</el-tag></label>
          <label v-if="scope.row.candidate">{{scope.row.candidate}}</label>
          <label v-if="scope.row.taskName && scope.row.assigneeName===null && scope.row.candidate===null">{{scope.row.taskName}}</label>
          <label v-if="scope.row.taskName && scope.row.assigneeName===undefined && scope.row.candidate===undefined">{{scope.row.taskName}}</label>
        </template>
      </el-table-column>
      <el-table-column label="流程版本" align="center" v-if="columns[5].visible">
        <template slot-scope="scope">
          <el-tag size="medium" >v{{scope.row.procDefVersion}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="流程发起人" align="center" v-if="columns[6].visible">
        <template slot-scope="scope">
          <label>{{scope.row.startUserName}} <el-tag type="info" size="mini" v-if="scope.row.startDeptName">{{scope.row.startDeptName}}</el-tag></label>
        </template>
      </el-table-column>
      <el-table-column label="接收时间" align="center" prop="createTime" width="180" v-if="columns[7].visible"/>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit-outline"
            @click="handleProcess(scope.row)"
          >处理
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import {
  todoList,
  batchComplete
} from "@/api/flowable/todo";

export default {
  name: "Tode",
  data() {
    return {
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 遮罩层
      loading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程待办任务表格数据
      todoList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        procDefName: null,
        category: null
      },
      // 列信息
      columns: [
        { key: 0, label: `任务编号`, visible: false },
        { key: 1, label: `流程标题`, visible: true },
        { key: 2, label: `流程名称`, visible: true },
        { key: 3, label: `任务节点`, visible: true },
        { key: 4, label: `办理`, visible: true },
        { key: 5, label: `流程版本`, visible: true },
        { key: 6, label: `流程发起人`, visible: true },
        { key: 6, label: `接收时间`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
     this.dateRange = this.timeDefault();
     this.getList();
  },
  methods: {
    // 默认时间
    timeDefault() {
      let date = new Date();
      // 通过时间戳计算
      let defalutStartTime = date.getTime() - 7 * 24 * 3600 * 1000; // 转化为时间戳 -几即表示几天内
      let defalutEndTime = date.getTime();
      let startDateNs = new Date(defalutStartTime);
      let endDateNs = new Date(defalutEndTime);
      // 月，日 不够10补0
      defalutStartTime =
        startDateNs.getFullYear() +
        "-" +
        (startDateNs.getMonth() + 1 >= 10
          ? startDateNs.getMonth() + 1
          : "0" + (startDateNs.getMonth() + 1)) +
        "-" +
        (startDateNs.getDate() >= 10
          ? startDateNs.getDate()
          : "0" + startDateNs.getDate()) + ' 00:00:00';
      defalutEndTime =
        endDateNs.getFullYear() +
        "-" +
        (endDateNs.getMonth() + 1 >= 10
          ? endDateNs.getMonth() + 1
          : "0" + (endDateNs.getMonth() + 1)) +
        "-" +
        (endDateNs.getDate() >= 10
          ? endDateNs.getDate()
          : "0" + endDateNs.getDate()) + ' 23:59:59';
      return [defalutStartTime, defalutEndTime];
    },
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      todoList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.todoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 跳转到处理页面
    handleProcess(row){
      this.$router.push({ path: '/flowable/task/record/index',
        query: {
          procInsId: row.procInsId,
          executionId: row.executionId,
          deployId: row.deployId,
          taskId: row.taskId,
          finished: true
        }})
    },
    /** 批量审批操作 */
    batchhandle(){
      if(this.ids.length > 10){
        this.$modal.msgWarning('批量审批最多不能超过10条');
        return false;
      }
      batchComplete(this.ids).then(res => {
        if(res.code == 200){
          this.$modal.msgSuccess(res.msg);
          this.getList();
        }else{
          this.$modal.msgError(res.msg);
        }
      })
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.taskId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
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
    }
  }
};
</script>

