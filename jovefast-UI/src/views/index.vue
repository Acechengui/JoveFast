<template>
  <div class="app-container home">
    
    <el-row :gutter="10">
      <el-col :sm="48" :lg="24">
        <h2>公司简介</h2>
        <p>
           中富电路成立于2004年，注册地址为：深圳市宝安区沙井街道和一社区和二工业区兴业西路8号。公司是一家专业从事印制电路板的研发、生产和销售的国家级高新技术企业。公司自创办以来，长期坚持质量为先、技术为核心、客户需求为导向的发展策略，专注于有优势、有前景的细分电路板市场，取得了多项国家技术专利，研发出多种特殊的产品工艺。

      公司目前有三个生产基地，分别位于深圳市宝安区沙井街道、松岗街道以及鹤山市鹤城镇。公司产品广泛应用于通信、新能源、工业控制、消费电子、汽车电子及医疗电子等应用领域，主要客户为：华为、中兴、Vertiv（维谛）、Sumsung（三星）、Schneider（施耐德）、Asteelflash（飞旭）、LACROIX、LENZE（伦茨）、理邦等国内外知名企业。

      中富电路始终肩负着“以科技和实业利益社会、富强中国”的时代使命，致力于成为全球电子电路制造行业的先进企业。
        </p>
        <p>
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTarget('http://www.jovepcb.com/')"
            >访问官网</el-button
          >
        </p>
      </el-col>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>联系信息</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion"></i> 官网：<el-link
                href="http://www.jovepcb.com"
                target="_blank"
                >http://www.jovepcb.com</el-link
              >
            </p>
            <p>
              <i class="el-icon-chat-dot-round"></i> 开发者：<a
                href="javascript:;"
                >/ JOVEPCB-IT TEAM</a
              >
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>公告通知</span>
          </div>
          <ol>
            <li v-for="(item,index) in noticeList" :key="index" class="noticeTitle" v-html="item.noticeTitle" @click="seeNotice(item.noticeContent)"></li>
          </ol>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>技术支持</span>
          </div>
          <div class="body">
            <img
              :src="technicalSupport"
              alt="donate"
              width="100%"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { listNotice} from "@/api/system/notice";
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.6.1",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 公告表格数据
      noticeList: [],
      technicalSupport:require("@/assets/images/hacknet.jpg")
    };
  },
  created() {
    this.getList();
  },
  methods: {
    //公告列表
   async getList() {
      listNotice(this.queryParams).then(response => {
        this.noticeList = response.rows;
        this.noticeList.forEach(e => {
          e.noticeTitle=e.noticeTitle + '[查看]';
        });
      });
    },
    //查看通知内容
    seeNotice(content) {
      this.$notify.info({
          title: '通知',
          dangerouslyUseHTMLString: true,
          message: content,
          //duration: 0 //不会自动关闭
        });
    },
    goTarget(href) {
      window.open(href, "_blank");
    },
  },
};
</script>

<style scoped lang="scss">
.noticeTitle {
  cursor: pointer;//悬浮时变手指
  color: rgb(255, 145, 0);
  font-size: larger;
}
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

