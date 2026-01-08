package com.my.family.shop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.family.shop.domain.entity.ProductDO;
import com.my.family.shop.service.ImageService;
import com.my.family.shop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 商品控制器测试类
 *
 * @author fan
 * @date 2026/01/05
 */
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ProductController productController;

    private ProductDO mockProductDO;

    @BeforeEach
    public void setUp() {
        mockProductDO = new ProductDO();
        mockProductDO.setId(1L);
        mockProductDO.setProductId(1001L);
        mockProductDO.setCategory("手机");
        mockProductDO.setProductName("测试商品");
        mockProductDO.setProductDesc("测试描述");
        mockProductDO.setStock(100);
        mockProductDO.setPrice(new BigDecimal("1999.00"));
        mockProductDO.setImageUrl("2026/01/05/test.jpg");
        mockProductDO.setStatus(1);
        mockProductDO.setDeleted(0);
        mockProductDO.setCreateTime(LocalDateTime.now());
        mockProductDO.setUpdateTime(LocalDateTime.now());
    }

    @Test
    public void testUploadImage_Success() {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        String expectedPath = "2026/01/05/uuid123.jpg";

        // Mock行为
        when(imageService.uploadImage(any(MultipartFile.class))).thenReturn(expectedPath);

        // 执行测试
        ResponseEntity<String> response = productController.uploadImage(file);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPath, response.getBody());
        verify(imageService, times(1)).uploadImage(any(MultipartFile.class));
    }

    @Test
    public void testUploadImage_EmptyFile() {
        // 准备测试数据
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        // 执行测试
        ResponseEntity<String> response = productController.uploadImage(emptyFile);

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("图片文件不能为空", response.getBody());
    }

    @Test
    public void testViewImage_Success() {
        // 准备测试数据
        String imagePath = "2026/01/05/test.jpg";
        byte[] imageBytes = "test image bytes".getBytes();
        String contentType = "image/jpeg";

        // Mock行为
        when(imageService.getImageBytes(anyString())).thenReturn(imageBytes);
        when(imageService.getImageContentType(anyString())).thenReturn(contentType);

        // 执行测试
        ResponseEntity<byte[]> response = productController.viewImage(imagePath);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(imageBytes.length, response.getBody().length);
        assertEquals(contentType, response.getHeaders().getContentType().toString());
        verify(imageService, times(1)).getImageBytes(imagePath);
        verify(imageService, times(1)).getImageContentType(imagePath);
    }

    @Test
    public void testViewImage_EmptyPath() {
        // 执行测试
        ResponseEntity<byte[]> response = productController.viewImage("");

        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testPageProducts_Success() {
        // 准备测试数据
        IPage<ProductDO> mockPage = mock(IPage.class);

        // Mock行为
        when(productService.pageProducts(anyString(), anyString(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockPage);

        // 执行测试
        ResponseEntity<IPage<ProductDO>> response = productController.pageProducts("手机", "测试", 1, null, null, 1, 10);

        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
        verify(productService, times(1))
                .pageProducts("手机", "测试", 1, null, null, 1, 10);
    }
}

