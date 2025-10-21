package com.github.qrtoinvoicecore.QRCodeParser.strategy;

import java.io.IOException;
import java.io.InputStream;

/**
 * 二维码解析策略接口
 */
public interface QRCodeParseStrategy {
    /**
     * 获取策略名称
     */
    String getStrategyName();

    /**
     * 是否支持该文件类型
     */
    boolean supports(String fileType);

    /**
     * 解析MultipartFile中的二维码
     */
    String parse(InputStream input) throws IOException;

    /**
     * 解析文件路径中的二维码
     */
    String parse(String filePath) throws IOException;


}