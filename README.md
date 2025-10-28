# QrToInvoice

QrToInvoice 是一个基于 Java 开发的工具库，旨在通过解析二维码信息快速提取发票数据，支持多种文件格式的二维码解析和不同类型发票的识别转换，提供简洁的 API 和 Spring Boot 集成能力。

## 核心功能

1. **二维码解析**：支持从图片（JPG、PNG、GIF 等）和 PDF 文件中识别二维码内容
2. **发票转换**：将二维码字符串解析为结构化的发票对象，包含发票代码、号码、金额、日期等关键信息
3. **多类型支持**：处理数电票、区块链发票、增值税发票等多种发票类型
4. **灵活集成**：提供核心功能模块和 Spring Boot Starter，方便在各类项目中使用

## 模块结构

### 1. QrToInvoice-core（核心模块）

包含二维码解析和发票转换的核心逻辑：

- **二维码解析**
  - `QRCodeParser`：统一入口，根据文件类型选择对应策略解析二维码
  - 策略模式设计：`ImageQRCodeParseStrategy`（图片解析）、`PdfQRCodeParseStrategy`（PDF解析）
  - 基于 ZXing 实现二维码识别，支持多格式文件输入（InputStream、byte[]、File）

- **发票转换**
  - `InvoiceParser`：通过责任链模式处理不同类型的二维码数据
  - 处理器链：依次处理 HTTP 链接型二维码、数电票、特殊代码发票等
  - 结构化模型：`Invoice` 类封装发票信息，包含版本、类型、代码、金额等字段

### 2. QrToInvoice-spring-boot-starter（Spring Boot 集成模块）

提供自动配置和 REST 接口：

- 自动配置类 `QrToInvoiceAutoConfiguration` 简化 Bean 注入
- 提供 `/api/qrcode/parse` 接口，支持文件上传并返回解析结果

### 3. QrToInvoice-demo（演示模块）

包含示例代码，展示如何使用核心功能。


## 支持的票种及识别规则

### 1. 数电票及衍生类型
- **识别规则**：通过 20 位发票号码判断，结合第 5 位字符和二维码类型（`qrType`）进一步细分
  - 第 5 位为 `8`：电子发票（航空运输客票电子行程单）
  - 第 5 位为 `9`：电子发票（铁路电子客票）
  - `qrType=32` 且发票号码第 4-6 位为 `79`：数电票（通行费）

### 2. 增值税专用发票系列
- **识别规则**：
  - 10 位发票代码，第 8 位为 `1` 或 `5`：增值税专用发票
  - 10 位发票代码，第 8 位为 `2` 或 `7`：货物运输业增值税专用发票
  - 12 位发票代码，第 1-2 位为 `01` 且后两位为 `13`：电子发票（增值税专用发票）
  - 二维码类型 `qrType=20`：自动映射为增值税电子专用发票

### 3. 增值税普通发票系列
- **识别规则**：
  - 10 位发票代码，第 8 位为 `3` 或 `6`：增值税普通发票
  - 12 位发票代码，后两位为 `11`：增值税电子普通发票
  - 12 位发票代码，后两位为 `04` 或 `05`：增值税普通发票（折叠票）
  - 二维码类型 `qrType=10`：增值税电子普通发票

### 4. 特殊行业发票
- **通行费发票**：12 位发票代码后两位为 `12`，或 `qrType=14`
- **卷式发票**：12 位发票代码后两位为 `06` 或 `07`，或 `qrType=11`
- **机动车销售发票**：`qrType=33` 对应电子/纸质机动车销售统一发票
- **二手车销售发票**：`qrType=34` 对应电子/纸质二手车销售统一发票

### 5. 区块链发票
- **识别规则**：
  - 12 位发票代码第 1 位为 `1`，且第 8-9 位为 `09`、最后一位为 `0`
  - 支持解析深圳区块链发票（通过 `bcfp.shenzhen.chinatax.gov.cn` 链接提取数据）

### 6. 其他类型
- 未匹配上述规则的发票统一归类为「其他发票」，包含但不限于特殊行业定制发票


## 技术依赖

- Java 17+
- Maven 3.6+
- ZXing（二维码识别）
- PDFBox（PDF 处理）
- Lombok（简化模型类代码）
- Spring Boot（可选，用于 Starter 模块）

## 使用示例

### 核心模块使用

```java
// 解析图片中的二维码
QRCodeParser qrCodeParser = new QRCodeParser();
String qrContent = qrCodeParser.parseQRCode(new File("invoice-qr.png"));

// 将二维码内容转换为发票对象
Invoice invoice = InvoiceParser.getInvoiceFromQr(qrContent);
System.out.println("发票代码: " + invoice.getInvoiceCode());
System.out.println("发票金额: " + invoice.getAmount());
```

### Spring Boot 集成

1. 引入 Starter 依赖
2. 直接注入 `QRCodeParser` 和 `InvoiceParser` 使用
3. 或调用现成接口：
   ```bash
   curl -X POST -F "file=@invoice-qr.pdf" http://localhost:8080/api/qrcode/parse
   ```

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](https://github.com/GitHub1he/QrToInvoice/blob/master/LICENSE) 文件。
