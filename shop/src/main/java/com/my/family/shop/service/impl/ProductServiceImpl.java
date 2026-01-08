package com.my.family.shop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.family.shop.domain.entity.ProductDO;
import com.my.family.shop.domain.enums.ProductStatusEnum;
import com.my.family.shop.mapper.ProductMapper;
import com.my.family.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品服务实现类
 * 负责商品的新增、冻结、启用以及分页查询逻辑
 *
 * @author fan
 * @date 2026/01/05
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductMapper productMapper;

    /**
     * 简单认证码，用于演示权限校验
     */
    @Value("${shop.auth.code:123456}")
    private String authCodeConfig;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(ProductDO productDO, String authCode) {
        LOGGER.info("[SaveProduct] 开始新增商品, productDO={}", productDO);
        checkAuth(authCode);
        if (productDO == null) {
            throw new IllegalArgumentException("商品信息不能为空");
        }
        if (!StringUtils.hasText(productDO.getProductName())) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (!StringUtils.hasText(productDO.getCategory())) {
            throw new IllegalArgumentException("商品类别不能为空");
        }
        if (productDO.getStock() == null || productDO.getStock() < 0) {
            throw new IllegalArgumentException("库存不能小于0");
        }
        // 设置业务商品ID：如果前端未传，则查询当前最大product_id并加1；如无数据则从1开始
        if (productDO.getProductId() == null) {
            Long maxProductId = productMapper.selectMaxProductId();
            long nextProductId = (maxProductId == null ? 1L : maxProductId + 1L);
            productDO.setProductId(nextProductId);
            LOGGER.info("[SaveProduct] 自动生成商品ID, maxProductId={}, nextProductId={}", maxProductId, nextProductId);
        }
        // 设置默认值
        if (productDO.getStatus() == null) {
            productDO.setStatus(ProductStatusEnum.ENABLED.getCode());
        }
        if (productDO.getDeleted() == null) {
            productDO.setDeleted(0);
        }
        // 设置时间
        LocalDateTime now = LocalDateTime.now();
        if (productDO.getCreateTime() == null) {
            productDO.setCreateTime(now);
        }
        if (productDO.getUpdateTime() == null) {
            productDO.setUpdateTime(now);
        }
        // 执行插入
        int result = productMapper.insert(productDO);
        if (result <= 0) {
            LOGGER.error("[SaveProduct] 新增商品失败, productDO={}", productDO);
            throw new RuntimeException("新增商品失败");
        }
        LOGGER.info("[SaveProduct] 新增商品成功, productId={}", productDO.getProductId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductDO productDO, String authCode) {
        LOGGER.info("[UpdateProduct] 开始更新商品, productDO={}", productDO);
        checkAuth(authCode);
        if (productDO == null || productDO.getId() == null) {
            throw new IllegalArgumentException("更新商品时主键ID不能为空");
        }
        if (!StringUtils.hasText(productDO.getProductName())) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (!StringUtils.hasText(productDO.getCategory())) {
            throw new IllegalArgumentException("商品类别不能为空");
        }
        if (productDO.getStock() == null || productDO.getStock() < 0) {
            throw new IllegalArgumentException("库存不能小于0");
        }
        if (productDO.getStatus() == null) {
            productDO.setStatus(ProductStatusEnum.ENABLED.getCode());
        }
        if (productDO.getDeleted() == null) {
            productDO.setDeleted(0);
        }
        productDO.setUpdateTime(LocalDateTime.now());
        int result = productMapper.updateById(productDO);
        if (result <= 0) {
            LOGGER.error("[UpdateProduct] 更新商品失败, productDO={}", productDO);
            throw new RuntimeException("更新商品失败");
        }
        LOGGER.info("[UpdateProduct] 更新商品成功, productId={}", productDO.getProductId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void freezeProduct(Long productId, String authCode) {
        LOGGER.info("[FreezeProduct] 开始冻结商品, productId={}", productId);
        checkAuth(authCode);
        ProductDO productDO = getByProductId(productId);
        productDO.setStatus(ProductStatusEnum.FROZEN.getCode());
        productDO.setUpdateTime(LocalDateTime.now());
        int result = productMapper.updateById(productDO);
        if (result <= 0) {
            LOGGER.error("[FreezeProduct] 冻结商品失败, productId={}", productId);
            throw new RuntimeException("冻结商品失败");
        }
        LOGGER.info("[FreezeProduct] 冻结商品成功, productId={}", productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableProduct(Long productId, String authCode) {
        LOGGER.info("[EnableProduct] 开始启用商品, productId={}", productId);
        checkAuth(authCode);
        ProductDO productDO = getByProductId(productId);
        productDO.setStatus(ProductStatusEnum.ENABLED.getCode());
        productDO.setUpdateTime(LocalDateTime.now());
        int result = productMapper.updateById(productDO);
        if (result <= 0) {
            LOGGER.error("[EnableProduct] 启用商品失败, productId={}", productId);
            throw new RuntimeException("启用商品失败");
        }
        LOGGER.info("[EnableProduct] 启用商品成功, productId={}", productId);
    }

    @Override
    public IPage<ProductDO> pageProducts(String category, String productName, Integer status, BigDecimal minPrice, BigDecimal maxPrice,
                                         int pageIndex, int pageSize) {
        LOGGER.info("[PageProducts] 开始分页查询商品列表, category={}, productName={}, status={}, minPrice={}, maxPrice={}, pageIndex={}, pageSize={}",
                category, productName, status, minPrice, maxPrice, pageIndex, pageSize);
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        Page<ProductDO> page = new Page<>(pageIndex, pageSize);
        IPage<ProductDO> resultPage = productMapper.selectPage(page, category, productName, status, minPrice, maxPrice);
        LOGGER.info("[PageProducts] 分页查询完成, total={}", resultPage.getTotal());
        return resultPage;
    }

    /**
     * 校验认证信息
     *
     * @param authCode 认证码
     */
    private void checkAuth(String authCode) {
        if (!StringUtils.hasText(authCode) || !authCode.equals(authCodeConfig)) {
            LOGGER.warn("[CheckAuth] 认证失败, authCode={}", authCode);
            throw new SecurityException("没有权限执行该操作");
        }
    }

    /**
     * 根据商品ID（业务编号）查询商品信息
     *
     * @param productId 商品ID，业务编号
     * @return 商品数据对象
     */
    private ProductDO getByProductId(Long productId) {
        if (productId == null || productId <= 0L) {
            throw new IllegalArgumentException("商品ID不合法");
        }
        ProductDO productDO = productMapper.selectByProductId(productId);
        if (productDO == null) {
            throw new IllegalArgumentException("商品不存在，productId=" + productId);
        }
        return productDO;
    }
}


