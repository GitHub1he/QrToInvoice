package com.github.qrtoinvoice.QRCodeParser.strategy;

import com.github.qrtoinvoice.QRCodeParser.QRCodeDecoder;
import com.google.zxing.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 *  图片二维码解析策略
 */
@Component
public class ImageQRCodeParseStrategy implements QRCodeParseStrategy {
    // 支持的图片格式
    private static final Set<String> SUPPORTED_FORMATS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    private final QRCodeDecoder qrCodeDecoder;

    public ImageQRCodeParseStrategy(QRCodeDecoder qrCodeDecoder) {
        this.qrCodeDecoder = qrCodeDecoder;
    }

    @Override
    public boolean supports(String fileType) {
        return SUPPORTED_FORMATS.contains(fileType.toLowerCase());
    }

    @Override
    public String parse(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("无法读取图片文件");
            }
            return qrCodeDecoder.decodeFromImage(image);
        } catch (NotFoundException e) {
            throw new IOException("未在图片中找到二维码", e);
        }
    }

    @Override
    public String parse(String filePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        if (image == null) {
            throw new IOException("无法读取图片文件: " + filePath);
        }
        try {
            return qrCodeDecoder.decodeFromImage(image);
        } catch (NotFoundException e) {
            throw new IOException("未在图片中找到二维码: " + filePath, e);
        }
    }

    @Override
    public String getStrategyName() {
        return "图片二维码解析策略";
    }
}