<template>
  <div class="app-container">
    <div slot="header" class="clearfix" style="padding-bottom: 10px;">
      <span class="el-icon-document">基础信息</span>
      <el-button style="float: right;" icon="el-icon-arrow-left" type="primary" size="mini" @click="goBack">返回</el-button>
      <el-button style="float: right; margin-right: 10px;" icon="el-icon-video-camera" type="warning" size="mini"
        v-print="printOption">全页打印</el-button>
      <el-button style="float: right; margin-right: 10px;" icon="el-icon-picture-outline" type="primary" size="mini"
        @click="flowChart">流程图</el-button>
    </div>
    <div id="nbprint">
      <!--流程处理表单模块-->
      <el-col :span="24" v-if="variableOpen">
        <div>
          <ng-form-build ref="variableParserFormBuild" :models="models" :disabled="formPreview"
            :formTemplate="formTemplate" :config="formBuildConfig" />
        </div>
        <div style="margin-left:10%;margin-bottom: 20px;font-size: 14px;" v-if="finished === 'false'">
          <el-button icon="el-icon-edit-outline" type="success" size="mini" @click="handleComplete">审批</el-button>
          <el-button icon="el-icon-edit-outline" type="primary" size="mini" @click="handleTransfer">转办</el-button>
          <el-button icon="el-icon-thumb" type="info" size="mini" @click="handleCourtesycopy">抄送</el-button>
          <el-button icon="el-icon-refresh-left" type="warning" size="mini" @click="handleReturn">退回到任意上一步</el-button>
          <el-button icon="el-icon-circle-close" type="danger" size="mini" @click="handleReject">驳回</el-button>
        </div>
      </el-col>

      <!--初始化流程加载表单信息-->
      <el-col :span="24" v-if="formConfOpen">
        <div class="key-form">
          <ng-form-build ref="parserFormBuild" :disabled="false" :formTemplate="formConf" :config="formBuildConfig" />
          <div style="text-align: center;">
            <el-button type="primary" size="medium" icon="el-icon-success" @click="initSubmitForm()">提交</el-button>
            <el-button type="warning" size="medium" icon="el-icon-remove" @click="resetSubmitForm()">重置</el-button>
          </div>
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
              <el-timeline-item v-for="(item, index ) in flowRecordList" :key="index" :icon="setIcon(item.finishTime)"
                :color="setColor(item.finishTime)">
                <p style="font-weight: 700">{{ item.taskName }}</p>
                <el-card :body-style="{ padding: '10px' }">
                  <el-descriptions class="margin-top" :column="1" size="small" border>
                    <el-descriptions-item v-if="item.assigneeName" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-user"></i>实际办理</template>
                      {{ item.assigneeName }}
                      <el-tag type="info" size="mini" v-if="item.deptName">{{ item.deptName }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.candidate" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-user"></i>候选办理</template>
                      {{ item.candidate }}
                    </el-descriptions-item>
                    <el-descriptions-item label-class-name="my-label">
                      <template slot="label"><i class="el-icon-date"></i>接收时间</template>
                      {{ item.createTime }}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.finishTime" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-date"></i>处理时间</template>
                      {{ item.finishTime }}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.duration" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-time"></i>耗时</template>
                      {{ item.duration }}
                    </el-descriptions-item>
                    <el-descriptions-item v-if="item.comment.comment" label-class-name="my-label">
                      <template slot="label"><i class="el-icon-tickets"></i>处理意见</template>
                      {{ item.comment.comment }}
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

    <!--审批流程-->
    <el-dialog :title="completeTitle" :visible.sync="completeOpen" append-to-body>
      <CompleteTask :deptOptions="deptOptions" :checkSendUser="checkSendUser"
        :taskForm="taskForm"></CompleteTask>
    </el-dialog>

    <!--转办流程-->
    <el-dialog :title="transferTitle" :visible.sync="transferOpen" append-to-body>
      <TransferTask :deptOptions="deptOptions" :checkSendUser="checkSendUser"
        :taskForm="taskForm"></TransferTask>
    </el-dialog>

    <!--抄送流程-->
    <el-dialog :title="courtesyCopyTitle" :visible.sync="courtesyCopyOpen" append-to-body>
      <CourtesycopyTask :deptOptions="deptOptions" :taskForm="taskForm"></CourtesycopyTask>
    </el-dialog>

    <!--退回流程-->
    <el-dialog :title="returnTitle" :visible.sync="returnOpen" append-to-body>
      <ReturnTask :returnTaskList="returnTaskList" :taskForm="taskForm">
      </ReturnTask>
    </el-dialog>

    <!--驳回流程-->
    <el-dialog :title="rejectTitle" :visible.sync="rejectOpen" append-to-body>
      <RejectTask  :taskForm="taskForm"></RejectTask>
    </el-dialog>

    <!-- 返回顶部 -->
    <GoTop></GoTop>
  </div>
</template>

<script>
import GoTop from "@/components/GoTop/index";
import { flowRecord } from "@/api/flowable/finished";
import { definitionStart, getProcessVariables, readXml, getFlowViewer } from "@/api/flowable/definition";
import { returnList, getNextFlowNode } from "@/api/flowable/todo";
import flow from '@/views/flowable/task/record/flow'
import { getToken } from '@/utils/auth'
import { getChangeForm, getForm } from "@/api/flowable/form";
import { deptTreeSelect } from "@/api/system/user";
import CompleteTask from "@/views/flowable/task/record/complete/index";
import TransferTask from "@/views/flowable/task/record/transfer/index";
import ReturnTask from "@/views/flowable/task/record/return/index";
import RejectTask from "@/views/flowable/task/record/reject/index";
import CourtesycopyTask from "@/views/flowable/task/record/courtesycopy/index";

export default {
  name: "Record",
  components: {
    flow,
    GoTop,
    CompleteTask,
    TransferTask,
    ReturnTask,
    RejectTask,
    CourtesycopyTask
  },
  data() {
    return {
      printOption: {
        id: 'nbprint', // 打印元素的id 不需要携带#号
        preview: false, // 开启打印预览
        previewTitle: '打印预览', // 打印预览标题
        popTitle: '流程信息', // 页眉标题 默认浏览器标题 空字符串时显示undefined 使用html语言
        previewBeforeOpenCallback: () => {
          //console.log("触发打印预览打开前回调");
        },
        previewOpenCallback: () => {
          //console.log("触发打开打印预览回调");
        },
        beforeOpenCallback: () => {
          //console.log("触发打印工具打开前回调");
        },
        openCallback: () => {
          //console.log("触发打开打印工具回调");
        },
        closeCallback: () => {
          //console.log("触发关闭打印工具回调");
        },
        clickMounted: () => {
          //console.log("触发点击打印回调");
        }
      },
      //部门树
      deptOptions: [],
      // 模型xml数据
      xmlData: "",
      taskList: [],
      // 用户表格数据
      userList: null,
      //流程标题
      processTitle: undefined,
      // 查询参数
      queryParams: {
        deptId: undefined,
        nickName: undefined,
        pageNum: 1,
        pageSize: 10,
      },
      //是否显示流程图
      flowChartOpen: false,
      flowRecordList: [], // 流程流转数据
      taskForm: {
        multiple: false,
        comment: undefined, // 意见内容
        procInsId: undefined, // 流程实例编号
        instanceId: undefined, // 流程实例编号
        deployId: undefined,  // 流程定义编号
        taskId: undefined,// 流程任务编号
        procDefId: undefined,  // 流程编号
        targetKey: ""
      },
      // 流程候选人
      userDataList: [],
      assignee: null,
      // 默认表单数据
      formConf: {},
      // 是否加载默认表单数据
      formConfOpen: false,
      // 是否加载流程变量数据
      variableOpen: false,
      // 回退列表数据
      returnTaskList: [],
      finished: 'false',
      completeTitle: null,
      completeOpen: false,
      returnTitle: null,
      returnOpen: false,
      rejectOpen: false,
      rejectTitle: null,
      transferOpen: false,
      transferTitle: null,
      courtesyCopyTitle: null,
      courtesyCopyOpen: false,
      userData: [],
      // 是否展示选择人员模块
      checkSendUser: false,
      //表单配置
      formId: null,
      formTemplate: null,
      //是否只能预览
      formPreview: false,
      models: [],
      formBuildConfig: {
        httpConfig: (config) => {
          config.headers['Authorization'] = 'Bearer ' + getToken()
          return config
        }
      },

    };
  },
  created() {
    this.$modal.loading("正在加载数据中，请稍候...");
    this.taskForm.deployId = this.$route.query && this.$route.query.deployId;
    this.taskForm.taskId = this.$route.query && this.$route.query.taskId;
    this.taskForm.procInsId = this.$route.query && this.$route.query.procInsId;
    this.taskForm.executionId = this.$route.query && this.$route.query.executionId;
    this.taskForm.instanceId = this.$route.query && this.$route.query.procInsId;
    this.formPreview = this.$route.query && this.$route.query.preview;
    // 初始化表单
    this.taskForm.procDefId = this.$route.query && this.$route.query.procDefId;
    // 获取传递的表单ID
    this.formId = this.$route.query && this.$route.query.formId;
    if (this.formId) {
      // 获取表单模板数据
      this.getFormTemplate(this.taskForm.procInsId, this.taskForm.taskId)
    }
    // 流程任务重获取变量表单
    if (this.taskForm.taskId) {
      this.processVariables(this.taskForm.taskId)
      this.getNextFlowNodeHandel(this.taskForm.taskId)
      this.taskForm.deployId = null
    }
    this.getFlowRecordList(this.taskForm.procInsId, this.taskForm.deployId);
    this.finished = this.$route.query && this.$route.query.finished;
    this.$modal.closeLoading();
  },
  provide() {
    return {
      parentInstance: this
    };
  },
  methods: {
    // 回显流程记录
    flowChart() {
      this.$modal.loading("正在加载数据中，请稍候...");
      this.flowChartOpen = true;
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
    /** 获取表单模板 */
    getFormTemplate(procInsId, taskId) {
      const params = { formId: this.formId, instanceId: procInsId, taskId: taskId };
      if (this.$route.query.finished === 'true') {
        getForm(this.formId).then(resp => {
          this.variableOpen = true;
          this.formTemplate = JSON.parse(resp.data.formContent);
        })
      } else {
         getChangeForm(params).then(resp => {
           this.formPreview = resp.data.formPreview;
           this.variableOpen = true;
           this.formTemplate = JSON.parse(resp.data.formContent);
         })
      }

    },
    /** 获取表单数据 */
    processVariables(taskId) {
      if (taskId) {
        // 提交流程申请时填写的表单存入了流程变量中后续任务处理时需要展示
        getProcessVariables(taskId).then(res => {
          this.$nextTick(() => {
            this.models = res.data;
          })
        });
      }
    },
    /** 根据当前任务获取流程设计配置的下一步节点 */
    getNextFlowNodeHandel(taskId) {
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
    /** 打开审批任务弹窗 */
    handleComplete() {
      this.completeOpen = true;
      this.completeTitle = "审批流程";
      if (this.checkSendUser === true) {
        this.getTreeselect();
      }
    },
    /** 打开转办任务弹窗 */
    handleTransfer() {
      this.transferOpen = true;
      this.transferTitle = "转办流程";
      this.getTreeselect();
    },
    /** 打开退回上一步弹窗 */
    handleReject() {
      this.rejectOpen = true;
      this.rejectTitle = "驳回流程";
    },

    /** 打开驳回到任意上一步弹窗 */
    handleReturn() {
      this.returnOpen = true;
      this.returnTitle = "退回流程";
      returnList(this.taskForm).then(res => {
        this.returnTaskList = res.data;
        this.taskForm.values = null;
      })
    },

    /** 打开抄送弹窗 */
    handleCourtesycopy() {
      this.courtesyCopyOpen = true;
      this.courtesyCopyTitle = "抄送任务";
      this.getTreeselect();
    },

    /** 返回页面 */
    goBack() {
      // 关闭当前标签页并返回上个页面
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.go(-1)
    },
    /** 申请流程表单重置 */
    resetSubmitForm() {
      this.$refs.parserFormBuild.reset();
    },
    /** 申请流程表单数据提交 */
    initSubmitForm() {
      this.$refs.parserFormBuild.getData(false).then((data) => {
        this.$modal.loading("提交中，请稍候...");
        const models = data;
        this.$prompt('请输入流程标题', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(({ value }) => {
          this.processTitle = value;
          this.$message({
            type: 'success',
            message: '设置的流程标题为:' + this.processTitle
          });
          if (this.taskForm.procDefId != null) {
            //设置一个流程标题
            models.processTitle = this.processTitle;
            // 启动流程并将表单数据加入流程变量
            definitionStart(this.taskForm.procDefId, JSON.stringify(models)).then(res => {
              this.$modal.closeLoading();
              this.$modal.msgSuccess(res.msg);
              this.goBack();
            }).catch(e => {
              this.$modal.closeLoading();
            })
          }
        }).catch(() => {
          this.$modal.closeLoading();
          this.$message({
            type: 'info',
            message: '取消操作'
          });
        });

      })

    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      deptTreeSelect().then((response) => {
        this.deptOptions = response.data;
      });
    },
  }
};
</script>
<style lang="scss" scoped>
.el-dialog-div {
  max-height: 90vh; //如果高度过高，可用max-height
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

.t-download {
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
