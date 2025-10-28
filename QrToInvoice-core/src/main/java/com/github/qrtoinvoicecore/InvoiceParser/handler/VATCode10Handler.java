package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;

/**
 * 10 位增值税发票代码
 * 处理发票代码为10位的情况
 */
public class VATCode10Handler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String invoiceCode = fields[2];

        if (invoiceCode != null && invoiceCode.length() == 10) {
            char code10Type = invoiceCode.charAt(7); // 第8位

            // todo 1-4 位：行政区划代码（对应地区名称） 判断代码是否符合 GB/T 2260 标准字典

            if (code10Type == '1' || code10Type == '5') {
                Invoice invoice = parseBasicFields(value);
                invoice.setInvoiceType(InvoiceTypeEnum.VAT_SPECIAL);
                return invoice;
            } else if (code10Type == '3' || code10Type == '6') {
                Invoice invoice = parseBasicFields(value);
                invoice.setInvoiceType(InvoiceTypeEnum.VAT_REGULAR);
                return invoice;
            } else if (code10Type == '2' || code10Type == '7') {
                Invoice invoice = parseBasicFields(value);
                invoice.setInvoiceType(InvoiceTypeEnum.FREIGHT_VAT_SPECIAL);
                return invoice;
            }
        }

        return null;
    }
}