package com.my.family.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.family.shop.domain.entity.ProductDO;

import java.math.BigDecimal;

/**
 * 商品服务接口
 * 提供商品新增、冻结、启用以及分页查询能力
 *
 * @author fan
 * @date 2026/01/05
 */
public interface ProductService {

    /**
     * 新增商品
     *
     * @param productDO 商品数据对象，包含商品基础信息
     * @param authCode  认证信息，用于权限校验
     */
    void saveProduct(ProductDO productDO, String authCode);

    /**
     * 更新商品
     *
     * @param productDO 商品数据对象，必须包含主键ID或商品ID
     * @param authCode  认证信息，用于权限校验
     */
    void updateProduct(ProductDO productDO, String authCode);

    /**
     * 冻结商品
     *
     * @param productId 商品ID，业务编号
     * @param authCode  认证信息，用于权限校验
     */
    void freezeProduct(Long productId, String authCode);

    /**
     * 启用商品
     *
     * @param productId 商品ID，业务编号
     * @param authCode  认证信息，用于权限校验
     */
    void enableProduct(Long productId, String authCode);

    /**
     * 分页查询商品列表
     *
     * @param category    商品类别，可为空
     * @param productName 商品名称，支持模糊查询，可为空
     * @param status      商品状态，可为空
     * @param minPrice    最小价格，可为空
     * @param maxPrice    最大价格，可为空
     * @param pageIndex   页码，从1开始
     * @param pageSize    每页条数
     * @return 分页结果，包含商品列表及分页信息
     */
    IPage<ProductDO> pageProducts(String category, String productName, Integer status, BigDecimal minPrice, BigDecimal maxPrice,
                                  int pageIndex, int pageSize);
}


