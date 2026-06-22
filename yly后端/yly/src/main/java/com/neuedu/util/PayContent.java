package com.neuedu.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayContent {
    private String out_trade_no;
    private String total_amount;
    private String subject;
    private String body;
    private String product_code;
}
