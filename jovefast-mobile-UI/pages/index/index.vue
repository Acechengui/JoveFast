<template>
	<view class="index-content">
		<Navbar :hideBtn="true" title="首页" bgColor="#fff" :h5Show="false" :fixed="false"></Navbar>
		<view class="index-block">
			<view class="index-block-title">当前状态</view>
			<u-row gutter="16">
				<u-col span="4">
					<view class="item-tj item-tj-second">
						<view style="padding: 16rpx; height: 100%; position: relative;">
							<view style="display: flex; color: white;">
								<u-icon name="photo" color="#fff"></u-icon>
								<text>当前城市</text>
							</view>
							<view style="position: absolute; bottom: 48rpx; right: 15rpx;color:#fff;font-size: 15px;">
								{{currentCity.province}} {{currentCity.city}}
							</view>
						</view>
					</view>
				</u-col>
				<u-col span="4">
					<view class="item-tj item-tj-frist">
						<view style="padding: 16rpx; height: 100%; position: relative;">
							<view style="display: flex; color: white;">
								<u-icon name="man-add" color="#fff"></u-icon>
								<text>在线人数</text>
							</view>
							<view style="position: absolute; bottom: 48rpx;right: 16rpx; font-size: 15px;">
								<u-count-to :startVal="0" :endVal="numberOfOnlineUsers" :duration="1500" color="#fff"
									separator=","></u-count-to>
							</view>
						</view>
					</view>
				</u-col>
				<u-col span="4">
					<view class="item-tj item-tj-thrid">
						<view style="padding: 16rpx; height: 100%; position: relative;">
							<view style="display: flex; color: white;">
								<u-icon name="man-add-fill" color="#fff"></u-icon>
								<text>实时天气</text>
							</view>
							<view style="position: absolute; bottom: 48rpx; color:#fff;font-size: 15px;">
								天气:{{weatherInFo.weather}}
								气温:{{weatherInFo.temperature}}°c
							</view>
						</view>
					</view>
				</u-col>
			</u-row>
		</view>
		<view class="index-block">
			<view class="index-block-title">近7天访问量统计</view>
			<view>
				<qiun-data-charts type="line" canvasId="finance_a" :canvas2d="isCanvas2d" :resshow="delayload"
					:opts="{xAxis:{itemCount:12,disableGrid:true},yAxis:{disableGrid:true,data:[{disabled:true}]}}"
					:chartData="historyData" />
			</view>
			<view class="index-block-title">访问量统计</view>
			<view>
				<qiun-data-charts type="bar" canvasId="finance_b" :canvas2d="isCanvas2d" :resshow="delayload"
					:opts="{xAxis:{disabled: true,disableGrid:true},extra:{bar:{barBorderCircle:true,width:20}},legend:{show:false}}"
					:chartData="historyData" />
			</view>
		</view>
	</view>
</template>

<script>
	import * as IndexApi from '@/api/index'
	import Navbar from '@/components/navbar/Navbar'

	export default {
		components: {
			Navbar,
		},
		data() {
			return {
				currentCity: [],
				numberOfOnlineUsers: 0,
				weatherInFo: [],
				isCanvas2d: true,
				delayload: false,
				historyData: {}
			}
		},
		created() {
			this.getCityInFo();
			this.getOnlineUsers();
			this.getAccessingData();
		},
		methods: {
			getCityInFo() {
				uni.request({
					url: 'https://restapi.amap.com/v3/ip?key=你的key',
					method: 'get',
					success: (res) => {
						this.currentCity = res.data
						this.getWeather();
					},
					fail: (err) => {
						console.log(err)
					}
				})
			},
			getOnlineUsers() {
				IndexApi.onlineUsers().then(res => {
					this.numberOfOnlineUsers = res.total;
				})
			},
			getWeather() {
				uni.request({
					url: 'https://restapi.amap.com/v3/weather/weatherInfo?key=你的key' +
						this.currentCity.adcode,
					method: 'get',
					success: (res) => {
						this.weatherInFo = res.data.lives[0]
					},
					fail: (err) => {
						console.log(err)
					}
				})
			},
			getAccessingData() {
				const app=this;
				IndexApi.accessingStatistics().then(res => {
					app.historyData=res.data;
				})
			}
		}
	}
</script>

<style lang="scss">
	.index-content {
		background-color: #f3f4f6;
		min-height: 100vh;
	}

	.index-block {
		padding: 40rpx;
		background-color: #fff;
	}

	.index-block-title {
		font-size: 40rpx;
		font-weight: bold;
		padding: 0 0 40rpx 0;
	}

	.item-tj {
		width: 100%;
		height: 160rpx;
		border-radius: 16rpx;

		&-frist {
			background-color: rgba($color: #2979ff, $alpha: 0.8);
			// background-image: url('/static/img/bg/qb.png');
		}

		&-second {
			background-color: rgba($color: #303133, $alpha: 0.8);
			// background-image: url('/static/img/bg/qb.png');
		}

		&-thrid {
			background-color: rgba($color: #19be6b, $alpha: 0.8);
			// background-image: url('/static/img/bg/qb.png');
		}
	}

	.detail_list {
		height: 700rpx;
		overflow: auto;
		color: #9E9E9E;

		.detail_item {
			display: flex;
			margin: 20rpx 0;
			align-items: center;

			.icon {
				width: 30%;
				text-align: center;

				li {
					font-size: 80rpx;
				}
			}

			.right_content {
				width: 50%;
				text-align: center;
			}

			.icon-income {
				color: #4AABF9;
			}

			.icon-expend {
				color: #E45521;
			}

			.money {
				color: #000;
			}
		}
	}
</style>