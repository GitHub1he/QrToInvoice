package com.github.qrtoinvoice.InvoiceParser.handler;

import com.github.qrtoinvoice.model.Invoice;
import com.github.qrtoinvoice.model.InvoiceTypeEnum;
import com.github.qrtoinvoice.InvoiceParser.InvoiceParseHandle;

/**
 * 动态类型处理器
 * 处理二维码类型为31、32、33、34的情况
 */
public class DynamicTypeHandler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String qrType = fields[1];
        String invoiceCode = fields[2];

        if (isDynamicType(qrType)) {
            Invoice invoice = parseBasicFields(value);
            InvoiceTypeEnum invoiceType = recognizeDynamicType(qrType, invoiceCode);
            invoice.setInvoiceType(invoiceType);
            return invoice;
        }

        return null;
    }

    private boolean isDynamicType(String qrType) {
        return "31".equals(qrType) || "32".equals(qrType) ||
                "33".equals(qrType) || "34".equals(qrType);
    }

    private InvoiceTypeEnum recognizeDynamicType(String qrType, String invoiceCode) {
        boolean isElectronic = invoiceCode == null || invoiceCode.trim().isEmpty();

        switch (qrType) {
            case "31":
                return isElectronic ? InvoiceTypeEnum.ELECTRONIC_INVOICE_SPECIAL : InvoiceTypeEnum.PAPER_INVOICE_SPECIAL;
            case "32":
                return isElectronic ? InvoiceTypeEnum.ELECTRONIC_INVOICE_REGULAR : InvoiceTypeEnum.PAPER_INVOICE_REGULAR;
            case "33":
                return isElectronic ? InvoiceTypeEnum.ELECTRONIC_VEHICLE_SALES : InvoiceTypeEnum.PAPER_VEHICLE_SALES;
            case "34":
                return isElectronic ? InvoiceTypeEnum.ELECTRONIC_USED_CAR_SALES : InvoiceTypeEnum.PAPER_USED_CAR_SALES;
            default:
                return InvoiceTypeEnum.OTHER;
        }
    }
}