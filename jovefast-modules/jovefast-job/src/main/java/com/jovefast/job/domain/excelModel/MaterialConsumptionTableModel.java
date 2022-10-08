package com.jovefast.job.domain.excelModel;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.job.domain.dto.MaterialConsumptionDTO;
import com.jovefast.job.domain.dto.OutboundRecordDTO;
import com.jovefast.job.domain.dto.ProductionProcessOverDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author MuTouYSK
 * @date 2022/4/26
 * 采购物料消耗月表
 */
public class MaterialConsumptionTableModel {
    private XSSFWorkbook wb;
    int year;
    int month;

    /**
     * 初始化
     */
    private void initialization() {
        wb = new XSSFWorkbook();
        year = DateUtils.singleDate(1);
        month = DateUtils.singleDate(2);

        //样式初始化
        setSongTwelveThickVertical();
        setLevelVerticalFrameSongTen();
        setLevelVerticalFrameSongTenThick();
        setLevelVerticalFrameCornflowerBlueSongTen();
    }

    /**
     * 主表模板
     *
     * @param proList 生产工序过数记录
     * @param outList 出库记录
     * @return HSSFWorkbook Excel表
     */
    public XSSFWorkbook MaterialConsumptionTableExcel(List<ProductionProcessOverDTO> proList, List<OutboundRecordDTO> outList) {
        initialization();
        Map<String, List<MaterialConsumptionDTO>> map = dataCuration(proList, outList);

        XSSFSheet sheet = wb.createSheet();
        //cellWidth(sheet);
        header(sheet);
        message(sheet, map);
        return wb;
    }

