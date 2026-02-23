package com.rois.happy_shopping.mapper;

import com.rois.happy_shopping.entity.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/mapper/OrderMapper.class */
public interface OrderMapper {
    int insert(Order order);

    Order findById(@Param("id") String str);

    List<Order> findByUserId(@Param("userId") String str);

    int updateStatus(@Param("id") String str, @Param("status") String str2);
}
