<template>
    <div>
        <el-form ref="taskForm" :model="taskForm" :rules="rules" label-width="80px">
            <el-form-item prop="targetKey">
                <el-row :gutter="20">
                    <!--部门数据-->
                    <el-col :span="6" :xs="24">
                        <h6>展开部门列表选择</h6>
                        <div class="head-container">
                            <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small"
                                prefix-icon="el-icon-search" style="margin-bottom: 20px" />
                        </div>
                        <div class="head-container">
                            <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false"
                                :filter-node-method="filterNode" ref="tree" highlight-current
                                @node-click="handleNodeClick" />
                        </div>
                    </el-col>
                    <el-col :span="10" :xs="24">
                        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
                            <el-form-item label="用户名称" prop="nickName">
                                <el-input v-model="queryParams.nickName" placeholder="请输入用户名称" clearable
                                    @keyup.enter.native="handleQuery" style="width: 240px" />
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{
                                    $t("common.search") }}</el-button>
                                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset')
                                }}</el-button>
                            </el-form-item>
                        </el-form>

                        <h6>待选人员</h6>
                        <el-table ref="dataTable" :data="userList" border style="width: 100%"
                            @selection-change="handleSelectionChange" v-horizontal-scroll="'always'">
                            <el-table-column type="selection" width="50" align="center" />
                            <el-table-column label="用户名" align="center" prop="nickName" />
                            <el-table-column label="部门" align="center" prop="dept.deptName" />
                        </el-table>
                        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                            :limit.sync="queryParams.pageSize" layout="sizes, prev, pager, next" :pager-count="3" small
                            @pagination="getList" />
                    </el-col>
                    <el-col :span="8" :xs="24">
                        <h6>已选人员</h6>
                        <el-tag v-for="(user, index) in userData" :key="index" closable @close="handleClose(user)">
                            {{ user.nickName }}
                        </el-tag>
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item label="抄送说明" prop="comment" :rules="[
                { required: true, message: '请输入抄送说明', trigger: 'blur' },
            ]">
                <el-input type="textarea" v-model="taskForm.comment" placeholder="请输入抄送说明" />
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="cancel">{{ $t("common.cancel") }}</el-button>
            <el-button type="primary" @click="taskCourtesyCopy">{{
                $t("common.determine")
            }}</el-button>
        </div>
    </div>
</template>
<script>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Treeselect from "@riophae/vue-treeselect";
import { courtesyCopy } from "@/api/flowable/todo";
import { listUser } from "@/api/system/user";
export default {
    components: {
        Treeselect,
    },
    inject: ['parentInstance'],
    props: {
        deptOptions: {
            type: Array,
            required: false,
            default: []
        },
        taskForm: {
            type: Object,
            required: true,
            default: {}
        }
    },
    data() {
        return {
            // 部门名称
            deptName: undefined,
            defaultProps: {
                children: "children",
                label: "label"
            },
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
                    this.taskForm.userId = selectVal.join(',')
                } else {
                    this.taskForm.userId = selectVal
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
            this.userData=[];
            this.taskForm.comment=undefined;
            this.parentInstance.courtesyCopyOpen = false;
        },
        /** 抄送任务 */
        taskCourtesyCopy() {
            if (this.userData.length === 0) {
                this.$modal.msgError("请选择你要抄送的人员");
                return;
            }
            this.$modal.loading("请稍等处理中...");
            courtesyCopy(this.taskForm)
                .then((res) => {
                    if (res.code == 200) {
                        this.$modal.closeLoading();
                        this.$modal.msgSuccess(res.msg);
                        this.taskForm.comment=undefined;
                        this.parentInstance.courtesyCopyOpen = false;
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
  