package com.github.qrtoinvoicespringbootstarter.config.config;

import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParser;
import com.github.qrtoinvoicecore.QRCodeParser.QRCodeParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(InvoiceParser.class)
@EnableConfigurationProperties(QrToInvoiceProperties.class)
public class QrToInvoiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InvoiceParser qrCodeParser() {
        return new InvoiceParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public QRCodeParser qrCodeParseService(QRCodeParser qrCodeParser) {
//        return new QRCodeParseService(qrCodeParser);
        return null;
    }
}
