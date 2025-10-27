package com.github.qrtoinvoicecore.QRCodeParser.strategy;


import com.github.qrtoinvoicecore.QRCodeParser.QRCodeDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 二维码解析策略工厂
 */
public class QRCodeParseStrategyFactory {

    private final Map<String, QRCodeParseStrategy> strategyMap;

    public QRCodeParseStrategyFactory() {
        // 手动创建所有策略实例
        List<QRCodeParseStrategy> strategies = createStrategies();
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        QRCodeParseStrategy::getStrategyName,
                        strategy -> strategy
                ));
    }

    /**
     * 手动创建所有策略实例
     */
    private List<QRCodeParseStrategy> createStrategies() {
        // 创建 QRCodeDecoder
        QRCodeDecoder qrCodeDecoder = new QRCodeDecoder();

        // 创建所有策略实例
        List<QRCodeParseStrategy> strategies = new ArrayList<>();
        strategies.add(new ImageQRCodeParseStrategy(qrCodeDecoder));
        strategies.add(new PdfQRCodeParseStrategy(qrCodeDecoder));

        // 可以根据需要添加更多策略

        return strategies;
    }

    /**
     * 根据文件类型获取解析策略
     */
    public QRCodeParseStrategy getStrategy(String fileType) {
        return strategyMap.values().stream()
                .filter(strategy -> strategy.supports(fileType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("不支持的文件类型: " + fileType));
    }

    /**
     * 获取所有支持的策略
     */
    public List<QRCodeParseStrategy> getAllStrategies() {
        return new ArrayList<>(strategyMap.values());
    }

}