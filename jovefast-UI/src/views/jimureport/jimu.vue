<template>
  <i-frame :src="openUrl" id="jimuReportFrame"></i-frame>
</template>
<script>
  import { getToken } from '@/utils/auth'
  import iFrame from '@/components/iFrame/index'
  export default {
    name: "Jimu",
    components: {iFrame},
    data() {
      return {
        openUrl: process.env.VUE_APP_BASE_API + "/jove-report/jmreport/list?token="+getToken(),//（部署到服务器上用这个地址）
      };
    },
    mounted: function() {

    }
  };
</script>

