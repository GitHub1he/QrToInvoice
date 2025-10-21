package com.github.qrtoinvoicecore.QRCodeParser;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class QRCodeDecoder {

    public String decodeFromImage(BufferedImage image) throws NotFoundException {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        EnumMap<DecodeHintType, Object> decodeHints = new EnumMap<>(DecodeHintType.class);
        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);
        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//        decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);
        decodeHints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeMultiReader multiReader = new QRCodeMultiReader();
        Result[] results = multiReader.decodeMultiple(bitmap, decodeHints);

        List<String> qrCodes = new ArrayList<>();
        for (Result result : results) {
            qrCodes.add(result.getText());
        }
        return qrCodes.get(0);
    }

    public String decodeFromImageSafely(BufferedImage image) {
        try {
            return decodeFromImage(image);
        } catch (Exception e) {
            return null;
        }
    }
}