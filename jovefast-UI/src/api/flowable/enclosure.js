import request from '@/utils/request'

// 查询列表
export function listAttachment(query) {
  return request({
    url: '/flowable/enclosure/list',
    method: 'get',
    params: query
  })
}

// 删除
export function delAttachment(ids) {
  return request({
    url: '/flowable/enclosure/delete/' + ids,
    method: 'delete'
  })
}
