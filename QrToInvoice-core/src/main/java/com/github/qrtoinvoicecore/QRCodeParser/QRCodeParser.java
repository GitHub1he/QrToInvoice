package com.github.qrtoinvoicecore.QRCodeParser;

import com.github.qrtoinvoicecore.QRCodeParser.strategy.QRCodeParseStrategy;
import com.github.qrtoinvoicecore.QRCodeParser.strategy.QRCodeParseStrategyFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.*;

/**
 * 二维码解析服务
 */
public class QRCodeParser {

    private final QRCodeParseStrategyFactory strategyFactory;

    public QRCodeParser() {
        this.strategyFactory = new QRCodeParseStrategyFactory();
    }

    public QRCodeParser(QRCodeParseStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * 解析上传文件中的二维码
     */
    public String parseQRCode(InputStream inputStream, String originalFilename) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("文件不能为空");
        }
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String fileExtension = FilenameUtils.getExtension(originalFilename);
        QRCodeParseStrategy strategy = strategyFactory.getStrategy(fileExtension);

        return strategy.parse(inputStream);
    }

    public String parseQRCode(byte[] fileData, String fileName) throws IOException {
        String fileType = FilenameUtils.getExtension(fileName);
        QRCodeParseStrategy strategy = strategyFactory.getStrategy(fileType);

        // 如果策略使用 InputStream
        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            return strategy.parse(inputStream);
        }
    }

    public String parseQRCode(File file) throws IOException {
        String fileType = FilenameUtils.getExtension(file.getName());
        QRCodeParseStrategy strategy = strategyFactory.getStrategy(fileType);

        try (InputStream inputStream = new FileInputStream(file)) {
            return strategy.parse(inputStream);
        }
    }

}