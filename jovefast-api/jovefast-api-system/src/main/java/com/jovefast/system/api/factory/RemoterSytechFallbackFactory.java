package com.jovefast.system.api.factory;

import com.alibaba.fastjson2.JSONObject;
import com.jovefast.system.api.RemoteSytechService;
import com.jovefast.system.api.domain.Srm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @Description 生益互联
 * @Author Acechengui
 * @Date Created in 2022/7/14
 */
@Component
public class RemoterSytechFallbackFactory implements FallbackFactory<RemoteSytechService> {

    private static Log logger = LogFactory.getLog(RemoterSytechFallbackFactory.class);

    /**
     * Returns an instance of the fallback appropriate for the given cause.
     *
     * @param cause cause of an exception.
     * @return fallback
     */
    @Override
    public RemoteSytechService create(Throwable cause) {
        return new RemoteSytechService() {
            /**
             * @param uri
             * @param apikey
             * @param method
             * @param srm
             * @return
             */
            @Override
            public String getEngineMesasgeTest(URI uri, String apikey, String method, Srm srm) {
                JSONObject jn=new JSONObject();
                jn.put("code",500);
                jn.put("message","调用第三方API失败！！！");
                return jn.toString();
            }

            /**
             * @param uri
             * @param apikey
             * @param method
             * @param srm
             * @return
             */
            @Override
            public String getEngineMesasgeLive(URI uri, String apikey, String method, Srm srm) {
                JSONObject jn=new JSONObject();
                jn.put("code",500);
                jn.put("message","调用第三方API失败！！！");
                return jn.toString();
            }

        };
    }
}
