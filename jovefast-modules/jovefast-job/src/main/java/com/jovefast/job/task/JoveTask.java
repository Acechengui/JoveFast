package com.jovefast.job.task;

import com.jovefast.common.core.utils.DateUtils;
import com.jovefast.common.core.utils.StringUtils;
import com.jovefast.common.core.utils.file.FileUtils;
import com.jovefast.common.core.utils.poi.PoiExcelUtils;
import com.jovefast.job.domain.dto.*;
import com.jovefast.job.domain.excelModel.MaterialConsumptionTableModel;
import com.jovefast.job.enums.RobotsTarget;
import com.jovefast.job.util.QiWxUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component("JoveTask")
public class JoveTask {
    private static final Logger log = LoggerFactory.getLogger(JoveTask.class);

    /**
     * 临时文件路径
     */
    private final static String TEMP_DIR = System.getProperty("user.dir") + "/temp/";


    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void noParams() {
        System.out.println("执行无参方法");
    }



    /**
     * 物料消耗月报总表 - 示例
     */
    public void task1() throws ParseException, IOException {
        String proFromTime = DateUtils.singleDate(1) + "-01-01 08:00:00";
        List<ProductionProcessOverDTO> proList = new ArrayList<>();
        String outFromTime = DateUtils.singleDate(1) + "-01-01";
        String outArriveTime = DateUtils.getFirstDayMonth(1, 2);
        List<OutboundRecordDTO> outList = new ArrayList<>();
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
}
