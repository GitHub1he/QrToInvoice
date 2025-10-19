package com.github.qrtoinvoice.QRCodeParser;

import com.github.qrtoinvoice.QRCodeParser.strategy.QRCodeParseStrategy;
import com.github.qrtoinvoice.QRCodeParser.strategy.QRCodeParseStrategyFactory;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 二维码解析服务
 */
@Service
public class QRCodeParseService {

    private final QRCodeParseStrategyFactory strategyFactory;

    public QRCodeParseService(QRCodeParseStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * 解析上传文件中的二维码
     */
    public String parseQRCode(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String fileExtension = FilenameUtils.getExtension(originalFilename);
        QRCodeParseStrategy strategy = strategyFactory.getStrategy(fileExtension);

        return strategy.parse(file);
    }

    /**
     * 解析本地文件中的二维码
     */
    public String parseQRCode(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        String fileExtension = FilenameUtils.getExtension(filePath);
        QRCodeParseStrategy strategy = strategyFactory.getStrategy(fileExtension);

        return strategy.parse(filePath);
    }

    /**
     * 获取支持的文件类型
     */
    public List<String> getSupportedFileTypes() {
        return strategyFactory.getSupportedFileTypes();
    }
}