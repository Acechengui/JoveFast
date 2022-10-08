package com.jovefast.job.task;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.file.FileUtils;
import com.jovefast.common.core.utils.poi.PoiExcelUtils;
import com.jovefast.job.domain.dto.VCustomerWipDTO;
import com.jovefast.job.domain.dto.VfinishedgoodsInventoryStatDTO;
import com.jovefast.job.domain.excelModel.MaterialConsumptionTableModel;
import com.jovefast.job.domain.dto.OutboundRecordDTO;
import com.jovefast.job.domain.dto.ProductionProcessOverDTO;
import com.jovefast.job.domain.dto.UnboundBoxLabelDTO;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.service.*;
import com.jovefast.job.util.QiWxUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jovefast.common.core.utils.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时任务调度
 *
 * @author Acechengui
 */
@Component("joveTask")
public class JoveTask {
    private static final Logger log = LoggerFactory.getLogger(JoveTask.class);

    /**
     * 临时文件路径
     */
    private final static String TEMP_DIR = System.getProperty("user.dir") + "/temp/";

    @Autowired
    private IErpPurchaseService iErpPurchaseService;

    @Autowired
    private IErpSpotCheckService ierpspotcheckservice;

    @Autowired
    private IErpCustomerWIPService iCustomerWIPService;

    @Autowired
    private ITimeControllTempService iTimeControllTempService;

    @Autowired
    private IVOutMaterilePurchaseService ivOutMaterilePurchaseService;

    @Autowired
    private ILockMessageService iLockMessageService;

    @Autowired
    private IPackRelatedService iPackRelatedService;

    @Resource
    private IBoxLabelService boxLabelService;


    public void sendDeliveryInstruction() {
        iPackRelatedService.sendDeliveryInstruction();
    }

    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void noParams() {
        System.out.println("执行无参方法");
    }

    /**
     * 点检图片数据 保留一年，超一年则清除
     */
    public void runTask() {
        ierpspotcheckservice.delExpireData();
    }

    /**
     * 超时管控  每一小时扫描一次
     */
    public void runScanningTimeControllTemp() {
        iTimeControllTempService.scanningEarlyWarningTimeControllTemp();
        iTimeControllTempService.scanningCallPoliceTimeControllTemp();
    }

    /**
     * 每天各物料消耗情况
     * 多次运行防止特殊无法运行的情况，每天只会成功运行一次
     */
    public void materialConsumptionDetails() {
        ivOutMaterilePurchaseService.materialConsumptionDetails(TEMP_DIR);
    }

    /**
     * 每月各物料购买情况
     * 多次运行防止特殊无法运行的情况，每个月只会成功运行一次
     */
    public void materialPurchaseDetails() {
        ivOutMaterilePurchaseService.materialPurchaseDetails(TEMP_DIR);
    }

    /**
     * 过数锁定超时报警
     */
    public void theLockTimeoutPush() {
        iLockMessageService.theLockTimeoutPush();
    }

    /**
     * 过数锁定消息推送
     */
    public void hierarchicalClassificationLockPush() {
        iLockMessageService.hierarchicalClassificationLockPush();
    }

    /**
     * 物料消耗月报总表
     */
    public void task1() throws ParseException, IOException {
        String proFromTime = DateUtils.singleDate(1) + "-01-01 08:00:00";
        //"2022-02-01 08:00:00"
        String proArriveTime = DateUtils.getFirstDayMonth(3, 1) + " 08:00:00";
        List<ProductionProcessOverDTO> proList = iErpPurchaseService.searchProductionProcessOverList(proFromTime, proArriveTime);
        String outFromTime = DateUtils.singleDate(1) + "-01-01";
        String outArriveTime = DateUtils.getFirstDayMonth(1, 2);
        List<OutboundRecordDTO> outList = iErpPurchaseService.searchOutboundRecordList(outFromTime, outArriveTime);
//        List<OutboundRecordDTO> outList = iErpPurchaseService.testSearchOutboundRecordList();
        //导出的数据表格路径--  此处改为在项目运行陌路创建，避免服务器没有D盘
        String path = TEMP_DIR + "物料消耗月报总表";

        String fileName = "最新物料消耗表_" + DateUtils.dateTimeNow();

        String suffix = ".xlsx";

        //创建目录
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream output = new FileOutputStream(path + "/" + fileName + suffix);
        XSSFWorkbook ex = new MaterialConsumptionTableModel().MaterialConsumptionTableExcel(proList, outList);
        ex.write(output);
        ex.close();
        output.close();
        //企业微信群机器人 推送信息
        QiWxUtil.forFile(RobotsTarget.TEST_BUSINESS_KEY.getValue(), path + "/" + fileName + suffix);
        //上传完成之后删除表格文件
        FileUtils.delFile(path + "/" + fileName + suffix);
    }

