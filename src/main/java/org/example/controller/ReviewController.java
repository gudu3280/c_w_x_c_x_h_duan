package org.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Review;
import org.example.service.ReviewService;
import org.springframework.web.bind.annotation.*;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody Review review) {
        Long userId = (Long) request.getAttribute("userId");
        reviewService.addReview(userId, review);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Page<Review>> list(
            @RequestParam String targetType,
            @RequestParam Long targetId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(reviewService.getReviews(targetType, targetId, page, size));
    }

    @GetMapping("/merchant/{merchantId}")
    public Result<Page<Review>> merchantReviews(
            @PathVariable Long merchantId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(reviewService.getMerchantReviews(merchantId, page, size));
    }
}
