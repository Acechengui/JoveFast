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
      <el-form-item label="开始时间">
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['flowable:deployment:add']"
        >新增流程</el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
        :columns="columns"
      ></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="myProcessList" border>
      <el-table-column label="流程编号" align="center" prop="procInsId" :show-overflow-tooltip="true" v-if="columns[0].visible"/>
      <el-table-column label="流程标题" align="center" prop="processTitle" :show-overflow-tooltip="true" v-if="columns[1].visible"/>
      <el-table-column label="流程名称" align="center" prop="procDefName" :show-overflow-tooltip="true" v-if="columns[2].visible"/>
      <el-table-column label="流程类别" align="center" prop="category" width="100px" v-if="columns[3].visible"/>
      <el-table-column label="流程版本" align="center" width="80px" v-if="columns[4].visible">
        <template slot-scope="scope">
          <el-tag size="medium" >v{{ scope.row.procDefVersion }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" align="center" prop="createTime" width="180" v-if="columns[5].visible"/>
      <el-table-column label="流程状态" align="center" width="100" v-if="columns[6].visible">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.finishTime == null" size="mini">进行中</el-tag>
          <el-tag type="success" v-if="scope.row.finishTime != null" size="mini">已完成</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="耗时" align="center" prop="duration" width="180" v-if="columns[7].visible"/>
      <el-table-column label="发起人" align="center" prop="startUserName" width="180" v-if="columns[8].visible"/>
      <el-table-column label="当前节点" align="center" prop="taskName" v-if="columns[9].visible"/>
      <el-table-column label="办理" align="center" v-if="columns[10].visible">
        <template slot-scope="scope">
          <label v-if="scope.row.assigneeName">{{scope.row.assigneeName}} <el-tag type="info" size="mini" v-if="scope.row.deptName">{{scope.row.deptName}}</el-tag></label>
          <label v-if="scope.row.candidate">{{scope.row.candidate}}</label>
          <label v-if="scope.row.taskName && scope.row.assigneeName===null && scope.row.candidate===null">{{scope.row.taskName}}</label>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link">
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item icon="el-icon-tickets" @click.native="handleFlowRecord(scope.row)" v-hasPermi="['flowable:deployment:list']">
                详情
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-close" @click.native="handleStop(scope.row)" v-hasPermi="['flowable:task:stopProcess']">
                取消申请
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-delete" @click.native="handleDelete(scope.row)" v-hasPermi="['flowable:instance:del']">
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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

    <!-- 发起流程 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form :model="queryProcessParams" ref="queryProcessForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="queryProcessParams.name"
            placeholder="请输入名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleProcessQuery">{{ $t('common.search') }}</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetProcessQuery">{{ $t('common.reset') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="processLoading" fit :data="definitionList" border >
        <el-table-column label="流程名称" align="center" prop="name" />
        <el-table-column label="流程版本" align="center">
          <template slot-scope="scope">
            <el-tag size="medium" >v{{ scope.row.version }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="流程分类" align="center" prop="category" />
        <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit-outline"
              @click="handleStartProcess(scope.row)"
            >发起流程</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="processTotal>0"
        :total="processTotal"
        :page.sync="queryProcessParams.pageNum"
        :limit.sync="queryProcessParams.pageSize"
        @pagination="listDefinition"
      />
    </el-dialog>

  </div>
</template>

<script>
import {
  getDeployment,
  delDeployment
} from "@/api/flowable/finished";
import { myProcessList,stopProcess } from "@/api/flowable/process";
import {listDefinition} from "@/api/flowable/definition";
export default {
  name: "Deploy",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      processLoading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 日期范围
      dateRange: [],
      // 总条数
      total: 0,
      processTotal:0,
      // 我发起的流程列表数据
      myProcessList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      src: "",
      definitionList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        procDefName: null,
        category: null,
        key: null,
        tenantId: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      },
      // 查询参数
      queryProcessParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      },
      // 列信息
      columns: [
        { key: 0, label: `流程编号`, visible: false },
        { key: 1, label: `流程标题`, visible: true },
        { key: 2, label: `流程名称`, visible: true },
        { key: 3, label: `流程类别`, visible: true },
        { key: 4, label: `流程版本`, visible: true },
        { key: 5, label: `提交时间`, visible: true },
        { key: 6, label: `流程状态`, visible: true },
        { key: 7, label: `耗时`, visible: true },
        { key: 8, label: `发起人`, visible: true },
        { key: 9, label: `当前节点`, visible: true },
        { key: 10, label: `办理`, visible: true }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
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
      myProcessList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.myProcessList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      };
      this.resetForm("form");
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
    /** 搜索按钮操作 */
    handleProcessQuery() {
      this.queryProcessParams.pageNum = 1;
      this.listDefinition();
    },
    /** 重置按钮操作 */
    resetProcessQuery() {
      this.resetForm("queryProcessForm");
      this.handleProcessQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "发起流程";
      this.listDefinition();
    },
    listDefinition(){
      listDefinition(this.queryProcessParams).then(response => {
        this.definitionList = response.rows;
        this.processTotal = response.total;
        this.processLoading = false;
      });
    },
    /**  发起流程申请 */
    handleStartProcess(row){
      this.open = false;
      this.$router.push({ path: '/flowable/task/record/index',
        query: {
          deployId: row.deploymentId,
          procDefId: row.id,
          finished: true
        }
      })
    },
    /**  取消流程申请 */
    handleStop(row){
      const params = {
        instanceId: row.procInsId
      }
      stopProcess(params).then( res => {
        this.$modal.msgSuccess(res.msg);
        this.getList();
      });
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
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDeployment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程定义";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.procInsId;
      this.$confirm('是否确认删除流程定义编号为"' + ids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delDeployment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      })
    },
  }
};
</script>

