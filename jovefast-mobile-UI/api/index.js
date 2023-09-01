import request from '@/config/request.js';

// 在线用户
export const onlineUsers = () => request.get('/system/online/list')

export const accessingStatistics = () => request.get('/mobile/index/statistics')

