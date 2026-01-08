-- 商品表结构定义：t_product
CREATE TABLE IF NOT EXISTS t_product (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    product_id   BIGINT       NOT NULL COMMENT '商品ID，业务编号，数字递增',
    category     VARCHAR(64)  NOT NULL COMMENT '商品类别',
    product_name VARCHAR(128) NOT NULL COMMENT '商品名称',
    product_desc VARCHAR(512)          DEFAULT NULL COMMENT '商品描述',
    stock        INT          NOT NULL DEFAULT 0 COMMENT '库存数量',
    price        DECIMAL(10,2)         DEFAULT 0.00 COMMENT '商品单价',
    image_url    VARCHAR(255)          DEFAULT NULL COMMENT '商品图片地址',
    status       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：0-冻结，1-启用',
    deleted      TINYINT      NOT NULL DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_product_id (product_id),
    KEY idx_category_status (category, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';


