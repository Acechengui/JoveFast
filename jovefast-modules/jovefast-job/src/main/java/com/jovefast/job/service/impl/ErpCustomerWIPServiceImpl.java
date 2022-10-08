package com.jovefast.job.service.impl;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.datasource.annotation.Slave;
import com.jovefast.job.domain.dto.VCustomerWipDTO;
import com.jovefast.job.mapper.VCustomerWIPMapper;
import com.jovefast.job.service.IErpCustomerWIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 市场跟单数据操作
 * @Author Acechengui
 * @Date Created in 2022/9/16
 */
@Service
@Slave
public class ErpCustomerWIPServiceImpl implements IErpCustomerWIPService {

    @Autowired
    private VCustomerWIPMapper vCustomerWIPMapper;

    /**
     * 工序超时预警
     */
    public List<VCustomerWipDTO> operationTimeout(List<VCustomerWipDTO> ls){
        int hours;
        //获取当前工序制程顺序号
        Integer currtstep;
        for (VCustomerWipDTO vc : ls) {
            //处理市场备注合并
            vc.setMarketnotes(vc.getMarketnotesa() + vc.getMarketnotesb());
            //当前时间距离交期相差小时数 交期以 12:00 为准
            hours = (int) DateUtils.getDatehour(DateUtils.getTime(), vc.getDeliveryresponse() + " 12:00:00");
            //生产在线状态为空 并且 交期已超当前日期 并且 未交数不为0  --》直接异常预警
            if (hours < 0 && vc.getOnlinestatus().trim().length() == 0 && vc.getUnpaidamount() != 0) {
                vc.setEarlywarnstatus("异常");
                continue;
            }
            //判断当前工序是否为空
            if (vc.getStep() != null) {
                //获取当前工序制程顺序号
                currtstep = vc.getStep();
                Map<String, Integer> steps = new HashMap<>();
                //得到几大工序的位置
                steps.put("stepnumber1", vc.getStepnumber1() == null ? 0 : vc.getStepnumber1());
                steps.put("stepnumber2", vc.getStepnumber2() == null ? 0 : vc.getStepnumber2());
                steps.put("stepnumber3", vc.getStepnumber3() == null ? 0 : vc.getStepnumber3());
                steps.put("stepnumber4", vc.getStepnumber4() == null ? 0 : vc.getStepnumber4());
                steps.put("stepnumber5", vc.getStepnumber5() == null ? 0 : vc.getStepnumber5());
                steps.put("stepnumber6", vc.getStepnumber6() == null ? 0 : vc.getStepnumber6());
                steps.put("stepnumber7", vc.getStepnumber7() == null ? 0 : vc.getStepnumber7());

                //这里将map.entrySet()转换成list
                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(steps.entrySet());
                //然后通过比较器来实现排序
                list.sort(new Comparator<Map.Entry<String, Integer>>() {
                    //倒序排序
                    public int compare(Map.Entry<String, Integer> o1,
                                       Map.Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }

                });
                //判断是否跳出当前循环
                boolean flag = true;
                for (Map.Entry<String, Integer> mapping : list) {
                    //判断当前工序处于几大工序的哪一个位置
                    if (currtstep >= mapping.getValue()) {
                        switch (mapping.getKey()) {
                            case "stepnumber1":
                                //判断距离交期时间 是否大于等于 当前工序所处几大工序中的标准工时?  如果小于，即表示超时，进行预警
                                if (vc.getProc1() != null && hours < vc.getProc1()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber2":
                                if (vc.getProc2() != null && hours < vc.getProc2()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber3":
                                if (vc.getProc3() != null && hours < vc.getProc3()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber4":
                                if (vc.getProc4() != null && hours < vc.getProc4()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber5":
                                if (vc.getProc5() != null && hours < vc.getProc5()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber6":
                                if (vc.getProc6() != null && hours < vc.getProc6()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            case "stepnumber7":
                                if (vc.getProc7() != null && hours < vc.getProc7()) {
                                    vc.setEarlywarnstatus("异常");
                                } else {
                                    vc.setEarlywarnstatus("");
                                }
                                flag = false;
                                break;
                            default:
                                break;
                        }
                    }
                    if (!flag) {
                        break;
                    }
                }

            } else {
                vc.setEarlywarnstatus("");
            }
        }
        return ls;

    }

    /**
     * 内外层数据整理
     */
    public List<VCustomerWipDTO> recoin(List<VCustomerWipDTO> outers, List<VCustomerWipDTO> inners) {
        //临时列表
        List<String> temporary = new ArrayList<>();

        //定义返回集合
        List<VCustomerWipDTO> collect = new ArrayList<>();

        for (VCustomerWipDTO wipBean : outers) {
            //判断是否有重复的外层
            boolean item = false;
            for (String s : temporary) {
                if (wipBean.getSalesorder().equals(s)) {
                    //已循环过
                    item = true;
                    break;
                }
            }

            if (!item) {
                //将已循环过的外层存入临时列表中
                temporary.add(wipBean.getSalesorder());

                //存放当前相同外层的List
                List<VCustomerWipDTO> rootptrs = new ArrayList<>();
                //循环判断是否有相同的外层
                for (VCustomerWipDTO outer : outers) {
                    if (wipBean.getSalesorder().equals(outer.getSalesorder())) {
                        rootptrs.add(outer);
                    }
                }

                //定义在线状态
                StringBuilder sb = new StringBuilder();
                Map<String, Integer> qtybacklogMap = new HashMap<>();
                //定义结存数
                Integer qty = null;

                //循环外层List
                for (VCustomerWipDTO rootptr : rootptrs) {
                    //循环内层List inners
                    for (VCustomerWipDTO inner : inners) {
                        //是否有内层
                        if (inner.getRootptr() != null && inner.getRootptr().equals(rootptr.getWoptr())) {
                            if (qty == null) {
                                //初始化pty，进来了就代表有内层
                                qty = Integer.parseInt(inner.getQtybacklog());
                            } else if (qty > Integer.parseInt(inner.getQtybacklog())) {
                                //获取最小的结存数
                                qty = Integer.parseInt(inner.getQtybacklog());
                            }
                        }
                    }
                    if (rootptr.getDeptname() != null && rootptr.getQtybacklog() != null) {
                        String deptname = rootptr.getDeptname().trim();
                        Integer qtybacklog = Integer.parseInt(rootptr.getQtybacklog().trim());
                        if (!qtybacklogMap.containsKey(deptname)) {
                            //插入外层在线状态
                            qtybacklogMap.put(deptname, qtybacklog);
                        } else {
                            qtybacklogMap.put(deptname, qtybacklogMap.get(deptname) + qtybacklog);
                        }
                    }
                }
                if (qty != null) {
                    sb.append("内层").append(qty).append(" ");
                }
                for (String key : qtybacklogMap.keySet()) {
                    sb.append(key).append(qtybacklogMap.get(key)).append(" ");
                }
                //添加到返回集合中
                wipBean.setOnlinestatus(sb.toString());
                collect.add(wipBean);
            }
        }
        return collect;
    }

    /**
     * 计划下卡前状态细化
     */
    public List<VCustomerWipDTO> rTStateBeforeTCIR(List<VCustomerWipDTO> ls){
        for (VCustomerWipDTO vc : ls) {
            if (vc.getTransdescription1() != null && vc.getTransdescription2() == null && vc.getTransdescription3() == null && vc.getMicreate() != null) {
                vc.setMarketstate("在市场");
            } else if (vc.getTransdescription1() == null && vc.getTransdescription2() != null && vc.getTransdescription3() == null && vc.getMicreate() == null ||
                    vc.getTransdescription1() != null && vc.getTransdescription2() != null && vc.getTransdescription3() == null && vc.getMicreate() == null) {
                vc.setMarketstate("在市场工程");
            } else if (vc.getMicreate() != null || vc.getTransdescription3() != null) {
                vc.setMarketstate("在工程");
            }

        }
        return ls;
    }

    /**
     * 预警
     */
    @Override
    public List<VCustomerWipDTO> earlyWarn(Map<String, Object> params) {
        List<VCustomerWipDTO> outerWIP = vCustomerWIPMapper.listOuterCustomerWIPExport(params);
        StringBuilder sb = new StringBuilder();
        //去重
        List<VCustomerWipDTO> uniqueList = outerWIP.stream().filter(fi->fi.getWoptr() != null).collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(VCustomerWipDTO::getWoptr))), ArrayList::new)
        );
        //组装参数
        for (VCustomerWipDTO wipBean : uniqueList) {
           sb.append(wipBean.getWoptr()).append(",");
        }
        String woptr = sb.toString();
        if (woptr.length() > 0) {
            woptr = woptr.substring(0, woptr.length() - 1);
            params.put("rootptr", woptr);
        }
        List<VCustomerWipDTO> innerWIP = vCustomerWIPMapper.listInnerCustomerWIPExport(params);
        return operationTimeout(rTStateBeforeTCIR(recoin(outerWIP, innerWIP)));
    }
}
