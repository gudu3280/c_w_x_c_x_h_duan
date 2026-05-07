package org.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.common.Result;
import org.example.entity.*;
import org.example.service.MallService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城控制器
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MallController {

    private final MallService mallService;

    // ============ 商品 ============

    @GetMapping("/products")
    public Result<Page<Product>> products(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(mallService.getProducts(category, page, size));
    }

    @GetMapping("/product/{id}")
    public Result<Product> productDetail(@PathVariable Long id) {
        return Result.success(mallService.getProductDetail(id));
    }

    // ============ 购物车 ============

    @GetMapping("/cart/list")
    public Result<List<Cart>> cartList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(mallService.getCartList(userId));
    }

    @PostMapping("/cart/add")
    public Result<?> addToCart(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long userId = (Long) request.getAttribute("userId");
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = Integer.valueOf(params.getOrDefault("quantity", 1).toString());
        mallService.addToCart(userId, productId, quantity);
        return Result.success();
    }

    @PutMapping("/cart/update")
    public Result<?> updateCart(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long userId = (Long) request.getAttribute("userId");
        Long cartId = Long.valueOf(params.get("cartId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        mallService.updateCartQuantity(userId, cartId, quantity);
        return Result.success();
    }

    @DeleteMapping("/cart/{id}")
    public Result<?> deleteCart(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        mallService.deleteCartItem(userId, id);
        return Result.success();
    }

    // ============ 商城订单 ============

    @PostMapping("/order/create")
    public Result<OrderMall> createOrder(HttpServletRequest request, @RequestBody Map<String, String> params) {
        Long userId = (Long) request.getAttribute("userId");
        String addressJson = params.get("addressJson");
        return Result.success(mallService.createOrder(userId, addressJson));
    }

    @GetMapping("/order/mall/list")
    public Result<List<OrderMall>> orderList(HttpServletRequest request,
                                             @RequestParam(required = false) String status) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(mallService.getUserOrders(userId, status));
    }

    @GetMapping("/order/mall/{id}")
    public Result<Map<String, Object>> orderDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        OrderMall order = mallService.getOrderDetail(id, userId);
        List<OrderItem> items = mallService.getOrderItems(id);
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return Result.success(result);
    }
}
