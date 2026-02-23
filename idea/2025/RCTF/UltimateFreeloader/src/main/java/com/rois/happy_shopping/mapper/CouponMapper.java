package com.rois.happy_shopping.mapper;

import com.rois.happy_shopping.entity.Coupon;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/mapper/CouponMapper.class */
public interface CouponMapper {
    int insert(Coupon coupon);

    Coupon findById(@Param("id") String str);

    List<Coupon> findAvailableByUserId(@Param("userId") String str);

    int updateUsedStatus(@Param("id") String str, @Param("isUsed") Boolean bool);

    List<Coupon> findByUserId(@Param("userId") String str);
}
