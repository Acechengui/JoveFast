<template>
  <view class="mobile-item-container">
    <Navbar title="用户管理" bgColor="#fff" :h5Show="false"></Navbar>
    <view style="padding: 16px 0 10px;">
      <u-search :show-action="true" actionText="搜索" :animation="true" height="40px"></u-search>
    </view>
    <view v-if="list && list.length > 0">
      <u-cell v-for="(item, index) in list" :key="index" :isLink="true" :border="true" @click="navigateTo(item)">
        <u-avatar slot="icon" v-if="item.avatar" :src="item.avatar"></u-avatar>
        <view slot="title">
          <view style="display: flex; padding: 8px 0;">
            <text style="font-size: 18px; font-weight: bold;">{{item.nickName}}</text>
            <u-tag :text="item.sex == 0 ? '男' : '女'" :type="item.sex == 0 ? 'primary' : 'error'" shape="circle" size="mini" style="margin-left: 8px;"></u-tag>
          </view>
          <view style="display: flex; justify-content:space-between;">
            <!-- <text>部门：{{item.dept.deptName}}</text> -->
            <text>电话：{{(item.phonenumber === undefined || item.phonenumber ===null || item.email =='')  ? '无':item.phonenumber}}</text>
          </view>
          <view>
            <text>邮件：{{(item.email === undefined || item.email ===null || item.email =='')  ? '无':item.email}}
			</text>
          </view>
        </view>
        <view slot="label">
        </view>
      </u-cell>
      <u-loadmore :status="status" color="#1CD29B" lineColor="#1CD29B"
	    :loading-text="loadingText" 
        :loadmore-text="loadmoreText" 
        :nomore-text="nomoreText">
		</u-loadmore>
    </view>
    <u-empty v-else></u-empty>
    <FloatButton type="primary" icon="plus" @click="navigateTo"></FloatButton>
  </view>
</template>

<script>
import * as UserManageApi from '@/api/work/userManage'
import Navbar from '@/components/navbar/Navbar'
import FloatButton from '@/components/button/FloatButton'

export default {
  components: {
    Navbar,
    FloatButton
  },
  data () {
    return {
	  status: 'loadmore',
	  loadingText: '努力加载中',
	  loadmoreText: '上拉或点击加载更多',
	  nomoreText: '实在没有了',
      params: {
        pageNum: 0,
        pageSize: 10
      },
      list: []
    }
  },
  onLoad () {
    this.loadData();
  },
  methods: {
	  //下拉触发
	onReachBottom() {
		    const that = this
			that.params.pageSize += 10;
			if(that.params.pageNum >= 3) that.status = 'nomore';
			else that.status = 'loading';
			UserManageApi.userList(that.params).then(res => {
			  that.list = res.rows;
			})
			
	},
    // 加载用户列表数据
    loadData () {
      const app = this
      this.params.pageNum += 1;
      UserManageApi.userList(this.params).then(res => {
        app.list = res.rows;
      })
    },
    navigateTo (user) {
      if (user) {
        uni.navigateTo({ url: '/pages/work/user/edit?id=' + user.userId })
      } else {
        uni.navigateTo({ url: '/pages/work/user/edit' })
      }
    }
  }
}
</script>
