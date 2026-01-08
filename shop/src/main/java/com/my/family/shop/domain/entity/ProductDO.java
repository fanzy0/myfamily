package com.my.family.shop.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品数据对象，对应数据库表：t_product
 * 主要用于存储商品的基础信息和状态
 *
 * @author fan
 * @date 2026/01/05
 */
public class ProductDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 商品ID，业务编号，数字递增
     */
    private Long productId;

    /**
     * 商品类别，例如：手机、图书等
     */
    private String category;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述，详细说明
     */
    private String productDesc;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 商品单价
     */
    private BigDecimal price;

    /**
     * 商品图片地址路径
     */
    private String imageUrl;

    /**
     * 商品状态：0-冻结，1-启用
     */
    private Integer status;

    /**
     * 是否删除标记：0-未删除，1-已删除
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ProductDO{" + "id=" + id + ", productId=" + productId + ", category='" + category + '\'' + ", productName='" + productName
                + '\'' + ", productDesc='" + productDesc + '\'' + ", stock=" + stock + ", price=" + price + ", imageUrl='" + imageUrl + '\''
                + ", status=" + status + ", deleted=" + deleted + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
}


