package org.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.Merchant;
import org.example.entity.Product;
import org.example.service.MallService;
import org.example.service.MerchantService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一搜索控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final MallService mallService;
    private final MerchantService merchantService;

    /**
     * 搜索服务和商品
     */
    @GetMapping("/search")
    public Result<Map<String, Object>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.error("请输入搜索关键词");
        }

        // 并发搜索（简化：顺序执行）
        List<Merchant> merchants = merchantService.searchMerchants(keyword.trim());
        Page<Product> products = mallService.searchProducts(keyword.trim(), page, size);

        Map<String, Object> result = new HashMap<>();
        result.put("merchants", merchants);
        result.put("products", products.getRecords());
        result.put("totalProducts", products.getTotal());
        return Result.success(result);
    }
}