    /**
     * 表头
     *
     * @param sheet 行
     */
    private void header(XSSFSheet sheet) {
        XSSFRow row0 = sheet.createRow(0);
        XSSFCell cell0_0 = row0.createCell(0);
        cell0_0.setCellValue("三厂物料消耗月报总表");
        cell0_0.setCellStyle(setSongTwelveThickVertical);
        //合并列
        CellRangeAddress region0_3 = new CellRangeAddress(0, 0, 0, 3);
        sheet.addMergedRegion(region0_3);
        int cell = 0;
        XSSFRow row1 = sheet.createRow(1);
        XSSFCell cell1_0 = row1.createCell(0);
        cell1_0.setCellStyle(setLevelVerticalFrameSongTenThick);
        XSSFRow row2 = sheet.createRow(2);
        XSSFCell cell2_0 = row2.createCell(0);
        cell2_0.setCellValue("部门");
        cell2_0.setCellStyle(setLevelVerticalFrameSongTen);
        for (int i = 1; i < month; i++) {
            //判断是否属于第一个月份,第一个月份之前有个:部门
            if(i != 1){
                //参考公式:(i - 1) * 3以及i-(i-1)
                int j= (i - 1) * 3;
                int k= i-(i-1);
                XSSFCell cell1_i_1 = row1.createCell(((i-(i-1)) + cell+j));
                cell1_i_1.setCellValue(year + "年" + i + "月份");
                cell1_i_1.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_2 = row1.createCell((k+ cell + j)+1);
                cell1_i_2.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_3 = row1.createCell((k + cell + j)+2);
                cell1_i_3.setCellStyle(setLevelVerticalFrameSongTenThick);
                //合并列
                CellRangeAddress region1 = new CellRangeAddress(1, 1, (k + cell+j), (k + cell +j)+2);
                sheet.addMergedRegion(region1);
                XSSFCell cell2_i_1 = row2.createCell((k + cell+j));
                cell2_i_1.setCellValue("产量(M2)");
                cell2_i_1.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_2 = row2.createCell((k  + cell + j)+1);
                cell2_i_2.setCellValue("消耗(元)");
                cell2_i_2.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_3 = row2.createCell((k  + cell + j)+2);
                cell2_i_3.setCellValue("元/㎡");
                cell2_i_3.setCellStyle(setLevelVerticalFrameSongTen);

                XSSFCell cell1_i_4 = row1.createCell(k + cell+j+3);
                cell1_i_4.setCellValue(i + "月累计");
                cell1_i_4.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_5 = row1.createCell(k + cell + j+4);
                cell1_i_5.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_6 = row1.createCell(k + cell + j+5);
                cell1_i_6.setCellStyle(setLevelVerticalFrameSongTenThick);
                //合并列
                CellRangeAddress region2 = new CellRangeAddress(1, 1, (k + cell+j+3), (k +j+5 + cell));
                sheet.addMergedRegion(region2);
                XSSFCell cell2_i_4 = row2.createCell(k + cell+j+3);
                cell2_i_4.setCellValue("产量(M2)");
                cell2_i_4.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_5 = row2.createCell( k + cell + j+4);
                cell2_i_5.setCellValue("消耗(元)");
                cell2_i_5.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_6 = row2.createCell(k  + cell + j+5);
                cell2_i_6.setCellValue("元/㎡");
                cell2_i_6.setCellStyle(setLevelVerticalFrameSongTen);
            }else {
                XSSFCell cell1_i_1 = row1.createCell((i + cell));
                cell1_i_1.setCellValue(year + "年" + i + "月份");
                cell1_i_1.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_2 = row1.createCell((i + cell + 1));
                cell1_i_2.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_3 = row1.createCell((i + cell + 2));
                cell1_i_3.setCellStyle(setLevelVerticalFrameSongTenThick);
                //合并列
                CellRangeAddress region1 = new CellRangeAddress(1, 1, (i + cell), (i + 2 + cell));
                sheet.addMergedRegion(region1);
                XSSFCell cell2_i_1 = row2.createCell((i + cell));
                cell2_i_1.setCellValue("产量(M2)");
                cell2_i_1.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_2 = row2.createCell((i + cell + 1));
                cell2_i_2.setCellValue("消耗(元)");
                cell2_i_2.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_3 = row2.createCell((i + cell + 2));
                cell2_i_3.setCellValue("元/㎡");
                cell2_i_3.setCellStyle(setLevelVerticalFrameSongTen);


                XSSFCell cell1_i_4 = row1.createCell(i + cell+3);
                cell1_i_4.setCellValue(i + "月累计");
                cell1_i_4.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_5 = row1.createCell(i + cell + 4);
                cell1_i_5.setCellStyle(setLevelVerticalFrameSongTenThick);
                XSSFCell cell1_i_6 = row1.createCell(i + cell + 5);
                cell1_i_6.setCellStyle(setLevelVerticalFrameSongTenThick);
                //合并列
                CellRangeAddress region2 = new CellRangeAddress(1, 1, (i + cell+3), (i + 5 + cell));
                sheet.addMergedRegion(region2);
                XSSFCell cell2_i_4 = row2.createCell(i + cell+3);
                cell2_i_4.setCellValue("产量(M2)");
                cell2_i_4.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_5 = row2.createCell( i + cell + 4);
                cell2_i_5.setCellValue("消耗(元)");
                cell2_i_5.setCellStyle(setLevelVerticalFrameSongTen);
                XSSFCell cell2_i_6 = row2.createCell(i  + cell + 5);
                cell2_i_6.setCellValue("元/㎡");
                cell2_i_6.setCellStyle(setLevelVerticalFrameSongTen);
            }
            cell += 3;

        }
    }

    /**
     * 设置列宽
     *
     * @param sheet 行
     */
    private void cellWidth(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 16 * 256);

