package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.entity.*;
import org.example.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家端管理服务
 * 提供商家对店铺、服务、日历、订单的管理能力
 */
@Service
@RequiredArgsConstructor
public class MerchantAdminService {

    private final MerchantMapper merchantMapper;
    private final BoardingSettingMapper boardingSettingMapper;
    private final GroomingServiceMapper groomingServiceMapper;
    private final ServiceCalendarMapper serviceCalendarMapper;
    private final OrderBookingMapper orderBookingMapper;

    // ======== 店铺信息 ========

    public void updateMerchant(Merchant merchant) {
        Merchant exist = merchantMapper.selectById(merchant.getId());
        if (exist == null) throw new BusinessException("商家不存在");
        merchantMapper.updateById(merchant);
    }

    // ======== 寄养价格 ========

    public void saveBoardingSetting(BoardingSetting setting) {
        BoardingSetting exist = boardingSettingMapper.selectOne(
                new LambdaQueryWrapper<BoardingSetting>()
                        .eq(BoardingSetting::getMerchantId, setting.getMerchantId()));
        if (exist != null) {
            setting.setId(exist.getId());
            boardingSettingMapper.updateById(setting);
        } else {
            boardingSettingMapper.insert(setting);
        }
    }

    // ======== 美容服务 ========

    public List<GroomingService> getGroomingServices(Long merchantId) {
        return groomingServiceMapper.selectList(
                new LambdaQueryWrapper<GroomingService>()
                        .eq(GroomingService::getMerchantId, merchantId));
    }

    public void addGroomingService(GroomingService service) {
        groomingServiceMapper.insert(service);
    }

    public void updateGroomingService(GroomingService service) {
        groomingServiceMapper.updateById(service);
    }

    public void deleteGroomingService(Long id) {
        groomingServiceMapper.deleteById(id);
    }

    // ======== 可预约日历 ========

    public void setCalendarDay(ServiceCalendar calendar) {
        ServiceCalendar exist = serviceCalendarMapper.selectOne(
                new LambdaQueryWrapper<ServiceCalendar>()
                        .eq(ServiceCalendar::getMerchantId, calendar.getMerchantId())
                        .eq(ServiceCalendar::getServiceDate, calendar.getServiceDate()));
        if (exist != null) {
            calendar.setId(exist.getId());
            calendar.setUsedCapacity(exist.getUsedCapacity()); // 保留已用量
            serviceCalendarMapper.updateById(calendar);
        } else {
            serviceCalendarMapper.insert(calendar);
        }
    }

    @Transactional
    public void batchSetCalendar(List<ServiceCalendar> calendars) {
        for (ServiceCalendar c : calendars) {
            setCalendarDay(c);
        }
    }

    // ======== 订单管理 ========

    public List<OrderBooking> getMerchantBookings(Long merchantId, String status) {
        LambdaQueryWrapper<OrderBooking> wrapper = new LambdaQueryWrapper<OrderBooking>()
                .eq(OrderBooking::getMerchantId, merchantId)
                .orderByDesc(OrderBooking::getCreateTime);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderBooking::getStatus, status);
        }
        return orderBookingMapper.selectList(wrapper);
    }

    public void updateBookingStatus(Long bookingId, String status) {
        OrderBooking booking = orderBookingMapper.selectById(bookingId);
        if (booking == null) throw new BusinessException("订单不存在");
        booking.setStatus(status);
        orderBookingMapper.updateById(booking);
    }

    // ======== 统计 ========

    public Map<String, Object> getMerchantStats(Long merchantId) {
        // 全部订单
        List<OrderBooking> allBookings = orderBookingMapper.selectList(
                new LambdaQueryWrapper<OrderBooking>()
                        .eq(OrderBooking::getMerchantId, merchantId));

        // 已完成
        long completedCount = allBookings.stream()
                .filter(b -> "completed".equals(b.getStatus())).count();

        // 总收入（已完成 + 已支付）
        double totalRevenue = allBookings.stream()
                .filter(b -> "completed".equals(b.getStatus()) || "paid".equals(b.getStatus()) || "in_service".equals(b.getStatus()))
                .mapToDouble(b -> b.getTotalAmount() != null ? b.getTotalAmount().doubleValue() : 0)
                .sum();

        // 各状态数量
        Map<String, Long> statusCount = new HashMap<>();
        for (OrderBooking b : allBookings) {
            statusCount.merge(b.getStatus(), 1L, Long::sum);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", allBookings.size());
        stats.put("completedOrders", completedCount);
        stats.put("totalRevenue", totalRevenue);
        stats.put("statusCount", statusCount);
        return stats;
    }
}
