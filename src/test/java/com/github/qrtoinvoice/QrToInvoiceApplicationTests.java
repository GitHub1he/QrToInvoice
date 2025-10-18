package com.github.qrtoinvoice;

import com.github.qrtoinvoice.model.Invoice;
import com.github.qrtoinvoice.qr.InvoiceParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QrToInvoiceApplicationTests {
    private static void processInvoice(String invoiceData) {
        try {
            Invoice invoice = InvoiceParser.getInvoiceFromQr(invoiceData);
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

        // 示例1：普通电子发票
        String example1 = "01,32,,25442000000069606292,140.74,20250208,,A6A1";
        processInvoice(example1);

        // 示例2：数电票航空运输
        String example2 = "01,32,,25442800000069606292,140.74,20250208,,A6A1";
        processInvoice(example2);

        // 示例3：二维码类型20
        String example3 = "01,20,,25442000000069606292,140.74,20250208,,A6A1";
        processInvoice(example3);

        // 示例4：动态类型（电子发票）
        String example4 = "01,31,,25442000000069606292,140.74,20250208,,A6A1";
        processInvoice(example4);

        // 示例5：动态类型（纸质发票）
        String example5 = "01,31,1234567890,25442000000069606292,140.74,20250208,,A6A1";
        processInvoice(example5);


    }

}
