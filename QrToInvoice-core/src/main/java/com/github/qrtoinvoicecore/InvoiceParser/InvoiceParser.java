package com.github.qrtoinvoicecore.InvoiceParser;

import com.github.qrtoinvoicecore.InvoiceParser.handler.*;
import com.github.qrtoinvoicecore.model.Invoice;

/**
 * 发票解析工具类
 */
public class InvoiceParser {

    /**
     * 根据传入二维码字符串生成Invoice
     */
    public static Invoice getInvoiceFromQr(String qrCodeResult) {
        // 构建责任链
        InvoiceParseHandle chain = buildChain();

        // 执行责任链处理
        return chain.handleRequest(qrCodeResult);
    }

    /**
     * 构建责任链
     * 顺序：链接 -> 数电票识别 -> 特殊代码20 -> 动态类型 -> 代码10位 -> 代码12位 -> 默认映射
     */
    private static InvoiceParseHandle buildChain() {
        HttpLinksHandler linksHandler = new HttpLinksHandler();
        DigitalInvoiceHandler digitalHandler = new DigitalInvoiceHandler();
        SpecialCode20Handler special20Handler = new SpecialCode20Handler();
        DynamicTypeHandler dynamicHandler = new DynamicTypeHandler();
        VATCode10Handler vatCode10Handler = new VATCode10Handler();
        VATCode12Handler vatCode12Handler = new VATCode12Handler();
        DefaultMappingHandler defaultHandler = new DefaultMappingHandler();

        linksHandler.setNext(digitalHandler);
        digitalHandler.setNext(special20Handler);
        special20Handler.setNext(dynamicHandler);
        dynamicHandler.setNext(vatCode10Handler);
        vatCode10Handler.setNext(vatCode12Handler);
        vatCode12Handler.setNext(defaultHandler);

        return linksHandler;
    }
}