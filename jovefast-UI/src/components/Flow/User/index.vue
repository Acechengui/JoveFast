<template>
    <div>
      <el-row :gutter="20">
        <!--部门数据-->
        <el-col :span="4" :xs="24">
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
              node-key="id"
              :default-expand-all="false"
              highlight-current
              @node-click="handleNodeClick"
            />
          </div>
        </el-col>
        <!--用户数据-->
        <el-col :span="16" :xs="24">
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="用户账号" prop="userName">
              <el-input
                v-model="queryParams.userName"
                placeholder="请输入用户账号"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="用户姓名" prop="nickName">
              <el-input
                v-model="queryParams.nickName"
                placeholder="请输入用户姓名"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
            </el-form-item>
          </el-form>
          <el-table v-show="checkType === 'multiple'" ref="dataTable" v-loading="loading" :data="userList" 
          @selection-change="handleMultipleUserSelect"
          :row-key="row => row.userId"
          >
            <el-table-column type="selection" :reserve-selection="true" width="50" align="center" />
            <el-table-column label="用户编号" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
            <el-table-column label="登录账号" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
            <el-table-column label="用户姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
            <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible" :show-overflow-tooltip="true" />
          </el-table>
          <el-table v-show="checkType === 'single'" v-loading="loading" :data="userList" @current-change="handleSingleUserSelect">
            <el-table-column  width="55" align="center" >
              <template slot-scope="scope">
                <el-radio v-model="radioSelected" :label="scope.row.userId">{{''}}</el-radio>
              </template>
            </el-table-column>
            <el-table-column label="用户编号" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
            <el-table-column label="登录账号" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
            <el-table-column label="用户姓名" align="center" key="nickName" prop="nickName" v-if="columns[2].visible" :show-overflow-tooltip="true" />
            <el-table-column label="部门" align="center" key="deptName" prop="dept.deptName" v-if="columns[3].visible" :show-overflow-tooltip="true" />
          </el-table>
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
            />
        </el-col>
        <el-col :span="4" :xs="24" v-show="checkType === 'multiple'">
            <h6>已选人员</h6>
            <el-divider></el-divider>
            <el-tag v-for="(user,index) in userData" :key="index" closable @close="handleClose(user)">
              {{user.nickName}}
            </el-tag>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import { listUser, deptTreeSelect } from "@/api/system/user";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {StrUtil} from "@/utils/StrUtil";
  
  export default {
    name: "FlowUser",
    dicts: ['sys_normal_disable', 'sys_user_sex'],
    components: { Treeselect },
    // 接受父组件的值
    props: {
      // 回显数据传值
      selectValues: {
        type: Number | String | Array,
        default: null,
        required: false
      },
      // 表格类型
      checkType: {
        type: String,
        default: 'multiple',
        required: true
      },
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
        // 用户表格数据
        userList: [],
        // 已选用户数据
        userData: [],
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        deptName: undefined,
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 5,
          userName: undefined,
          nickName: undefined,
          status: undefined,
          deptId: undefined
        },
        // 列信息
        columns: [
          { key: 0, label: `用户编号`, visible: true },
          { key: 1, label: `用户名称`, visible: true },
          { key: 2, label: `用户昵称`, visible: true },
          { key: 3, label: `部门`, visible: true },
          { key: 4, label: `手机号码`, visible: true },
          { key: 5, label: `状态`, visible: true },
          { key: 6, label: `创建时间`, visible: true }
        ],
        radioSelected: null, // 单选框传值
        selectUserList: [] // 回显数据传值
      };
    },
    watch: {
      // 根据名称筛选部门树
      deptName(val) {
        this.$refs.tree.filter(val);
      },
      selectValues: {
        handler(newVal) {
          if (StrUtil.isNotBlank(newVal)) {
            if (newVal instanceof Number) {
              this.radioSelected = newVal
            } else {
              this.selectUserList = newVal;
            }
          }
        },
        immediate: true
      },
      // userList: {
      //   handler(newVal) {
      //     if (StrUtil.isNotBlank(newVal)  && this.selectUserList.length > 0) {
      //       this.$nextTick(() => {
      //         this.$refs.dataTable.clearSelection(); // 清除已选择的行
      //         this.selectUserList?.split(',').forEach(key => {
      //           this.$refs.dataTable.toggleRowSelection(newVal.find(
      //             item => key == item.userId
      //           ), true)
      //         });
      //       });
      //     }
      //   },
      //   immediate: true, // 立即生效
      //   deep: true  //监听对象或数组的时候，要用到深度监听
      // }
    },
    mounted() {
      this.getList();
      this.getDeptTree();
    },
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listUser(this.queryParams).then(response => {
            this.userList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询部门下拉树结构 */
      getDeptTree() {
        deptTreeSelect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 多选框选中数据
      handleMultipleUserSelect(selection) {
        if (selection.length > 0) {
          this.userData = selection;
          this.$emit('handleUserSelect', selection);
        }
      },
      // 单选框选中数据
      handleSingleUserSelect(selection) {
        //点击当前行时,radio同样有选中效果
        this.radioSelected = selection.userId;
        this.$emit('handleUserSelect', selection);
      },
      // 关闭标签
      handleClose(tag) {
        this.userData.splice(this.userData.indexOf(tag), 1);
        this.$refs.dataTable.toggleRowSelection(tag, false)
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.handleQuery();
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.queryParams.deptId = undefined;
        this.$refs.tree.setCurrentKey(null);
        this.handleQuery();
      },
    }
  };
  </script>
  <style>
  </style>
  