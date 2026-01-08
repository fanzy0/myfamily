<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    class="product-dialog"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="productForm"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="product-form"
    >
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="formData.productName" placeholder="请输入商品名称" />
      </el-form-item>
      <el-form-item label="商品类别" prop="category">
        <el-input v-model="formData.category" placeholder="请输入商品类别" />
      </el-form-item>
      <el-form-item label="商品价格" prop="price">
        <el-input-number
          v-model="formData.price"
          :precision="2"
          :min="0"
          :max="999999"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="库存数量" prop="stock">
        <el-input-number
          v-model="formData.stock"
          :min="0"
          :max="999999"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="商品描述" prop="productDesc">
        <el-input
          v-model="formData.productDesc"
          type="textarea"
          :rows="4"
          placeholder="请输入商品描述"
        />
      </el-form-item>
      <el-form-item label="商品图片">
        <div class="image-upload-section">
          <el-upload
            class="image-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :http-request="handleUpload"
          >
            <img v-if="imagePreview" :src="imagePreview" class="image-preview" />
            <i v-else class="el-icon-plus image-uploader-icon"></i>
          </el-upload>
          <div class="upload-tips">
            <p>支持 JPG、PNG 格式，建议尺寸 300x300</p>
            <p v-if="formData.imageUrl" class="image-path">图片路径：{{ formData.imageUrl }}</p>
          </div>
        </div>
      </el-form-item>
      <el-form-item label="商品状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">冻结</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="danger" @click="handleSave" :loading="saving" class="festival-btn">
        保存
      </el-button>
    </div>

    <!-- 认证对话框 -->
    <auth-dialog
      :visible.sync="authDialogVisible"
      @confirm="handleAuthConfirm"
    />
  </el-dialog>
</template>

<script>
import { saveProduct, updateProduct, uploadImage, getImageUrl } from '@/api/product'
import AuthDialog from './AuthDialog.vue'

export default {
  name: 'ProductDialog',
  components: {
    AuthDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    productData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      formData: {
        productName: '',
        category: '',
        price: 0,
        stock: 0,
        productDesc: '',
        imageUrl: '',
        status: 1
      },
      rules: {
        productName: [
          { required: true, message: '请输入商品名称', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请输入商品类别', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请输入商品价格', trigger: 'blur' },
          { type: 'number', min: 0, message: '价格必须大于等于0', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '请输入库存数量', trigger: 'blur' },
          { type: 'number', min: 0, message: '库存必须大于等于0', trigger: 'blur' }
        ]
      },
      imagePreview: '',
      saving: false,
      authDialogVisible: false
    }
  },
  computed: {
    dialogTitle() {
      return this.productData ? '🎊 编辑商品 🎊' : '🎊 新增商品 🎊'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initFormData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 初始化表单数据
    initFormData() {
      if (this.productData) {
        this.formData = {
          id: this.productData.id,
          productId: this.productData.productId,
          productName: this.productData.productName || '',
          category: this.productData.category || '',
          price: this.productData.price || 0,
          stock: this.productData.stock || 0,
          productDesc: this.productData.productDesc || '',
          imageUrl: this.productData.imageUrl || '',
          status: this.productData.status !== undefined ? this.productData.status : 1
        }
        if (this.formData.imageUrl) {
          this.imagePreview = getImageUrl(this.formData.imageUrl)
        } else {
          this.imagePreview = ''
        }
      } else {
        this.formData = {
          productName: '',
          category: '',
          price: 0,
          stock: 0,
          productDesc: '',
          imageUrl: '',
          status: 1
        }
        this.imagePreview = ''
      }
      this.$nextTick(() => {
        if (this.$refs.productForm) {
          this.$refs.productForm.clearValidate()
        }
      })
    },
    // 上传前校验
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传文件只能是图片格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    // 自定义上传
    async handleUpload(options) {
      try {
        this.$message.info('图片上传中...')
        const imagePath = await uploadImage(options.file)
        this.formData.imageUrl = imagePath
        this.imagePreview = getImageUrl(imagePath)
        this.$message.success('图片上传成功')
      } catch (error) {
        this.$message.error('图片上传失败：' + (error.message || '未知错误'))
      }
    },
    // 保存
    handleSave() {
      this.$refs.productForm.validate(valid => {
        if (valid) {
          this.authDialogVisible = true
        }
      })
    },
    // 认证确认
    async handleAuthConfirm(authCode) {
      this.saving = true
      try {
        if (this.productData && this.formData.id) {
          await updateProduct(this.formData, authCode)
          this.$message.success('编辑商品成功')
        } else {
          await saveProduct(this.formData, authCode)
          this.$message.success('新增商品成功')
        }
        this.handleClose()
        this.$emit('refresh')
      } catch (error) {
        const message = error.response?.data || error.message || '保存失败'
        this.$message.error(message)
      } finally {
        this.saving = false
      }
    },
    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.$refs.productForm.resetFields()
      this.formData = {
        productName: '',
        category: '',
        price: 0,
        stock: 0,
        productDesc: '',
        imageUrl: '',
        status: 1
      }
      this.imagePreview = ''
    }
  }
}
</script>

<style scoped>
.product-dialog /deep/ .el-dialog__header {
  background: linear-gradient(135deg, #c41e3a 0%, #e74c3c 100%);
  padding: 20px;
  border-radius: 4px 4px 0 0;
}

.product-dialog /deep/ .el-dialog__title {
  color: #ffd700;
  font-size: 18px;
  font-weight: bold;
}

.product-dialog /deep/ .el-dialog__headerbtn .el-dialog__close {
  color: #fff;
  font-size: 20px;
}

.product-form {
  padding: 20px 0;
}

.image-upload-section {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.image-uploader {
  border: 2px dashed #c41e3a;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 150px;
  height: 150px;
  background: #fff5f5;
  transition: all 0.3s;
}

.image-uploader:hover {
  border-color: #e74c3c;
  background: #ffe8e8;
}

.image-uploader-icon {
  font-size: 40px;
  color: #c41e3a;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}

.image-preview {
  width: 150px;
  height: 150px;
  display: block;
  object-fit: cover;
}

.upload-tips {
  flex: 1;
  color: #666;
  font-size: 12px;
  line-height: 1.8;
}

.image-path {
  margin-top: 10px;
  color: #c41e3a;
  word-break: break-all;
}

.dialog-footer {
  text-align: right;
}

.festival-btn {
  background: linear-gradient(135deg, #c41e3a 0%, #e74c3c 100%);
  border: none;
}
</style>

