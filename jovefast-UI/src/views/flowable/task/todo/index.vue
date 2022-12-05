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
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="todoList" border>
      <el-table-column label="任务编号" align="center" prop="taskId" :show-overflow-tooltip="true"/>
      <el-table-column label="流程名称" align="center" prop="procDefName"/>
      <el-table-column label="任务节点" align="center" prop="taskName"/>
      <el-table-column label="流程版本" align="center">
        <template slot-scope="scope">
          <el-tag size="medium" >v{{scope.row.procDefVersion}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="流程发起人" align="center">
        <template slot-scope="scope">
          <label>{{scope.row.startUserName}} <el-tag type="info" size="mini" v-if="scope.row.startDeptName">{{scope.row.startDeptName}}</el-tag></label>
        </template>
      </el-table-column>
      <el-table-column label="接收时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
  todoList
} from "@/api/flowable/todo";

export default {
  name: "Tode",
  components: {},
  data() {
    return {
      // 遮罩层
      loading: true,
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

