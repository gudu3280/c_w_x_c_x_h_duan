package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.service.BookingService;
import org.example.service.MallService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 支付控制器（Mock模拟）
 */
@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PayController {

    private final BookingService bookingService;
    private final MallService mallService;

    /**
     * 模拟微信支付统一下单
     * 返回模拟的支付参数（小程序端直接跳过真实支付流程）
     */
    @PostMapping("/wx/prepay")
    public Result<Map<String, String>> prepay(@RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String orderType = params.get("orderType"); // booking 或 mall

        // 模拟返回支付参数
        Map<String, String> payParams = Map.of(
                "timeStamp", String.valueOf(System.currentTimeMillis() / 1000),
                "nonceStr", "mock_nonce_" + System.currentTimeMillis(),
                "package", "prepay_id=mock_prepay_" + orderNo,
                "signType", "RSA",
                "paySign", "mock_sign_" + orderNo
        );
        return Result.success(payParams);
    }

    /**
     * 模拟支付回调（前端调用，模拟支付成功）
     */
    @PostMapping("/wx/callback")
    public Result<?> payCallback(@RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String orderType = params.get("orderType");

        if ("booking".equals(orderType)) {
            bookingService.paySuccess(orderNo);
        } else if ("mall".equals(orderType)) {
            mallService.mallPaySuccess(orderNo);
        }
        return Result.success();
    }
}
