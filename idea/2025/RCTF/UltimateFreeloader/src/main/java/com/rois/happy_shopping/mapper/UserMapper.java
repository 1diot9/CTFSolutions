package com.rois.happy_shopping.mapper;

import com.rois.happy_shopping.entity.User;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/mapper/UserMapper.class */
public interface UserMapper {
    int insert(User user);

    User findByUsername(@Param("username") String str);

    User findById(@Param("id") String str);

    int updateBalance(@Param("id") String str, @Param("balance") BigDecimal bigDecimal);

    int countByUsername(@Param("username") String str);

    int countByEmail(@Param("email") String str);
}
