package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.alibaba.fastjson2.util.DateUtils;
import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;
import com.github.qrtoinvoicecore.utils.AreaUtils;
import com.github.qrtoinvoicecore.utils.GBT.GBT2260_2013;

import java.math.BigDecimal;

/**
 * 财政票据类型处理器
 */
public class FinanceInvoiceHandler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String czType = fields[0];
        if (null == czType || czType.isBlank()) {
            return null;
        }

        if (czType.startsWith("CZ")) {
            String[] split = czType.split("-", -1);
            if (split.length < 2) {
                return null;
            }
            if (!AreaUtils.isValidRegionCode(split[2])) {
                return null;
            }

            // 财政票据
            Invoice invoice = new Invoice();
            invoice.setInvoiceType(InvoiceTypeEnum.FINANCE);
            invoice.setInvoiceCode(fields[2]);
            invoice.setInvoiceNumber(fields[3]);
            invoice.setCheckCode(fields[4]);
            invoice.setIssueDate(DateUtils.parseDate(fields[5]));
            invoice.setAmount(new BigDecimal(fields[6]));

            return invoice;
        }


        return null;
    }

}