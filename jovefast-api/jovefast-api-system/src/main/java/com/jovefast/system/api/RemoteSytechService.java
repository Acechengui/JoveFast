package com.jovefast.system.api;

import com.jovefast.system.api.domain.Srm;
import com.jovefast.system.api.factory.RemoterSytechFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

/**
 * @Description Description
 * @Author Acechengui
 * @Date Created in 2022/7/14
 */
@FeignClient(name = "GenerateBenefitsClient",fallbackFactory= RemoterSytechFallbackFactory.class,url = "http://srm.syst.com.cn:8000")
public interface RemoteSytechService {

    @RequestMapping(value="/sap/sycrm/test",method= RequestMethod.POST,consumes = "application/json", produces = "application/json;charset=UTF-8")
    String getEngineMesasgeTest(URI uri, @RequestParam("apikey") String apikey, @RequestParam("method") String method, @RequestBody Srm srm);

    @RequestMapping(value="/sap/sycrm/api01",method= RequestMethod.POST,consumes = "application/json", produces = "application/json;charset=UTF-8")
    String getEngineMesasgeLive(URI uri, @RequestParam("apikey") String apikey, @RequestParam("method") String method,@RequestBody Srm srm);
}
