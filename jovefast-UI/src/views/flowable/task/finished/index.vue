<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程标题" prop="processTitle">
        <el-input
          v-model="queryParams.processTitle"
          placeholder="请输入完整流程名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起人" prop="startUserName">
        <el-input
          v-model="queryParams.startUserName"
          placeholder="请输入发起人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发起部门" prop="startDeptName">
        <el-input
          v-model="queryParams.startDeptName"
          placeholder="请输入发起部门"
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
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
        :columns="columns"
      ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="finishedList" border>
      <el-table-column label="发起人ID" align="center" prop="startUserId" v-if="columns[0].visible"/>
      <el-table-column label="任务编号" align="center" prop="taskId" :show-overflow-tooltip="true" v-if="columns[1].visible"/>
      <el-table-column label="流程标题" align="center" prop="processTitle" :show-overflow-tooltip="true" v-if="columns[2].visible"/>
      <el-table-column label="流程名称" align="center" prop="procDefName" :show-overflow-tooltip="true" v-if="columns[3].visible"/>
      <el-table-column label="任务节点" align="center" prop="taskName" v-if="columns[4].visible"/>
      <el-table-column label="流程发起人" align="center" v-if="columns[5].visible">
        <template slot-scope="scope">
          <label>{{scope.row.startUserName}} <el-tag type="info" size="mini" v-if="scope.row.startDeptName">{{scope.row.startDeptName}}</el-tag></label>
        </template>
      </el-table-column>
      <el-table-column label="接收时间" align="center" prop="createTime" width="180" v-if="columns[6].visible"/>
      <el-table-column label="审批时间" align="center" prop="finishTime" width="180" v-if="columns[7].visible"/>
      <el-table-column label="耗时" align="center" prop="duration" width="180" v-if="columns[8].visible"/>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-tickets"
            @click="handleFlowRecord(scope.row)"
          >流转记录</el-button>
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
import { finishedList } from "@/api/flowable/finished";

export default {
  name: "Finished",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 已办任务列表数据
      finishedList: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        processTitle: null,
        startUserName: undefined,
        startDeptName:undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `发起人ID`, visible: false },
        { key: 1, label: `任务编号`, visible: false },
        { key: 2, label: `流程标题`, visible: true },
        { key: 3, label: `流程名称`, visible: true },
        { key: 4, label: `任务节点`, visible: true },
        { key: 5, label: `流程发起人`, visible: true },
        { key: 6, label: `接收时间`, visible: true },
        { key: 7, label: `审批时间`, visible: true },
        { key: 8, label: `耗时`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
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
      finishedList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.finishedList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    setIcon(val){
      if (val){
        return "el-icon-check";
      }else {
        return "el-icon-time";
      }

    },
    setColor(val){
      if (val){
        return "#2bc418";
      }else {
        return "#b3bdbb";
      }

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
    /** 流程流转记录 */
    handleFlowRecord(row){
      this.$router.push({ path: '/flowable/task/record/index',
        query: {
          procInsId: row.procInsId,
          deployId: row.deployId,
          taskId: row.taskId,
          finished: false
        }})
    }
  }
};
</script>

