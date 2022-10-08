package com.jovefast.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.jovefast.gateway.handler.SentinelFallbackHandler;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * 网关限流配置
 * 
 * @author Acechengui
 */
@Configuration
public class GatewayConfig
{
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler()
    {
        return new SentinelFallbackHandler();
    }

    /*@Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter()
    {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit()
    {
        // 加载网关限流规则
        initGatewayRules();
    }

    *//**
     * 网关限流规则
     *//*
    private void initGatewayRules()
    {
        Set<GatewayFlowRule> rules = new HashSet<>();
        //对应路由id
        rules.add(new GatewayFlowRule("jove-report-test")
                // 限流阈值
                .setCount(5)
                // 统计时间窗口，单位是秒，默认是 1 秒
                .setIntervalSec(10));
        rules.add(new GatewayFlowRule("jove-platformdock-test")
                // 限流阈值
                .setCount(5)
                .setIntervalSec(10));
        // 加载网关限流规则
        GatewayRuleManager.loadRules(rules);
        // 加载限流分组
        //initCustomizedApis();
    }*/

    /**
     * 限流分组
     */
    /*private void initCustomizedApis()
    {
        Set<ApiDefinition> definitions = new HashSet<>();
        // jove-report 组
        ApiDefinition api1 = new ApiDefinition("jove-report-test").setPredicateItems(new HashSet<ApiPredicateItem>()
        {
            private static final long serialVersionUID = 1L;
            {
                // 匹配
                add(new ApiPathPredicateItem().setPattern("/jmreport/view/**")
                        .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }
        });
        // jove-platformdock 组
        ApiDefinition api2 = new ApiDefinition("jove-platformdock-test").setPredicateItems(new HashSet<ApiPredicateItem>()
        {
            private static final long serialVersionUID = 1L;
            {
                // 匹配
                add(new ApiPathPredicateItem().setPattern("/sysdock/**"));
            }
        });
        definitions.add(api1);
        definitions.add(api2);
        // 加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }*/
}