<template>
  <i-frame :src="openUrl" />
</template>
 
<script>
import { getToken } from '@/utils/auth'
import iFrame from "@/components/iFrame/index";
 
export default {
  name: 'jimuview',
  components: { iFrame },
  props: {
    reportID: {
      type: [String],
      required: false,
      default: ''
    },
  },
  data() {
    return {
      openUrl: '',
    }
  },
  created() {
    if(this.reportID.length != 0){
      this.openUrl = process.env.VUE_APP_BASE_API + '/jove-report/jmreport/view/' + this.reportID + '?token=' +  getToken()
    }else{
      this.openUrl = process.env.VUE_APP_BASE_API + '/jove-report/jmreport/view/' + this.$route.path.substring(this.$route.path.lastIndexOf("/")+1) + '?token=' +  getToken()
    }
    
  }
}
</script>
 
<style scoped>
 
</style>






