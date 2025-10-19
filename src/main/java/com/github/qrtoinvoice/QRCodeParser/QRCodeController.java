package com.github.qrtoinvoice.QRCodeParser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final QRCodeParseService qrCodeParseService;

    public QRCodeController(QRCodeParseService qrCodeParseService) {
        this.qrCodeParseService = qrCodeParseService;
    }

    @PostMapping("/parse")
    public ResponseEntity<?> parseQRCode(@RequestParam("file") MultipartFile file) {
        try {
            String result = qrCodeParseService.parseQRCode(file);
            return ResponseEntity.ok(QRCodeResult.success(result, "解析成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(QRCodeResult.failure("解析失败: " + e.getMessage()));
        }
    }

    @GetMapping("/supported-types")
    public ResponseEntity<?> getSupportedTypes() {
        List<String> supportedTypes = qrCodeParseService.getSupportedFileTypes();
        return ResponseEntity.ok(supportedTypes);
    }

    /**
     * 统一的响应结果类
     */
    public static class QRCodeResult {
        private final boolean success;
        private final String content;
        private final String message;
        private final long timestamp;

        private QRCodeResult(boolean success, String content, String message) {
            this.success = success;
            this.content = content;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public static QRCodeResult success(String content, String message) {
            return new QRCodeResult(true, content, message);
        }

        public static QRCodeResult failure(String message) {
            return new QRCodeResult(false, null, message);
        }

        // getters
        public boolean isSuccess() { return success; }
        public String getContent() { return content; }
        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }
}