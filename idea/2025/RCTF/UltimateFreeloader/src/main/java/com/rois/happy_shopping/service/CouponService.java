package com.rois.happy_shopping.service;

import com.rois.happy_shopping.entity.Coupon;
import com.rois.happy_shopping.mapper.CouponMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/service/CouponService.class */
public class CouponService {

    @Autowired
    private CouponMapper couponMapper;

    public List<Coupon> getAvailableCoupons(String userId) {
        return this.couponMapper.findAvailableByUserId(userId);
    }

    public Coupon getCouponById(String id) {
        return this.couponMapper.findById(id);
    }

    public boolean validateCoupon(String couponId, String userId) {
        Coupon coupon = this.couponMapper.findById(couponId);
        if (coupon == null || !coupon.getUserId().equals(userId) || coupon.getIsUsed().booleanValue() || coupon.getExpireTime().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    public boolean useCoupon(String couponId) {
        return this.couponMapper.updateUsedStatus(couponId, true) > 0;
    }

    public boolean restoreCoupon(String couponId) {
        return this.couponMapper.updateUsedStatus(couponId, false) > 0;
    }

    public List<Coupon> getUserCoupons(String userId) {
        return this.couponMapper.findByUserId(userId);
    }
}
