<template>
  <div>
    <el-form ref="taskForm" :model="taskForm" label-width="80px">
      <el-form-item v-if="checkSendUser" prop="targetKey">
        <el-row :gutter="20">
          <!--部门数据-->
          <el-col :span="6" :xs="24">
            <el-tag type="warning"
              >提示:默认不需要指定审批人</el-tag
            >
            <h6>展开部门列表选择</h6>
            <div class="head-container">
              <el-input
                v-model="deptName"
                placeholder="请输入部门名称"
                clearable
                size="small"
                prefix-icon="el-icon-search"
                style="margin-bottom: 20px"
              />
            </div>
            <div class="head-container">
              <el-tree
                :data="deptOptions"
                :props="defaultProps"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="tree"
                highlight-current
                @node-click="handleNodeClick"
              />
            </div>
          </el-col>
          <el-col :span="10" :xs="24">
            <el-form
              :model="queryParams"
              ref="queryForm"
              size="small"
              :inline="true"
            >
              <el-form-item label="用户名称" prop="nickName">
                <el-input
                  v-model="queryParams.nickName"
                  placeholder="请输入用户名称"
                  clearable
                  @keyup.enter.native="handleQuery"
                  style="width: 240px"
                />
              </el-form-item>
               <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                  >{{ $t("common.search") }}</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
              </el-form-item>
            </el-form>

            <h6>待选人员</h6>
            <el-table
              ref="dataTable"
              :data="userList"
              border
              style="width: 100%"
              @selection-change="handleSelectionChange"
              v-horizontal-scroll="'always'"
            >
              <el-table-column type="selection" width="50" align="center" />
              <el-table-column label="用户名" align="center" prop="nickName" />
              <el-table-column
                label="部门"
                align="center"
                prop="dept.deptName"
              />
            </el-table>
            <pagination
              v-show="total > 0"
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              layout="sizes, prev, pager, next"
              :pager-count="3"
              small
              @pagination="checkSendUser ? getList : []"
            />
          </el-col>
          <el-col :span="8" :xs="24">
            <h6>已选人员</h6>
            <el-tag
              v-for="(user, index) in userData"
              :key="index"
              closable
              @close="handleClose(user)"
            >
              {{ user.nickName }}
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
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="处理意见"
        prop="comment"
        :rules="[
          { required: true, message: '请输入处理意见', trigger: 'blur' },
        ]"
      >
        <el-input
          type="textarea"
          v-model="taskForm.comment"
          placeholder="请输入处理意见"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t("common.cancel") }}</el-button>
        <el-button type="primary" @click="taskComplete">{{
            $t("common.determine")
        }}</el-button>
    </div>
  </div>
</template>
<script>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Treeselect from "@riophae/vue-treeselect";
import { complete } from "@/api/flowable/todo";
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
      //是否同意快捷选项
      commentOptions: [
        {
          value: "同意",
          label: "同意",
        }
      ],
      // 部门名称
      deptName: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      },
      commentVar: undefined,
      // 用户表格数据
      userList: null,
      // 已选用户
      userData: [],
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
      this.$refs.dataTable.toggleRowSelection(tag, false)
    },
    //下拉选择监听
    bclxChange(selectValue) {
        this.taskForm.comment = selectValue;
    },
    /** 取消 */
    cancel() {
      this.taskForm.comment = undefined;
      this.parentInstance.completeOpen = false;
    },
    /** 审批任务 */
    taskComplete() {
      if (!this.taskForm.comment) {
        this.$modal.msgError("请输入审批意见");
        return;
      }
      this.$modal.loading("请稍等处理中...");
      this.taskForm.variables=this.parentInstance.$refs.variableParserFormBuild.getData();
      complete(this.taskForm)
        .then((res) => {
          if (res.code == 200) {
            this.$modal.closeLoading();
            this.$modal.msgSuccess(res.msg);
            this.parentInstance.goBack();
          } else {
            this.$modal.closeLoading();
            this.$modal.msgError(res.msg);
          }
        })
        .catch((e) => {
          this.$modal.closeLoading();
        });
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
