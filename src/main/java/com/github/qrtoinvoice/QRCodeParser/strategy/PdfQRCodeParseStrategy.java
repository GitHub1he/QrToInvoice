package com.github.qrtoinvoice.QRCodeParser.strategy;

import com.github.qrtoinvoice.QRCodeParser.QRCodeDecoder;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *  PDF二维码解析策略，依赖图片二维码解析策略
  */
@Component
public class PdfQRCodeParseStrategy implements QRCodeParseStrategy {

    private final QRCodeDecoder qrCodeDecoder;

    public PdfQRCodeParseStrategy(QRCodeDecoder qrCodeDecoder) {
        this.qrCodeDecoder = qrCodeDecoder;
    }

    @Override
    public boolean supports(String fileType) {
        return "pdf".equalsIgnoreCase(fileType);
    }

    @Override
    public String parse(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            return parsePdfDocument(document, file.getOriginalFilename());
        }
    }

    @Override
    public String parse(String filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            return parsePdfDocument(document, filePath);
        }
    }

    @Override
    public String getStrategyName() {
        return "PDF二维码解析策略";
    }

    /**
     * 解析PDF文档
     */
    private String parsePdfDocument(PDDocument document, String sourceName) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // 遍历PDF的每一页
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            try {
                // 将PDF页面渲染为图片（使用合适的DPI）
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 200);

                // 尝试解析二维码
                String result = qrCodeDecoder.decodeFromImageSafely(image);
                if (result != null && !result.trim().isEmpty()) {
                    return result;
                }
            } catch (Exception e) {
                // 当前页面没有二维码，继续下一页
                continue;
            }
        }
        throw new IOException("未在PDF中找到二维码: " + sourceName);
    }
}