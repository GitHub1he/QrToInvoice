package com.github.qrtoinvoice.qr;

import com.github.qrtoinvoice.model.Invoice;
import com.github.qrtoinvoice.qr.handler.DefaultMappingHandler;
import com.github.qrtoinvoice.qr.handler.DigitalInvoiceHandler;
import com.github.qrtoinvoice.qr.handler.DynamicTypeHandler;
import com.github.qrtoinvoice.qr.handler.SpecialCode20Handler;

/**
 * 发票解析工具类
 */
public class InvoiceParser {

    /**
     * 根据传入二维码字符串生成Invoice
     */
    public static Invoice getInvoiceFromQr(String qrCodeResult) {
        // 构建责任链
        QrCodeHandle chain = buildChain();

        // 执行责任链处理
        return chain.handleRequest(qrCodeResult);
    }

    /**
     * 构建责任链
     * 顺序：数电票识别 -> 特殊代码20 -> 动态类型 -> 默认映射
     */
    private static QrCodeHandle buildChain() {
        DigitalInvoiceHandler digitalHandler = new DigitalInvoiceHandler();
        SpecialCode20Handler special20Handler = new SpecialCode20Handler();
        DynamicTypeHandler dynamicHandler = new DynamicTypeHandler();
        DefaultMappingHandler defaultHandler = new DefaultMappingHandler();

        digitalHandler.setNext(special20Handler);
        special20Handler.setNext(dynamicHandler);
        dynamicHandler.setNext(defaultHandler);

        return digitalHandler;
    }
}