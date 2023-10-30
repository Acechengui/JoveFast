<template>
    <div>
        <ng-form-design ref="formDesign" :config="formConfig" > 
        </ng-form-design>
    </div>
</template>
<script>
export default {
    name: "ngformDesign",
    data() {
        return {
            formConfig: {
                // 数据字典配置
                // 此处可先后台获取需要的字典后配置
                dict: [
                    { type: 'sex', label: '男', value: '1' },
                    { type: 'sex', label: '女', value: '2' },
                    { type: 'yes_or_no', label: '是', value: '1' },
                    { type: 'yes_or_no', label: '否', value: '2' },
                    { type: 'nation', label: '汉族', value: '1' },
                    { type: 'nation', label: '蒙古族', value: '2' },
                    { type: 'nation', label: '藏族', value: '3' },
                    { type: 'nation', label: '壮族', value: '4' }
                ]
            }
        }
    }
}
