package com.jovefast.job.enums;

/**
 * @description:  机器人需推送的目标
 * @author Acechengui
 * @date Created in 2022/9/19
 */
public enum RobotsTarget {
    /**
     * 测试群
     */
    TEST_BUSINESS_KEY("946f77d0-3631-852e-a089-f207eec36f2c");


    // 以上是枚举的成员，必须先定义，而且使用分号结束
    private final String value;
    RobotsTarget(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
