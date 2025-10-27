package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;

/**
 * 数电票识别处理器
 * 处理发票号码为20位的情况
 */
public class DigitalInvoiceHandler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String qrType = fields[1];
        String invoiceNumber = fields[3];

        // 检查是否为数电票（20位发票号码）
        if (invoiceNumber != null && invoiceNumber.length() == 20) {
            Invoice invoice = parseBasicFields(value);
            char fifthChar = invoiceNumber.charAt(4); // 第5位

            if (fifthChar == '8') {
                invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_AIR_TICKET);
                return invoice;
            } else if (fifthChar == '9') {
                invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_RAILWAY_TICKET);
                return invoice;
            } else if (isDigitalToll(qrType, invoiceNumber)) {
                invoice.setInvoiceType(InvoiceTypeEnum.ELECTRONIC_TOLL_TICKET);
                return invoice;
            }
        }

        return null;
    }

    private boolean isDigitalToll(String qrType, String invoiceNumber) {
        return "32".equals(qrType) &&
                invoiceNumber.length() == 20 &&
                "79".equals(invoiceNumber.substring(4, 6));
    }
}
