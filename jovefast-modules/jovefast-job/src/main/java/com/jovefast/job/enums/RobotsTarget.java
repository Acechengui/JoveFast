package com.jovefast.job.enums;

/**
 * @Description 机器人需推送的目标
 * @Author Acechengui
 * @Date Created in 2022/9/19
 */
public enum RobotsTarget {
    /**
     * 测试群
     */
    TEST_BUSINESS_KEY("946f36d0-3471-492e-a089-f207eec36f2c"),
    /**
     * 美国业务沟通群
     */
    US_BUSINESS_KEY("182f5fe0-f2b9-4a82-987e-c800a61405b9"),
    /**
     * 订单履行群
     */
    VCWIP_KEY("89245d3c-5407-4df6-b4d2-650d0eec3167"),
    /**
     * 工序时间停留管制：报警信息推送群 P3
     */
    CALL_POLICE_P3_KEY("e98120dc-594e-48e8-85ba-c0d961090b69"),
    /**
     * 工序时间停留管制：报警信息推送群 P2
     */
    CALL_POLICE_P2_KEY("d9d328d2-a380-418b-a377-4246284b0edb"),
    /**
     * 工序时间停留管制：预警信息推送APP key
     */
    EARLY_WARNING_APP_KEY("pNuCY-1oJ5OnEAI55qjH_ESudQ_w7VU2LbEVXq-oGbY"),
    /**
     * 消息推送APP secret ID
     */
    MESSAGE_APP_KEY("4sm6seS2rgGJmfR3TvzXCkZAK3jESIf07ILnlViBY0U"),
    /**
     * 企业微信应用-自主预警
     */
    APP_IEW("GeK197MqCBKiKGNdOFubg1VxyVkvlHYYRSCuZBJdfoM"),
    /**
     * 分层分级过数自主预警 机器人KEY
     */
    FCFJ_ROBOT_KEY("wrfdPQDAAAbmPPwqeCMLYxbGdyICIyAQ"),

    /**
     * 成品库存超周期
     */
    INVENTORY_OVERRUN("920a0529-11f8-4cfe-8476-ada63b24dc72"),
    /**
     * 一厂未与货架绑定的入仓签
     */
    UNBOUND_BOX_LABEL_ONE("015543b9-909e-4433-b7fb-7b940236c086"),
    /**
     * 电测过数自主预警 机器人KEY
     */
    DC_ROBOT_KEY("wrfdPQDAAAu0QSR6qbsaZwrDFNbPynEA");

    // 以上是枚举的成员，必须先定义，而且使用分号结束
    private final String value;
    RobotsTarget(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
