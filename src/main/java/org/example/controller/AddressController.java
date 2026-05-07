package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.UserAddress;
import org.example.service.UserAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址管理控制器
 */
@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final UserAddressService userAddressService;

    @GetMapping("/list")
    public Result<List<UserAddress>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userAddressService.getList(userId));
    }

    @GetMapping("/{id}")
    public Result<UserAddress> detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userAddressService.getById(id, userId));
    }

    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody UserAddress address) {
        Long userId = (Long) request.getAttribute("userId");
        userAddressService.add(userId, address);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<?> update(HttpServletRequest request, @RequestBody UserAddress address) {
        Long userId = (Long) request.getAttribute("userId");
        userAddressService.update(userId, address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userAddressService.delete(id, userId);
        return Result.success();
    }
}
