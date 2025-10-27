package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;

/**
 * 特殊代码20处理器
 * 二维码中发票类型代码为20时，转换为08
 */
public class SpecialCode20Handler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String qrType = fields[1];

        if ("20".equals(qrType)) {
            Invoice invoice = parseBasicFields(value);
            invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_VAT_SPECIAL);
            return invoice;
        }

        return null;
    }
}