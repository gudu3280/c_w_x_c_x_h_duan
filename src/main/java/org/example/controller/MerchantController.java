package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Merchant;
import org.example.entity.ServiceCalendar;
import org.example.service.MerchantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商家控制器
 */
@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    /**
     * 附近商家查询
     */
    @GetMapping("/nearby")
    public Result<List<Merchant>> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "10") double radius) {
        return Result.success(merchantService.findNearby(lng, lat, type, radius));
    }

    /**
     * 商家详情
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        Map<String, Object> detail = merchantService.getDetail(id);
        if (detail == null) {
            return Result.error("商家不存在");
        }
        return Result.success(detail);
    }

    /**
     * 可预约日历
     */
    @GetMapping("/calendar")
    public Result<List<ServiceCalendar>> calendar(
            @RequestParam Long merchantId,
            @RequestParam(required = false) String yearMonth) {
        return Result.success(merchantService.getCalendar(merchantId, yearMonth));
    }
}
