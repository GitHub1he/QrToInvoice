package com.github.qrtoinvoicespringbootstarter.controller;

import com.github.qrtoinvoicecore.QRCodeParser.QRCodeParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    private final QRCodeParser qrCodeParser;

    public QRCodeController(QRCodeParser qrCodeParser) {
        this.qrCodeParser = qrCodeParser;
    }

    @PostMapping("/parse")
    public ResponseEntity<?> parseQRCode(@RequestParam("file") MultipartFile file) {
        try {
            String result = qrCodeParser.parseQRCode(file.getInputStream(), file.getOriginalFilename());
            return ResponseEntity.ok(QRCodeResult.success(result, "解析成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(QRCodeResult.failure("解析失败: " + e.getMessage()));
        }
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