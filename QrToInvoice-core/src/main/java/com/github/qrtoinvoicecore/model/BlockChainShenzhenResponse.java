package com.github.qrtoinvoicecore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockChainShenzhenResponse {
    private String retcode;
    private String retmsg;
    private BillRecod bill_record;

    public static class BillRecod {
        private String bill_code;
        private String bill_num;
        private String amount;
        private String tx_hash;
        private String total_amount;
        private Long time;
        private String tax_amount;
        private String seller_name;
        private String seller_taxpayer_id;
        private String buyer_name;
        private String status;
        private String invalid_mark;

        public String getBill_code() {
            return bill_code;
        }

        public void setBill_code(String bill_code) {
            this.bill_code = bill_code;
        }

        public String getBill_num() {
            return bill_num;
        }

        public void setBill_num(String bill_num) {
            this.bill_num = bill_num;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTx_hash() {
            return tx_hash;
        }

        public void setTx_hash(String tx_hash) {
            this.tx_hash = tx_hash;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getTax_amount() {
            return tax_amount;
        }

        public void setTax_amount(String tax_amount) {
            this.tax_amount = tax_amount;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getSeller_taxpayer_id() {
            return seller_taxpayer_id;
        }

        public void setSeller_taxpayer_id(String seller_taxpayer_id) {
            this.seller_taxpayer_id = seller_taxpayer_id;
        }

        public String getBuyer_name() {
            return buyer_name;
        }

        public void setBuyer_name(String buyer_name) {
            this.buyer_name = buyer_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getInvalid_mark() {
            return invalid_mark;
        }

        public void setInvalid_mark(String invalid_mark) {
            this.invalid_mark = invalid_mark;
        }
    }
}
