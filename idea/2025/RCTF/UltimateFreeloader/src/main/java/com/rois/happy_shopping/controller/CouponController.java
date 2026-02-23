package com.rois.happy_shopping.controller;

import com.rois.happy_shopping.common.Result;
import com.rois.happy_shopping.entity.Coupon;
import com.rois.happy_shopping.service.CouponService;
import com.rois.happy_shopping.util.JwtUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/api/coupon"})
@RestController
@CrossOrigin(origins = {"*"})
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/controller/CouponController.class */
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping({"/available"})
    public Result<List<Coupon>> getAvailableCoupons(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        List<Coupon> coupons = this.couponService.getAvailableCoupons(userId);
        return Result.success("Get available coupons successfully", coupons);
    }

    @GetMapping({"/my"})
    public Result<List<Coupon>> getMyCoupons(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !this.jwtUtil.validateToken(token).booleanValue()) {
            return Result.error(401, "Unauthorized access");
        }
        String userId = this.jwtUtil.getUserIdFromToken(token);
        List<Coupon> coupons = this.couponService.getUserCoupons(userId);
        return Result.success("Get my coupons successfully", coupons);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
