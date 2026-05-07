package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.config.JwtUtil;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    /**
     * 模拟微信登录（用手机号简化）
     */
    public Map<String, Object> login(String mobile, String code) {
        // 模拟验证码校验（开发期间任何验证码都通过）
        if (code == null || code.length() < 4) {
            throw new BusinessException("验证码无效");
        }

        // 查找或创建用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));

        if (user == null) {
            user = new User();
            user.setMobile(mobile);
            user.setNickname("用户" + mobile.substring(7));
            user.setOpenid("mock_" + mobile);
            userMapper.insert(user);
        }

        // 生成token
        String token = jwtUtil.generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", user);
        return result;
    }

    /**
     * 获取用户信息
     */
    public User getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    /**
     * 更新用户信息
     */
    public void updateUserInfo(Long userId, User updateUser) {
        updateUser.setId(userId);
        updateUser.setOpenid(null); // 不允许修改openid
        updateUser.setMobile(null); // 不允许修改手机号
        userMapper.updateById(updateUser);
    }
}
