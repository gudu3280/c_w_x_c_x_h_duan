package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.*;
import org.example.service.MerchantAdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商家端管理 API
 * 用于商家维护店铺信息、服务价格、可预约库存、管理订单等
 * 后续可对接商家端管理界面
 */
@RestController
@RequestMapping("/api/merchant/admin")
@RequiredArgsConstructor
public class MerchantAdminController {

    private final MerchantAdminService adminService;

    // ======== 店铺信息 ========

    /**
     * 更新商家基本信息
     */
    @PutMapping("/update")
    public Result<?> updateMerchant(@RequestBody Merchant merchant) {
        adminService.updateMerchant(merchant);
        return Result.success();
    }

    // ======== 寄养价格 ========

    /**
     * 保存/更新寄养服务价格设置
     */
    @PutMapping("/boarding-setting")
    public Result<?> saveBoardingSetting(@RequestBody BoardingSetting setting) {
        adminService.saveBoardingSetting(setting);
        return Result.success();
    }

    // ======== 美容服务 ========

    /**
     * 获取美容服务列表
     */
    @GetMapping("/grooming-services")
    public Result<List<GroomingService>> getGroomingServices(@RequestParam Long merchantId) {
        return Result.success(adminService.getGroomingServices(merchantId));
    }

    /**
     * 新增美容服务
     */
    @PostMapping("/grooming-service")
    public Result<?> addGroomingService(@RequestBody GroomingService service) {
        adminService.addGroomingService(service);
        return Result.success();
    }

    /**
     * 更新美容服务
     */
    @PutMapping("/grooming-service")
    public Result<?> updateGroomingService(@RequestBody GroomingService service) {
        adminService.updateGroomingService(service);
        return Result.success();
    }

    /**
     * 删除美容服务
     */
    @DeleteMapping("/grooming-service/{id}")
    public Result<?> deleteGroomingService(@PathVariable Long id) {
        adminService.deleteGroomingService(id);
        return Result.success();
    }

    // ======== 可预约日历 ========

    /**
     * 设置某天的可预约容量
     */
    @PostMapping("/calendar/set")
    public Result<?> setCalendarDay(@RequestBody ServiceCalendar calendar) {
        adminService.setCalendarDay(calendar);
        return Result.success();
    }

    /**
     * 批量设置日历（多天）
     */
    @PostMapping("/calendar/batch")
    public Result<?> batchSetCalendar(@RequestBody List<ServiceCalendar> calendars) {
        adminService.batchSetCalendar(calendars);
        return Result.success();
    }

    // ======== 订单管理 ========

    /**
     * 获取商家的预约订单列表
     */
    @GetMapping("/bookings")
    public Result<List<OrderBooking>> getBookings(@RequestParam Long merchantId,
                                                   @RequestParam(required = false) String status) {
        return Result.success(adminService.getMerchantBookings(merchantId, status));
    }

    /**
     * 商家确认订单 / 更新订单状态
     * status: paid(确认支付) / in_service(开始服务) / completed(完成) / cancelled(取消)
     */
    @PutMapping("/booking/status")
    public Result<?> updateBookingStatus(@RequestParam Long bookingId,
                                          @RequestParam String status) {
        adminService.updateBookingStatus(bookingId, status);
        return Result.success();
    }

    // ======== 统计 ========

    /**
     * 商家统计数据（收入、订单量）
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@RequestParam Long merchantId) {
        return Result.success(adminService.getMerchantStats(merchantId));
    }
}
