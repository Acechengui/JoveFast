<template>
    <div>
        <ng-form-design ref="formDesign" :config="formConfig">
            <template slot="controlButton">
                <el-button icon="el-icon-check" type="text" size="medium" @click="handleForm()">保存</el-button>
            </template>
        </ng-form-design>

        <!--表单保存/更新弹窗-->
        <el-dialog :title="formTitle" :visible.sync="formOpen" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="表单名称" prop="formName">
            <el-input v-model="form.formName" placeholder="请输入表单名称" />
            </el-form-item>
            <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" placeholder="请输入备注" />
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">{{ $t('common.determine') }}</el-button>
            <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
        </div>
        </el-dialog>
    </div>
</template>
<script>
import { getToken } from '@/utils/auth'
import { getForm, addForm, updateForm} from "@/api/flowable/form";
export default {
    name: "DynamicFormDesign",
    data() {
        return {
            // 表单参数
            formOpen: false,
            formTitle: null,
            form: {
                formId: null,
                formName: null,
                formContent: null,
                remark: null
            },
            // 表单弹窗校验
            rules: {
                formName: [
                    { required: true, trigger: "blur"}
                ],
            },
            formConfig: {
                formConfig: {
    				httpConfig: (config)=>{ 
				        config.headers['Authorization'] = 'Bearer ' + getToken()
				        return config 
				    }
    			}
            }

        }
    },
    created(){
    	this.init() 
    },
    methods: {
        init() {
            if(this.$route.query.formId){
                getForm(this.$route.query.formId).then(resp =>{
                    this.$nextTick(()=> {
                        // 赋值
                        this.form=resp.data
                        this.$refs.formDesign.initModel(JSON.parse(this.form.formContent))
                    })
                })
            }
            
    	},
        handleForm(){
            this.form.formId=this.$route.query.formId;
            this.formOpen = true;
            this.formTitle = "新增/编辑";
        },
        submitForm() {
            this.form.formContent = JSON.stringify(this.$refs.formDesign.getModel())
            if (this.form.formId != null) {
                updateForm(this.form).then(resp => {
                    this.$modal.msgSuccess("修改成功");
                });
            } else {
                addForm(this.form).then(resp => {
                    this.$modal.msgSuccess("新增成功");
                });
            }
            // 关闭当前标签页并返回上个页面
            this.$store.dispatch("tagsView/delView", this.$route);
            this.$router.go(-1)
        },
        // 取消按钮
        cancel() {
            this.formOpen = false;
        },
    }
    
}
</script>