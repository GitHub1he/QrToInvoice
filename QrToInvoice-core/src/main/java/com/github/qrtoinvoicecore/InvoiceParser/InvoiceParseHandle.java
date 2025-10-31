package com.github.qrtoinvoicecore.InvoiceParser;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.util.DateUtils;
import com.github.qrtoinvoicecore.model.Invoice;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
@Slf4j
public abstract class InvoiceParseHandle {
    protected InvoiceParseHandle next;

    public abstract Invoice handle(String value);

    public InvoiceParseHandle next() {
        return this.next;
    }

    public void setNext(InvoiceParseHandle next) {
        this.next = next;
    }

    protected Invoice handleRequest(String value) {
        Invoice invoice = handle(value);
        if (null != invoice) {
            return invoice;
        }
        if (next() != null) {
            return next().handleRequest(value);
        }

        return null;
    }


    /**
     * 解析基础字段到Invoice对象
     */
    protected Invoice parseBasicFields(String value) {
        String[] fields = value.split(",", -1);

        Invoice invoice = new Invoice();
        try{
            invoice.setVersion(fields[0]);
            // 第1位是二维码类型，在具体处理器中处理
            invoice.setInvoiceCode(fields[2]);
            invoice.setInvoiceNumber(fields[3]);

            // 解析金额
            if (fields[4] != null && !fields[4].trim().isEmpty()) {
                try {
                    invoice.setAmount(new BigDecimal(fields[4]));
                } catch (NumberFormatException e) {
                    log.info("金额格式不正确: {}" , fields[4]);
                }
            }

            // 解析日期
            if (fields[5] != null && !fields[5].trim().isEmpty()) {
                invoice.setIssueDate(DateUtils.parseDate(fields[5]));
            }

            invoice.setCheckCode(fields[6]);
            invoice.setCrc(fields[7]);
        }catch (Exception ex) {
            log.info("parseBasicFields error, ", ex);
        }

        return invoice;
    }
}
