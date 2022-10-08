package com.jovefast.job.service.impl;

import com.jovefast.common.core.qiwx.WeixinApiConfig;
import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.datasource.annotation.Slave;
import com.jovefast.job.domain.TimeControllTemp;
import com.jovefast.job.domain.dto.VfinishedgoodsInventoryStatDTO;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.mapper.TimeControllTempMapper;
import com.jovefast.job.service.ITimeControllTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 超时停留接口实现
 */
@Service
@Slave
public class ITimeControllTempServiceImpl implements ITimeControllTempService {

    @Autowired
    TimeControllTempMapper timeControllTempMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");


    /**
     * 预警扫描
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanningEarlyWarningTimeControllTemp() {
        List<TimeControllTemp> beans = timeControllTempMapper.scanningEarlyWarningTimeControllTemp();
        if (beans == null || beans.size() == 0) {
            return;
        }
        for (TimeControllTemp bean : beans) {
            if ("内层AOI".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("内层AOI", "棕化", 72, bean);
                if (bean_1 != bean) {
                    if ("000".equals(bean_1.getWo().split("-")[3])) {
                        warningMess(bean_1);
                    } else {
                        bean_1.setStatus(1);
                        timeControllTempMapper.updateTimeControll(bean_1);
                        timeControllTempMapper.updateTimeControllByLastWarningTime(bean_1);
                    }
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_1);
                }
                TimeControllTemp bean_2 = timeout("棕化", "BDP棕化放板", 42, bean);
                if (bean_2 != bean) {
                    if ("000".equals(bean_2.getWo().split("-")[3])) {
                        warningMess(bean_2);
                    } else {
                        bean_2.setStatus(1);
                        timeControllTempMapper.updateTimeControll(bean_2);
                        timeControllTempMapper.updateTimeControllByLastWarningTime(bean_2);
                    }
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_2);
                }
                bean.setStatus(1);
                timeControllTempMapper.updateTimeControll(bean);
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
            } else if ("图形电镀".equals(bean.getStartdepartment()) && "外层碱性蚀刻".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("图形电镀", "二次钻孔", 48, bean);
                if (bean_1 != bean) {
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_1);
                    warningMess(bean_1);
                }
                TimeControllTemp bean_2 = timeout("二次钻孔", "外层碱性蚀刻", 48, bean);
                if (bean_2 != bean) {
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_2);
                    warningMess(bean_2);
                }
                bean.setStatus(1);
                timeControllTempMapper.updateTimeControll(bean);
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
            } else if ("铣外形".equals(bean.getStartdepartment()) && "最后检查".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("铣外形", "E-T测试", 96, bean);
                if (bean_1 != bean) {
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_1);
                    warningMess(bean_1);
                }
                TimeControllTemp bean_2 = timeout("E-T测试", "最后检查", 72, bean);
                if (bean_2 != bean) {
                    timeControllTempMapper.saveFirstEarlyWarningRecord(bean_2);
                    warningMess(bean_2);
                }
                bean.setStatus(1);
                timeControllTempMapper.updateTimeControll(bean);
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
            } else if ("BDP棕化放板".equals(bean.getStartdepartment()) && "BDP叠板".equals(bean.getEnddepartment())) {
                if ("000".equals(bean.getWo().split("-")[3])) {
                    warningMess(bean);
                } else {
                    bean.setStatus(1);
                    timeControllTempMapper.updateTimeControll(bean);
                    timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                }
                timeControllTempMapper.saveFirstEarlyWarningRecord(bean);
            } else if ("BDP叠板".equals(bean.getStartdepartment()) && "钻孔".equals(bean.getEnddepartment())) {
                if ("000".equals(bean.getWo().split("-")[3])) {
                    warningMess(bean);
                } else {
                    bean.setStatus(1);
                    timeControllTempMapper.updateTimeControll(bean);
                    timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                }
                timeControllTempMapper.saveFirstEarlyWarningRecord(bean);
            } else {
                timeControllTempMapper.saveFirstEarlyWarningRecord(bean);
                warningMess(bean);
            }
        }
    }

    @Override
    public List<VfinishedgoodsInventoryStatDTO> finishedGoodInventoryOverCycleAlert(String custCode, Integer days, Integer defaultDays) {
        List<VfinishedgoodsInventoryStatDTO> result = new ArrayList<>();
        Date time = DateUtils.parseDate(DateUtils.getTime());
        //客户交期为近一月的数据
        List<VfinishedgoodsInventoryStatDTO> inventoryStatDTOS = timeControllTempMapper.selectVfinishedgoodsInventoryStat();
        //检查是否输入了客户代码,若为空则全部取默认天数
        if (StringUtils.isNotEmpty(custCode) && StringUtils.isNotNull(custCode)) {
            List<String> strList = Arrays.asList(custCode.split(","));
            for (VfinishedgoodsInventoryStatDTO dto : inventoryStatDTOS) {
                //对输入了的客户代码做特殊天数处理,否则取默认的天数
                if (strList.contains(dto.getCustCode())) {
                    if (DateUtils.getDaysDifference(time, DateUtils.parseDate(dto.getMfgDate())) > days) {
                        result.add(dto);
                    }
                } else {
                    if (DateUtils.getDaysDifference(time, DateUtils.parseDate(dto.getMfgDate())) > defaultDays) {
                        result.add(dto);
                    }
                }
            }
        } else {
            for (VfinishedgoodsInventoryStatDTO dto : inventoryStatDTOS) {
                if (DateUtils.getDaysDifference(time, DateUtils.parseDate(dto.getMfgDate())) > defaultDays) {
                    result.add(dto);
                }
            }
        }
        return result;
    }

    /**
     * 报警扫描  锁单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void scanningCallPoliceTimeControllTemp() {
        List<TimeControllTemp> beans = timeControllTempMapper.scanningCallPoliceTimeControllTemp();
        if (beans == null || beans.size() == 0) {
            return;
        }
        int count2 = 1;
        int count3 = 1;
        for (TimeControllTemp bean : beans) {
            if ("内层AOI".equals(bean.getStartdepartment()) && "棕化".equals(bean.getEnddepartment())) {
                continue;
            } else if ("棕化".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment())) {
                continue;
            } else if ("图形电镀".equals(bean.getStartdepartment()) && "二次钻孔".equals(bean.getEnddepartment())) {
                continue;
            } else if ("二次钻孔".equals(bean.getStartdepartment()) && "外层碱性蚀刻".equals(bean.getEnddepartment())) {
                continue;
            } else if ("铣外形".equals(bean.getStartdepartment()) && "E-T测试".equals(bean.getEnddepartment())) {
                continue;
            } else if ("E-T测试".equals(bean.getStartdepartment()) && "最后检查".equals(bean.getEnddepartment())) {
                continue;
            }
            if ("内层AOI".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("内层AOI", "棕化", 72, bean);
                if (bean_1 != bean) {
                    if ("000".equals(bean_1.getWo().split("-")[3])) {
                        String[] count = policeMess(bean_1, count2, count3).split(" ");
                        count2 = Integer.parseInt(count[0]);
                        count3 = Integer.parseInt(count[1]);
                    } else {
                        bean_1.setStatus(2);
                        bean_1.setSon(1);
                        timeControllTempMapper.updateTimeControll(bean_1);
                        timeControllTempMapper.lockWorkOrderNumber(bean_1);
                    }
                }
                TimeControllTemp bean_2 = timeout("棕化", "BDP棕化放板", 48, bean);
                if (bean_2 != bean) {
                    if ("000".equals(bean_2.getWo().split("-")[3])) {
                        String[] count = policeMess(bean_2, count2, count3).split(" ");
                        count2 = Integer.parseInt(count[0]);
                        count3 = Integer.parseInt(count[1]);
                    } else {
                        bean_2.setStatus(2);
                        bean_2.setSon(1);
                        timeControllTempMapper.updateTimeControll(bean_2);
                        timeControllTempMapper.lockWorkOrderNumber(bean_2);
                    }
                }
                bean.setStatus(2);
                timeControllTempMapper.updateTimeControll(bean);
            } else if ("图形电镀".equals(bean.getStartdepartment()) && "外层碱性蚀刻".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("图形电镀", "二次钻孔", 48, bean);
                if (bean_1 != bean) {
                    String[] count = policeMess(bean_1, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                }
                TimeControllTemp bean_2 = timeout("二次钻孔", "外层碱性蚀刻", 72, bean);
                if (bean_2 != bean) {
                    String[] count = policeMess(bean_2, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                }
                bean.setStatus(2);
                timeControllTempMapper.updateTimeControll(bean);
            } else if ("铣外形".equals(bean.getStartdepartment()) && "最后检查".equals(bean.getEnddepartment())) {
                TimeControllTemp bean_1 = timeout("铣外形", "E-T测试", 96, bean);
                if (bean_1 != bean) {
                    String[] count = policeMess(bean_1, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                }
                TimeControllTemp bean_2 = timeout("E-T测试", "最后检查", 96, bean);
                if (bean_2 != bean) {
                    String[] count = policeMess(bean_2, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                }
                bean.setStatus(2);
                timeControllTempMapper.updateTimeControll(bean);
            } else if ("BDP棕化放板".equals(bean.getStartdepartment()) && "BDP叠板".equals(bean.getEnddepartment())) {
                if ("000".equals(bean.getWo().split("-")[3])) {
                    String[] count = policeMess(bean, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                } else {
                    bean.setStatus(2);
                    bean.setSon(1);
                    timeControllTempMapper.updateTimeControll(bean);
                    timeControllTempMapper.lockWorkOrderNumber(bean);
                }
            } else if ("BDP叠板".equals(bean.getStartdepartment()) && "钻孔".equals(bean.getEnddepartment())) {
                if ("000".equals(bean.getWo().split("-")[3])) {
                    String[] count = policeMess(bean, count2, count3).split(" ");
                    count2 = Integer.parseInt(count[0]);
                    count3 = Integer.parseInt(count[1]);
                } else {
                    bean.setStatus(2);
                    bean.setSon(1);
                    timeControllTempMapper.updateTimeControll(bean);
                    timeControllTempMapper.lockWorkOrderNumber(bean);
                }
            } else {
                String[] count = policeMess(bean, count2, count3).split(" ");
                count2 = Integer.parseInt(count[0]);
                count3 = Integer.parseInt(count[1]);
            }
        }
    }


    private TimeControllTemp timeout(String startdepartment, String enddepartment, int time, TimeControllTemp bean) {
        HashMap<String, String> map = new HashMap<>();
        map.put("startdepartment", startdepartment);
        map.put("enddepartment", enddepartment);
        map.put("wo", bean.getWo());
        List<TimeControllTemp> tctb = timeControllTempMapper.scanningTimeControllTempByRange(map);
        if (tctb.size() != 0) {
            if (tctb.get(0).getEnddate() == null) {
                long range_1 = System.currentTimeMillis() - tctb.get(0).getStartdate().getTime();
                long range_2 = time * 60 * 60 * 1000;
                if (range_2 < range_1) {
                    return tctb.get(0);
                }
            } else {
                long range_1 = tctb.get(0).getEnddate().getTime() - tctb.get(0).getStartdate().getTime();
                long range_2 = time * 60 * 60 * 1000;
                if (range_2 < range_1) {
                    return tctb.get(0);
                }
            }
        }
        return bean;
    }

    private void warningMess(TimeControllTemp bean) {
        StringBuffer text = new StringBuffer();
        text.append("\t\t\t预警信息\n");

        //二厂沉金和金手指在三厂做，需要区分厂别
        if (bean.getStartdepartment() != null
                && (bean.getStartdepartment().indexOf("沉金") != -1 || bean.getStartdepartment().indexOf("金手指") != -1)) {
            text.append("厂   别   :  " + bean.getFactoryname() + "\n");
        }

        text.append("工   序   :  " + bean.getStartdepartment() + "\n");
        text.append("工   卡   :  " + bean.getWo() + "\n");
        text.append("生产型号  :  " + bean.getMfg() + "\n");
        text.append("起始时间  :  " + sdf.format(bean.getStartdate()) + "\n");
        text.append("触发报警  :  " + bean.getAlarms() + "\n");
        text.append("预警时间  :  " + sdf.format(bean.getWarndate()));
        bean.setStatus(1);
        int status = timeControllTempMapper.updateTimeControll(bean);
        timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
        String msg = "{\n" +
                "   \"touser\" :  \"" + bean.getAuditor() + "\",\n" +
                "   \"msgtype\" : \"text\",\n" +
                "   \"agentid\" : 1000090,\n" +
                "   \"text\" : {\n" +
                "       \"content\" : \"" + text + "\"\n" +
                "   },\n" +
                "}";
        WeixinApiConfig.rotSendInfo(RobotsTarget.EARLY_WARNING_APP_KEY.getValue(), msg, 1);
    }

    private String policeMess(TimeControllTemp bean, int count2, int count3) {
        StringBuffer text = new StringBuffer();
        text.append("<font color=\\\"red\\\">\t\t\t\t报警信息</font>\n");

        //二厂沉金和金手指在三厂做，需要区分厂别
        if (bean.getStartdepartment() != null
                && (bean.getStartdepartment().contains("沉金") || bean.getStartdepartment().contains("金手指"))) {
            text.append("厂   别  :  " + bean.getFactoryname() + "\n");
        }

        text.append("工   序  :  " + bean.getStartdepartment() + "\n");
        text.append("工   卡  :  " + bean.getWo() + "\n");
        text.append("生产型号  :  " + bean.getMfg() + "\n");
        text.append("开始时间  :  " + sdf.format(bean.getStartdate()) + "\n");
        text.append("停留时间  :  " + bean.getAlarms() + "\n");
        text.append("报警时间  :  " + sdf.format(bean.getAlarmsdate()));
        bean.setStatus(2);
        bean.setSon(0);
        timeControllTempMapper.updateTimeControll(bean);
        String msg = "{\n" +
                "    \"msgtype\": \"markdown\",\n" +
                "    \"markdown\": {\n" +
                "        \"content\": \"" + text + "\",\n" +
                "    }\n" +
                "}";

        int status = timeControllTempMapper.lockWorkOrderNumber(bean);
        if (bean.getFactoryname() == 2) {
            WeixinApiConfig.rotSendInfo(RobotsTarget.CALL_POLICE_P2_KEY.getValue(), msg, 0);
            count2++;
        } else {
            WeixinApiConfig.rotSendInfo(RobotsTarget.CALL_POLICE_P3_KEY.getValue(), msg, 0);
            count3++;
        }
        if (count2 == 20 || count3 == 20) {
            count2 = 1;
            count3 = 1;
            try {
                Thread.sleep(1000 * 65);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return count2 + " " + count3;
    }

//    /**
//     * 预警扫描
//     * @param timeControllTempMapper
//     */
//    public static void scanningEarlyWarningTimeControllTemp(TimeControllTempMapper timeControllTempMapper){
//
//    }


//    /**
//     * 报警扫描  锁单
//     * @param timeControllTempMapper
//     */
//    public static void scanningCallPoliceTimeControllTemp(TimeControllTempMapper timeControllTempMapper, String robotID){
//
//    }


