package com.rois.happy_shopping.mapper;

import com.rois.happy_shopping.entity.Product;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/mapper/ProductMapper.class */
public interface ProductMapper {
    int insert(Product product);

    Product findById(@Param("id") String str);

    List<Product> findAll();
}
