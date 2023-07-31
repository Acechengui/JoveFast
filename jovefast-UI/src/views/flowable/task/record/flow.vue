<template>
  <div>
    <flow-view :xmlData="xmlData" :taskList="taskData"/>
  </div>
</template>
<script>
import bpmnModeler from '@/components/Process/index'
import FlowView from './flowview'

export default {
  name: "Flow",
  components: {
    bpmnModeler,
    FlowView
  },
  props: {
    xmlData: {
      type: String,
      default: ''
    },
    taskData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {};
  }
};
</script>
