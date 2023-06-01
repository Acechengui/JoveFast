<template>
  <div class="app-container">
    <div slot="header" class="clearfix">
      <span class="el-icon-document">基础信息</span>
      <el-button style="float: right;" icon="el-icon-arrow-left" type="primary"  size="mini" @click="goBack">返回</el-button>
      <el-button style="float: right; margin-right: 10px;" icon="el-icon-picture-outline" type="danger" size="mini" @click="flowChart">流程图</el-button>
    </div>
    <div id="nbprint">
      <!--流程处理表单模块-->
      <el-col :span="24" v-if="variableOpen">
        <div>
          <parser :key="new Date().getTime()" :form-conf="variablesData" @submit="submitVariable" ref="variableParser" />
        </div>
        <div style="margin-left:2%;margin-bottom: 20px" v-if="fileDisplay">
          <!--对上传文件进行显示处理 -->
          <el-tag type="success" effect="plain" v-if="fileList">附件列表:</el-tag>

          <el-card class="t-box-card">
            <div v-for="o in fileList" :key="o" class="text item">
              {{ o.name }}
              <el-button type="text" class="button t-download" @click="handleFilePreview(o)">下载</el-button>
            </div>
          </el-card>
        </div>
        <div style="margin-left:10%;margin-bottom: 20px;font-size: 14px;" v-if="finished === 'true'">
          <el-button icon="el-icon-edit-outline" type="success" size="mini" @click="handleComplete">审批</el-button>
          <!--                <el-button  icon="el-icon-edit-outline" type="primary" size="mini" @click="handleDelegate">委派</el-button>-->
          <el-button  icon="el-icon-edit-outline" type="primary" size="mini" @click="handleAssign">转办</el-button>-
          <!--                <el-button  icon="el-icon-edit-outline" type="primary" size="mini" @click="handleDelegate">签收</el-button>-->
          <el-button icon="el-icon-refresh-left" type="warning" size="mini" @click="handleReturn">退回</el-button>
          <el-button icon="el-icon-circle-close" type="danger" size="mini" @click="handleReject">驳回</el-button>
        </div>
      </el-col>

      <!--初始化流程加载表单信息-->
      <el-col :span="24" v-if="formConfOpen">
        <div class="key-form">
          <parser :key="new Date().getTime()" :form-conf="formConf" @submit="submitForm" ref="parser"
                  @getData="getData" />
        </div>
      </el-col>

      <!--流程流转记录-->
      <el-card class="box-card" v-if="flowRecordList">
        <div slot="header" class="clearfix">
          <span class="el-icon-notebook-1">审批记录</span>
        </div>
        <el-col :span="16" :offset="4">
          <div class="block">
            <el-timeline>
              <el-timeline-item v-for="(item,index ) in flowRecordList" :key="index" :icon="setIcon(item.finishTime)"
                                :color="setColor(item.finishTime)">
                <p style="font-weight: 700">{{item.taskName}}</p>
                <el-card :body-style="{ padding: '10px' }">
                  <el-descriptions class="margin-top" :column="1" size="small" border>
                    <el-descriptions-item v-if="item.assigneeName" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-user"></i>实际办理</template>
                      {{item.assigneeName}}
                      <el-tag type="info" size="mini" v-if="item.deptName">{{item.deptName}}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.candidate" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-user"></i>候选办理</template>
                      {{item.candidate}}
                    </el-descriptions-item>
                    <el-descriptions-item label-class-name="my-label">
                      <template slot="label"><i class="el-icon-date"></i>接收时间</template>
                      {{item.createTime}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.finishTime" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-date"></i>处理时间</template>
                      {{item.finishTime}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.duration" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-time"></i>耗时</template>
                      {{item.duration}}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.comment.comment" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-tickets"></i>处理意见</template>
                      {{item.comment.comment}}
                    </el-descriptions-item>
                  </el-descriptions>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-col>
      </el-card>

    </div>

    <!--流程图-->
    <el-dialog title="流程图" :visible.sync="flowChartOpen" width="60%" class="el-dialog-div" append-to-body>
      <flow :xmlData="xmlData" :taskData="taskList"></flow>
    </el-dialog>

    <!--审批正常流程-->
    <el-dialog :title="completeTitle" :visible.sync="completeOpen" :width="checkSendUser? '80%':'80%'" append-to-body>
      <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item v-if="checkSendUser" prop="targetKey">

          <el-row :gutter="20">
            <!--部门数据-->
            <el-col :span="6" :xs="24">
              <el-tag type="danger">默认不需要指定,但可以在下方指定审批人</el-tag>
              <h6>展开部门列表选择</h6>
              <div class="head-container">
                <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search"
                          style="margin-bottom: 20px" />
              </div>
              <div class="head-container">
                <el-tree
                  :data="deptOptions"
                  :props="defaultProps"
                  :expand-on-click-node="false"
                  :filter-node-method="filterNode"
                  ref="tree"
                  highlight-current
                  @node-click="handleNodeClick" />
              </div>
            </el-col>
            <el-col :span="10" :xs="24">
              <h6>待选人员</h6>
              <el-table ref="singleTable" :data="userList" border style="width: 100%"
                        @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column label="用户名" align="center" prop="nickName" />
                <el-table-column label="部门" align="center" prop="dept.deptName" />
              </el-table>
              <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="checkSendUser ? getList:[]"
              />
            </el-col>
            <el-col :span="8" :xs="24">
              <h6>已选人员</h6>
              <el-tag v-for="(user,index) in userData" :key="index" closable @close="handleClose(user)">
                {{user.nickName}} {{user.dept.deptName}}
              </el-tag>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="快捷选择" prop="commentVar">
          <el-select v-model="commentVar" clearable @change="bclxChange" placeholder="请选择">
            <el-option
              v-for="item in commentOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理意见" prop="comment" :rules="[{ required: true, message: '请输入处理意见', trigger: 'blur' }]">
          <el-input type="textarea" v-model="taskForm.comment" placeholder="请输入处理意见" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="completeOpen = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="taskComplete">{{ $t('common.determine') }}</el-button>
      </span>
    </el-dialog>

    <!--退回流程-->
    <el-dialog :title="returnTitle" :visible.sync="returnOpen" width="60%" append-to-body>
      <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item label="退回节点" prop="targetKey">
          <el-radio-group v-model="taskForm.targetKey">
            <el-radio-button v-for="item in returnTaskList" :key="item.id" :label="item.id">{{item.name}}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="退回意见" prop="comment" :rules="[{ required: true, message: '请输入意见', trigger: 'blur' }]">
          <el-input style="width: 80%" type="textarea" v-model="taskForm.comment" placeholder="请输入意见" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="returnOpen = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="taskReturn">{{ $t('common.determine') }}</el-button>
      </span>
    </el-dialog>

    <!--驳回流程-->
    <el-dialog :title="rejectTitle" :visible.sync="rejectOpen" width="60%" append-to-body>
      <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item label="驳回意见" prop="comment" :rules="[{ required: true, message: '请输入意见', trigger: 'blur' }]">
          <el-input style="width: 80%" type="textarea" v-model="taskForm.comment" placeholder="请输入意见" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="rejectOpen = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="taskReject">{{ $t('common.determine') }}</el-button>
      </span>
    </el-dialog>

    <!--转办流程-->
    <el-dialog :title="transferTitle" :visible.sync="transferOpen" :width="'80%'" append-to-body>
      <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item prop="targetKey">
          <el-row :gutter="20">
            <!--部门数据-->
            <el-col :span="6" :xs="24">
              <el-tag type="danger">把任务转办给他人</el-tag>
              <h6>展开部门列表选择</h6>
              <div class="head-container">
                <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search"
                          style="margin-bottom: 20px" />
              </div>
              <div class="head-container">
                <el-tree
                  :data="deptOptions"
                  :props="defaultProps"
                  :expand-on-click-node="false"
                  :filter-node-method="filterNode"
                  ref="tree"
                  highlight-current
                  @node-click="handleNodeClick" />
              </div>
            </el-col>
            <el-col :span="10" :xs="24">
              <h6>待选人员</h6>
              <el-table ref="singleTable" :data="userList" border style="width: 100%"
                        @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50" align="center" />
                <el-table-column label="用户名" align="center" prop="nickName" />
                <el-table-column label="部门" align="center" prop="dept.deptName" />
              </el-table>
              <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="checkSendUser ? getList:[]"
              />
            </el-col>
            <el-col :span="8" :xs="24">
              <h6>已选人员</h6>
              <el-tag v-for="(user,index) in userData" :key="index" closable @close="handleClose(user)">
                {{user.nickName}} {{user.dept.deptName}}
              </el-tag>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="转发意见" prop="comment" :rules="[{ required: true, message: '请输入转发意见', trigger: 'blur' }]">
          <el-input type="textarea" v-model="taskForm.comment" placeholder="请输入转发意见" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="transferOpen = false">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="taskTransfer">{{ $t('common.determine') }}</el-button>
      </span>
    </el-dialog>

    <!-- 返回顶部 -->
    <GoTop></GoTop>
  </div>
</template>

<script>
import GoTop from "@/components/GoTop/index";
import { flowRecord } from "@/api/flowable/finished";
import Parser from '@/components/parser/Parser'
import { definitionStart, getProcessVariables, readXml, getFlowViewer } from "@/api/flowable/definition";
import { complete, rejectTask, returnList, returnTask, transferTask,getNextFlowNode, delegate } from "@/api/flowable/todo";
import flow from '@/views/flowable/task/record/flow'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Treeselect from "@riophae/vue-treeselect";
import { listUser,deptTreeSelect } from "@/api/system/user";

export default {
  name: "Record",
  components: {
    Parser,
    flow,
    Treeselect,
    GoTop
  },
  data() {
    return {
      // 模型xml数据
      xmlData: "",
      taskList: [],
      // 部门名称
      deptName: undefined,
      // 部门树选项
      deptOptions: undefined,
      commentVar:undefined,
      //是否同意快捷选项
      commentOptions:[{
        value: '同意',
        label: '同意'
      }],
      // 用户表格数据
      userList: null,
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        deptId: undefined
      },
      // 遮罩层
      loading: true,
      //是否显示流程图
      flowChartOpen: false,
      flowRecordList: [], // 流程流转数据
      formConfCopy: {},
      src: null,
      rules: {}, // 表单校验
      variablesForm: {}, // 流程变量数据
      taskForm: {
        returnTaskShow: false, // 是否展示回退表单
        delegateTaskShow: false, // 是否展示回退表单
        defaultTaskShow: true, // 默认处理
        sendUserShow: false, // 审批用户
        multiple: false,
        comment: "", // 意见内容
        procInsId: "", // 流程实例编号
        instanceId: "", // 流程实例编号
        deployId: "",  // 流程定义编号
        taskId: "",// 流程任务编号
        procDefId: "",  // 流程编号
        vars: "",
        targetKey: "",
        variables:undefined
      },
      userDataList: [], // 流程候选人
      assignee: null,
      formConf: {}, // 默认表单数据
      formConfOpen: false, // 是否加载默认表单数据
      variables: [], // 流程变量数据
      variablesData: {}, // 流程变量数据
      variableOpen: false, // 是否加载流程变量数据
      fileDisplay: false, // 是否显示上传的文件控件
      fileList: [], //表单设计器上传的文件列表
      returnTaskList: [],  // 回退列表数据
      finished: 'false',
      completeTitle: null,
      completeOpen: false,
      returnTitle: null,
      returnOpen: false,
      rejectOpen: false,
      rejectTitle: null,
      transferOpen: false,
      transferTitle: null,
      userData: [],
      checkSendUser: false // 是否展示选择人员模块
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.$modal.loading("正在加载数据中，请稍候...");
    this.taskForm.deployId = this.$route.query && this.$route.query.deployId;
    this.taskForm.taskId = this.$route.query && this.$route.query.taskId;
    this.taskForm.procInsId = this.$route.query && this.$route.query.procInsId;
    this.taskForm.executionId = this.$route.query && this.$route.query.executionId;
    this.taskForm.instanceId = this.$route.query && this.$route.query.procInsId;
    // 初始化表单
    this.taskForm.procDefId = this.$route.query && this.$route.query.procDefId;
    // 流程任务重获取变量表单
    if (this.taskForm.taskId) {
      this.processVariables(this.taskForm.taskId)
      this.getNextFlowNode(this.taskForm.taskId)
      this.taskForm.deployId = null
    }
    this.getFlowRecordList(this.taskForm.procInsId, this.taskForm.deployId);
    this.finished = this.$route.query && this.$route.query.finished;
    this.$modal.closeLoading();
  },
  methods: {
    //下拉选择监听
    bclxChange(selectValue) {
      this.taskForm.comment = selectValue;
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    },
    /** 查询用户列表 */
    getList() {
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows;
          this.total = response.total;
        }
      );
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
    // 回显流程记录
    flowChart(){
      this.$modal.loading("正在加载数据中，请稍候...");
      this.flowChartOpen=true;
      this.getFlowViewer(this.taskForm.procInsId, this.taskForm.executionId);
      this.getModelDetail(this.$route.query && this.$route.query.deployId);
      this.$modal.closeLoading();
    },
    /** xml 文件 */
    getModelDetail(deployId) {
      // 发送请求，获取xml
      readXml(deployId).then(res => {
        this.xmlData = res.data
      })
    },
    getFlowViewer(procInsId, executionId) {
      getFlowViewer(procInsId, executionId).then(res => {
        this.taskList = res.data
      })
    },
    setIcon(val) {
      if (val) {
        return "el-icon-check";
      } else {
        return "el-icon-time";
      }
    },
    setColor(val) {
      if (val) {
        return "#2bc418";
      } else {
        return "#b3bdbb";
      }
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      if (selection) {
        this.userData = selection
        const selectVal = selection.map(item => item.userId);
        if (selectVal instanceof Array) {
          this.taskForm.values = {
            "approval": selectVal.join(',')
          }
        } else {
          this.taskForm.values = {
            "approval": selectVal
          }
        }
      }
    },
    // 关闭标签
    handleClose(tag) {
      this.userData.splice(this.userData.indexOf(tag), 1);
      this.$refs.singleTable.toggleRowSelection(tag, false)
    },
    /** 流程变量赋值 */
    handleCheckChange(val) {
      if (val instanceof Array) {
        this.taskForm.values = {
          "approval": val.join(',')
        }
      } else {
        this.taskForm.values = {
          "approval": val
        }
      }
    },
    /** 流程流转记录 */
    getFlowRecordList(procInsId, deployId) {
      const params = { procInsId: procInsId, deployId: deployId }
      flowRecord(params).then(res => {
        this.flowRecordList = res.data.flowList;
        // 流程过程中不存在初始化表单 直接读取的流程变量中存储的表单值
        if (res.data.formData) {
          this.formConf = res.data.formData;
          this.formConfOpen = true
        }
      }).catch(res => {
        this.goBack();
      })
    },
    //点击文件列表中已上传文件进行下载
    handleFilePreview(file) {
      // 根据地址保存文件
      this.$download.saveAs(file.url, file.name);
    },
    fillFormData(fields, formConfs,whetherWritable) {
      fields.forEach((item, i) => {
        //回显时控制是否允许还能上传文件
        if(item['name'] === 'files' && !whetherWritable){
          item.disabled=true;
        }
        const val = item.__config__.defaultValue
        // 特殊处理el-upload，包括 回显图片
        if (item.__config__.tag === 'el-upload') {
          // 回显图片
          if (item['list-type'] != 'text') {
            this.fileList = [];    //隐藏加的el-upload文件列表
            if(val){
              this.fileDisplay = true
              item['file-list'] = JSON.parse(val)
            }
          }else {  //图片
            item['file-list'] = [] //隐藏加的表单设计器的文件列表
            if(val){
              this.fileDisplay = true
              this.fileList = JSON.parse(val)
            }

          }

        }
        // 设置各表单项的默认值（回填表单），包括el-upload的默认值
        if (val) {
          item.__config__.defaultValue = val
        }
        if (Array.isArray(item.__config__.children)) {
          this.fillFormData(item.__config__.children, formConfs)
        }
      })
    },
    /** 获取流程变量内容 */
    processVariables(taskId) {
      if (taskId) {
        // 提交流程申请时填写的表单存入了流程变量中后续任务处理时需要展示
        getProcessVariables(taskId).then(res => {
          this.variablesData = res.data.variables;
          // 回填数据,这里主要是处理文件显示
          this.fillFormData(this.variablesData.fields, this.variablesData,res.data.whetherWritable)
          //判断是否允许编辑数据
          if(res.data.whetherWritable){
            this.variablesData.disabled = false;
          }else{
            this.variablesData.disabled = true;
          }
          this.variablesData.formBtns = false;
          this.variableOpen = true
        });
      }
    },
    /** 根据当前任务获取流程设计配置的下一步节点 */
    getNextFlowNode(taskId) {
      // 根据当前任务获取流程设计配置的下一步节点 todo 暂时未涉及到考虑网关、表达式和多节点情况
      const params = { taskId: taskId }
      getNextFlowNode(params).then(res => {
        const data = res.data;
        if (data.type) {
          this.checkSendUser = true
          if (data.type === 'assignee') { // 指定人员
            this.userDataList = res.data.userList;
          } else if (data.type === 'candidateUsers') {  // 指定人员(多个)
            this.userDataList = res.data.userList;
            this.taskForm.multiple = true;
          } else if (data.type === 'candidateGroups') { // 指定组(所属角色接收任务)
            res.data.roleList.forEach(role => {
              role.userId = role.roleId;
              role.nickName = role.roleName;
            })
            this.userDataList = res.data.roleList;
            this.taskForm.multiple = false;
          } else if (data.type === 'multiInstance') { // 会签?
            this.userDataList = res.data.userList;
            this.taskForm.multiple = true;
          } else if (data.type === 'fixed') { // 已经固定人员接收下一任务
            this.checkSendUser = false;
          }
        }
      })
    },
    /** 审批任务选择 */
    handleComplete() {
      //触发表单提交
      this.$refs.variableParser.submitForm();
      this.completeOpen = true;
      this.completeTitle = "审批流程";
      if(this.checkSendUser == true){
        this.getTreeselect();
      }
    },
    /** 审批任务 */
    taskComplete() {
      // if (!this.taskForm.values && this.checkSendUser) {
      //   this.$modal.msgError("请选择流程接收人员");
      //   return;
      // }
      if (!this.taskForm.comment) {
        this.$modal.msgError("请输入审批意见");
        return;
      }
      this.$modal.loading('请稍等处理中...');
      complete(this.taskForm).then(res => {
        if(res.code == 200){
          this.$modal.closeLoading();
          this.$modal.msgSuccess(res.msg);
          this.goBack();
        }else{
          this.$modal.closeLoading();
          this.$modal.msgError(res.msg);
        }
      }).catch(e=>{
        this.$modal.closeLoading();
      })
    },
    /** 审批过程中流程表单数据提交 */
    submitVariable(data) {
      if (data) {
        this.taskForm.variables = data.valData;
        this.taskForm.variables.variables = data.formData;
      }
    },
    /** 委派任务 */
    handleDelegate() {
      this.taskForm.delegateTaskShow = true;
      this.taskForm.defaultTaskShow = false;
    },
    /** 转办任务弹窗 */
    handleAssign() {
      //触发表单提交
      this.$refs.variableParser.submitForm();
      this.transferOpen = true;
      this.transferTitle = "转办流程";
      this.getTreeselect();
    },
    /** 转办任务处理 */
    taskTransfer(){
      if (!this.taskForm.comment) {
        this.$modal.msgError("请输入转办意见");
        return;
      }
      this.$modal.loading('请稍等处理中...');
      transferTask(this.taskForm).then(res => {
        this.$modal.closeLoading();
        if(res.code == 200){
          this.$modal.msgSuccess(res.msg);
          this.goBack();
        }else{
          this.$modal.msgError(res.msg);
        }
      }).catch(e=>{
        this.$modal.closeLoading();
      })
    },
    /** 返回页面 */
    goBack() {
      // 关闭当前标签页并返回上个页面
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1)
    },
    /** 接收子组件传的值 */
    getData(data) {
      if (data) {
        const variables = [];
        data.fields.forEach(item => {
          let variableData = {};
          variableData.label = item.__config__.label
          // 表单值为多个选项时
          if (item.__config__.defaultValue instanceof Array) {
            const array = [];
            item.__config__.defaultValue.forEach(val => {
              array.push(val)
            })
            variableData.val = array;
          } else {
            variableData.val = item.__config__.defaultValue
          }
          variables.push(variableData)
        })
        this.variables = variables;
      }
    },
    /** 申请流程表单数据提交 */
    submitForm(data) {
      if (data) {
        this.$prompt('请输入流程标题', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({ value }) => {
          this.$message({
            type: 'success',
            message: '设置的流程标题为:' + value
          });

          const variables = data.valData;
          const formData = data.formData;
          if (this.taskForm.procDefId) {
            variables.variables = formData;
            //设置一个流程标题
            variables.processTitle=value;
            // 启动流程并将表单数据加入流程变量
            definitionStart(this.taskForm.procDefId,JSON.stringify(variables)).then(res => {
              this.$modal.msgSuccess(res.msg);
              formData.disabled = true;
              formData.formBtns = false;
              this.goBack();
            })
          }
        }).catch(() => {});
      }
    },
    /** 驳回任务 */
    handleReject() {
      this.rejectOpen = true;
      this.rejectTitle = "驳回流程";
    },
    /** 驳回任务 */
    taskReject() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          rejectTask(this.taskForm).then(res => {
            this.$modal.msgSuccess(res.msg);
            this.goBack();
          });
        }
      });
    },
    /** 可退回任务列表 */
    handleReturn() {
      this.returnOpen = true;
      this.returnTitle = "退回流程";
      returnList(this.taskForm).then(res => {
        this.returnTaskList = res.data;
        this.taskForm.values = null;
      })
    },
    /** 提交退回任务 */
    taskReturn() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          returnTask(this.taskForm).then(res => {
            this.$modal.msgSuccess(res.msg);
            this.goBack()
          });
        }
      });
    },
    /** 取消回退任务按钮 */
    cancelTask() {
      this.taskForm.returnTaskShow = false;
      this.taskForm.defaultTaskShow = true;
      this.taskForm.sendUserShow = true;
      this.returnTaskList = [];
    },
    /** 委派任务 */
    submitDeleteTask() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          delegate(this.taskForm).then(response => {
            this.$modal.msgSuccess(response.msg);
            this.goBack();
          });
        }
      });
    },
    /** 取消回退任务按钮 */
    cancelDelegateTask() {
      this.taskForm.delegateTaskShow = false;
      this.taskForm.defaultTaskShow = true;
      this.taskForm.sendUserShow = true;
      this.returnTaskList = [];
    },
  }
};
</script>
<style lang="scss" scoped>
.el-dialog-div{
  max-height: 90vh;//如果高度过高，可用max-height
  overflow: auto;
}

.key-form {
  width: 100%;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
}

.el-tag+.el-tag {
  margin-left: 10px;
}

.my-label {
  background: #E1F3D8;
}

.text {
  font-size: 15px;
  color: red;
}

.item {
  margin-bottom: 10px;
}
.t-box-card {
  width: 50%;
}
.t-download{
  margin-left: 50%;
}
//禁用状态保持表单样式
::v-deep .el-input.is-disabled .el-input__inner{
  color: #000;
}
::v-deep .el-textarea.is-disabled .el-textarea__inner{
  color: #000;
}

</style>
