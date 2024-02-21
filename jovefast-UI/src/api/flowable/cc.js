import request from '@/utils/request'

// 查询待办任务列表
export function ccList(query) {
    return request({
      url: '/flowable/task/ccList',
      method: 'get',
      params: query
    })
  }