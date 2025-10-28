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

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。
