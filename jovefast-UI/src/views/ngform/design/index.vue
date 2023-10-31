<template>
    <div>
        <ng-form-design ref="formDesign" :config="formConfig" :custom-components="customComponents">
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
//自定义
import BackgroundImageComponent from '../customComponents/backgroundImage/index.vue'
import BackgroundImagePropertie from '../customComponents/backgroundImage/properties.vue'
export default {
    name: "ngformDesign",
    components: {
		BackgroundImageComponent,BackgroundImagePropertie
	},
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
            },
            customComponents: [
                /**
                 {
                    type: '类型', // 唯一，不能和已有组件冲突
                    label: '组件名称', // 唯一，不能和已有组件冲突
                    component: 组件实际的渲染文件 ,// .vue
                    properties: 组件的属性配置面板 , // .vue
                    icon: 组件显示的图标 // base64
                    ..... // 其他配置项
                } */ 

				{
					type: 'ngImage',
					label: '自定义组件',
					component: BackgroundImageComponent,
					properties: BackgroundImagePropertie,
                    options:{
                        style: null,
                        imgurl: null
                    },
                    icon:'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDgiIGhlaWdodD0iNDgiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHJlY3QgeD0iNS44IiB5PSIxMC44IiB3aWR0aD0iMzYuNCIgaGVpZ2h0PSIyNi40IiByeD0iMy4yIiBmaWxsPSIjZmZmIiBzdHJva2U9IiM3NTc1NzUiIHN0cm9rZS13aWR0aD0iMS42Ii8+PGNpcmNsZSBjeD0iMTMuNSIgY3k9IjE4LjUiIHI9IjMuNSIgZmlsbD0iI0VFQ0E4NiIvPjxwYXRoIGQ9Ik0yNy45MjMgMTguMzY2YTEgMSAwIDAgMSAxLjY5Ni0uMDE4bDguMzk1IDEzLjExM0ExIDEgMCAwIDEgMzcuMTcyIDMzSDIwLjc4MWExIDEgMCAwIDEtLjg1NC0xLjUybDcuOTk2LTEzLjExNFoiIGZpbGw9IiM4MkJGOTkiLz48cGF0aCBkPSJNMTYuNjc2IDI2LjE5OWExIDEgMCAwIDEgMS42NDggMGwzLjU5OSA1LjIzNEExIDEgMCAwIDEgMjEuMDk5IDMzSDEzLjlhMSAxIDAgMCAxLS44MjQtMS41NjdsMy41OTktNS4yMzRaIiBmaWxsPSIjODJCRjk5Ii8+PC9zdmc+Cg==',
				}
			]

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
        }
    }
    
}
</script>