    /**
     * 市场跟单表
     */
    public void task2() throws Exception {
        File file = new File(TEMP_DIR + "自动导出报表/市场跟单表/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = TEMP_DIR + "自动导出报表/市场跟单表/";
        String excelname = "市场跟单表_汇总";
        // 保存查询参数的map
        Map<String, Object> ps = new HashMap<String, Object>(5);
        ps.put("status", "'有效','暂停'");
        // -1:全部 0 :国内 1:美国 2:台湾
        ps.put("customerregion", -1);
        String suffix = ".xlsx";
        //预警
        List<VCustomerWipDTO> wipList = iCustomerWIPService.earlyWarn(ps);
        LinkedHashMap<String, String> propertyHeaderMap = getTile(6);
        SXSSFWorkbook ex = PoiExcelUtils.generateXlsxWorkbook(excelname, propertyHeaderMap, wipList);
        FileOutputStream output = new FileOutputStream(path + excelname + suffix);
        //写入磁盘
        ex.write(output);
        Objects.requireNonNull(output).close();
        Objects.requireNonNull(ex).close();
        //分组导出
        vCustomerWIPGroup(wipList, path, suffix);
        //导出全部异常
        vCustomerWIPabnormal(wipList, path, suffix);
        // 企业微信群机器人 推送信息
        QiWxUtil.forFiles(RobotsTarget.VCWIP_KEY.getValue(), path);
        //上传完成之后删除表格文件
        FileUtils.deleteDir(new File(path));
    }

    /**
     * 美国客户WIP
     */
    public void task3() throws Exception {
        File file = new File(TEMP_DIR + "自动导出报表/美国客户WIP/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = TEMP_DIR + "自动导出报表/美国客户WIP/";
        String excelname = "美国客户WIP";
        // 保存查询参数的map
        Map<String, Object> ps = new HashMap<String, Object>(5);
        ps.put("status", "'有效','暂停'");
        // -1:全部 0 :国内 1:美国 2:台湾
        ps.put("customerregion", 1);
        String suffix = ".xlsx";
        //预警
        List<VCustomerWipDTO> wipList = iCustomerWIPService.earlyWarn(ps);
        LinkedHashMap<String, String> propertyHeaderMap = getTile(9);
        SXSSFWorkbook ex = PoiExcelUtils.generateXlsxWorkbook(excelname, propertyHeaderMap, wipList);
        FileOutputStream output = new FileOutputStream(path + excelname + suffix);
        //写入磁盘
        ex.write(output);
        Objects.requireNonNull(output).close();
        Objects.requireNonNull(ex).close();
        // 企业微信群机器人 推送信息
        QiWxUtil.forFile(RobotsTarget.US_BUSINESS_KEY.getValue(), path + excelname + suffix);
        //上传完成之后删除表格文件
        FileUtils.deleteDir(new File(path + excelname + suffix));
    }

    /**
     * 成品出货超周期预警(提前一个月提醒市场)
     *
     * @param custCode    客户代码(多个客户代码用 "," 分割)
     * @param days        指定客户管控天数
     * @param defaultDays 其他客户默认管控天数
     */
    public void task4(String custCode, Integer days, Integer defaultDays) throws IOException {
        File file = new File(TEMP_DIR + "自动导出报表/成品库存超周期/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = TEMP_DIR + "自动导出报表/成品库存超周期/";
        String excelname = "成品库存超周期";
        String suffix = ".xlsx";
        List<VfinishedgoodsInventoryStatDTO> dtoList = iTimeControllTempService.finishedGoodInventoryOverCycleAlert(custCode, days, defaultDays);
        LinkedHashMap<String, String> propertyHeaderMap = getTile(10);
        SXSSFWorkbook ex = PoiExcelUtils.generateXlsxWorkbook(excelname, propertyHeaderMap, dtoList);
        FileOutputStream output = new FileOutputStream(path + excelname + suffix);
        //写入磁盘
        ex.write(output);
        Objects.requireNonNull(output).close();
        Objects.requireNonNull(ex).close();
        // 企业微信群机器人 推送信息
        QiWxUtil.forFiles(RobotsTarget.INVENTORY_OVERRUN.getValue(), path);
        //上传完成之后删除表格文件
        FileUtils.deleteDir(new File(path));
    }

    /**
     * 未与货架绑定的入仓签
     */
    public void task5() throws Exception {
        String yesterday = DateUtils.getyesterday();
        String path = TEMP_DIR + "自动导出报表/未与货架绑定的入仓签/";
        String excelName1 = "一厂未与货架绑定的入仓签";
        // 二厂、三厂暂时不推送
//        String excelName2 = "二厂未与货架绑定的入仓签";
//        String excelName3 = "三厂未与货架绑定的入仓签";
        String suffix = ".xlsx";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        LinkedHashMap<String, String> propertyHeaderMap = getTile(11);
        // 一厂未与货架绑定的入仓签
        List<UnboundBoxLabelDTO> list1 = boxLabelService.selectUnboundBoxLabelList(yesterday, 1);
        SXSSFWorkbook ex1 = PoiExcelUtils.generateXlsxWorkbook(excelName1, propertyHeaderMap, list1);
        FileOutputStream output1 = new FileOutputStream(path + excelName1 + suffix);
        // 二厂未与货架绑定的入仓签
//        List<UnboundBoxLabelDTO> list2 = boxLabelService.selectUnboundBoxLabelList(yesterday, 2);
//        SXSSFWorkbook ex2 = PoiExcelUtils.generateXlsxWorkbook(excelName2, propertyHeaderMap, list2);
//        FileOutputStream output2 = new FileOutputStream(path + excelName2 + suffix);
        // 三厂未与货架绑定的入仓签
//        List<UnboundBoxLabelDTO> list3 = boxLabelService.selectUnboundBoxLabelList(yesterday, 3);
//        SXSSFWorkbook ex3 = PoiExcelUtils.generateXlsxWorkbook(excelName3, propertyHeaderMap, list3);
//        FileOutputStream output3 = new FileOutputStream(path + excelName3 + suffix);
        // 写入磁盘
        ex1.write(output1);
        Objects.requireNonNull(output1).close();
        Objects.requireNonNull(ex1).close();
//        ex2.write(output2);
//        Objects.requireNonNull(output2).close();
//        Objects.requireNonNull(ex2).close();
//        ex3.write(output3);
//        Objects.requireNonNull(output3).close();
//        Objects.requireNonNull(ex3).close();
        // 企业微信群机器人 推送信息
        // 推送到一厂提醒群
        QiWxUtil.forFile(RobotsTarget.UNBOUND_BOX_LABEL_ONE.getValue(), path + excelName1 + suffix);
        // 推送到二厂提醒群
//        QiWxUtil.forFile(RobotsTarget.TEST_BUSINESS_KEY.getValue(), path + excelName2 + suffix);
        // 推送到三厂提醒群
//        QiWxUtil.forFile(RobotsTarget.TEST_BUSINESS_KEY.getValue(), path + excelName3 + suffix);
        // 上传完成之后删除表格文件
        FileUtils.deleteDir(new File(path));
    }

    /**
     * 分组导出
     */
    private void vCustomerWIPGroup(List<VCustomerWipDTO> wipList, String path, String suffix) throws Exception {
        //根据字段进行分组
        Map<String, List<VCustomerWipDTO>> map = wipList.stream().filter(fi -> StringUtils.isNotNull(fi.getAnalysiscode3()) && StringUtils.isNotEmpty(fi.getAnalysiscode3())).collect(
                Collectors.groupingBy(
                        VCustomerWipDTO::getAnalysiscode3
                ));
        SXSSFWorkbook ex = null;
        FileOutputStream output = null;
        for (String s : map.keySet()) {
            String excelname = "市场跟单表_" + s;
            LinkedHashMap<String, String> propertyHeaderMap = getTile(6);
            ex = PoiExcelUtils.generateXlsxWorkbook(excelname, propertyHeaderMap, map.get(s));
            output = new FileOutputStream(path + "/" + excelname + suffix);
            ex.write(output);
        }
        Objects.requireNonNull(output).close();
        Objects.requireNonNull(ex).close();
    }

    /**
     * 只导出异常
     */
    private void vCustomerWIPabnormal(List<VCustomerWipDTO> wipList, String path, String suffix) throws Exception {
        String excelname = "市场跟单表_异常汇总";
        //根据字段进行筛选
        List<VCustomerWipDTO> collect = wipList.stream().filter(fi -> "异常".equals(fi.getEarlywarnstatus())).collect(Collectors.toList());
        LinkedHashMap<String, String> propertyHeaderMap = getTile(6);
        SXSSFWorkbook ex = PoiExcelUtils.generateXlsxWorkbook(excelname, propertyHeaderMap, collect);
        FileOutputStream output = new FileOutputStream(path + "/" + excelname + suffix);
        ex.write(output);
        Objects.requireNonNull(output).close();
        Objects.requireNonNull(ex).close();
    }

    /**
     * 无需制作excel模板的
     */
    private LinkedHashMap<String, String> getTile(int flag) {
        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
        switch (flag) {
            case 0: {
                propertyHeaderMap.put("work_order_number", "工单号");
                propertyHeaderMap.put("prod_spec_01", "层数");
                propertyHeaderMap.put("tdate", "申请日期");
                propertyHeaderMap.put("cust_abc", "正负片");
                propertyHeaderMap.put("customer_part_number", "生产编号");
                propertyHeaderMap.put("cust_code", "客户代码");
                propertyHeaderMap.put("sales_order", "MO号");
                propertyHeaderMap.put("qty_reject", "报废数量");
                propertyHeaderMap.put("qty_reject_area", "报废面积");
                propertyHeaderMap.put("rej_code", "缺陷代码");
                propertyHeaderMap.put("reject_description", "缺陷名称");
                propertyHeaderMap.put("dept_name", "责任部门");
                propertyHeaderMap.put("applydeptname", "申请部门");
                propertyHeaderMap.put("pnl_planned", "开料数(PNL)");
                propertyHeaderMap.put("set_pnl", "拼版数(SET_PNL)");
                propertyHeaderMap.put("set_planned", "开料数(SET)");
                propertyHeaderMap.put("setl", "set长(mm)");
                propertyHeaderMap.put("setw", "set宽(mm)");
                propertyHeaderMap.put("qty_instock", "入库数(set)");
                propertyHeaderMap.put("lotno_1", "LOT号+周期");
                propertyHeaderMap.put("lotno_3", "LOT号");
                propertyHeaderMap.put("price", "价格");
                propertyHeaderMap.put("thickness", "成品板厚");
                propertyHeaderMap.put("outsideThickness", "外铜厚");
                propertyHeaderMap.put("lot_cut_pnl", "LOT开料数量(PNL)");
                propertyHeaderMap.put("pcs_pnl", "连片数(PCS_PNL)");
                propertyHeaderMap.put("set_qtyreject", "总报废数量(SET)");
                propertyHeaderMap.put("lot_cut_set", "LOT开料数量(SET)");
                propertyHeaderMap.put("set_plannedarea", "MO开料成品面积(SET)");
                propertyHeaderMap.put("set_rejectarea", "总报废面积(SET)");
                propertyHeaderMap.put("prod_plant", "生产厂别");
                propertyHeaderMap.put("rej_plant", "报废厂别");
                propertyHeaderMap.put("abbr_name", "雇员名");
                propertyHeaderMap.put("qrcode", "二维码信息");
                propertyHeaderMap.put("note_pad_line", "备注");
                propertyHeaderMap.put("note1", "备注");
                break;
            }
            case 1: {
                propertyHeaderMap.put("prod_part", "prod_part");
                propertyHeaderMap.put("sales_order", "sales_order");
                propertyHeaderMap.put("item_nbr", "item_nbr");
                propertyHeaderMap.put("so_status", "so_status");
                propertyHeaderMap.put("cust_code", "cust_code");
                propertyHeaderMap.put("cust_name", "cust_name");
                propertyHeaderMap.put("customer_part_number", "customer_part_number");
                propertyHeaderMap.put("customer_part_desc", "customer_part_desc");
                propertyHeaderMap.put("cust_part_rev", "cust_part_rev");
                propertyHeaderMap.put("setl", "setl");
                propertyHeaderMap.put("setw", "setw");
                propertyHeaderMap.put("ent_date", "ent_date");
                propertyHeaderMap.put("conf_date", "conf_date");
                propertyHeaderMap.put("due_date", "due_date");
                propertyHeaderMap.put("sch_date", "sch_date");
                propertyHeaderMap.put("sales_name", "sales_name");
                propertyHeaderMap.put("unit_name", "unit_name");
                propertyHeaderMap.put("order_qty", "order_qty");
                propertyHeaderMap.put("pcs_set", "pcs_set");
                propertyHeaderMap.put("devli_qty", "devli_qty");
                propertyHeaderMap.put("qty_instock", "qty_instock");
                propertyHeaderMap.put("po_number", "po_number");
                propertyHeaderMap.put("basethickness", "basethickness");
                propertyHeaderMap.put("finishedthickness", "finishedthickness");
                propertyHeaderMap.put("layernum", "layernum");
                propertyHeaderMap.put("technic", "technic");
                propertyHeaderMap.put("ship_to_addr", "ship_to_addr");
                propertyHeaderMap.put("curr_name", "curr_name");
                propertyHeaderMap.put("part_price", "part_price");
                propertyHeaderMap.put("amount_jiaji", "amount_jiaji");
                propertyHeaderMap.put("amount_eng", "amount_eng");
                propertyHeaderMap.put("amount_film", "amount_film");
                propertyHeaderMap.put("amount_model", "amount_model");
                propertyHeaderMap.put("amount_jiaju", "amount_jiaju");
                propertyHeaderMap.put("amount_feizhen", "amount_feizhen");
                propertyHeaderMap.put("amount_king", "amount_king");
                propertyHeaderMap.put("amount_gangwang", "amount_gangwang");
                propertyHeaderMap.put("amount_freight", "amount_freight");
                propertyHeaderMap.put("amount_commission", "amount_commission");
                propertyHeaderMap.put("amount_other", "amount_other");
                propertyHeaderMap.put("state_id", "state_id");
                propertyHeaderMap.put("state_tax", "state_tax");
                propertyHeaderMap.put("state_prod_tax_flag", "state_prod_tax_flag");
                propertyHeaderMap.put("state_ship_tax_flag", "state_ship_tax_flag");
                propertyHeaderMap.put("state_tool_tax_flag", "state_tool_tax_flag");
                propertyHeaderMap.put("amount_total", "amount_total");
                propertyHeaderMap.put("prod_plant", "prod_plant");
                propertyHeaderMap.put("price", "单价");
                propertyHeaderMap.put("area", "面积");
                propertyHeaderMap.put("area_price", "平米价");
                propertyHeaderMap.put("sum_amount", "总金额汇总");
                propertyHeaderMap.put("note_pad_line", "备注");
                break;
            }
            case 2: {
                propertyHeaderMap.put("so_status", "so_status");
                propertyHeaderMap.put("prod_part", "prod_part");
                propertyHeaderMap.put("sales_name", "sales_name");
                propertyHeaderMap.put("sales_order", "sales_order");
                propertyHeaderMap.put("item_nbr", "item_nbr");
                propertyHeaderMap.put("customer_part_number", "customer_part_number");
                propertyHeaderMap.put("po_number", "po_number");
                propertyHeaderMap.put("cust_code", "cust_code");
                propertyHeaderMap.put("cust_name", "cust_name");
                propertyHeaderMap.put("setl", "setl");
                propertyHeaderMap.put("setw", "setw");
                propertyHeaderMap.put("ent_date", "ent_date");
                propertyHeaderMap.put("sch_date", "sch_date");
                propertyHeaderMap.put("due_date", "due_date");
                propertyHeaderMap.put("order_qty", "order_qty");
                propertyHeaderMap.put("unit_name", "unit_name");
                propertyHeaderMap.put("pcs_set", "pcs_set");
                propertyHeaderMap.put("mrb_Cm2", "mrb_Cm2");
                propertyHeaderMap.put("set_Price", "Set板费");
                propertyHeaderMap.put("pcs_Price", "pcs板费");
                propertyHeaderMap.put("net_Price", "净价");
                propertyHeaderMap.put("set_Net_Price", "set板费");
                propertyHeaderMap.put("pcs_Net_Price", "pcs板费");
                propertyHeaderMap.put("amount_commission", "amount_commission");
                propertyHeaderMap.put("amount_gangwang", "amount_gangwang");
                propertyHeaderMap.put("part_price", "part_price");
                propertyHeaderMap.put("amount_king", "amount_king");
                propertyHeaderMap.put("amount_eng", "amount_eng");
                propertyHeaderMap.put("amount_jiaju", "amount_jiaju");
                propertyHeaderMap.put("amount_jiaji", "amount_jiaji");
                propertyHeaderMap.put("amount_other", "amount_other");
                propertyHeaderMap.put("amount_film", "amount_film");
                propertyHeaderMap.put("amount_feizhen", "amount_feizhen");
                propertyHeaderMap.put("amount_freight", "amount_freight");
                propertyHeaderMap.put("amount", "运费");
                propertyHeaderMap.put("commission", "佣金");
                propertyHeaderMap.put("amount_Proportion", "运费比例");
                propertyHeaderMap.put("commission_Proportion", "佣金比例");
                propertyHeaderMap.put("amount_totals", "amount_total");
                propertyHeaderMap.put("curr_name", "curr_name");
                propertyHeaderMap.put("state_tax", "state_tax");
                propertyHeaderMap.put("layernum", "layernum");
                propertyHeaderMap.put("technic", "technic");
                propertyHeaderMap.put("so_remark", "so_remark");
                propertyHeaderMap.put("pre_Eng_Remark", "pre_Eng_Remark");
                propertyHeaderMap.put("basethickness", "basethickness");
                propertyHeaderMap.put("finishedthickness", "finishedthickness");
                propertyHeaderMap.put("ship_fob", "ship_fob");
                propertyHeaderMap.put("description", "description");
                propertyHeaderMap.put("prod_Part_Number", "prod_Part_Number");
                propertyHeaderMap.put("state_prod_tax_flag", "state_prod_tax_flag");
                propertyHeaderMap.put("state_ship_tax_flag", "state_ship_tax_flag");
                propertyHeaderMap.put("state_tool_tax_flag", "state_tool_tax_flag");
                break;
            }
            case 3: {
                propertyHeaderMap.put("po", "Po");
                propertyHeaderMap.put("orderline", "Orderline");
                propertyHeaderMap.put("articleno", "Articleno");
                propertyHeaderMap.put("squaremeter", "Square Meters");
                propertyHeaderMap.put("cutting", "Cutting");
                propertyHeaderMap.put("cuttingstatus", "Cutting Status");
                propertyHeaderMap.put("press", "Press:");
                propertyHeaderMap.put("pressstatus", "Press Status");
                propertyHeaderMap.put("patternplating", "Pattern Plating:");
                propertyHeaderMap.put("pp_status", "PP Status");
                propertyHeaderMap.put("sm", "SM");
                propertyHeaderMap.put("sm_status", "SM Status");
                propertyHeaderMap.put("st1", "Surface Treatment1:");
                propertyHeaderMap.put("st1_status", "ST1 Status");
                propertyHeaderMap.put("routing", "Routing");
                propertyHeaderMap.put("routingstatus", "Routing Status");
                propertyHeaderMap.put("st2", "Surface Treatment2:");
                propertyHeaderMap.put("st2_status", "ST2 Status");
                propertyHeaderMap.put("fqc", "FQC:");
                propertyHeaderMap.put("ok", "OK");
                break;
            }
            case 4: {
                propertyHeaderMap.put("po", "Po");
                propertyHeaderMap.put("orderline", "Orderline");
                propertyHeaderMap.put("sales_order", "sales_order");
                propertyHeaderMap.put("customer_part_number", "customer_part_number");
                propertyHeaderMap.put("articleno", "Articleno");
                propertyHeaderMap.put("squaremeter", "Square Meters");
                propertyHeaderMap.put("cutting", "Cutting");
                propertyHeaderMap.put("cuttingstatus", "Cutting Status");
                propertyHeaderMap.put("press", "Press:");
                propertyHeaderMap.put("pressstatus", "Press Status");
                propertyHeaderMap.put("patternplating", "Pattern Plating:");
                propertyHeaderMap.put("pp_status", "PP Status");
                propertyHeaderMap.put("sm", "SM");
                propertyHeaderMap.put("sm_status", "SM Status");
                propertyHeaderMap.put("st1", "Surface Treatment1:");
                propertyHeaderMap.put("st1_status", "ST1 Status");
                propertyHeaderMap.put("routing", "Routing");
                propertyHeaderMap.put("routingstatus", "Routing Status");
                propertyHeaderMap.put("st2", "Surface Treatment2:");
                propertyHeaderMap.put("st2_status", "ST2 Status");
                propertyHeaderMap.put("fqc", "FQC:");
                propertyHeaderMap.put("ok", "OK");
                break;
            }
            case 5: {
                propertyHeaderMap.put("sales_order", "sales_order");
                propertyHeaderMap.put("prod_part", "prod_part");
                propertyHeaderMap.put("item_nbr", "item_nbr");
                propertyHeaderMap.put("cust_code", "cust_code");
                propertyHeaderMap.put("po_number", "po_number");
                propertyHeaderMap.put("prod_part_desc", "prod_part_desc");
                propertyHeaderMap.put("customer_part_number", "customer_part_number");
                propertyHeaderMap.put("customer_part_desc", "customer_part_desc");
                propertyHeaderMap.put("cust_part_rev", "cust_part_rev");
                propertyHeaderMap.put("order_qty", "order_qty");
                propertyHeaderMap.put("pcs_set", "pcs_set");
                propertyHeaderMap.put("note_pad_line", "市场备注");
                propertyHeaderMap.put("unit_name", "unit_name");
                propertyHeaderMap.put("cust_name", "cust_name");
                propertyHeaderMap.put("devli_qty", "devli_qty");
                propertyHeaderMap.put("qty_instock", "qty_instock");
                propertyHeaderMap.put("basethickness", "basethickness");
                propertyHeaderMap.put("finishedthickness", "finishedthickness");
                propertyHeaderMap.put("layernum", "layernum");
                propertyHeaderMap.put("technic", "technic");
                propertyHeaderMap.put("ship_to_addr", "ship_to_addr");
                propertyHeaderMap.put("curr_name", "curr_name");
                propertyHeaderMap.put("part_price", "part_price");
                propertyHeaderMap.put("amount_jiaji", "amount_jiaji");
                propertyHeaderMap.put("amount_eng", "amount_eng");
                propertyHeaderMap.put("amount_film", "amount_film");
                propertyHeaderMap.put("amount_model", "amount_model");
                propertyHeaderMap.put("amount_jiaju", "amount_jiaju");
                propertyHeaderMap.put("amount_feizhen", "amount_feizhen");
                propertyHeaderMap.put("amount_king", "amount_king");
                propertyHeaderMap.put("amount_gangwang", "amount_gangwang");
                propertyHeaderMap.put("amount_freight", "amount_freight");
                propertyHeaderMap.put("amount_commission", "amount_commission");
                propertyHeaderMap.put("amount_other", "amount_other");
                propertyHeaderMap.put("state_id", "state_id");
                propertyHeaderMap.put("state_tax", "state_tax");
                propertyHeaderMap.put("state_prod_tax_flag", "state_prod_tax_flag");
                propertyHeaderMap.put("state_tool_tax_flag", "state_tool_tax_flag");
                propertyHeaderMap.put("amount_total", "amount_total");
                propertyHeaderMap.put("prod_plant", "prod_plant");
                propertyHeaderMap.put("setl", "setl");
                propertyHeaderMap.put("setw", "setw");
                propertyHeaderMap.put("ent_date", "ent_date");
                propertyHeaderMap.put("conf_date", "conf_date");
                propertyHeaderMap.put("due_date", "due_date");
                propertyHeaderMap.put("sch_date", "sch_date");
                propertyHeaderMap.put("sales_name", "sales_name");
                propertyHeaderMap.put("so_status", "so_status");

                /*note_pad_line
                 * propertyHeaderMap.put("ship_fob", "ship_fob");
                 * propertyHeaderMap.put("description", "description");
                 * propertyHeaderMap.put("so_remark", "so_remark");
                 */
                break;
            }
            case 6: {
                propertyHeaderMap.put("salesorder", "MO");
                propertyHeaderMap.put("orderstatus", "订单状态");
                propertyHeaderMap.put("productionmodel", "生产型号");
                propertyHeaderMap.put("customermodel", "客户型号");
                propertyHeaderMap.put("quansch", "补投数量");
                propertyHeaderMap.put("remadetime", "补投时间");
                propertyHeaderMap.put("versions", "版本");
                propertyHeaderMap.put("plant", "厂别");
                //propertyHeaderMap.put("partprice", "单价");
                propertyHeaderMap.put("ordertype", "订单类型");
                propertyHeaderMap.put("customertype", "客户代码");
                propertyHeaderMap.put("finalpo", "PO");
                propertyHeaderMap.put("customername", "客户名称");
                propertyHeaderMap.put("orderdate", "下单日期");
                propertyHeaderMap.put("deliveryresponse", "计划交期");
                propertyHeaderMap.put("numberorder", "订单数(psc)");
                propertyHeaderMap.put("paid", "已交数(psc)");
                propertyHeaderMap.put("unpaidamount", "未交数(psc)");
                propertyHeaderMap.put("orderarea", "订单面积(m2)");
                propertyHeaderMap.put("marketstate", "下卡前状态");
                propertyHeaderMap.put("dictlabel", "工程状态");
                propertyHeaderMap.put("onlinestatus", "生产在线状态");
                propertyHeaderMap.put("qtyonhand", "成品库存(psc)");
                propertyHeaderMap.put("planresponse", "计划回复");
                propertyHeaderMap.put("fixturestatus", "夹具跟踪");
                propertyHeaderMap.put("remarks", "备注");
                propertyHeaderMap.put("marketnotes", "市场备注");
                propertyHeaderMap.put("earlywarnstatus", "预警状态");
                propertyHeaderMap.put("DELIVERYTIME", "采购部确认交期时间");
                propertyHeaderMap.put("lowmaterialstatus", "欠料状态");
                break;
            }
            case 7: {
                propertyHeaderMap.put("salesorder", "MO");
                propertyHeaderMap.put("workorder", "工单");
                propertyHeaderMap.put("schdate", "计划交期");
                propertyHeaderMap.put("groupname", "组别");
                propertyHeaderMap.put("custcode", "客户代码");
                propertyHeaderMap.put("demandnumber", "需求数");
                propertyHeaderMap.put("unitname", "单位");
                propertyHeaderMap.put("netdemand", "净需求");
                propertyHeaderMap.put("productname", "物料名称");
                propertyHeaderMap.put("invpartnumber", "部件号码");
                propertyHeaderMap.put("partdescription", "部件描述");
                propertyHeaderMap.put("customerpartnumber", "生产型号");
                propertyHeaderMap.put("inventory", "库存数");
                propertyHeaderMap.put("avlstock", "有效库存");
                propertyHeaderMap.put("plant1", "厂1");
                propertyHeaderMap.put("plant2", "厂2");
                propertyHeaderMap.put("plant3", "厂3");
                propertyHeaderMap.put("vrt", "要求交期");
                propertyHeaderMap.put("vtt", "到料日期");
                propertyHeaderMap.put("lowmaterialstatus", "欠料状态");
                propertyHeaderMap.put("remark", "备注");
                break;
            }
            case 8: {
                propertyHeaderMap.put("customerCode", "客户代码");
                propertyHeaderMap.put("customerName", "客户名称");
                propertyHeaderMap.put("amountToDate", "截至当前金额");
                propertyHeaderMap.put("areaToDate", "截至当前面积");
                propertyHeaderMap.put("cumulativeAmountOfThisMonth", "本月累计金额");
                propertyHeaderMap.put("cumulativeArea", "累计面积");
                propertyHeaderMap.put("cumulativeAveragePrice", "累计均价(￥/m2)");
                propertyHeaderMap.put("cumulativeAmountOfLastMonth", "上月累计金额");
                propertyHeaderMap.put("averagePriceOfLastMonth", "上个月均价(￥/m2)");
                propertyHeaderMap.put("cumulativeAmountThisYear", "今年累计金额");
                propertyHeaderMap.put("proportionOfTotalAmountThisYear", "今年总金额占比");
                propertyHeaderMap.put("accumulatedAmountLastYear", "去年累积金额");
                propertyHeaderMap.put("proportionOfTotalAmountLastYear", "去年总金额占比");

                break;
            }
            case 9: {
                propertyHeaderMap.put("ordertype", "Type");
                propertyHeaderMap.put("salesorder", "MO");
                propertyHeaderMap.put("productionmodel", "JOVE PN");
                propertyHeaderMap.put("finalpo", "CUSTOMER PO");
                propertyHeaderMap.put("customermodel", "CUSTOMER PN");
                propertyHeaderMap.put("customertype", "CODE");
                propertyHeaderMap.put("customername", "NAME");
                propertyHeaderMap.put("orderdate", "order date");
                propertyHeaderMap.put("deliveryresponse", "factory DD");
                propertyHeaderMap.put("numberorder", "order (pcs)");
                propertyHeaderMap.put("paid", "delivery(pcs)");
                propertyHeaderMap.put("unpaidamount", "open (psc)");
                propertyHeaderMap.put("dictlabel", "CAM Statu");
                propertyHeaderMap.put("plant", "plant");
                propertyHeaderMap.put("onlinestatus", "WIP");
                propertyHeaderMap.put("qtyonhand", " STOCK(psc)");
                break;
            }
            case 10: {
                propertyHeaderMap.put("salesPartNumber", "销售部件");
                propertyHeaderMap.put("surface", "表面处理");
                propertyHeaderMap.put("customerPartNumber", "生产编号");
                propertyHeaderMap.put("shelfLife", "保质期");
                propertyHeaderMap.put("customerPartDesc", "客户型号");
                propertyHeaderMap.put("custCode", "客户代码");
                propertyHeaderMap.put("customerName", "客户名称");
                propertyHeaderMap.put("prodCycle", "生产周期");
                propertyHeaderMap.put("mfgDate", "入库时间");
                propertyHeaderMap.put("qtyOnHand", "库存数(PCS)");
                propertyHeaderMap.put("location", "仓位");
                propertyHeaderMap.put("sumQty", "库存总数");
                propertyHeaderMap.put("setl", "set长");
                propertyHeaderMap.put("setw", "set宽");
                propertyHeaderMap.put("pcsSet", "拼板数PCS_SET");
                propertyHeaderMap.put("analysisCode3", "LOT号");
                propertyHeaderMap.put("areaFg", "库存总面积");
                propertyHeaderMap.put("expireDate", "到期时间");
                propertyHeaderMap.put("holdStatus", "状态");
                propertyHeaderMap.put("note1", "备注");
                propertyHeaderMap.put("notePadLine1", "位置");
                break;
            }
            case 11: {
                propertyHeaderMap.put("mo", "MO");
                propertyHeaderMap.put("lotNum", "LOT号");
                propertyHeaderMap.put("labelId", "序列号");
                propertyHeaderMap.put("custCode", "客户代码");
                propertyHeaderMap.put("custName", "客户名称");
                propertyHeaderMap.put("custPn", "客户型号");
                propertyHeaderMap.put("custDesc", "型号描述");
                propertyHeaderMap.put("mfg", "生产编号");
                propertyHeaderMap.put("qty", "数量");
                propertyHeaderMap.put("pcsSet", "拼板数");
                propertyHeaderMap.put("dateCode", "生产周期");
                propertyHeaderMap.put("operator", "操作员");
                propertyHeaderMap.put("printDate", "打印日期");
                propertyHeaderMap.put("printTime", "打印时间");
                propertyHeaderMap.put("custVer", "客户版本");
                propertyHeaderMap.put("surface", "表面处理");
                propertyHeaderMap.put("plateThick", "板厚");
                break;
            }
            default:
                break;
        }
        return propertyHeaderMap;
    }
}
