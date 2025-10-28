package com.github.qrtoinvoicecore;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParser;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.utils.GBT.GBT2260_2013;
import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.util.Map;

public class test {
    public static void main(String[] args) throws IOException, NotFoundException {

//        QRCodeDecoder qrCodeDecoder = new QRCodeDecoder();
//        BufferedImage bufferedImage = ImageIO.read(new File("D:\\02_code\\QrToInvoice\\QrToInvoice-core\\src\\main\\resources\\img.jpg"));
//        try{
//            String result = qrCodeDecoder.decodeFromImage(bufferedImage);
//            System.out.println(result);
//        } catch (Exception e) {
//            System.out.println("e.getMessage() = " + e.getMessage());
//        }
//        Invoice invoiceFromQr = InvoiceParser.getInvoiceFromQr("01,10,153002209100,11335527,91530102MA7KNHQC36,459.00,20221102,00fe434c8ab38e968174bcbc88efb4ea48a736b0b3b84c9824a9bf4c81d5cf7973");
//        System.out.println("invoiceFromQr = " + invoiceFromQr);

        GBT2260_2013 gbt2260 = GBT2260_2013.getInstance();
        String beijing = gbt2260.getAreaName(11); // 返回 "北京市"
        System.out.println("beijing = " + beijing);
        Map<Integer, String> allAreas = gbt2260.getDictionary();
        System.out.println("allAreas = " + allAreas);
    }

}
