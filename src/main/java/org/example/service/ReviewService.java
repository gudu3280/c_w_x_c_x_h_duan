package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.entity.Review;
import org.example.entity.User;
import org.example.mapper.ReviewMapper;
import org.example.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 评价服务
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    public void addReview(Long userId, Review review) {
        review.setUserId(userId);
        reviewMapper.insert(review);
    }

    public Page<Review> getReviews(String targetType, Long targetId, int page, int size) {
        Page<Review> pageResult = reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getTargetType, targetType)
                        .eq(Review::getTargetId, targetId)
                        .orderByDesc(Review::getCreateTime));
        // 填充用户信息
        for (Review review : pageResult.getRecords()) {
            User user = userMapper.selectById(review.getUserId());
            if (user != null) {
                review.setUserNickname(user.getNickname());
                review.setUserAvatar(user.getAvatarUrl());
            }
        }
        return pageResult;
    }

    public Page<Review> getMerchantReviews(Long merchantId, int page, int size) {
        Page<Review> pageResult = reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getMerchantId, merchantId)
                        .orderByDesc(Review::getCreateTime));
        for (Review review : pageResult.getRecords()) {
            User user = userMapper.selectById(review.getUserId());
            if (user != null) {
                review.setUserNickname(user.getNickname());
                review.setUserAvatar(user.getAvatarUrl());
            }
        }
        return pageResult;
    }
}
