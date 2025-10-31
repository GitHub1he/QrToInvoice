package com.github.qrtoinvoicecore.model;

/**
 * 系统发票类型枚举
 */
public enum InvoiceTypeEnum {
    VAT_SPECIAL("01", "增值税专用发票"),
    FREIGHT_VAT_SPECIAL("02", "货物运输业增值税专用发票"),
    VEHICLE_SALES("03", "机动车销售统一发票"),
    VAT_REGULAR("04", "增值税普通发票"),
    ELECTRONIC_VAT_SPECIAL("08", "增值税电子专用发票"),
    ELECTRONIC_VAT_REGULAR("10", "增值税电子普通发票"),
    ROLL_INVOICE("11", "卷式发票"),
    TOLL_INVOICE("14", "通行费发票"),
    USED_CAR_SALES("15", "二手车销售统一发票"),
    ELECTRONIC_RAILWAY_TICKET("51", "电子发票（铁路电子客票）"),
    ELECTRONIC_TOLL_TICKET("59", "数电票（通行费）"),
    ELECTRONIC_AIR_TICKET("61", "电子发票（航空运输客票电子行程单）"),
    ELECTRONIC_INVOICE_SPECIAL("81", "电子发票（增值税专用发票）"),
    ELECTRONIC_INVOICE_REGULAR("82", "电子发票（普通发票）"),
    ELECTRONIC_VEHICLE_SALES("83", "机动车销售电子统一发票"),
    ELECTRONIC_USED_CAR_SALES("84", "二手车销售电子统一发票"),
    PAPER_INVOICE_SPECIAL("85", "纸质发票（增值税专用发票）"),
    PAPER_INVOICE_REGULAR("86", "纸质发票（普通发票）"),
    PAPER_VEHICLE_SALES("87", "纸质发票（机动车销售统一发票）"),
    PAPER_USED_CAR_SALES("88", "纸质发票（二手车销售统一发票）"),
    BLOCKCHAIN("1009", "区块链发票"),
    FINANCE("xx", "财政票据"),
    OTHER("999", "其他发票")

    ;

    private final String code;
    private final String description;

    InvoiceTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static InvoiceTypeEnum fromCode(String code) {
        for (InvoiceTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知发票类型代码: " + code);
    }
}