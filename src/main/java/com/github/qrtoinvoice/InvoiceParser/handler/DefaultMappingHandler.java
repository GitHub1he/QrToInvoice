package com.github.qrtoinvoice.InvoiceParser.handler;

import com.github.qrtoinvoice.model.Invoice;
import com.github.qrtoinvoice.model.InvoiceTypeEnum;
import com.github.qrtoinvoice.InvoiceParser.InvoiceParseHandle;

/**
 * 默认映射处理器
 * 处理其他二维码类型到系统类型的映射
 */
public class DefaultMappingHandler extends InvoiceParseHandle {
    @Override
    public Invoice handle(String value) {
        String[] fields = value.split(",");
        String qrType = fields[1];

        Invoice invoice = parseBasicFields(value);
        InvoiceTypeEnum invoiceType = mapQrTypeToSystemType(qrType);
        invoice.setInvoiceType(invoiceType);

        return invoice;
    }

    private InvoiceTypeEnum mapQrTypeToSystemType(String qrType) {
        switch (qrType) {
            case "01": return InvoiceTypeEnum.VAT_SPECIAL;
            case "02": return InvoiceTypeEnum.FREIGHT_VAT_SPECIAL;
            case "03": return InvoiceTypeEnum.VEHICLE_SALES;
            case "04": return InvoiceTypeEnum.VAT_REGULAR;
            case "10": return InvoiceTypeEnum.ELECTRONIC_VAT_REGULAR;
            case "11": return InvoiceTypeEnum.ROLL_INVOICE;
            case "14": return InvoiceTypeEnum.TOLL_INVOICE;
            case "15": return InvoiceTypeEnum.USED_CAR_SALES;
            case "51": return InvoiceTypeEnum.ELECTRONIC_RAILWAY_TICKET;
            case "61": return InvoiceTypeEnum.ELECTRONIC_AIR_TICKET;
            default:
                return InvoiceTypeEnum.OTHER;
        }
    }
}