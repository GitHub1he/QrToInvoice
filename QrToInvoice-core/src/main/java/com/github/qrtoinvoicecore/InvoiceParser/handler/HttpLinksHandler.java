package com.github.qrtoinvoicecore.InvoiceParser.handler;

import com.alibaba.fastjson2.JSON;
import com.github.qrtoinvoicecore.InvoiceParser.InvoiceParseHandle;
import com.github.qrtoinvoicecore.model.BlockChainShenzhenResponse;
import com.github.qrtoinvoicecore.model.Invoice;
import com.github.qrtoinvoicecore.model.InvoiceTypeEnum;
import com.github.qrtoinvoicecore.utils.HttpUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理二维码为http链接，目前支持发票类型：区块链
 */
public class HttpLinksHandler extends InvoiceParseHandle {

    // 配置映射：host -> 对应的处理器
    private static final Map<String, UrlProcessor> CONFIG_MAP = new HashMap<>();

    static {
        // 注册不同host对应的处理器
        CONFIG_MAP.put("bcfp.shenzhen.chinatax.gov.cn", new BlockchainShenZhenInvoiceProcessor());
        // 可以轻松添加新的处理器
        // CONFIG_MAP.put("other.host.com", new OtherInvoiceProcessor());
    }

    @Override
    public Invoice handle(String value) {
        if (!value.startsWith("http")) return null;

        try {
            URL url = new URL(value);
            String host = url.getHost();

            // 获取对应的处理器
            UrlProcessor processor = CONFIG_MAP.get(host);
            if (processor == null) {
                return null;
            }

            // 直接调用处理器的解析方法
            return processor.parse(url);

        } catch (Exception e) {
            System.err.println("处理URL失败: " + e.getMessage());
            return null;
        }
    }

    // ========== 处理器接口和实现 ==========

    /**
     * URL处理器接口
     */
    interface UrlProcessor {
        Invoice parse(URL url) throws Exception;
    }

    /**
     * 区块链发票处理器
     */
    static class BlockchainShenZhenInvoiceProcessor implements UrlProcessor {
        private static final String API_URL = "https://bcfp.shenzhen.chinatax.gov.cn/dzswj/bers_ep_web/query_bill_detail";
        private static final String[] REQUIRED_PARAMS = {"hash", "bill_num", "total_amount"};

        @Override
        public Invoice parse(URL url) {
            try {
                Map<String, String> params = HttpUtils.parseQueryParams(url.getQuery());

                // 验证必要参数
                if (!hasRequiredParams(params, REQUIRED_PARAMS)) {
                    System.out.println("缺少必要参数: " + params);
                    return null;
                }

                // 调用API
                Map<String, String> requestMap = new HashMap<>();
                requestMap.put("tx_hash", params.get("hash"));
                requestMap.put("total_amount", params.get("total_amount"));
                requestMap.put("bill_num", params.get("bill_num"));

                String response = HttpUtils.postRequest(API_URL, requestMap);

                // 解析响应
                return parseBlockchainResponse(response);
            } catch (Exception e) {
                System.err.println("处理区块链发票失败: " + e.getMessage());
                return null;
            }

        }

        private Invoice parseBlockchainResponse(String response) {
            BlockChainShenzhenResponse blockChainShenzhenResponse = JSON.parseObject(response, BlockChainShenzhenResponse.class);
            BlockChainShenzhenResponse.BillRecod billRecord = blockChainShenzhenResponse.getBill_record();

            if (billRecord == null) return null;

            Invoice invoice = new Invoice();
            invoice.setInvoiceType(InvoiceTypeEnum.BLOCKCHAIN);
            invoice.setInvoiceCode(billRecord.getBill_code());
            invoice.setInvoiceNumber(billRecord.getBill_num());
            invoice.setAmount(new BigDecimal(billRecord.getAmount()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            invoice.setIssueDate(new Date(billRecord.getTime() * 1000));
            invoice.setCheckCode(billRecord.getTx_hash().substring(billRecord.getTx_hash().length() - 5, billRecord.getTx_hash().length()));

            return invoice;
        }
    }

    /**
     * 其他类型发票处理器的示例
     */
    static class OtherInvoiceProcessor implements UrlProcessor {
        @Override
        public Invoice parse(URL url) throws Exception {
            // 实现其他类型发票的解析逻辑
            // 不同的参数验证、API调用和响应解析逻辑
            return null;
        }
    }

    // ========== 通用工具方法 ==========

    private static boolean hasRequiredParams(Map<String, String> params, String[] required) {
        for (String param : required) {
            if (params.get(param) == null) return false;
        }
        return true;
    }
}