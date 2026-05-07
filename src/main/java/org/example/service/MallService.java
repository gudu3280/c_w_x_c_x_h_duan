package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.example.common.BusinessException;
import org.example.entity.*;
import org.example.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 商城服务（商品+购物车+订单）
 */
@Service
@RequiredArgsConstructor
public class MallService {

    private final ProductMapper productMapper;
    private final CartMapper cartMapper;
    private final OrderMallMapper orderMallMapper;
    private final OrderItemMapper orderItemMapper;

    // ============ 商品 ============

    public Page<Product> searchProducts(String keyword, int page, int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)
                .and(w -> w.like(Product::getName, keyword)
                        .or()
                        .like(Product::getDescription, keyword))
                .orderByDesc(Product::getSalesVolume);
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Page<Product> getProducts(String category, int page, int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getSalesVolume);
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Product::getCategory, category);
        }
        return productMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Product getProductDetail(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }
        return product;
    }

    // ============ 购物车 ============

    public List<Cart> getCartList(Long userId) {
        List<Cart> carts = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .orderByDesc(Cart::getCreateTime));
        // 填充商品信息
        for (Cart cart : carts) {
            cart.setProduct(productMapper.selectById(cart.getProductId()));
        }
        return carts;
    }

    public void addToCart(Long userId, Long productId, Integer quantity) {
        // 检查商品是否存在
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() != 1) {
            throw new BusinessException("商品不存在或已下架");
        }

        // 检查是否已在购物车
        Cart existing = cartMapper.selectOne(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getProductId, productId));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(quantity);
            cart.setSelected(1);
            cartMapper.insert(cart);
        }
    }

    public void updateCartQuantity(Long userId, Long cartId, Integer quantity) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        if (quantity <= 0) {
            cartMapper.deleteById(cartId);
        } else {
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }
    }

    public void deleteCartItem(Long userId, Long cartId) {
        Cart cart = cartMapper.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartMapper.deleteById(cartId);
    }

    // ============ 商城订单 ============

    @Transactional
    public OrderMall createOrder(Long userId, String addressJson) {
        // 获取选中的购物车商品
        List<Cart> selectedCarts = cartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .eq(Cart::getUserId, userId)
                        .eq(Cart::getSelected, 1));
        if (selectedCarts.isEmpty()) {
            throw new BusinessException("请选择要购买的商品");
        }

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : selectedCarts) {
            Product product = productMapper.selectById(cart.getProductId());
            if (product == null || product.getStatus() != 1) {
                throw new BusinessException("商品 " + cart.getProductId() + " 已下架");
            }
            if (product.getStock() < cart.getQuantity()) {
                throw new BusinessException("商品 " + product.getName() + " 库存不足");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        // 创建订单
        OrderMall order = new OrderMall();
        order.setOrderNo("ML" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending_pay");
        order.setAddressJson(addressJson);
        orderMallMapper.insert(order);

        // 创建订单商品项 + 扣减库存
        for (Cart cart : selectedCarts) {
            Product product = productMapper.selectById(cart.getProductId());
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductImage(product.getMainImage());
            item.setPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            orderItemMapper.insert(item);

            // 扣减库存 + 增加销量
            product.setStock(product.getStock() - cart.getQuantity());
            product.setSalesVolume(product.getSalesVolume() + cart.getQuantity());
            productMapper.updateById(product);
        }

        // 清空已购买的购物车项
        for (Cart cart : selectedCarts) {
            cartMapper.deleteById(cart.getId());
        }

        return order;
    }

    /**
     * 商城订单支付成功
     */
    public void mallPaySuccess(String orderNo) {
        OrderMall order = orderMallMapper.selectOne(
                new LambdaQueryWrapper<OrderMall>().eq(OrderMall::getOrderNo, orderNo));
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!"pending_pay".equals(order.getStatus())) {
            return;
        }
        order.setStatus("paid");
        order.setPayTime(LocalDateTime.now());
        orderMallMapper.updateById(order);
    }

    /**
     * 获取用户商城订单列表
     */
    public List<OrderMall> getUserOrders(Long userId, String status) {
        LambdaQueryWrapper<OrderMall> wrapper = new LambdaQueryWrapper<OrderMall>()
                .eq(OrderMall::getUserId, userId)
                .orderByDesc(OrderMall::getCreateTime);
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderMall::getStatus, status);
        }
        return orderMallMapper.selectList(wrapper);
    }

    /**
     * 获取订单详情（含商品列表）
     */
    public OrderMall getOrderDetail(Long orderId, Long userId) {
        OrderMall order = orderMallMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderId));
    }
}
