package com.rois.happy_shopping.controller;

import com.rois.happy_shopping.common.Result;
import com.rois.happy_shopping.entity.Coupon;
import com.rois.happy_shopping.entity.Order;
import com.rois.happy_shopping.entity.Product;
import com.rois.happy_shopping.entity.User;
import com.rois.happy_shopping.service.CouponService;
import com.rois.happy_shopping.service.OrderService;
import com.rois.happy_shopping.service.ProductService;
import com.rois.happy_shopping.service.UserService;
import com.rois.happy_shopping.util.JwtUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api/flag"})
@RestController
@CrossOrigin(origins = {"*"})
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/controller/FlagController.class */
public class FlagController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping({"/get"})
    public Result<?> getFlag(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "unauthorized");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        User user = this.userService.findById(userId);
        if (user == null) {
            return Result.error("account doesn't exist");
        }
        List<Order> userOrders = this.orderService.getUserOrders(userId);
        Set<String> purchasedProductIds = (Set) userOrders.stream().filter(order -> {
            return "COMPLETED".equals(order.getStatus());
        }).map((v0) -> {
            return v0.getProductId();
        }).collect(Collectors.toSet());
        List<Product> allProducts = this.productService.getAllProducts();
        String xiaotudouId = null;
        String diguaId = null;
        String yuId = null;
        String datudouId = null;
        for (Product product : allProducts) {
            if ("Little Potato".equals(product.getName())) {
                xiaotudouId = product.getId();
            } else if ("Sweet Potato".equals(product.getName())) {
                diguaId = product.getId();
            } else if ("Fish Fish".equals(product.getName())) {
                yuId = product.getId();
            } else if ("Large Potato".equals(product.getName())) {
                datudouId = product.getId();
            }
        }
        if (xiaotudouId == null || diguaId == null || yuId == null || datudouId == null) {
            return Result.error("internal error");
        }
        if (!purchasedProductIds.contains(xiaotudouId)) {
            return Result.error("little potato needed~");
        }
        if (!purchasedProductIds.contains(diguaId)) {
            return Result.error("sweet potato needed~");
        }
        if (!purchasedProductIds.contains(yuId)) {
            return Result.error("fish needed~");
        }
        if (!purchasedProductIds.contains(datudouId)) {
            return Result.error("large potato needed~");
        }
        if (user.getBalance().compareTo(new BigDecimal("10.00")) != 0) {
            return Result.error("your balance is lower than 10~");
        }
        List<Coupon> userCoupons = this.couponService.getUserCoupons(userId);
        boolean hasUnusedCoupon = userCoupons.stream().anyMatch(coupon -> {
            return !coupon.getIsUsed().booleanValue();
        });
        if (!hasUnusedCoupon) {
            return Result.error("you cannot use your coupon~");
        }
        return Result.success("Ultimate Freeloader! ！", "RCTF{test_flag}");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
