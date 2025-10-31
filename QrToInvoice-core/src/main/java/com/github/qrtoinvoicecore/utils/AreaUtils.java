package com.github.qrtoinvoicecore.utils;

import com.github.qrtoinvoicecore.utils.GBT.GBT2260_2013;

public class AreaUtils {
    /**
     * 验证地区代码是否有效
     */
    public static boolean isValidRegionCode(String regionCodeStr) {
        try {
            int regionCode = Integer.parseInt(regionCodeStr);
            return GBT2260_2013.getInstance().containsCode(regionCode);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
