<template>
    <div>
        <el-form ref="taskForm" :model="taskForm" label-width="80px">
        <el-form-item label="退回节点" prop="targetKey">
          <el-radio-group v-model="taskForm.targetKey">
            <el-radio-button v-for="item in returnTaskList" :key="item.id" :label="item.id">{{ item.name }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="退回意见" prop="comment" :rules="[{ required: true, message: '请输入意见', trigger: 'blur' }]">
          <el-input style="width: 80%" type="textarea" v-model="taskForm.comment" placeholder="请输入意见" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
        <el-button type="primary" @click="taskReturn">{{ $t('common.determine') }}</el-button>
      </span>
    </div>
  </template>
  <script>
  import { returnTask } from "@/api/flowable/todo";
  export default {
    inject: ['parentInstance'],
    props:{
        returnTaskList:{
        type: Array,      //类型
        required: true,   //是否必填
        default: []    //默认值
      },
      taskForm:{
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
        this.parentInstance.returnOpen = false;
      },
      /** 提交退回任务 */
      taskReturn() {
         this.$refs["taskForm"].validate(valid => {
            if (valid) {
                returnTask(this.taskForm).then(res => {
                this.$modal.msgSuccess(res.msg);
                this.parentInstance.goBack()
                });
            }
        });
      },

    }
  };
  </script>
  