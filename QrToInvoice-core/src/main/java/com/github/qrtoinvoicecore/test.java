package com.github.qrtoinvoicecore;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParser;
import com.github.qrtoinvoicecore.QRCodeParser.QRCodeDecoder;
import com.github.qrtoinvoicecore.QRCodeParser.QRCodeParser;
import com.github.qrtoinvoicecore.QRCodeParser.strategy.ImageQRCodeParseStrategy;
import com.github.qrtoinvoicecore.QRCodeParser.strategy.PdfQRCodeParseStrategy;
import com.github.qrtoinvoicecore.QRCodeParser.strategy.QRCodeParseStrategy;
import com.github.qrtoinvoicecore.model.Invoice;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

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
//        try {
//            List<String>  result2 = decodeMultiple(bufferedImage);
//
//
//            System.out.println("result2 = " + result2);
//        } catch (Exception e) {
//            System.out.println("e.getMessage() = " + e.getMessage());
//        }
        Invoice invoiceFromQr = InvoiceParser.getInvoiceFromQr("https://bcfp.shenzhen.chinatax.gov.cn/verify/scan?hash=01955ef909a61c7d14c7b80ad4ae5284bd39079f03e73cc8585af3949ff2dbfa20&bill_num=30532037&total_amount=7200");
        System.out.println("invoiceFromQr = " + invoiceFromQr);

    }


    private static List<String> decodeMultiple(BufferedImage bufferedImage) throws NotFoundException, FormatException, ChecksumException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        EnumMap<DecodeHintType, Object> decodeHints = new EnumMap<>(DecodeHintType.class);
        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
        decodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeMultiReader multiReader = new QRCodeMultiReader();
        Result[] results = multiReader.decodeMultiple(bitmap, decodeHints);

        List<String> qrCodes = new ArrayList<>();
        for (Result result : results) {
            qrCodes.add(result.getText());
        }
        return qrCodes;
    }


    public String decodeFromImage(BufferedImage image) throws IOException {
        List<String> results = new ArrayList<>();
        List<Exception> exceptions = new ArrayList<>();

        // 方法1：原始方法
        try {
            String result = decodeWithHybridBinarizer(image);
            results.add(result);
        } catch (NotFoundException e) {
            exceptions.add(e);
        }

        // 方法2：全局直方图二值化
        try {
            String result = decodeWithGlobalHistogram(image);
            results.add(result);
        } catch (NotFoundException e) {
            exceptions.add(e);
        }

        // 方法3：缩放图片后识别
        try {
            String result = decodeWithScaling(image);
            results.add(result);
        } catch (NotFoundException e) {
            exceptions.add(e);
        }

        // 方法4：预处理后识别
        try {
            String result = decodeWithPreprocessing(image);
            results.add(result);
        } catch (NotFoundException e) {
            exceptions.add(e);
        }

        if (!results.isEmpty()) {
            // 返回第一个成功的结果
            return results.get(0);
        }

        throw new IOException("无法识别二维码，尝试了 " + exceptions.size() + " 种方法都失败");
    }

    private String decodeWithHybridBinarizer(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return decodeBitmap(bitmap);
    }

    private String decodeWithGlobalHistogram(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        return decodeBitmap(bitmap);
    }

    private String decodeWithScaling(BufferedImage image) throws NotFoundException {
        // 尝试放大图片（对于小二维码）
        BufferedImage scaled = scaleImage(image, 2.0);
        LuminanceSource source = new BufferedImageLuminanceSource(scaled);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return decodeBitmap(bitmap);
    }

    private String decodeWithPreprocessing(BufferedImage image) throws NotFoundException {
        BufferedImage processed = preprocessImage(image);
        LuminanceSource source = new BufferedImageLuminanceSource(processed);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return decodeBitmap(bitmap);
    }

    private String decodeBitmap(BinaryBitmap bitmap) throws NotFoundException {
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS,
                Arrays.asList(BarcodeFormat.QR_CODE));

        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    private BufferedImage scaleImage(BufferedImage image, double scale) {
        int newWidth = (int) (image.getWidth() * scale);
        int newHeight = (int) (image.getHeight() * scale);

        BufferedImage scaled = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return scaled;
    }

    private BufferedImage preprocessImage(BufferedImage image) {
        // 转换为灰度图
        BufferedImage grayImage = new BufferedImage(
                image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY
        );
        Graphics2D g = grayImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // 增强对比度
        RescaleOp rescaleOp = new RescaleOp(1.3f, 10, null);
        return rescaleOp.filter(grayImage, null);
    }
}
