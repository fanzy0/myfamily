package com.my.family.shop.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.family.shop.domain.entity.ProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 商品表数据访问层
 * 负责商品基础信息的增删改查
 *
 * @author fan
 * @date 2026/01/05
 */
@Mapper
public interface ProductMapper {

    /**
     * 新增商品
     *
     * @param productDO 商品数据对象
     * @return 影响行数
     */
    int insert(ProductDO productDO);

    /**
     * 根据主键ID更新商品信息
     *
     * @param productDO 商品数据对象，必须包含id
     * @return 影响行数
     */
    int updateById(ProductDO productDO);

    /**
     * 根据商品ID（业务编号）查询商品信息
     *
     * @param productId 商品ID，业务编号
     * @return 商品数据对象，不存在返回null
     */
    ProductDO selectByProductId(@Param("productId") Long productId);

    /**
     * 查询当前未删除商品中最大的商品ID（product_id）
     *
     * @return 最大的商品ID，如果没有数据则返回null
     */
    Long selectMaxProductId();

    /**
     * 分页查询商品列表
     *
     * @param page        分页对象
     * @param category    商品类别，可为空
     * @param productName 商品名称，支持模糊查询，可为空
     * @param status      商品状态，可为空
     * @param minPrice    最小价格，可为空
     * @param maxPrice    最大价格，可为空
     * @return 分页结果
     */
    IPage<ProductDO> selectPage(Page<ProductDO> page,
                                @Param("category") String category,
                                @Param("productName") String productName,
                                @Param("status") Integer status,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice);
}


