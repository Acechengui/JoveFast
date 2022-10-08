package com.jovefast.common.core.utils.poi;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * POI公用工具类
 *
 * @author MuTouYSK
 * @date 2022-01-19 15:30
 */
public class PoiExcelUtils {

    /**
     * 判断该Excel文件是xlsx还是xls格式的
     *
     * @param path 文件路径
     * @return 返回Workbook
     * @throws IOException io异常
     */
    public static Workbook isWorkbook(String path) throws IOException {
        //读取Excel文件
        FileInputStream fis = new FileInputStream(path);
        //获取文件后缀名
        String suffixName = path.substring(path.lastIndexOf("."));
        try {
            if (suffixName.contains("xlsx")) {
                return new XSSFWorkbook(fis);
            } else if (suffixName.contains("xls")) {
                return new HSSFWorkbook(fis);
            } else {
                throw new IOException("文件类型格式错误。（只支持[xlsx|xls]）");
            }
        } catch (POIXMLException e) {
            throw new IOException("文件初始化失败。");
        }
    }

    /**
     * EasyPOI导入Excel
     *
     * @param filePath   excel文件路径
     * @param titleRows  表格内数据标题行
     * @param headerRows 表头行
     * @param pojoClass  pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedSave(true);
        params.setSaveUrl("/excel/");
        try {
            return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new IOException("模板不能为空");
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * POI读取Excel单元格格式
     *
     * @param cell
     * @return
     */
    public static String getCellVal(Cell cell) {
        String strCell;
        if (cell == null) {
            return "";
        }
        try {
            switch (cell.getCellType()) {
                case STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        strCell = sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
                        break;
                    } else {
                        strCell = new DecimalFormat("0.00").format(cell.getNumericCellValue());
                    }
                    break;
                case BOOLEAN:
                    strCell = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    try {
                        strCell = String.valueOf(cell.getNumericCellValue());
                    } catch (IllegalStateException e) {
                        strCell = String.valueOf(cell.getRichStringCellValue());
                    }
                    break;
                case BLANK:
                    strCell = "";
                    break;
                default:
                    strCell = "未知类型";
                    break;
            }
        } catch (IllegalStateException e) {
            return null;
        }
        return strCell;
    }


    /**
     * 生成一个标题style
     *
     * @return style
     */
    public static XSSFCellStyle getHeaderStyle(Workbook workbook) {
        return getHeaderStyle(workbook, IndexedColors.DARK_BLUE, IndexedColors.WHITE.getIndex());
    }

    /**
     * 生成一个指定颜色的标题style
     *
     * @param workbook
     * @param foregroundColor
     * @param fontColor
     * @return
     */
    public static XSSFCellStyle getHeaderStyle(Workbook workbook, IndexedColors foregroundColor, short fontColor) {

        // 生成一个样式（用于标题）
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(foregroundColor.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setColor(fontColor);
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 生成一个用于内容的style
     *
     * @param workbook
     * @return
     */
    public static XSSFCellStyle getContentStyle(Workbook workbook) {
        // 生成并设置另一个样式（用于内容）
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setBold(true); //是否加粗
        // 把字体应用到当前的样式
        style.setFont(font);

        return style;
    }


    /**
     * 根据输入的数据生成一个XSSFWorkbook
     *
     * @param title：sheet名称
     * @param propertyHeaderMap：<property, header>（<T中的property名称、有getter就行, 对应显示在Excel sheet中的列标题>）
     *                                     用LinkedHashMap保证读取的顺序和put的顺序一样
     * @param dataSet：实体类集合
     * @return：XSSFWorkbook
     */
    public static <T> SXSSFWorkbook generateXlsxWorkbook(String title, LinkedHashMap<String, String> propertyHeaderMap, Collection<T> dataSet) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        SXSSFWorkbook swb = new SXSSFWorkbook(workbook, 100);
        // 生成一个表格
        Sheet sheet = swb.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((int) 15);
        XSSFCellStyle headerStyle = getHeaderStyle(swb);
        XSSFCellStyle contentStyle = getContentStyle(swb);

        // 生成表格标题行
        Row row = sheet.createRow(0);
        int i = 0;
        for (String key : propertyHeaderMap.keySet()) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            XSSFRichTextString text = new XSSFRichTextString(propertyHeaderMap.get(key));
            cell.setCellValue(text);
            i++;
        }

        //循环dataSet，每一条对应一行
        int index = 0;
        for (T data : dataSet) {
            index++;
            row = sheet.createRow(index);

            int j = 0;
            for (String property : propertyHeaderMap.keySet()) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(contentStyle);

                //拼装getter方法名
                String getMethodName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1);

                try {
                    //利用反射机制获取dataSet中的属性值，填进cell中
                    Class<? extends Object> tCls = data.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(data, new Object[]{}); //调用getter从data中获取数据

                    // 判断值的类型后进行类型转换
                    String textValue = null;
                    if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cell.setCellValue(sdf.format(date));
                    } else if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Double) {
                        cell.setCellValue((double) value);
                    } else if (!StringUtils.isEmpty(String.valueOf(value)) && value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        cell.setCellValue(value.toString().trim());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                j++;
            }
        }

        return swb;

    }

    public static void excelLocal(String path, String fileName, String[] headers, List<Object[]> datas) {
        Workbook workbook = getWorkbook(headers, datas);
        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);

                String suffix = ".xls";
                File file = new File(path + File.separator + fileName + suffix);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 生成Excel表格
     * @param sheetMaps excel数据对象
     *                  说明: List<Map<工作区名称,Object设置List集合 第一行是表头 其他的行是表数据>> sheetMaps = new ArrayList<>();
     *                  示例:
     *                      -- 创建一个用来保存工作区的List集合
     *                      List<Map<String,List<Object[]>>> sheetMaps = new ArrayList<>();
     *
     *                      -- 创建一个工作区，命名为 “sheet_A”
     *                      Map<String,List<Object[]>> sheet_A = new HashMap<>();
     *
     *                      -- 为sheet_A工作区创建一个List集合用来保存数据
     *                      List<Object[]> sheet_A_datas = new ArrayList<>();
     *
     *                      -- sheet_A_datas的第一行是表头，自动加粗显示，宽带自适应
     *                      sheet_A_datas.add(new String[]{"学生编号", "学生姓名", "学生性别", ...});
     *
     *                      -- sheet_A_datas除了第一行，其他的行全部是表格数据
     *                      sheet_A_datas.add(new String[]{"1001", "张三", "男", ...});
     *                      sheet_A_datas.add(new String[]{"1002", "李四", "男", ...});
     *                      sheet_A_datas.add(new String[]{"1003", "王五", "男", ...});
     *                      ...
     *
     *                      -- key是工作区的名称，value是工作区的数据
     *                      sheet_A.put("学生基本信息",sheet_A_datas);
     *
     *                      -- 保存工作区
     *                      sheetMaps.add(sheet_A);
     *
     *
     *
     *                      -- 创建一个工作区，命名为 “sheet_B”
     *                      Map<String,List<Object[]>> sheet_B = new HashMap<>();
     *
     *                      -- 为sheet_B工作区创建一个List集合用来保存数据
     *                      List<Object[]> sheet_B_datas = new ArrayList<>();
     *
     *                      -- sheet_B_datas的第一行是表头，自动加粗显示，宽带自适应
     *                      sheet_B_datas.add(new String[]{"学生编号", "科目编号", "成绩", ...});
     *
     *                      -- sheet_B_datas除了第一行，其他的行全部是表格数据
     *                      sheet_B_datas.add(new String[]{"1001", "101", "98", ...});
     *                      sheet_B_datas.add(new String[]{"1002", "101", "100", ...});
     *                      sheet_B_datas.add(new String[]{"1003", "103", "99", ...});
     *                      ...
     *
     *                      -- key是工作区的名称，value是工作区的数据
     *                      sheet_B.put("学生成绩信息",sheet_B_datas);
     *
     *                      -- 保存工作区
     *                      sheetMaps.add(sheet_B);
     *
     *
     *
     *
     *
     * @param path 生成路径
     *             示例: D:\\自动导出报临时缓存表\\每日物料消耗\\
     *
     *
     *
     *
     * @param excelname 文件名称（不需要文件后缀）
     *                  示例: 每月物料消耗情况
     */
    public static void excelExport(List<Map<String,List<Object[]>>> sheetMaps, String path, String excelname){

        //数值格式化
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false);

        //创建表格操作对象
        Workbook workbook = new HSSFWorkbook();

        //当前工作区索引
        int sheetIndex = 0;

        //循环工作区集合
        for(Map<String,List<Object[]>> for_sheetMaps_item : sheetMaps){

            //得到工作区名称
            for(String for_map_key_sheetName : for_sheetMaps_item.keySet()){

                //创建工作区
                Sheet sheet = workbook.createSheet();

                //设置工作区名称
                workbook.setSheetName(sheetIndex,for_map_key_sheetName);

                //得到工作区数据
                List<Object[]> sheetDatas = for_sheetMaps_item.get(for_map_key_sheetName);

                Row row = null;
                Cell cell = null;

                CellStyle style = workbook.createCellStyle();

                Font font = workbook.createFont();

                int line = 0, maxColumn = 0;
                if (sheetDatas.size() > 0 && sheetDatas.get(0) != null && sheetDatas.get(0).length > 0) {// 设置列头
                    row = sheet.createRow(line++);
                    font.setFontName("黑体");
                    //字体加粗
                    font.setBold(true);
                    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 设置背景色
                    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    style.setFont(font);
                    maxColumn = sheetDatas.get(0).length;
                    for (int i = 0; i < maxColumn; i++) {
                        cell = row.createCell(i);
                        cell.setCellValue(sheetDatas.get(0)[i].toString());
                        cell.setCellStyle(style);
                    }

                    //设置筛选
                    sheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + PoiExcelUtils.getColumnName(maxColumn) + "1"));

                }

                // 渲染数据
                if (sheetDatas.size() > 1 &&sheetDatas.get(1) != null && sheetDatas.get(1).length > 1) {
                    for(int index = 1, size = sheetDatas.size(); index < size; index++){
                        Object[] data = sheetDatas.get(index);

                        if (data != null && data.length > 0) {
                            row = sheet.createRow(line++);


                            int length = data.length;
                            if (length > maxColumn) {
                                maxColumn = length;
                            }

                            for (int i = 0; i < length; i++) {
                                cell = row.createCell(i);

                                if (data[i] instanceof Integer) {
                                    int value = ((Integer) data[i]).intValue();
                                    cell.setCellValue(value);

                                } else if (data[i] instanceof String) {
                                    String s = (String) data[i];
                                    cell.setCellValue(s);
                                } else if (data[i] instanceof Double) {

                                    double d = ((Double) data[i]).doubleValue();
                                    cell.setCellValue(df.format(d));

                                } else if (data[i] instanceof BigDecimal) {

                                    cell.setCellValue(((BigDecimal) data[i]).doubleValue());
                                } else if (data[i] instanceof Boolean) {
                                    boolean b = ((Boolean) data[i]).booleanValue();
                                    cell.setCellValue(b);
                                } else if (data[i] instanceof Date) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date d = (Date) data[i];
                                    cell.setCellValue(sdf.format(d));
                                } else {
                                    cell.setCellValue(data[i] == null ? null : data[i].toString());
                                }

                            }
                        }
                    }
                }
                for (int i = 0; i < maxColumn; i++) {
                    sheet.autoSizeColumn(i);
                    sheet.setColumnWidth(i,sheet.getColumnWidth(i)*17/10);
                }

            }

            //更新当前工作区索引，准备下一次循环的时候使用
            sheetIndex++;
        }


        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);
                String suffix = ".xls";
                FileOutputStream output = new FileOutputStream(path + excelname + suffix);
                workbook.write(output);//写入磁盘
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 导出excel
     *
     * @param fileName
     * @param headers
     * @param datas
     */
    public static void excelExport(String fileName, String[] headers, List<Object[]> datas, String path, String excelname) {
        Workbook workbook = getWorkbook(headers, datas);

        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);
                String suffix = ".xls";
                FileOutputStream output = new FileOutputStream(path + excelname + suffix);
                workbook.write(output);
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取生成的excel文件流
     *
     * @param headers
     * @param datas
     */
    public static InputStream getExcelExport(String[] headers, List<Object[]> datas) {
        Workbook workbook = getWorkbook(headers, datas);
        InputStream is = null;
        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);
                byte[] barray = byteArrayOutputStream.toByteArray();
                is = new ByteArrayInputStream(barray);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return is;
            }
        }else{
            return is;
        }
    }

    /**
     * @param headers 列头
     * @param datas   数据
     * @return
     */
    public static Workbook getWorkbook(String[] headers, List<Object[]> datas) {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        Row row = null;
        Cell cell = null;
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();

        int line = 0, maxColumn = 0;
        // 设置列头
        if (headers != null && headers.length > 0) {
            row = sheet.createRow(line++);
            font.setFontName("黑体");
            //字体加粗
            font.setBold(true);
            // 设置背景色
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFont(font);
            maxColumn = headers.length;
            for (int i = 0; i < maxColumn; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(style);


            }

            //设置筛选
            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + PoiExcelUtils.getColumnName(maxColumn) + "1"));
        }

        if (datas != null && datas.size() > 0) {// 渲染数据
            for (int index = 0, size = datas.size(); index < size; index++) {
                Object[] data = datas.get(index);
                if (data != null && data.length > 0) {
                    row = sheet.createRow(line++);

                    int length = data.length;
                    if (length > maxColumn) {
                        maxColumn = length;
                    }

                    for (int i = 0; i < length; i++) {
                        cell = row.createCell(i);
                        if (data[i] instanceof Integer) {
                            int value = ((Integer) data[i]).intValue();
                            cell.setCellValue(value);

                        } else if (data[i] instanceof String) {
                            String s = (String) data[i];
                            cell.setCellValue(s);
                        } else if (data[i] instanceof Double) {

                            double d = ((Double) data[i]).doubleValue();
                            System.out.println("*" + d);
                            cell.setCellValue(DecimalFormat.getInstance().format(d));

                        } else if (data[i] instanceof BigDecimal) {

                            cell.setCellValue(((BigDecimal) data[i]).doubleValue());
                        } else if (data[i] instanceof Boolean) {
                            boolean b = ((Boolean) data[i]).booleanValue();
                            cell.setCellValue(b);
                        } else if (data[i] instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date d = (Date) data[i];
                            cell.setCellValue(sdf.format(d));
                        } else {
                            cell.setCellValue(data[i] == null ? null : data[i].toString());
                        }
                    }
                }
            }
        }
        for (int i = 0; i < maxColumn; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    private static String getColumnName(int columnNum) {
        int first;
        int last;
        String result = "";
        if (columnNum > 256) {
            columnNum = 256;
        }
        first = columnNum / 27;
        last = columnNum - (first * 26);
        if (first > 0) {
            result = String.valueOf((char) (first + 64));
        }
        if (last > 0) {
            result = result + String.valueOf((char) (last + 64));
        }
        return result;

    }

}
