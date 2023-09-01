# 移动端后台管理系统 JoveFast-Mobile

### :peach: 项目简介
企事业单位信息化加速，移动端办工逐渐成为趋势，因此一款好的移动端基础平台十分有必要。<br>
项目采用当下流行的UniApp + uView2 框架开发，完成常见的日常工作功能与移动端特有功能组件。
<br><br>

### 安装依赖
进入根目录执行 npm i;

## 说明

uView UI，是[uni-app](https://uniapp.dcloud.io/)生态优秀的UI框架，全面的组件和便捷的工具会让您信手拈来，如鱼得水

## 特性

- 兼容安卓，iOS，微信小程序，H5，QQ小程序，百度小程序，支付宝小程序，头条小程序
- 60+精选组件，功能丰富，多端兼容，让您快速集成，开箱即用
- 众多贴心的JS利器，让您飞镖在手，召之即来，百步穿杨
- 众多的常用页面和布局，让您专注逻辑，事半功倍
- 详尽的文档支持，现代化的演示效果
- 按需引入，精简打包体积


## 安装

```bash
# npm方式安装，插件市场导入无需执行此命令
npm i uview-ui
```

## 快速上手

1. `main.js`引入uView库
```js
// main.js
import uView from 'uview-ui';
Vue.use(uView);
```

2. `App.vue`引入基础样式(注意style标签需声明scss属性支持)
```css
/* App.vue */
<style lang="scss">
@import "uview-ui/index.scss";
</style>
```

3. `uni.scss`引入全局scss变量文件
```css
/* uni.scss */
@import "uview-ui/theme.scss";
```

4. `pages.json`配置easycom规则(按需引入)

```js
// pages.json
{
	"easycom": {
		// npm安装的方式不需要前面的"@/"，下载安装的方式需要"@/"
		// npm安装方式
		"^u-(.*)": "uview-ui/components/u-$1/u-$1.vue"
		// 下载安装方式
		// "^u-(.*)": "@/uview-ui/components/u-$1/u-$1.vue"
	},
	// 此为本身已有的内容
	"pages": [
		// ......
	]
}
```

请通过[快速上手](https://www.uviewui.com/components/quickstart.html)了解更详细的内容 

## 使用方法
配置easycom规则后，自动按需引入，无需`import`组件，直接引用即可。

```html
<template>
	<u-button text="按钮"></u-button>
</template>
```

请通过[快速上手](https://www.uviewui.com/components/quickstart.html)了解更详细的内容 

## 链接

- [官方文档](https://www.uviewui.com/)
- [更新日志](https://www.www.uviewui.com/components/changelog.html)
- [升级指南](https://www.uviewui.com/components/changelog.html)
- [关于我们](https://www.uviewui.com/cooperation/about.html)

###  :watermelon:  技术栈
| 组件       | 版本     |
|----------|--------|
| uview-ui | 2.0.31 |
| qiun-data-charts | 2.4.3-20220505 |

<br><br>

###   :tw-1f348:   系统配置
1. 后端请求地址配置：

```
# 配置文件路径：/config/environment.js
# 修改baseURL属性

const environment = {
	// 开发环境配置
	development: {
		// 本地部署的后端
		baseURL: 'http://localhost:8080',
		
		// 直接使用线上后端
		// baseURL: 'http://xxx.xxx.com/prod-api'
	},
	// 生产环境配置
	production: {
		baseURL: 'http://xxx.xxx.com/prod-api' // 发布时需要修改为后端实际地址
	}
}

module.exports = {
  environment: environment[process.env.NODE_ENV]
}
```
2. H5启动端口配置：

***注意：不要在manifest.json中配置h5启动信息，可能会引发后端接口访问异常***

```
# 配置文件路径：/vue.config.js
# 修改port属性

const { environment } = require('./config/environment.js')

module.exports = {
  devServer: {
    port: 9001,
    proxy: {
      '/': {
        target: environment.baseURL,
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/': ''
        }
      }
    },
  }
}

```
