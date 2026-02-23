package com.rois.happy_shopping.service;

import com.rois.happy_shopping.dto.OrderRequestDTO;
import com.rois.happy_shopping.entity.Coupon;
import com.rois.happy_shopping.entity.Order;
import com.rois.happy_shopping.entity.Product;
import com.rois.happy_shopping.entity.User;
import com.rois.happy_shopping.mapper.OrderMapper;
import com.rois.happy_shopping.util.RedisLockUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/service/OrderService.class */
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private RedisLockUtil redisLockUtil;


    @Transactional
    public Map<String, Object> createOrder(String userId, OrderRequestDTO orderRequest) {
        Map<String, Object> result = new HashMap<>();
        String lockKey = "order:user:" + userId;
        String lockValue = this.redisLockUtil.generateLockValue();
        if (!this.redisLockUtil.tryLock(lockKey, lockValue, 3L)) {
            result.put("success", Boolean.valueOf(false));
            result.put("message", "System is busy, please try again later");
            return result;
        }
        try {
            Integer quantityNum;
            User user = this.userService.findById(userId);
            if (user == null) {
                result.put("success", Boolean.valueOf(false));
                result.put("message", "User not found");
                return result;
            }
            Product product = this.productService.getProductById(orderRequest.getProductId());
            if (product == null) {
                result.put("success", Boolean.valueOf(false));
                result.put("message", "Product not found");
                return result;
            }
            BigDecimal discountAmount = BigDecimal.ZERO;
            String couponId = null;
            if (orderRequest.getCouponId() != null) {
                if (!this.couponService.validateCoupon(orderRequest.getCouponId(), userId)) {
                    result.put("success", Boolean.valueOf(false));
                    result.put("message", "Coupon is not available");
                    return result;
                }
                Coupon coupon = this.couponService.getCouponById(orderRequest.getCouponId());
                discountAmount = coupon.getDiscountAmount();
                couponId = coupon.getId();
            }
            BigDecimal unitPrice = product.getPrice();
            BigDecimal quantity = new BigDecimal(orderRequest.getQuantity());
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                result.put("success", Boolean.valueOf(false));
                result.put("message", "Quantity must be greater than zero");
                return result;
            }
            BigDecimal compare = quantity.subtract(new BigDecimal("100"));
            if (compare.compareTo(BigDecimal.ZERO) > 0) {
                quantityNum = Integer.valueOf(1);
                quantity = new BigDecimal(quantityNum.intValue());
            } else {
                quantityNum = Integer.valueOf(Integer.parseInt(orderRequest.getQuantity()));
            }
            System.out.println(quantityNum);
            BigDecimal originalPrice = unitPrice.multiply(quantity).setScale(2, RoundingMode.HALF_UP);
            BigDecimal finalPrice = originalPrice.subtract(discountAmount);
            if (finalPrice.compareTo(BigDecimal.ZERO) < 0)
                finalPrice = BigDecimal.ZERO;
            finalPrice = finalPrice.setScale(2, RoundingMode.HALF_UP);
            if (user.getBalance().compareTo(finalPrice) < 0) {
                result.put("success", Boolean.valueOf(false));
                result.put("message", "Insufficient balance");
                return result;
            }
            Order order = new Order(userId, orderRequest.getProductId(), quantityNum, originalPrice);
            order.setDiscountAmount(discountAmount);
            order.setFinalPrice(finalPrice);
            order.setCouponId(couponId);
            this.orderMapper.insert(order);
            BigDecimal newBalance = user.getBalance().subtract(finalPrice);
            if (!this.userService.updateBalance(userId, newBalance))
                throw new RuntimeException("Failed to deduct balance");
            if (couponId != null &&
                    !this.couponService.useCoupon(couponId))
                throw new RuntimeException("Failed to use coupon");
            this.orderMapper.updateStatus(order.getId(), "COMPLETED");
            result.put("success", Boolean.valueOf(true));
            result.put("message", "Order created successfully");
            result.put("order", order);
            result.put("remainingBalance", newBalance);
            return result;
        } catch (Exception e) {
            result.put("success", Boolean.valueOf(false));
            result.put("message", "Order creation failed: " + e.getMessage());
            return result;
        } finally {
            this.redisLockUtil.unlock(lockKey, lockValue);
        }
    }

    public List<Order> getUserOrders(String userId) {
        return this.orderMapper.findByUserId(userId);
    }

    public Order getOrderById(String id) {
        return this.orderMapper.findById(id);
    }

    @Transactional
    public Map<String, Object> refundOrder(String orderId, String userId) {
        Map<String, Object> result = new HashMap<>();
        String lockKey = "refund:order:" + orderId;
        String lockValue = this.redisLockUtil.generateLockValue();
        try {
            if (!this.redisLockUtil.tryLock(lockKey, lockValue, 5L)) {
                result.put("success", false);
                result.put(ConstraintHelper.MESSAGE, "System is busy, please try again later");
                return result;
            }
            try {
                Order order = this.orderMapper.findById(orderId);
                if (order == null) {
                    result.put("success", false);
                    result.put(ConstraintHelper.MESSAGE, "Order not found");
                    this.redisLockUtil.unlock(lockKey, lockValue);
                    return result;
                }
                if (!order.getUserId().equals(userId)) {
                    result.put("success", false);
                    result.put(ConstraintHelper.MESSAGE, "No permission to operate this order");
                    this.redisLockUtil.unlock(lockKey, lockValue);
                    return result;
                }
                if (!"COMPLETED".equals(order.getStatus())) {
                    result.put("success", false);
                    result.put(ConstraintHelper.MESSAGE, "This order status does not support refund");
                    this.redisLockUtil.unlock(lockKey, lockValue);
                    return result;
                }
                User user = this.userService.findById(userId);
                if (user == null) {
                    result.put("success", false);
                    result.put(ConstraintHelper.MESSAGE, "User not found");
                    this.redisLockUtil.unlock(lockKey, lockValue);
                    return result;
                }
                BigDecimal newBalance = user.getBalance().add(order.getFinalPrice());
                if (!this.userService.updateBalance(userId, newBalance)) {
                    throw new RuntimeException("Failed to restore user balance");
                }
                if (order.getCouponId() != null && !this.couponService.restoreCoupon(order.getCouponId())) {
                    throw new RuntimeException("Failed to restore coupon");
                }
                if (!updateOrderStatus(orderId, "REFUNDED")) {
                    throw new RuntimeException("Failed to update order status");
                }
                result.put("success", true);
                result.put(ConstraintHelper.MESSAGE, "Refund successful");
                result.put("refundAmount", order.getFinalPrice());
                result.put("newBalance", newBalance);
                this.redisLockUtil.unlock(lockKey, lockValue);
                return result;
            } catch (Exception e) {
                result.put("success", false);
                result.put(ConstraintHelper.MESSAGE, "Refund failed: " + e.getMessage());
                this.redisLockUtil.unlock(lockKey, lockValue);
                return result;
            }
        } catch (Throwable th) {
            this.redisLockUtil.unlock(lockKey, lockValue);
            throw th;
        }
    }

    private boolean updateOrderStatus(String orderId, String status) {
        return this.orderMapper.updateStatus(orderId, status) > 0;
    }
}
