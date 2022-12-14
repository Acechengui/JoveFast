import request from '@/utils/request'

// 查询流程定义列表
export function listDefinition(query) {
  return request({
    url: '/flowable/definition/list',
    method: 'get',
    params: query
  })
}

// 部署流程实例
export function definitionStart(procDefId, data) {
  return request({
    url: '/flowable/definition/start/' + procDefId,
    method: 'post',
    data: data
  })
}

// 修改流程实例，更新流程变量
export function definitionEdit(procDefId, data) {
  return request({
    url: '/flowable/definition/edit/' + procDefId,
    method: 'post',
    data: data
  })
}

// 获取流程变量
export function getProcessVariables(taskId) {
  return request({
    url: '/flowable/task/processVariables/' + taskId,
    method: 'get'
  })
}

// 激活/挂起流程
export function updateState(params) {
  return request({
    url: '/flowable/definition/updateState',
    method: 'put',
    params: params
  })
}

// 读取xml文件
export function readXml(deployId) {
  return request({
    url: '/flowable/definition/readXml/' + deployId,
    method: 'get'
  })
}

// 读取image文件
export function readImage(deployId) {
  return request({
    url: '/flowable/definition/readImage/' + deployId,
    method: 'get'
  })
}

// 读取image文件
export function getFlowViewer(procInsId, executionId) {
  return request({
    url: '/flowable/task/flowViewer/' + procInsId + '/' + executionId,
    method: 'get'
  })
}

// 读取xml文件
export function saveXml(data) {
  return request({
    url: '/flowable/definition/save',
    method: 'post',
    data: data
  })
}

// 删除流程定义
export function delDeployment(query) {
  return request({
    url: '/flowable/definition/delete/'+ query,
    method: 'delete'
  })
}

