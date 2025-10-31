package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.alibaba.fastjson2.util.DateUtils;
import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;
import com.github.qrtoinvoicecore.utils.AreaUtils;
import com.github.qrtoinvoicecore.utils.GBT.GBT2260_2013;

import java.math.BigDecimal;

/**
 * 12 位增值税发票代码
 * 处理发票代码为12位的情况
 */
public class VATCode12Handler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String invoiceCode = fields[2];

        if (invoiceCode != null && invoiceCode.length() == 12) {
            char code0 = invoiceCode.charAt(0); // 第1位

            String areaCode = invoiceCode.substring(1, 5);
            if (!AreaUtils.isValidRegionCode(areaCode)) {
                return null;
            }

            if (code0 == '1') {
                if (invoiceCode.charAt(7) == '0' && invoiceCode.charAt(8) == '9' && invoiceCode.charAt(11) == '0') {
                    // 区块链
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceType(InvoiceTypeEnum.BLOCKCHAIN);
                    invoice.setInvoiceCode(invoiceCode);
                    invoice.setInvoiceNumber(fields[2]);
                    invoice.setAmount(new BigDecimal(fields[5]));
                    if (fields[6] != null && !fields[6].trim().isEmpty()) {
                        invoice.setIssueDate(DateUtils.parseDate(fields[6]));
                    }
                    invoice.setCheckCode(fields[7].substring(fields[7].length() - 6));
                    return invoice;
                }
            } else if (code0 == '0') {
                String codeType = invoiceCode.substring(10, 12);
                switch (codeType) {
                    case "11" -> {
                        Invoice invoice = parseBasicFields(value);
                        invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_VAT_REGULAR);
                        return invoice;
                    }
                    case "12" -> {
                        Invoice invoice = parseBasicFields(value);
                        invoice.setInvoiceType(InvoiceTypeEnum.TOLL_INVOICE);
                        return invoice;
                    }
                    case "13" -> {
                        Invoice invoice = parseBasicFields(value);
                        invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_VAT_SPECIAL);
                        return invoice;
                    }
                    case "06", "07" -> {
                        Invoice invoice = parseBasicFields(value);
                        invoice.setInvoiceType(InvoiceTypeEnum.ROLL_INVOICE);
                        return invoice;
                    }
                    case "04", "05" -> {
                        // 增值税普通发票（折叠票）
                        Invoice invoice = parseBasicFields(value);
                        invoice.setInvoiceType(InvoiceTypeEnum.VAT_REGULAR);
                        return invoice;
                    }
                }
            }
        }

        return null;
    }
}