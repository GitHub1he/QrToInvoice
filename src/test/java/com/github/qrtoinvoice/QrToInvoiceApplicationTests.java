package com.github.qrtoinvoice;

import com.github.qrtoinvoice.QRCodeParser.QRCodeParseService;
import com.github.qrtoinvoice.model.Invoice;
import com.github.qrtoinvoice.InvoiceParser.InvoiceParserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class QrToInvoiceApplicationTests {
    private static void processInvoice(String invoiceData) {
        try {
            System.out.println("正在解析二维码数据: " + invoiceData);
            Invoice invoice = InvoiceParserService.getInvoiceFromQr(invoiceData);
            System.out.println("解析成功: " + invoice);
            System.out.println("发票类型: " + invoice.getInvoiceType().getDescription());
            System.out.println("---");
        } catch (Exception e) {
            System.err.println("解析失败: " + e.getMessage());
            System.out.println("---");
        }
    }

    @Test
    void contextLoads() {

        // 示例1：电子发票（普通发票）
        String example1 = "01,32,,25442000000012341234,666.00,20250208,,A6A1";
        processInvoice(example1);

        // 示例2：电子发票（增值税专用发票）
        String example2 = "01,31,,24922000000012341234,666.00,20240208,,76A1";
        processInvoice(example2);

        // 示例3：二维码类型20
        String example3 = "01,02,,24117000000212341234,666.00,20250208,,";
        processInvoice(example3);

        // 示例4：动态类型（电子发票）
        String example4 = "01,32,,24117000000123412345,666.00,20250203,,";
        processInvoice(example4);


    }

    @Autowired
    private QRCodeParseService qrCodeParseService;
    @Test
    void testImageQRCodeParser() throws IOException {
        // 测试图片二维码解析
        String qrCode = qrCodeParseService.parseQRCode("D:\\04_document\\文档\\qrcode.png");
        System.out.println("图片二维码内容: " + qrCode);
        processInvoice(qrCode);
    }

    @Test
    void testPdfQRCodeParser() throws IOException {
        // 测试PDF二维码解析
        String qrCode = qrCodeParseService.parseQRCode("D:\\document.pdf");
        System.out.println("PDF二维码内容: " + qrCode);
        processInvoice(qrCode);
    }

}
