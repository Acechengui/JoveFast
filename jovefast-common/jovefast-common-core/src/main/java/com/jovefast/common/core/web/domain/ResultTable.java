package com.jovefast.common.core.web.domain;

/**
 * 积木报表API接口返回类
 */
public class ResultTable {
     /**
     * 状态码
     * */
    private Integer code;

    /**
     * 提示消息
     * */
    private String msg;

    /**
     * 消息总量
     * */
    private Long count;

    /**
     * 数据对象
     * */
    private Object data;

    /**
     * 分页总条数 默认每页10条记录
     * */
    private int total;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ResultTable() {
    }

    /**
     * 构 建
     * */
    public static ResultTable pageTable(long count,Object data){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(0);
        resultTable.setCount(count);
        return resultTable;
    }

    public static ResultTable pageTable(long count,Object data,int total){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(0);
        resultTable.setCount(count);
        resultTable.setTotal(total);
        return resultTable;
    }

    public static ResultTable dataTable(Object data){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(0);
        return resultTable;
    }

    public static ResultTable dataInFo(Integer code,String msg,Object data,Long count){
        ResultTable resultTable = new ResultTable();
        resultTable.setMsg(msg);
        resultTable.setData(data);
        resultTable.setCode(code);
        resultTable.setCount(count);
        return resultTable;
    }
}