    /**
     * 预警扫描
     */
    public void scanningEarlyWarningTimeControllTempFourHour() {
        List<TimeControllTemp> beans = timeControllTempMapper.scanningEarlyWarningTimeControllTempFourHour();
        if (beans == null || beans.size() == 0) {
            return;
        }
        for (TimeControllTemp bean : beans) {
            if ("内层AOI".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment())) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("图形电镀".equals(bean.getStartdepartment()) && "外层碱性蚀刻".equals(bean.getEnddepartment())) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("铣外形".equals(bean.getStartdepartment()) && "最后检查".equals(bean.getEnddepartment())) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("内层AOI".equals(bean.getStartdepartment()) && "棕化".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("棕化".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("BDP棕化放板".equals(bean.getStartdepartment()) && "BDP叠板".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            } else if ("BDP叠板".equals(bean.getStartdepartment()) && "钻孔".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
                continue;
            }
            StringBuffer text = new StringBuffer();
            text.append("\t\t\t预警信息\n");
//            text.append("厂   别   :  " + bean.getFactoryname() + "\n");
            text.append("工   序   :  " + bean.getStartdepartment() + "\n");
            text.append("工   卡   :  " + bean.getWo() + "\n");
//            text.append("客户代码  :  " + bean.getCustcode() + "\n");
            text.append("生产型号  :  " + bean.getMfg() + "\n");
            text.append("起始时间  :  " + sdf.format(bean.getStartdate()) + "\n");
            text.append("触发报警  :  " + bean.getAlarms() + "\n");
            text.append("预警时间  :  " + sdf.format(bean.getWarndate()));
            timeControllTempMapper.updateTimeControllByLastWarningTime(bean);
            String msg = "{\n" +
                    "   \"touser\" :  \"" + bean.getAuditor() + "\",\n" +
                    "   \"msgtype\" : \"text\",\n" +
                    "   \"agentid\" : 1000090,\n" +
                    "   \"text\" : {\n" +
                    "       \"content\" : \"" + text + "\"\n" +
                    "   },\n" +
                    "}";
            WeixinApiConfig.rotSendInfo(RobotsTarget.EARLY_WARNING_APP_KEY.getValue(), msg, 1);
        }
    }


    public void scanningCallPoliceTimeControllTempFourHour() {
        List<TimeControllTemp> beans = timeControllTempMapper.scanningCallPoliceTimeControllTempFourHour();
        if (beans == null || beans.size() == 0) {
            return;
        }
        int count2 = 1;
        int count3 = 1;
        for (TimeControllTemp bean : beans) {
            if ("内层AOI".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment())) {
                continue;
            } else if ("图形电镀".equals(bean.getStartdepartment()) && "外层碱性蚀刻".equals(bean.getEnddepartment())) {
                continue;
            } else if ("铣外形".equals(bean.getStartdepartment()) && "最后检查".equals(bean.getEnddepartment())) {
                continue;
            } else if ("内层AOI".equals(bean.getStartdepartment()) && "棕化".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                continue;
            } else if ("棕化".equals(bean.getStartdepartment()) && "BDP棕化放板".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                continue;
            } else if ("BDP棕化放板".equals(bean.getStartdepartment()) && "BDP叠板".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                continue;
            } else if ("BDP叠板".equals(bean.getStartdepartment()) && "钻孔".equals(bean.getEnddepartment()) && !"000".equals(bean.getWo().split("-")[3])) {
                continue;
            }
            StringBuffer text = new StringBuffer();
            text.append("<font color=\\\"red\\\">\t\t\t\t报警信息</font>\n");
//            text.append("厂   别  :  " + bean.getFactoryname() + "\n");
            text.append("工   序  :  " + bean.getStartdepartment() + "\n");
            text.append("工   卡  :  " + bean.getWo() + "\n");
//            text.append("客户代码  :  " + bean.getCustcode() + "\n");
            text.append("生产型号  :  " + bean.getMfg() + "\n");
            text.append("开始时间  :  " + sdf.format(bean.getStartdate()) + "\n");
            text.append("停留时间  :  " + bean.getAlarms() + "\n");
            text.append("报警时间  :  " + sdf.format(bean.getAlarmsdate()));
            String msg = "{\n" +
                    "    \"msgtype\": \"markdown\",\n" +
                    "    \"markdown\": {\n" +
                    "        \"content\": \"" + text + "\",\n" +
                    "    }\n" +
                    "}";
            if (bean.getFactoryname() == 2) {
                WeixinApiConfig.rotSendInfo(RobotsTarget.CALL_POLICE_P2_KEY.getValue(), msg, 0);
                count2++;
            } else {
                WeixinApiConfig.rotSendInfo(RobotsTarget.CALL_POLICE_P3_KEY.getValue(), msg, 0);
                count3++;
            }

            if (count2 == 20 || count3 == 20) {
                count2 = 1;
                count3 = 1;
                try {
                    Thread.sleep(1000 * 65);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