        int cell = 0;
        for (int i = 0; i < month; i++) {
            sheet.setColumnWidth((1 + i + cell), 10 * 256);
            sheet.setColumnWidth((2 + i + cell), 14 * 256);
            sheet.setColumnWidth((3 + i + cell), 10 * 256);
            sheet.setColumnWidth((4 + i + cell), 3 * 256);
            cell += 3;
        }
        sheet.setColumnWidth((5 + cell), 12 * 256);
        sheet.setColumnWidth((6 + cell), 13 * 256);
        sheet.setColumnWidth((7 + cell), 10 * 256);
    }

    /**
     * 数据体
     *
     * @param sheet 行
     * @param map   数据集合
     */
    private void message(XSSFSheet sheet, Map<String, List<MaterialConsumptionDTO>> map) {
        //定义组别（主要是不想写排序了，就用数组定义组别顺序。）
        String[] array = {"开料1厂", "钻孔1厂", "板面电镀1厂", "外层碱性蚀刻1厂", "沉铜（孔化）1厂", "图形电镀1厂", "金手指1厂", "沉金1厂", "干膜QC1厂", "干膜1厂",
                "阻焊1厂", "铣外形1厂", "E-T测试1厂", "最后检查1厂", "包装1厂", "层压1厂", "AOI1厂", "内层1厂", "仓库1厂", "人事行政1厂", "品质部1厂", "资讯部1厂",
                "工程1厂", "工艺1厂", "客诉1厂", "研发1厂", "计划部1厂", "生产部1厂", "污水站1厂", "维修部1厂", "设备1厂", "开料2厂", "钻孔2厂", "板面电镀2厂", "图形电镀2厂",
                "外层碱性蚀刻2厂", "退锡2厂", "沉铜2厂", "电镀VCP线2厂", "干膜2厂", "酸蚀2厂", "无铅喷锡2厂", "有铅喷锡2厂", "阻焊2厂", "字符2厂", "铣外形2厂", "冲床2厂",
                "测试2厂", "最后检查2厂", "包装2厂", "内层2厂", "内层AOI2厂", "外层AOI2厂", "压合2厂", "仓库2厂", "采购2厂", "管理2厂", "财务2厂", "品质部2厂", "资料室2厂",
                "工艺2厂", "市场部2厂", "计划部2厂", "资讯部2厂", "生产部2厂", "污水站2厂", "维修部2厂", "设备2厂"};

        int c = 3;
        for (String s : array) {
            if (map.get(s) == null) {
                continue;
            }
            XSSFRow row = sheet.createRow(c);
            XSSFCell cell0_i = row.createCell(0);
            cell0_i.setCellValue(s);
            cell0_i.setCellStyle(setLevelVerticalFrameSongTen);
            int cell = 0;
            for (int i = 1; i < month; i++) {
                boolean is = false;
                for (MaterialConsumptionDTO vo : map.get(s)) {
                    if (i == vo.getMoon()) {
                        XSSFCell cell1_i = row.createCell((i + cell));
                        cell1_i.setCellValue(vo.getYield());
                        cell1_i.setCellStyle(setLevelVerticalFrameSongTen);

                        XSSFCell cell2_i = row.createCell((i + cell + 1));
                        cell2_i.setCellValue(vo.getConsume());
                        cell2_i.setCellStyle(setLevelVerticalFrameSongTen);

                        XSSFCell cell3_i = row.createCell((i + cell + 2));
                        cell3_i.setCellValue(vo.getMoney());
                        cell3_i.setCellStyle(setLevelVerticalFrameSongTen);
                        is = true;
                    }
                }
                if (!is) {
                    XSSFCell cell1_i = row.createCell((i + cell));
                    cell1_i.setCellValue(0);
                    cell1_i.setCellStyle(setLevelVerticalFrameSongTen);

                    XSSFCell cell2_i = row.createCell((i + cell + 1));
                    cell2_i.setCellValue(0);
                    cell2_i.setCellStyle(setLevelVerticalFrameSongTen);

                    XSSFCell cell3_i = row.createCell((i + cell + 2));
                    cell3_i.setCellValue(0);
                    cell3_i.setCellStyle(setLevelVerticalFrameSongTen);
                }
                cell += 3;
            }
            c++;
        }
    }

    /**
     * 数据整理（把这两list合并成表需要的数据）
     *
     * @param proList 生产工序过数记录
     * @param outList 出库记录
     * @return Map<String, List < MaterialConsumptionDTO>>
     */
    private Map<String, List<MaterialConsumptionDTO>> dataCuration(List<ProductionProcessOverDTO> proList, List<OutboundRecordDTO> outList) {
        Map<Integer, Map<String, Double>> proMap = proCuration(proList);
        Map<Integer, Map<String, Double>> outMap = outCuration(outList);

        //合并数据
        List<MaterialConsumptionDTO> vos = new ArrayList<>();
        for (Integer key1 : outMap.keySet()) {  //循环月份
            List<String> list = new ArrayList<>();
            for (String key2 : outMap.get(key1).keySet()) { //循环组别
                MaterialConsumptionDTO vo = new MaterialConsumptionDTO();
                vo.setDepartment(key2);

                if (proMap.get(key1) == null || proMap.get(key1).get(key2) == null) {
                    vo.setYield(0.0);
                } else {
                    vo.setYield(proMap.get(key1).get(key2));
                }

                vo.setConsume(outMap.get(key1).get(key2));
                vo.setMoon(key1);
                vos.add(vo);
                list.add(key2);
            }

            if (proMap.get(key1) == null) {
                continue;
            }
            for (String key2 : proMap.get(key1).keySet()) {
                if (!list.contains(key2)) {
                    MaterialConsumptionDTO vo = new MaterialConsumptionDTO();
                    vo.setDepartment(key2);
                    vo.setYield(proMap.get(key1).get(key2));
                    vo.setConsume(0.0);
                    vo.setMoon(key1);
                    vos.add(vo);
                }
            }
        }

        //合并数据分组
        Map<String, List<MaterialConsumptionDTO>> map = new HashMap<>();
        for (MaterialConsumptionDTO vo : vos) {
            if (map.get(vo.getDepartment()) == null) {
                List<MaterialConsumptionDTO> list = new ArrayList<>();
                list.add(vo);
                map.put(vo.getDepartment(), list);
            } else {
                map.get(vo.getDepartment()).add(vo);
            }
        }
        return map;
    }

    /**
     * 按工序合并，得到每个工序的出库金额
     *
     * @param outList 出库记录
     * @return Map<String, Double>
     */
    private Map<Integer, Map<String, Double>> outCuration(List<OutboundRecordDTO> outList) {
        Map<Integer, Map<String, Double>> map = new HashMap<>();
        for (OutboundRecordDTO vo : outList) {
            int plant = plantAnalyze(vo.getFacNo());
            if (plant == 0) {
                continue;
            }
            String process = vo.getProcess();
            mapLoad(process, plant, vo.getErpAmount(), DateUtils.getSpecifyDateMonth(vo.getTranDate()), map);
        }
        return map;
    }

    /**
     * 按工序合并，得到每个工序的总产量
     *
     * @param proList 生产工序过数记录
     * @return Map<String, Double>
     */
    private Map<Integer, Map<String, Double>> proCuration(List<ProductionProcessOverDTO> proList) {
        //数据分组
        Map<Integer, Map<String, Double>> map = new HashMap<>();
        for (ProductionProcessOverDTO vo : proList) {
            int plant = vo.getRecPlant();
            if (vo.getRecRoute() == 0) {
                continue;
            }
            String process;
            switch (vo.getRecRoute()) {
                case 1:
                    process = "开料";
                    break;
                case 86:
                    process = "钻孔";
                    break;
                case 102:
                    process = "板面电镀";
                    break;
                case 122:
                    process = "外层碱性蚀刻";
                    break;
                case 1259:
                    process = "沉铜";
                    break;
                case 113:
                    process = "图形电镀";
                    break;
                case 115:
                    process = "金手指";
                    break;
                case 58:
                    process = "沉金";
                    break;
                case 110:
                    process = "干膜";
                    break;
                case 127:
                    process = "阻焊";
                    break;
                case 88:
                    process = "铣外形";
                    break;
                case 265:
                    process = "E-T测试";
                    break;
                case 249:
                    process = "最后检查";
                    break;
                case 13:
                    process = "包装";
                    break;
                case 84:
                    process = "层压";
                    break;
                case 76:
                    process = "AOI";
                    break;
                case 746:
                    process = "内层";
                    break;
                default:
                    process = null;
            }
            mapLoad(process, plant, vo.calculateSetArea(), proDateAnalyze(vo.getOutTime()), map);
        }
        return map;
    }

    /**
     * 初始化map key
     *
     * @param process 工序
     * @param plant   厂别
     * @param area    面积/消耗
     * @param moon    月份
     * @param map     集合
     */
    private void mapLoad(String process, int plant, Double area, int moon, Map<Integer, Map<String, Double>> map) {
        if (process == null || area == null || moon == 0) {
            return;
        }
        if (map.get(moon) == null) {
            Map<String, Double> group = new HashMap<>();
            map.put(moon, group);
        }

        String key = process + plant + "厂";
        map.get(moon).merge(key, area, Double::sum);
    }

    /**
     * 因厂别在工序中，所以要分析出是那个厂别的
     *
     * @param process 工序
     * @return 返回1、2、3厂别
     */
    private int plantAnalyze(String process) {
        if (process == null || "".equals(process)) {
            return 0;
        }
        if (process.contains("一") || process.contains("1")) {
            return 1;
        }
        if (process.contains("二") || process.contains("2")) {
            return 2;
        }
        if (process.contains("三") || process.contains("3")) {
            return 3;
        }
        return 0;
    }

    /**
     * 分析出产量收板日期的月份
     *
     * @param outTime 收板日期
     * @return 月份
     */
    private int proDateAnalyze(String outTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(outTime);
            for (int i = 1; i <= this.month; i++) {
                String start = this.year + "-" + i + "-01 08:00:00";
                String end = (i == 12 ? (this.year + 1) : this.year) + "-" + (i == 12 ? 1 : (i + 1)) + "-01 08:00:00";
                if (DateUtils.isSection(date, start, end)) {
                    return i;
                }
            }
            return 0;
        } catch (ParseException | NullPointerException e) {
            return 0;
        }
    }

    /**
     * 设置 宋体、12字号、加粗、垂直居中
     */
    private XSSFCellStyle setSongTwelveThickVertical;

    private void setSongTwelveThickVertical() {
        XSSFCellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 12); //字体字号
        font.setBold(true);   //字体加粗
        style.setFont(font);
        this.setSongTwelveThickVertical = style;
    }

    /**
     * 设置 水平、垂直、边框、宋体、10字号、加粗
     */
    private XSSFCellStyle setLevelVerticalFrameSongTenThick;

    private void setLevelVerticalFrameSongTenThick() {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); //水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 10); //字体字号
        font.setBold(true);   //字体加粗
        style.setFont(font);
        this.setLevelVerticalFrameSongTenThick = style;
    }

    /**
     * 设置 水平、垂直、边框、宋体、10字号
     */
    private XSSFCellStyle setLevelVerticalFrameSongTen;

    private void setLevelVerticalFrameSongTen() {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); //水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 10); //字体字号
        style.setFont(font);
        this.setLevelVerticalFrameSongTen = style;
    }

    /**
     * 设置 水平、垂直、边框、玉米花_蓝色、宋体、10字号
     */
    private XSSFCellStyle setLevelVerticalFrameCornflowerBlueSongTen;

    private void setLevelVerticalFrameCornflowerBlueSongTen() {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); //水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.index);   //黄色背景
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);   //背景模板
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");//设置字体名称
        font.setFontHeightInPoints((short) 10); //字体字号
        style.setFont(font);
        this.setLevelVerticalFrameCornflowerBlueSongTen = style;
    }
}
