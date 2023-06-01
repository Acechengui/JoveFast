<template>
  <div class="app-container home">

    <el-row :gutter="10">
      <el-col :sm="48" :lg="24">
        <h2>{{ $t('index.introTitle') }}</h2>
        <p>
          {{ $t('index.intro1') }}
          {{ $t('index.intro2') }}
          {{ $t('index.intro3') }}
        </p>
        <p>
          <el-button size="mini" icon="el-icon-s-home" plain @click="goTarget('http://www.jovepcb.com/')">
            {{ $t('index.visitWebsite') }}
          </el-button>
        </p>
      </el-col>
    </el-row>
    <el-divider />

    <el-row :gutter="20">
      <el-col>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('index.navigation') }}</span>
          </div>
          <el-row>
            <el-col :span="12" style="padding: 0">
              <el-card shadow="hover">
                <div style="display: flex;align-items: center;flex-direction: column;cursor: pointer" @click="() => {
                  gotoRoute({
                    path: '/workflowCenter/taskmanger/process'
                  })
                }">
                  <i class="el-icon-news"><a href="javascript:;" style="text-align: center;">{{ $t('index.deploy')
                    }}</a></i>
                </div>
              </el-card>
            </el-col>

            <el-col :span="12" style="padding: 0">
              <el-card shadow="hover">
                <div style="display: flex;align-items: center;flex-direction: column;cursor: pointer" @click="() => {
                  gotoRoute({
                    path: '/workflowCenter/taskmanger/todo'
                  })
                }">
                  <i class="el-icon-date"><a href="javascript:;" style="text-align: center;">{{ $t('index.todo')
                    }}</a></i>
                </div>
              </el-card>
            </el-col>

            <el-col :span="12" style="padding: 0">
              <el-card shadow="hover">
                <div style="display: flex;align-items: center;flex-direction: column;cursor: pointer" @click="() => {
                  gotoRoute({
                    path: '/workflowCenter/taskmanger/finished'
                  })
                }">
                  <i class="el-icon-success"><a href="javascript:;" style="text-align: center;">{{ $t('index.finished')
                    }}</a></i>
                </div>
              </el-card>
            </el-col>


            <el-col :span="12" style="padding: 0">
              <el-card shadow="hover">
                <div style="display: flex;align-items: center;flex-direction: column;cursor: pointer" @click="() => {
                  gotoRoute({
                    path: '/monitor/online'
                  })
                }">
                  <i class="el-icon-view"><a href="javascript:;" style="text-align: center;">{{ $t('index.online')
                    }}</a></i>
                </div>
              </el-card>
            </el-col>

          </el-row>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col>
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('index.question') }}</span>
          </div>
          <div class="body">
            <el-collapse accordion>
              <el-collapse-item>
                <template slot="title">
                  {{ $t('index.question1') }}<i class="header-icon el-icon-question"></i>
                </template>
                <div>
                  {{ $t('index.answer1') }}
                </div>
              </el-collapse-item>
              <el-collapse-item>
                <template slot="title">
                  {{ $t('index.question2') }}<i class="header-icon el-icon-question"></i>
                </template>
                <div>
                  {{ $t('index.answer2') }}
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col>
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('index.information') }}</span>
          </div>
          <div class="body">
            <p>
              <i class="el-icon-chat-dot-round"></i>{{ $t('index.developer') }}<a href="javascript:;">
              / JOVEPCB-IT TEAM</a>
            </p>
            <p>
              <i class="el-icon-s-promotion"></i>{{ $t('index.website') }}<el-link href="http://www.jovepcb.com"
                                                                                   target="_blank">
              http://www.jovepcb.com</el-link>
            </p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.6.2",
    };
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    gotoRoute(route) {
      this.$router.push(route);
    }
  },
};
</script>

<style scoped lang="scss">
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

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
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
}
</style>

