package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.OrderBooking;
import org.example.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预约订单控制器
 */
@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public Result<OrderBooking> create(HttpServletRequest request, @RequestBody OrderBooking booking) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(bookingService.createBooking(userId, booking));
    }

    @GetMapping("/list")
    public Result<List<OrderBooking>> list(HttpServletRequest request,
                                           @RequestParam(required = false) String status) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(bookingService.getUserBookings(userId, status));
    }

    @PostMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        bookingService.cancelBooking(id, userId);
        return Result.success();
    }
}
