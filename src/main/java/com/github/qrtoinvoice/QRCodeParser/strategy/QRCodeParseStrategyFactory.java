package com.github.qrtoinvoice.QRCodeParser.strategy;

import com.github.qrtoinvoice.QRCodeParser.strategy.ImageQRCodeParseStrategy;
import com.github.qrtoinvoice.QRCodeParser.strategy.PdfQRCodeParseStrategy;
import com.github.qrtoinvoice.QRCodeParser.strategy.QRCodeParseStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 二维码解析策略工厂
 */
@Component
public class QRCodeParseStrategyFactory {

    private final Map<String, QRCodeParseStrategy> strategyMap;

    public QRCodeParseStrategyFactory(List<QRCodeParseStrategy> strategies) {
        // 将策略列表转换为映射，便于查找
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getStrategyName(),
                        strategy -> strategy
                ));
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
        return List.copyOf(strategyMap.values());
    }

    /**
     * 获取所有支持的文件类型
     */
    public List<String> getSupportedFileTypes() {
        return strategyMap.values().stream()
                .map(strategy -> {
                    if (strategy instanceof ImageQRCodeParseStrategy) {
                        return "jpg,jpeg,png,gif,bmp,webp";
                    } else if (strategy instanceof PdfQRCodeParseStrategy) {
                        return "pdf";
                    }
                    return "";
                })
                .filter(type -> !type.isEmpty())
                .collect(Collectors.toList());
    }
}