import request from '@/utils/request'

// 查询流程表单列表
export function listForm(query) {
  return request({
    url: '/flowable/form/list',
    method: 'get',
    params: query
  })
}

export function listFormAll(query) {
  return request({
    url: '/flowable/form/list/all',
    method: 'get',
    params: query
  })
}

// 查询所有表单
export function listAllForm(query) {
  return request({
    url: '/flowable/form/formList',
    method: 'get',
    params: query
  })
}

// 查询默认设计的表单模板数据
export function getForm(formId) {
  return request({
    url: '/flowable/form/' + formId,
    method: 'get'
  })
}

// 查询已变更的表单模板数据
export function getChangeForm(data) {
  return request({
    url: '/flowable/form/processParams',
    method: 'post',
    data: data
  })
}

// 新增流程表单
export function addForm(data) {
  return request({
    url: '/flowable/form',
    method: 'post',
    data: data
  })
}

// 修改流程表单
export function updateForm(data) {
  return request({
    url: '/flowable/form',
    method: 'put',
    data: data
  })
}
// 挂载表单
export function addDeployForm(data) {
  return request({
    url: '/flowable/form/addDeployForm',
    method: 'post',
    data: data
  })
}

// 删除流程表单
export function delForm(formId) {
  return request({
    url: '/flowable/form/' + formId,
    method: 'delete'
  })
}
