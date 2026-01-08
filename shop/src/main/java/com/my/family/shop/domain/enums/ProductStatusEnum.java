package com.my.family.shop.domain.enums;

/**
 * 商品状态枚举
 * 0-冻结，1-启用
 *
 * @author fan
 * @date 2026/01/05
 */
public enum ProductStatusEnum {

    /**
     * 冻结：不可售卖，不在前端展示
     */
    FROZEN(0, "冻结"),

    /**
     * 启用：正常可售卖，可在前端展示
     */
    ENABLED(1, "启用");

    private final Integer code;

    private final String desc;

    ProductStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}


