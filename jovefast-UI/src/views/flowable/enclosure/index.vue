<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="附件名称" prop="hname">
            <el-input v-model="queryParams.hname" placeholder="请输入附件名称" clearable
                :style="{ width: '100%' }"></el-input>
            </el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
                    v-hasPermi="['flowable:enclosure:del']">{{ $t('common.del') }}</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table border v-loading="loading" :data="attachmentList" @selection-change="handleSelectionChange" v-horizontal-scroll="'always'">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column type="index" width="55" align="center" />
            <el-table-column label="ID" align="center" prop="hid" v-if="columns[0].visible"/>
            <el-table-column label="任务ID" align="center" prop="htaskId" v-if="columns[1].visible"/>
            <el-table-column label="流程ID" align="center" prop="hprocinstId" v-if="columns[2].visible"/>
            <el-table-column label="附件版本" align="center" prop="hrev" v-if="columns[3].visible"/>
            <el-table-column label="附件名称" align="center" prop="hname" v-if="columns[4].visible"/>
            <el-table-column label="附件描述" align="center" prop="hdescription" v-if="columns[5].visible"/>
            <el-table-column label="地址" align="center" prop="hurl" v-if="columns[6].visible"/>
            <el-table-column label="时间" align="center" prop="htime" v-if="columns[7].visible"/>
            <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['flowable:enclosure:del']">{{ $t('common.del') }}</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

    </div>
</template>

<script>
import { listAttachment, delAttachment} from "@/api/flowable/enclosure";

export default {
    name: "Enclosure",
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
            // 表格数据
            attachmentList: [],
            // 查询参数
            queryParams: {
                hname:null,
                pageNum: 1,
                pageSize: 10,
            },
            // 列信息
            columns: [
                { key: 0, label: `ID`, visible: true },
                { key: 1, label: `任务ID`, visible: false },
                { key: 2, label: `流程ID`, visible: false },
                { key: 3, label: `附件版本`, visible: true },
                { key: 4, label: `附件名称`, visible: true },
                { key: 5, label: `附件描述`, visible: true },
                { key: 6, label: `地址`, visible: true },
                { key: 7, label: `时间`, visible: true }
            ],
            // 表单校验
            rules: {
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询列表 */
        getList() {
            this.loading = true;
            listAttachment(this.queryParams).then(response => {
                this.attachmentList = response.rows;
                this.total = response.total;
                this.loading = false;
            });
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
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.hid)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const ids = row.hid || this.ids;
            this.$modal.confirm('是否确认删除已选数据项？').then(function () {
                return delAttachment(ids);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
    }
};
</script>
