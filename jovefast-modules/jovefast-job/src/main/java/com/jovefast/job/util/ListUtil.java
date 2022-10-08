package com.jovefast.job.util;

import com.jovefast.job.domain.VRptWarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ListUtil {

    /**
     * 根据单位进行二次分类
     * @param maps 已经经过物料类别分类的物料
     * @return
     */
    public static Map<String, List<VRptWarehouse>> groupVrsByUnitName(Map<String, List<VRptWarehouse>> maps){
         return new HashMap<String, List<VRptWarehouse>>(){
             private static final long serialVersionUID = -359992659162895335L;
             {
            for(String key : maps.keySet()){
                for(VRptWarehouse vr : maps.get(key)){
                    String this_key = key + " - " + vr.getUnitName();
                    if(containsKey(this_key)){
                        get(this_key).add(vr);
                    }else{
                        List<VRptWarehouse> list = new ArrayList<>();
                        list.add(vr);
                        put(this_key, list);
                    }
                }
            }
        }};
    }

    /**
     * 1. 将钻咀和锣刀分开成两类；
     * 2. 把复合模具类改为模具类；
     * 3. 删除不统计成品和半成品--脚本中已过滤
     * 4. 将铜球，锡球/锡条从贵金属类里分出来作为单独的两大类统计
     */
    public static List<VRptWarehouse> materialClassification(List<VRptWarehouse> inDTOList){
        inDTOList.forEach(f->{
            // [钻嘴/锣刀] 类处理
            if("R".contains(f.getProductCode())){
                if(f.getInvPartDescription().contains("钻咀") || f.getInvPartDescription().contains("槽刀")){
                    f.setProductName("钻咀");
                }else {
                    f.setProductName("锣刀");
                }
            }
            //复合模具类 改为 模具类
            if("M".contains(f.getProductCode())){
                f.setProductName("模具");
            }
            //贵重金属类
            if("S".contains(f.getProductCode())){
                if(f.getInvPartDescription().contains("锡球") || f.getInvPartDescription().contains("锡条")){
                    f.setProductName("锡球/锡条");
                }else if(f.getInvPartDescription().contains("铜球")){
                    f.setProductName("铜球");
                }else {
                    f.setProductName("其他贵重金属");
                }
            }
            f.setProductCode(f.getProductCode().trim());
            f.setProductName(f.getProductName().trim());
        });
        return inDTOList;
    }

    /**
     * 物料金额占比分组
     *
     * @param inDTOList 查询结果集合
     * @param map       分组集合
     * @return 结果集
     */
    public static List<VRptWarehouse> proportionOfMaterial(List<VRptWarehouse> inDTOList, Map<String, List<VRptWarehouse>> map) {
        List<VRptWarehouse> list = new ArrayList<>();
        double sum = inDTOList.stream().mapToDouble(VRptWarehouse::getPricesum).sum();

        map.forEach((key, value) -> {
            AtomicReference<Double> priceSum = new AtomicReference<>(0.00);
            AtomicReference<Double> qtyIssuedSum = new AtomicReference<>(0.00);
            map.get(key).forEach(f -> {
                if (f.getPricesum() != null) {
                    priceSum.set(priceSum.get() + f.getPricesum());
                }
                if (f.getQtyIssued() != null) {
                    qtyIssuedSum.set(qtyIssuedSum.get() + f.getQtyIssued());
                }
            });
            list.add(new VRptWarehouse(key, qtyIssuedSum.get(), priceSum.get(), (priceSum.get() / sum)));
        });
        return list;
    }

    /**
     * List集合拆分
     * @param list list集合对象
     * @param subNum 每组的大小
     */
    public static <T> List<List<T>> splistList(List<T> list,int subNum) {
        List<List<T>> tNewList = new ArrayList<List<T>>();
        int priIndex = 0;
        int lastPriIndex = 0;
        int insertTimes = list.size()/subNum;
        List<T> subList = new ArrayList<>();
        for (int i = 0;i <= insertTimes;i++) {
            priIndex = subNum*i;
            lastPriIndex = priIndex + subNum;
            if (i == insertTimes) {
                subList = list.subList(priIndex,list.size());
            } else {
                subList = list.subList(priIndex,lastPriIndex);
            }
            if (subList.size() > 0) {
                tNewList.add(subList);
            }
        }
        return tNewList;
    }

}
