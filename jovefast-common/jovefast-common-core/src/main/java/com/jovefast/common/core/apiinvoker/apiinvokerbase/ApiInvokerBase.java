package com.jovefast.common.core.apiinvoker.apiinvokerbase;


import com.alibaba.fastjson2.JSONObject;
import com.jovefast.common.core.apiinvoker.apiinvokerbase.requesttype.RequestType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * 自定义请求器基础类
 */
public class ApiInvokerBase {

    /**
     * API请求器基础方法
     *
     * @param url   请求的URL链接(必填)
     * @param rt    请求发生(必填)
     * @param param parameter参数(非必填)
     * @param body  body参数(非必填)
     * @return 返回响应体
     */
    protected static Response myApiInvoker(String url, RequestType rt, Map<String, Object> param, JSONObject body) {

        //请求器标准对象
        RequestSpecification rs = RestAssured.given();

        //设置param参数
        if (param != null) {
            for (String p : param.keySet()) {
                rs = rs.queryParam(p, param.get(p));
            }
        }

        //响应JSON格式
        rs = rs.contentType("application/json");

        //设置body参数
        if (body != null) {
            rs = rs.body(body);
        }

        //响应对象
        Response r = null;

        //设置请求方式
        switch (rt) {
            case GET:
                r = rs.get(url);
                break;

            case POST:
                r = rs.post(url);
                break;
            default:
                break;
        }

        //返回响应体
        return r.then()
                .extract()
                .response();
    }

}
