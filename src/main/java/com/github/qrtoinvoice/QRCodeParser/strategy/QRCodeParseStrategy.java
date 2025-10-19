package com.github.qrtoinvoice.QRCodeParser.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 二维码解析策略接口
 */
public interface QRCodeParseStrategy {

    /**
     * 是否支持该文件类型
     */
    boolean supports(String fileType);

    /**
     * 解析MultipartFile中的二维码
     */
    String parse(MultipartFile file) throws IOException;

    /**
     * 解析文件路径中的二维码
     */
    String parse(String filePath) throws IOException;

    /**
     * 获取策略名称
     */
    String getStrategyName();
}