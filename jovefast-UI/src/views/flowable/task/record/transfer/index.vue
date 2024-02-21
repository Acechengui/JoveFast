<template>
    <div>
        <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item prop="targetKey">
          <el-row :gutter="20">
            <!--部门数据-->
            <el-col :span="6" :xs="24">
              <el-tag type="danger">把任务转办给他人: 只能转给一人</el-tag>
              <h6>展开部门列表选择</h6>
              <div class="head-container">
                <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search"
                  style="margin-bottom: 20px" />
              </div>
              <div class="head-container">
                <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false"
                  :filter-node-method="filterNode" ref="tree" highlight-current @node-click="handleNodeClick" />
              </div>
            </el-col>
            <el-col :span="10" :xs="24">
              <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
                <el-form-item label="用户名称" prop="nickName">
                  <el-input v-model="queryParams.nickName" placeholder="请输入用户名称" clearable style="width: 240px" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search')}}</el-button>
                  <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
                </el-form-item>
              </el-form>
              <h6>待选人员</h6>
              <el-table ref="singleTable" :data="userList" border style="width: 100%" highlight-current-row
                v-horizontal-scroll="'always'" @current-change="handleSingleUserSelect">
                <el-table-column  width="55" align="center" >
                    <template slot-scope="scope">
                        <el-radio v-model="radioSelected" :label="scope.row.userId">{{''}}</el-radio>
                    </template>
                </el-table-column>
                <el-table-column label="用户名" align="center" key="nickName" prop="nickName" />
                <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName"/>
              </el-table>
              <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" layout="sizes, prev, pager, next"
                :limit.sync="queryParams.pageSize" :pager-count="3" small @pagination="getList" />
            </el-col>
            <el-col :span="8" :xs="24">
              <h6>选中人员</h6>
              <el-tag  closable @close="handleClose()" v-if=" selectedRow !== null">
                {{ selectedRow.nickName }}
              </el-tag>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="转发意见" prop="comment" :rules="[{ required: true, message: '请输入转发意见', trigger: 'blur' }]">
          <el-input type="textarea" v-model="taskForm.comment" placeholder="请输入转发意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">{{ $t("common.cancel") }}</el-button>
          <el-button type="primary" @click="taskTransfer">{{
              $t("common.determine")
          }}</el-button>
      </div>
    </div>
  </template>
  <script>
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import Treeselect from "@riophae/vue-treeselect";
  import { transferTask } from "@/api/flowable/todo";
  import { listUser } from "@/api/system/user";
  export default {
    components: {
      Treeselect,
    },
    inject: ['parentInstance'],
    props:{
        checkSendUser:{
            type: Boolean,      //类型
            required: true,   //是否必填
            default: false    //默认值
        },
        deptOptions: {
            type: Array,
            required: false,
            default: []
        },
        taskForm:{
          type: Object, 
          required: true, 
          default: {} 
        }
    },
    data() {
      return {
        // 部门名称
        deptName: undefined,
        // 单选
        radioSelected: null,
        defaultProps: {
            children: "children",
            label: "label"
        },
        commentVar: undefined,
        // 用户表格数据
        userList: null,
        // 已选数据
        selectedRow: null,
        // 是否展示选择人员模块
        checkSendUser: false,
        // 查询参数
        queryParams: {
          deptId: undefined,
          nickName: undefined,
          pageNum: 1,
          pageSize: 10,
        }
      };
    },
    watch: {
        // 根据名称筛选部门树
        deptName(val) {
            this.$refs.tree.filter(val);
        }
    },
    methods: {
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.deptId = undefined;
        this.$refs.tree.setCurrentKey(null);
      },
      //单选数据
      handleSingleUserSelect(selection) {
        if (selection) {
            //点击当前行时,radio同样有选中效果
            this.radioSelected = selection.userId;
            this.selectedRow = selection
            this.taskForm.values = {
                "approval": this.radioSelected
            }
        }
       
      },
      // 关闭标签
      handleClose() {
        this.selectedRow = null;
        this.radioSelected = null;
      },
      /** 取消 */
      cancel() {
        this.taskForm.comment = undefined;
        this.parentInstance.transferOpen = false;
      },
      /** 转办任务处理 */
      taskTransfer(){
        if (this.radioSelected == null) {
            this.$modal.msgError("请选择转办人员");
            return;
        }
        if (!this.taskForm.comment) {
            this.$modal.msgError("请输入转办意见");
            return;
        }
        this.$modal.loading('请稍等处理中...');
        this.taskForm.variables=this.parentInstance.$refs.variableParserFormBuild.getData();
        transferTask(this.taskForm).then(res => {
            this.$modal.closeLoading();
            if(res.code == 200){
            this.$modal.msgSuccess(res.msg);
            this.parentInstance.goBack();
            }else{
            this.$modal.msgError(res.msg);
            }
        }).catch(e=>{
            this.$modal.closeLoading();
        })
      },
      /** 查询用户列表 */
      getList() {
        listUser(this.queryParams).then((response) => {
          this.userList = response.rows;
          this.total = response.total;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.getList();
      },
    },
  };
  </script>
  