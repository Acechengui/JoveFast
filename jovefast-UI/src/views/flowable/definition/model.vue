<template>
  <div>
    <bpmn-modeler
      ref="refNode"
      :xml="xml"
      :users="users"
      :groups="groups"
      :forms="forms"
      :categorys="categorys"
      :is-view="false"
      @save="save"
      @showXML="showXML"
      @dataType="dataType"
    />
    <!--在线查看xml-->
    <el-dialog :title="xmlTitle" :visible.sync="xmlOpen" width="60%" append-to-body>
      <div>
        <pre v-highlight>
           <code class="xml">
                {{xmlContent}}
           </code>
        </pre>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {readXml, saveXml} from "@/api/flowable/definition";
import {listUserAll} from "@/api/system/user";
import {listRoleAll} from "@/api/system/role";
import {listFormAll} from "@/api/flowable/form";
import bpmnModeler from '@/components/Process/index'
import vkbeautify from 'vkbeautify'
import Hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-dark.css'

export default {
  name: "Model",
  components: {
    bpmnModeler,
    vkbeautify
  },
  // 自定义指令
  directives: {
    highlight:(el) => {
      let blocks = el.querySelectorAll('pre code');
      blocks.forEach((block) => {
        Hljs.highlightBlock(block)
      })
    }
  },
  data() {
    return {
      xml: "", // 后端查询到的xml
      modeler:"",
      xmlOpen: false,
      xmlTitle: '',
      xmlContent: '',
      users: [],
      groups: [],
      forms: [],
      categorys: []

    };
  },
  created () {
    const deployId = this.$route.query && this.$route.query.deployId;
    //  查询流程xml
    if (deployId) {
      this.getModelDetail(deployId);
    }
    this.getDicts("sys_process_category").then(res => {
      this.categorys = res.data;
    });
    this.getDataList()
  },
  methods: {
    /** xml 文件 */
    getModelDetail(deployId) {
      // 发送请求，获取xml
      readXml(deployId).then(res =>{
        this.xml = res.data;
        this.modeler = res.data
      })
    },
    /** 保存xml */
    save(data) {
      const params = {
        name: data.process.name,
        category: data.process.category,
        xml: data.xml
      }
      saveXml(params).then(res => {
        this.$message(res.msg)
        // 关闭当前标签页
        this.$store.dispatch("tagsView/delView", this.$route);
        this.$router.go(-1)
      })
    },
    /** 指定流程办理人员列表 */
    getDataList() {
      listUserAll().then(res =>{
        this.users = res.rows;
        let arr = {nickName: "流程发起人", userId: "${INITIATOR}"}
        this.users.push(arr)
      });
      listRoleAll().then(res =>{
        this.groups = res.rows;
      });
      listFormAll().then(res =>{
        this.forms = res.data;
      });
      
    },
    /** 展示xml */
    showXML(data){
      this.xmlTitle = 'xml查看';
      this.xmlOpen = true;
      this.xmlContent = vkbeautify.xml(data);
    },
    /** 获取数据类型 */
    dataType(data){
      this.users = [];
      this.groups = [];
      if (data) {
        if (data.dataType === 'dynamic') {
          if (data.userType === 'assignee') {
            this.users = [{nickName: "流程发起人", userId: "${INITIATOR}"},
                          {nickName: "${approval}", userId: "${approval}"}
              ]
          } else if (data.userType === 'candidateUsers') {
            this.users = [{nickName: "流程发起人", userId: "${INITIATOR}"},
                          {nickName: "${approval}", userId: "${approval}"}
              ]
          } else {
            this.groups = [{roleName: "${approval}", roleId: "${approval}"}]
          }
        } 
        else {
          //created已加载过一次
          this.getDataList()
        }
      }
    }
  },
};
</script>
