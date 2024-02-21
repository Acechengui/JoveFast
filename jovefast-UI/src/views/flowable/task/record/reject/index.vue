<template>
    <div>
        <el-form ref="taskForm" :model="taskForm" label-width="80px">
            <el-form-item label="驳回意见" prop="comment" :rules="[{ required: true, message: '请输入意见', trigger: 'blur' }]">
                <el-input style="width: 80%" type="textarea" v-model="taskForm.comment" placeholder="请输入意见" />
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
            <el-button type="primary" @click="taskReject">{{ $t('common.determine') }}</el-button>
        </span>
    </div>
</template>
<script>
import { rejectTask } from "@/api/flowable/todo";
export default {
    inject: ['parentInstance'],
    props: {
        taskForm: {
            type: Object,
            required: true,
            default: {}
        }
    },
    data() {
        return {

        };
    },
    methods: {
        /** 取消 */
        cancel() {
            this.taskForm.comment = undefined;
            this.parentInstance.rejectOpen = false;
        },
        /** 驳回任务 */
        taskReject() {
            this.$refs["taskForm"].validate(valid => {
                if (valid) {
                    rejectTask(this.taskForm).then(res => {
                        this.$modal.msgSuccess(res.msg);
                        this.parentInstance.goBack();
                    });
                }
            });
        },

    }
};
</script>
  