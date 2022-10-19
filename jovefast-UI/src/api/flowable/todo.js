import request from '@/utils/request'

// 查询待办任务列表
export function todoList(query) {
  return request({
    url: '/flowable/task/todoList',
    method: 'get',
    params: query
  })
}

// 完成任务
export function complete(data) {
  return request({
    url: '/flowable/task/complete',
    method: 'post',
    data: data
  })
}

// 委派任务
export function delegate(data) {
  return request({
    url: '/flowable/task/delegate',
    method: 'post',
    data: data
  })
}

// 退回任务
export function returnTask(data) {
  return request({
    url: '/flowable/task/return',
    method: 'post',
    data: data
  })
}

// 驳回任务
export function rejectTask(data) {
  return request({
    url: '/flowable/task/reject',
    method: 'post',
    data: data
  })
}

// 可退回任务列表
export function returnList(data) {
  return request({
    url: '/flowable/task/returnList',
    method: 'post',
    data: data
  })
}

// 下一节点
export function getNextFlowNode(data) {
  return request({
    url: '/flowable/task/nextFlowNode',
    method: 'post',
    data: data
  })
}

// 部署流程实例
export function deployStart(deployId) {
  return request({
    url: '/flowable/process/startFlow/' + deployId,
    method: 'get',
  })
}
