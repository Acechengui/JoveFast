<!-- 
<template>
    <div>
      <DashboardDesign ref="DashboardDesign" :header-show="headerShow" :code="code" />
    </div>
  </template>
  <script>
    import { DashboardDesign } from "@gcpaas/dash-board-ui";
    export default {
      components: {
        DashboardDesign,
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
          this.$refs.DashboardDesign.saveAndPreview();
        },
        // 保存
        save() {
          this.$refs.DashboardDesign.save();
        },
        // 清空
        empty() {
          this.$refs.DashboardDesign.empty();
        },
      },
    };
  </script>
-->