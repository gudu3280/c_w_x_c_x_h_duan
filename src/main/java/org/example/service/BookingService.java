package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.entity.OrderBooking;
import org.example.entity.ServiceCalendar;
import org.example.mapper.OrderBookingMapper;
import org.example.mapper.ServiceCalendarMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 预约订单服务
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final OrderBookingMapper orderBookingMapper;
    private final ServiceCalendarMapper serviceCalendarMapper;

    /**
     * 创建预约订单
     */
    @Transactional
    public OrderBooking createBooking(Long userId, OrderBooking booking) {
        booking.setUserId(userId);
        booking.setOrderNo(generateOrderNo());
        booking.setStatus("pending_pay");

        // 寄养预约需要检查容量
        if ("boarding".equals(booking.getOrderType())) {
            checkBoardingCapacity(booking.getMerchantId(), booking.getStartDate(), booking.getEndDate());
        }

        orderBookingMapper.insert(booking);
        return booking;
    }

    /**
     * 检查寄养容量
     */
    private void checkBoardingCapacity(Long merchantId, LocalDate startDate, LocalDate endDate) {
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            ServiceCalendar calendar = serviceCalendarMapper.selectOne(
                    new LambdaQueryWrapper<ServiceCalendar>()
                            .eq(ServiceCalendar::getMerchantId, merchantId)
                            .eq(ServiceCalendar::getServiceDate, current));

            if (calendar != null) {
                if (calendar.getIsClosed() == 1) {
                    throw new BusinessException("日期 " + current + " 商家休息，无法预约");
                }
                if (calendar.getUsedCapacity() >= calendar.getTotalCapacity()) {
                    throw new BusinessException("日期 " + current + " 已约满，请选择其他日期");
                }
            }
            current = current.plusDays(1);
        }
    }

    /**
     * 支付成功后更新订单状态和日历容量
     */
    @Transactional
    public void paySuccess(String orderNo) {
        OrderBooking booking = orderBookingMapper.selectOne(
                new LambdaQueryWrapper<OrderBooking>().eq(OrderBooking::getOrderNo, orderNo));
        if (booking == null) {
            throw new BusinessException("订单不存在");
        }
        if (!"pending_pay".equals(booking.getStatus())) {
            return; // 已处理
        }

        // 更新订单状态
        booking.setStatus("paid");
        booking.setPayTime(LocalDateTime.now());
        orderBookingMapper.updateById(booking);

        // 更新日历容量（寄养）
        if ("boarding".equals(booking.getOrderType())) {
            LocalDate current = booking.getStartDate();
            LocalDate end = booking.getEndDate() != null ? booking.getEndDate() : booking.getStartDate();
            while (!current.isAfter(end)) {
                serviceCalendarMapper.update(null,
                        new LambdaUpdateWrapper<ServiceCalendar>()
                                .eq(ServiceCalendar::getMerchantId, booking.getMerchantId())
                                .eq(ServiceCalendar::getServiceDate, current)
                                .setSql("used_capacity = used_capacity + 1"));
                current = current.plusDays(1);
            }
        }
    }

    /**
     * 取消订单
     */
    public void cancelBooking(Long bookingId, Long userId) {
        OrderBooking booking = orderBookingMapper.selectById(bookingId);
        if (booking == null || !booking.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (!"pending_pay".equals(booking.getStatus()) && !"paid".equals(booking.getStatus())) {
            throw new BusinessException("当前状态无法取消");
        }
        booking.setStatus("cancelled");
        orderBookingMapper.updateById(booking);
    }

    /**
     * 获取用户预约订单列表
     */
    public List<OrderBooking> getUserBookings(Long userId, String status) {
        LambdaQueryWrapper<OrderBooking> wrapper = new LambdaQueryWrapper<OrderBooking>()
                .eq(OrderBooking::getUserId, userId)
                .orderByDesc(OrderBooking::getCreateTime);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderBooking::getStatus, status);
        }
        return orderBookingMapper.selectList(wrapper);
    }

    private String generateOrderNo() {
        return "BK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
}
