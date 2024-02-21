import request from '@/utils/request'

// 查询我的抄送任务列表
export function ccList(query) {
    return request({
      url: '/flowable/task/ccList',
      method: 'get',
      params: query
    })
  }
