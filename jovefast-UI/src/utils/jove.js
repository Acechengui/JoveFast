﻿

/**
 * 通用js方法封装处理
 */

// 日期格式化
export function parseTime(time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}


//时间格式化:年-月-日
export function formatDateC(param) {
  let dtc = new Date(param)
  //获取月,默认月份从0开始
  let dtuMonth = dtc.getMonth() + 1
  //获取日
  let dtuDay = dtc.getDate()
  //处理1-9月前面加0
  if (dtuMonth < 10) {
    dtuMonth = "0" + (dtc.getMonth() + 1)
  }
  //处理1-9天前面加0
  if (dtuDay < 10) {
    dtuDay = "0" + dtc.getDate()
  }
  //获取小时
  let dtuHours = dtc.getHours()
  //处理1-9时前面加0
  if (dtuHours < 10) {
    dtuHours = "0" + dtc.getHours()
  }
  //获取分钟
  let dtuMinutes = dtc.getMinutes()
  //处理1-9分前面加0
  if (dtuMinutes < 10) {
    dtuMinutes = "0" + dtc.getMinutes()
  }
  //获取秒
  let dtuSeconds = dtc.getSeconds()
  //处理1-9秒前面加0
  if (dtuSeconds < 10) {
    dtuSeconds = "0" + dtc.getSeconds()
  }
  //组装年月日,按自己的要求来
  return dtc.getFullYear() + "-" + dtuMonth + "-" + dtuDay
}

// 表单重置
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}

// 添加单个日期范围
export function addDateRange(params, dateRange, propName) {
  let search = params;
  search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof (propName) === 'undefined') {
    search.params['beginTime'] = dateRange[0];
    search.params['endTime'] = dateRange[1];
  } else {
    search.params['begin' + propName] = dateRange[0];
    search.params['end' + propName] = dateRange[1];
  }
  return search;
}

// 添加多个日期范围[两个日期]
export function addMultipleDateRange(params, dateRange1, dateRange2, propName) {
  let search = params;
  search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
  dateRange1 = Array.isArray(dateRange1) ? dateRange1 : [];
  dateRange2 = Array.isArray(dateRange2) ? dateRange2 : [];
  if (typeof (propName) === 'undefined') {
    search.params['beginTime1'] = dateRange1[0];
    search.params['endTime1'] = dateRange1[1];
    search.params['beginTime2'] = dateRange2[0];
    search.params['endTime2'] = dateRange2[1];
  } else {
    search.params['begin1' + propName] = dateRange1[0];
    search.params['end1' + propName] = dateRange1[1];
    search.params['begin2' + propName] = dateRange2[0];
    search.params['end2' + propName] = dateRange2[1];
  }
  return search;
}

// 添加多个日期范围[三个日期]
export function addMultipleDateRangeThree(params, dateRange1, dateRange2, dateRange3,propName) {
  let search = params;
  search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
  dateRange1 = Array.isArray(dateRange1) ? dateRange1 : [];
  dateRange2 = Array.isArray(dateRange2) ? dateRange2 : [];
  dateRange3 = Array.isArray(dateRange2) ? dateRange3 : [];
  if (typeof (propName) === 'undefined') {
    search.params['beginTime1'] = dateRange1[0];
    search.params['endTime1'] = dateRange1[1];
    search.params['beginTime2'] = dateRange2[0];
    search.params['endTime2'] = dateRange2[1];
    search.params['beginTime3'] = dateRange3[0];
    search.params['endTime3'] = dateRange3[1];
  } else {
    search.params['begin1' + propName] = dateRange1[0];
    search.params['end1' + propName] = dateRange1[1];
    search.params['begin2' + propName] = dateRange2[0];
    search.params['end2' + propName] = dateRange2[1];
    search.params['begin3' + propName] = dateRange3[0];
    search.params['end3' + propName] = dateRange3[1];
  }
  return search;
}

// 回显数据字典
export function selectDictLabel(datas, value) {
  if (value === undefined) {
    return "";
  }
  var actions = [];
  Object.keys(datas).some((key) => {
    if (datas[key].value == ('' + value)) {
      actions.push(datas[key].label);
      return true;
    }
  })
  if (actions.length === 0) {
    actions.push(value);
  }
  return actions.join('');
}

// 回显数据字典（字符串、数组）
export function selectDictLabels(datas, value, separator) {
  if (value === undefined || value.length ===0) {
    return "";
  }
  if (Array.isArray(value)) {
    value = value.join(",");
  }
  var actions = [];
  var currentSeparator = undefined === separator ? "," : separator;
  var temp = value.split(currentSeparator);
  Object.keys(value.split(currentSeparator)).some((val) => {
    var match = false;
    Object.keys(datas).some((key) => {
      if (datas[key].value == ('' + temp[val])) {
        actions.push(datas[key].label + currentSeparator);
        match = true;
      }
    })
    if (!match) {
      actions.push(temp[val] + currentSeparator);
    }
  })
  return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
export function sprintf(str) {
  var args = arguments, flag = true, i = 1;
  str = str.replace(/%s/g, function () {
    var arg = args[i++];
    if (typeof arg === 'undefined') {
      flag = false;
      return '';
    }
    return arg;
  });
  return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str) {
  if (!str || str == "undefined" || str == "null") {
    return "";
  }
  return str;
}

// 数据合并
export function mergeRecursive(source, target) {
  for (var p in target) {
    try {
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p]);
      } else {
        source[p] = target[p];
      }
    } catch (e) {
      source[p] = target[p];
    }
  }
  return source;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params) {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + "=";
    if (value !== null && typeof (value) !== "undefined") {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + "=";
            result += subPart + encodeURIComponent(value[key]) + "&";
          }
        }
      } else {
        result += part + encodeURIComponent(value) + "&";
      }
    }
  }
  return result
}

// 验证是否为blob格式
export function blobValidate(data) {
  return data.type !== 'application/json'
}
