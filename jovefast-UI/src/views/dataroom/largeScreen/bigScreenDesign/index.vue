<!-- 大屏管理页页面 -->
<template>
    <!-- code为大屏设计时的编码，可以携带到本页面路由中获取 -->
    <BigScreenDesign ref="BigScreenDesign" :header-show="headerShow" :code="code" />
</template>
<script>
import { BigScreenDesign } from "@gcpaas/data-room-ui";
export default {
    components: {
        BigScreenDesign,
    },
    data() {
        return {
            // 是否展示头部，可隐藏后自己写头部
            headerShow: true,
            // 当头部隐藏后，用户自己设置的头部高度不定，我们可自定义此时下方的设计器高度
            height: '100vh'
        };
    },
    computed: {
        code() {
            return this.$route.query.code;
        },
    },
    methods: {
        // 保存并预览
        saveAndPreview() {
            this.$refs.BigScreenDesign.saveAndPreview();
        },
        // 保存
        save() {
            this.$refs.BigScreenDesign.save();
        },
        // 清空
        empty() {
            this.$refs.BigScreenDesign.empty();
        },
    },
};
</script>