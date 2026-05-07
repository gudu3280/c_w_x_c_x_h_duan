package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 登录（模拟微信登录）
     */
    @PostMapping("/login/wechat")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String mobile = params.get("mobile");
        String code = params.get("code");
        if (mobile == null || mobile.isEmpty()) {
            return Result.error(400, "手机号不能为空");
        }
        Map<String, Object> data = userService.login(mobile, code);
        return Result.success(data);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getUserInfo(userId));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/user/update")
    public Result<?> updateUserInfo(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateUserInfo(userId, user);
        return Result.success();
    }
}
