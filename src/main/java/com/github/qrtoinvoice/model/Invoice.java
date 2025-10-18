package com.github.qrtoinvoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String version;          // 版本号
    private InvoiceTypeEnum invoiceType; // 发票类型
    private String invoiceCode;      // 发票代码
    private String invoiceNumber;    // 发票号码
    private BigDecimal amount;       // 金额
    private Date issueDate;     // 开票日期
    private String checkCode;        // 校验码
    private String crc;              // CRC
}
