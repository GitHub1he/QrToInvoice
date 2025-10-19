package com.github.qrtoinvoice.InvoiceParser;

import com.github.qrtoinvoice.model.Invoice;

import java.math.BigDecimal;

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
        if (fields.length < 8) {
            throw new IllegalArgumentException("二维码数据格式不正确，字段数量不足");
        }

        Invoice invoice = new Invoice();
        invoice.setVersion(fields[0]);
        // 第1位是二维码类型，在具体处理器中处理
        invoice.setInvoiceCode(fields[2]);
        invoice.setInvoiceNumber(fields[3]);

        // 解析金额
        if (fields[4] != null && !fields[4].trim().isEmpty()) {
            try {
                invoice.setAmount(new BigDecimal(fields[4]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("金额格式不正确: " + fields[4]);
            }
        }

        // 解析日期
        if (fields[5] != null && !fields[5].trim().isEmpty()) {
            try {
                // 将YYYYMMDD格式转换为Date
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
                invoice.setIssueDate(sdf.parse(fields[5]));
            } catch (java.text.ParseException e) {
                throw new IllegalArgumentException("日期格式不正确: " + fields[5]);
            }
        }

        invoice.setCheckCode(fields[6]);
        invoice.setCrc(fields[7]);

        return invoice;
    }
}
