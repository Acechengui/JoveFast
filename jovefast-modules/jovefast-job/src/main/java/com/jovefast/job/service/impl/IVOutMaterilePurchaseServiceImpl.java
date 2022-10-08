package com.jovefast.job.service.impl;

import com.baomidou.dynamic.datasource.annotation.Slave;
import com.jovefast.common.core.apiinvoker.PushMessageLog;
import com.jovefast.common.core.qiwx.TokenUtilForWechat;
import com.jovefast.common.core.qiwx.WeixinApiConfig;
import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.file.FileUtils;
import com.jovefast.common.core.utils.poi.PoiExcelUtils;
import com.jovefast.common.core.apiinvoker.messagetype.ApplicationMessage;
import com.jovefast.job.domain.VRptWarehouse;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.mapper.VOutMaterilePurchaseMapper;
import com.jovefast.job.service.IVOutMaterilePurchaseService;
import com.jovefast.job.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料消耗情况接口实现
 */
@Service
@Slave
public class IVOutMaterilePurchaseServiceImpl implements IVOutMaterilePurchaseService {


    @Autowired
    VOutMaterilePurchaseMapper vOutMaterilePurchaseMapper;

    @Override
    public void materialConsumptionDetails(String temp_path) {
        String taskName = "每天早上自动推送昨天物料消耗情况";

        Map<String, Object> parms = new HashMap<>();
        parms.put("taskName",taskName);
        String lastRunDate = vOutMaterilePurchaseMapper.selectPlanjobsTaskLastRunDate(parms);

        //如果当天没有运行过
        if(lastRunDate == null || !lastRunDate.startsWith(DateUtils.getDate())){

            LocalDate localDate1 = LocalDate.now();
            String yesterday = localDate1.minus(1, ChronoUnit.DAYS).toString();    //获取昨天的日期



            //构建综合数据搜索参数
            VRptWarehouse params_all = new VRptWarehouse();
            params_all.setTime1(yesterday);
            params_all.setTime2(yesterday);
            params_all.setQueryCode("1");
            List<VRptWarehouse> inDTOList_all = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseOut(params_all));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_all = ListUtil.groupVrsByUnitName(inDTOList_all.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_all = ListUtil.proportionOfMaterial(inDTOList_all, map_all);
            vrs_all.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P1厂数据搜索参数
            VRptWarehouse params_p1 = new VRptWarehouse();
            params_p1.setLocation("一");
            params_p1.setTime1(yesterday);
            params_p1.setTime2(yesterday);
            params_p1.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p1 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseOut(params_p1));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p1 = ListUtil.groupVrsByUnitName(inDTOList_p1.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p1 = ListUtil.proportionOfMaterial(inDTOList_p1, map_p1);
            vrs_p1.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P2厂数据搜索参数
            VRptWarehouse params_p2 = new VRptWarehouse();
            params_p2.setLocation("二");
            params_p2.setTime1(yesterday);
            params_p2.setTime2(yesterday);
            params_p2.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p2 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseOut(params_p2));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p2 = ListUtil.groupVrsByUnitName(inDTOList_p2.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p2 = ListUtil.proportionOfMaterial(inDTOList_p2, map_p2);
            vrs_p2.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P3厂数据搜索参数
            VRptWarehouse params_p3 = new VRptWarehouse();
            params_p3.setLocation("三");
            params_p3.setTime1(yesterday);
            params_p3.setTime2(yesterday);
            params_p3.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p3 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseOut(params_p3));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p3 = ListUtil.groupVrsByUnitName(inDTOList_p3.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p3 = ListUtil.proportionOfMaterial(inDTOList_p3, map_p3);
            vrs_p3.sort(Comparator.comparing(VRptWarehouse::getProductName));

            //表头
            String[] headers = {"物料类别", "消耗数量", "本币总价", "金额占比"};


            //封装excel数据对象
            List<Map<String,List<Object[]>>> sheetMaps = new ArrayList<>();



            //新建工作区
            Map<String,List<Object[]>> sheet_all = new HashMap<>();
            List<Object[]> sheet_all_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_all_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_all){
                Object[] sheet_all_items_datas = {vr.getProductName(), vr.getQtyIssued(), vr.getPricesum(), String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_all_items.add(sheet_all_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_all.put("综合",sheet_all_items);
            sheetMaps.add(sheet_all);



            //新建工作区
            Map<String,List<Object[]>> sheet_p1 = new HashMap<>();
            List<Object[]> sheet_p1_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p1_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p1){
                Object[] sheet_p1_items_datas = {vr.getProductName(), vr.getQtyIssued(), vr.getPricesum(), String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p1_items.add(sheet_p1_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p1.put("P1厂",sheet_p1_items);
            sheetMaps.add(sheet_p1);



            //新建工作区
            Map<String,List<Object[]>> sheet_p2 = new HashMap<>();
            List<Object[]> sheet_p2_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p2_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p2){
                Object[] sheet_p2_items_datas = {vr.getProductName(), vr.getQtyIssued(), vr.getPricesum(), String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p2_items.add(sheet_p2_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p2.put("P2厂",sheet_p2_items);
            sheetMaps.add(sheet_p2);




            //新建工作区
            Map<String,List<Object[]>> sheet_p3 = new HashMap<>();
            List<Object[]> sheet_p3_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p3_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p3){
                Object[] sheet_p3_items_datas = {vr.getProductName(), vr.getQtyIssued(), vr.getPricesum(), String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p3_items.add(sheet_p3_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p3.put("P3厂",sheet_p3_items);
            sheetMaps.add(sheet_p3);



            //企业微信推送目标
            ArrayList<String> users = new ArrayList<>();
            users.add("ZhaoQingYuan-p3-GongYiBu");
            users.add("LiZhongMing-p3-itBu");
            users.add("ZhangShiYing");
            users.add("donald");
            users.add("ben");


            //导出的数据表格路径
            String path = temp_path.replace("/","\\") + "自动导出报临时缓存表\\昨天物料消耗情况";

            //导出的数据表格文件名
            String fileName = yesterday + "各物料消耗情况";

            //获取密钥
            String access_token = null;
            try {
                access_token = new TokenUtilForWechat().getToken(RobotsTarget.MESSAGE_APP_KEY.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }


            //创建目录
            FileUtils.createDirectory(path);

            //导出Excel数据表格
            PoiExcelUtils.excelExport(sheetMaps, path + "\\", fileName);

            //上传导出的数据到企业微信,并且返回文件key
            String media_id = WeixinApiConfig.temporaryFile(new File(path + "\\" + fileName + ".xls"),RobotsTarget.MESSAGE_APP_KEY.getValue(),1);

            //上传完成之后删除表格文件
            FileUtils.deleteFile(path + "\\" + fileName + ".xls");

            //推送
            PushMessageLog pushMessageLog = ApplicationMessage.fileMessageSend(access_token, 1000079, String.join("|", users), media_id);

            //运行完成保存日志
            Map<String, Object> paramsRunLog = new HashMap<>();
            paramsRunLog.put("TASK_NAME",taskName);
            paramsRunLog.put("TOKEN",pushMessageLog.getAccesstoken());
            paramsRunLog.put("AGENT_ID",pushMessageLog.getAgentid());
            paramsRunLog.put("TO_USERS",pushMessageLog.getTousers());
            paramsRunLog.put("MEDIA_ID",pushMessageLog.getMediaid());
            paramsRunLog.put("ERROR_CODE",pushMessageLog.getErrorcode());

            vOutMaterilePurchaseMapper.insertPlanjobsTaskRunLog(paramsRunLog);

        }
    }

    @Override
    public void materialPurchaseDetails(String temp_path) {
        String taskName = "每月初自动推送上个月物料购买情况";

        Map<String, Object> parms = new HashMap<>();
        parms.put("taskName",taskName);
        String lastRunDate = vOutMaterilePurchaseMapper.selectPlanjobsTaskLastRunDate(parms);

        //每个月只成功运行一次
        if(lastRunDate == null || !lastRunDate.startsWith(DateUtils.getDate().substring(0,7))){
            LocalDate date = LocalDate.now();
            LocalDate lastMonth = date.minusMonths(1); // 当前月份减1
            String firstDay = lastMonth.with(TemporalAdjusters.firstDayOfMonth()).toString(); // 获取当前月的第一天
            String lastDay = lastMonth.with(TemporalAdjusters.lastDayOfMonth()).toString(); // 获取当前月的最后一天



            //构建综合数据搜索参数
            VRptWarehouse params_all = new VRptWarehouse();
            params_all.setTime1(firstDay);
            params_all.setTime2(lastDay);
            params_all.setQueryCode("1");
            List<VRptWarehouse> inDTOList_all = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseIn(params_all));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_all = ListUtil.groupVrsByUnitName(inDTOList_all.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_all = ListUtil.proportionOfMaterial(inDTOList_all, map_all);
            vrs_all.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P1厂数据搜索参数
            VRptWarehouse params_p1 = new VRptWarehouse();
            params_p1.setLocation("一");
            params_p1.setTime1(firstDay);
            params_p1.setTime2(lastDay);
            params_p1.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p1 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseIn(params_p1));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p1 = ListUtil.groupVrsByUnitName(inDTOList_p1.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p1 = ListUtil.proportionOfMaterial(inDTOList_p1, map_p1);
            vrs_p1.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P2厂数据搜索参数
            VRptWarehouse params_p2 = new VRptWarehouse();
            params_p2.setLocation("二");
            params_p2.setTime1(firstDay);
            params_p2.setTime2(lastDay);
            params_p2.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p2 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseIn(params_p2));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p2 = ListUtil.groupVrsByUnitName(inDTOList_p2.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p2 = ListUtil.proportionOfMaterial(inDTOList_p2, map_p2);
            vrs_p2.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //构建P3厂数据搜索参数
            VRptWarehouse params_p3 = new VRptWarehouse();
            params_p3.setLocation("三");
            params_p3.setTime1(firstDay);
            params_p3.setTime2(lastDay);
            params_p3.setQueryCode("1");
            List<VRptWarehouse> inDTOList_p3 = ListUtil.materialClassification(vOutMaterilePurchaseMapper.selectVRptWarehouseIn(params_p3));
            //根据字段进行分组
            Map<String, List<VRptWarehouse>> map_p3 = ListUtil.groupVrsByUnitName(inDTOList_p3.stream().collect(
                    Collectors.groupingBy(
                            VRptWarehouse::getProductName
                    )));
            List<VRptWarehouse> vrs_p3 = ListUtil.proportionOfMaterial(inDTOList_p3, map_p3);
            vrs_p3.sort(Comparator.comparing(VRptWarehouse::getProductName));


            //表头
            String[] headers = {"物料类别", "购买数量","本币总价", "金额占比"};


            //封装excel数据对象
            List<Map<String,List<Object[]>>> sheetMaps = new ArrayList<>();


            //新建工作区
            Map<String,List<Object[]>> sheet_all = new HashMap<>();
            List<Object[]> sheet_all_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_all_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_all){
                Object[] sheet_all_items_datas = {vr.getProductName(),vr.getQtyIssued(),vr.getPricesum(),String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_all_items.add(sheet_all_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_all.put("综合",sheet_all_items);
            sheetMaps.add(sheet_all);



            //新建工作区
            Map<String,List<Object[]>> sheet_p1 = new HashMap<>();
            List<Object[]> sheet_p1_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p1_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p1){
                Object[] sheet_p1_items_datas = {vr.getProductName(),vr.getQtyIssued(),vr.getPricesum(),String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p1_items.add(sheet_p1_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p1.put("P1厂",sheet_p1_items);
            sheetMaps.add(sheet_p1);



            //新建工作区
            Map<String,List<Object[]>> sheet_p2 = new HashMap<>();
            List<Object[]> sheet_p2_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p2_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p2){
                Object[] sheet_p2_items_datas = {vr.getProductName(),vr.getQtyIssued(),vr.getPricesum(),String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p2_items.add(sheet_p2_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p2.put("P2厂",sheet_p2_items);
            sheetMaps.add(sheet_p2);



            //新建工作区
            Map<String,List<Object[]>> sheet_p3 = new HashMap<>();
            List<Object[]> sheet_p3_items = new ArrayList<>();
            //设置表头（自动将第一行设置为表头）
            sheet_p3_items.add(headers);
            //循环查询到的数据，封装excel数据对象
            for(VRptWarehouse vr : vrs_p3){
                Object[] sheet_p3_items_datas = {vr.getProductName(),vr.getQtyIssued(),vr.getPricesum(),String.format("%10.5f%%", vr.getProportion() * 100)};
                sheet_p3_items.add(sheet_p3_items_datas);
            }
            //key是工作区的名称，value是工作区的数据
            sheet_p3.put("P3厂",sheet_p3_items);
            sheetMaps.add(sheet_p3);



            //企业微信推送目标
            ArrayList<String> users = new ArrayList<>();
            users.add("ZhaoQingYuan-p3-GongYiBu");
            users.add("LiZhongMing-p3-itBu");
            users.add("ZhangShiYing");
            users.add("donald");
            users.add("ben");


            //导出的数据表格路径
            String path = temp_path.replace("/","\\") + "自动导出报临时缓存表\\每月物料购买情况";

            //导出的数据表格文件名
            String fileName = firstDay + "至" + lastDay + "各物料购买情况";

            //获取密钥
            String access_token = null;
            try {
                access_token = new TokenUtilForWechat().getToken(RobotsTarget.MESSAGE_APP_KEY.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }


            //创建目录
            FileUtils.createDirectory(path);

            //导出Excel数据表格
            PoiExcelUtils.excelExport(sheetMaps, path + "\\", fileName);

            //上传导出的数据到企业微信,并且返回文件key
            String media_id = WeixinApiConfig.temporaryFile(new File(path + "\\" + fileName + ".xls"),RobotsTarget.MESSAGE_APP_KEY.getValue(),1);

            //上传完成之后删除表格文件
            FileUtils.deleteFile(path + "\\" + fileName + ".xls");

            //推送
            PushMessageLog pushMessageLog = ApplicationMessage.fileMessageSend(access_token, 1000079, String.join("|", users), media_id);

            //运行完成保存日志
            Map<String, Object> paramsRunLog = new HashMap<>();
            paramsRunLog.put("TASK_NAME",taskName);
            paramsRunLog.put("TOKEN",pushMessageLog.getAccesstoken());
            paramsRunLog.put("AGENT_ID",pushMessageLog.getAgentid());
            paramsRunLog.put("TO_USERS",pushMessageLog.getTousers());
            paramsRunLog.put("MEDIA_ID",pushMessageLog.getMediaid());
            paramsRunLog.put("ERROR_CODE",pushMessageLog.getErrorcode());

            vOutMaterilePurchaseMapper.insertPlanjobsTaskRunLog(paramsRunLog);

        }
    }
}
