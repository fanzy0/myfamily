package com.my.family.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.family.shop.domain.entity.ProductDO;
import com.my.family.shop.service.ImageService;
import com.my.family.shop.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 商品控制器
 * 提供商品新增、冻结、启用以及分页查询接口
 *
 * 新增、冻结、启用接口需要前端传入认证信息 authCode，
 * 后端进行简单校验，不通过则返回无权限提示
 *
 * @author fan
 * @date 2026/01/05
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    private final ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    /**
     * 新增商品
     *
     * @param productDO 商品信息
     * @param authCode  认证信息
     * @return 操作结果
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveProduct(@RequestBody ProductDO productDO,
                                              @RequestParam("authCode") String authCode) {
        try {
            if (productDO == null) {
                return ResponseEntity.badRequest().body("商品信息不能为空");
            }
            if (!StringUtils.hasText(authCode)) {
                return ResponseEntity.badRequest().body("认证信息不能为空");
            }
            productService.saveProduct(productDO, authCode);
            return ResponseEntity.ok("新增商品成功");
        } catch (SecurityException e) {
            LOGGER.warn("[SaveProduct] 无权限, msg={}", e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("[SaveProduct] 参数校验失败, msg={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("[SaveProduct] 新增商品异常", e);
            return ResponseEntity.internalServerError().body("新增商品失败");
        }
    }

    /**
     * 更新商品
     *
     * @param productDO 商品信息，必须包含主键ID
     * @param authCode  认证信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody ProductDO productDO,
                                                @RequestParam("authCode") String authCode) {
        try {
            if (productDO == null || productDO.getId() == null) {
                return ResponseEntity.badRequest().body("商品ID不能为空");
            }
            if (!StringUtils.hasText(authCode)) {
                return ResponseEntity.badRequest().body("认证信息不能为空");
            }
            productService.updateProduct(productDO, authCode);
            return ResponseEntity.ok("编辑商品成功");
        } catch (SecurityException e) {
            LOGGER.warn("[UpdateProduct] 无权限, msg={}", e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("[UpdateProduct] 参数校验失败, msg={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("[UpdateProduct] 编辑商品异常", e);
            return ResponseEntity.internalServerError().body("编辑商品失败");
        }
    }

    /**
     * 冻结商品
     *
     * @param productId 商品ID
     * @param authCode  认证信息
     * @return 操作结果
     */
    @PostMapping("/freeze")
    public ResponseEntity<String> freezeProduct(@RequestParam("productId") Long productId,
                                                @RequestParam("authCode") String authCode) {
        try {
            if (productId == null || productId <= 0L) {
                return ResponseEntity.badRequest().body("商品ID不合法");
            }
            if (!StringUtils.hasText(authCode)) {
                return ResponseEntity.badRequest().body("认证信息不能为空");
            }
            productService.freezeProduct(productId, authCode);
            return ResponseEntity.ok("冻结商品成功");
        } catch (SecurityException e) {
            LOGGER.warn("[FreezeProduct] 无权限, msg={}", e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("[FreezeProduct] 参数校验失败, msg={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("[FreezeProduct] 冻结商品异常", e);
            return ResponseEntity.internalServerError().body("冻结商品失败");
        }
    }

    /**
     * 启用商品
     *
     * @param productId 商品ID
     * @param authCode  认证信息
     * @return 操作结果
     */
    @PostMapping("/enable")
    public ResponseEntity<String> enableProduct(@RequestParam("productId") Long productId,
                                                @RequestParam("authCode") String authCode) {
        try {
            if (productId == null || productId <= 0L) {
                return ResponseEntity.badRequest().body("商品ID不合法");
            }
            if (!StringUtils.hasText(authCode)) {
                return ResponseEntity.badRequest().body("认证信息不能为空");
            }
            productService.enableProduct(productId, authCode);
            return ResponseEntity.ok("启用商品成功");
        } catch (SecurityException e) {
            LOGGER.warn("[EnableProduct] 无权限, msg={}", e.getMessage());
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("[EnableProduct] 参数校验失败, msg={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("[EnableProduct] 启用商品异常", e);
            return ResponseEntity.internalServerError().body("启用商品失败");
        }
    }

    /**
     * 分页查询商品列表
     * 支持按类别、商品名称模糊、价格区间、状态进行筛选
     *
     * @param category    商品类别，可选
     * @param productName 商品名称，支持模糊查询，可选
     * @param status      状态，可选
     * @param minPrice    最小价格，可选
     * @param maxPrice    最大价格，可选
     * @param pageIndex   页码，默认1
     * @param pageSize    每页条数，默认10
     * @return 分页结果
     */
    @GetMapping("/page")
    public ResponseEntity<IPage<ProductDO>> pageProducts(@RequestParam(value = "category", required = false) String category,
                                                         @RequestParam(value = "productName", required = false) String productName,
                                                         @RequestParam(value = "status", required = false) Integer status,
                                                         @RequestParam(value = "minPrice", required = false) java.math.BigDecimal minPrice,
                                                         @RequestParam(value = "maxPrice", required = false) java.math.BigDecimal maxPrice,
                                                         @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            IPage<ProductDO> pageResult = productService.pageProducts(category, productName, status, minPrice, maxPrice, pageIndex, pageSize);
            return ResponseEntity.ok(pageResult);
        } catch (Exception e) {
            LOGGER.error("[PageProducts] 分页查询商品异常", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 上传商品图片
     * 前端在新增商品时，先调用此接口上传图片，获取图片路径后，再调用保存商品接口
     *
     * @param file 图片文件
     * @return 图片相对路径，用于后续保存商品时使用
     */
    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                LOGGER.warn("[UploadImage] 文件为空");
                return ResponseEntity.badRequest().body("图片文件不能为空");
            }

            String imagePath = imageService.uploadImage(file);
            LOGGER.info("[UploadImage] 图片上传成功, imagePath={}", imagePath);
            return ResponseEntity.ok(imagePath);

        } catch (IllegalArgumentException e) {
            LOGGER.warn("[UploadImage] 参数校验失败, msg={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("[UploadImage] 上传图片异常", e);
            return ResponseEntity.internalServerError().body("上传图片失败: " + e.getMessage());
        }
    }

    /**
     * 查看商品图片
     * 根据图片相对路径返回图片流，支持懒加载
     * 前端在商品列表中，先获取商品信息（包含图片路径），然后逐个调用此接口加载图片
     *
     * @param imagePath 图片相对路径，从商品信息中的imageUrl字段获取
     * @return 图片字节流
     */
    @GetMapping("/image/view")
    public ResponseEntity<byte[]> viewImage(@RequestParam("path") String imagePath) {
        try {
            if (!StringUtils.hasText(imagePath)) {
                LOGGER.warn("[ViewImage] 图片路径为空");
                return ResponseEntity.badRequest().build();
            }

            // 步骤1 - 读取图片字节
            byte[] imageBytes = imageService.getImageBytes(imagePath);

            // 步骤2 - 获取Content-Type
            String contentType = imageService.getImageContentType(imagePath);

            // 步骤3 - 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            // 设置缓存头，提高性能
            headers.setCacheControl("public, max-age=3600");
            headers.setContentLength(imageBytes.length);

            LOGGER.info("[ViewImage] 返回图片成功, imagePath={}, size={}", imagePath, imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            LOGGER.warn("[ViewImage] 参数校验失败, imagePath={}, msg={}", imagePath, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            LOGGER.error("[ViewImage] 查看图片异常, imagePath={}", imagePath, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}


