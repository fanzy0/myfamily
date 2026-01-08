<template>
  <div class="product-list-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">🏮 商品列表 🏮</h2>
      <el-button 
        type="danger" 
        icon="el-icon-plus" 
        @click="handleAdd"
        class="festival-btn"
      >
        新增商品
      </el-button>
    </div>

    <!-- 搜索筛选区 -->
    <div class="search-section">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="商品类别">
          <el-input 
            v-model="queryParams.category" 
            placeholder="请输入类别"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input 
            v-model="queryParams.productName" 
            placeholder="请输入商品名称"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="可选" :value="1" />
            <el-option label="不可选" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" icon="el-icon-search" @click="handleSearch" class="festival-btn">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 商品表格 -->
    <div class="table-section">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        class="festival-table"
        style="width: 100%"
      >
        <el-table-column prop="productId" label="商品ID" width="100" align="center" />
        <el-table-column label="商品图片" width="120" align="center">
          <template slot-scope="scope">
            <el-image
              v-if="scope.row.imageUrl"
              :src="getImageUrl(scope.row.imageUrl)"
              :preview-src-list="[getImageUrl(scope.row.imageUrl)]"
              style="width: 80px; height: 80px"
              fit="cover"
            />
            <span v-else>暂无图片</span>
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="商品名称" min-width="150" />
        <el-table-column prop="category" label="类别" width="120" align="center" />
        <el-table-column prop="price" label="价格" width="120" align="center">
          <template slot-scope="scope">
            <span class="price-text">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '可选' : '不可选' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="productDesc" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button
                size="mini"
                type="warning"
                @click="handleEdit(scope.row)"
                icon="el-icon-edit"
              >
                编辑
              </el-button>
              <el-button
                v-if="scope.row.status === 1"
                size="mini"
                type="danger"
                @click="handleFreeze(scope.row)"
                icon="el-icon-lock"
              >
                冻结
              </el-button>
              <el-button
                v-else
                size="mini"
                type="success"
                @click="handleEnable(scope.row)"
                icon="el-icon-unlock"
              >
                启用
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          class="festival-pagination"
        />
      </div>
    </div>

    <!-- 新增/编辑商品对话框 -->
    <product-dialog
      :visible.sync="dialogVisible"
      :product-data="currentProduct"
      @refresh="loadData"
    />

    <!-- 认证对话框 -->
    <auth-dialog
      :visible.sync="authDialogVisible"
      @confirm="handleAuthConfirm"
    />
  </div>
</template>

<script>
import { getProductList, freezeProduct, enableProduct, getImageUrl as getImageUrlFromApi } from '@/api/product'
import ProductDialog from './components/ProductDialog.vue'
import AuthDialog from './components/AuthDialog.vue'

export default {
  name: 'ProductList',
  components: {
    ProductDialog,
    AuthDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      queryParams: {
        category: '',
        productName: '',
        status: null,
        minPrice: null,
        maxPrice: null
      },
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        total: 0
      },
      dialogVisible: false,
      authDialogVisible: false,
      currentProduct: null,
      pendingAction: null, // 待执行的操作
      pendingProductId: null // 待操作的商品ID
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryParams,
          pageIndex: this.pagination.pageIndex,
          pageSize: this.pagination.pageSize
        }
        // 清理空值
        Object.keys(params).forEach(key => {
          if (params[key] === '' || params[key] === null || params[key] === undefined) {
            delete params[key]
          }
        })
        const response = await getProductList(params)
        this.tableData = response.records || []
        this.pagination.total = response.total || 0
      } catch (error) {
        this.$message.error('加载商品列表失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.pageIndex = 1
      this.loadData()
    },
    // 重置
    handleReset() {
      this.queryParams = {
        category: '',
        productName: '',
        status: null,
        minPrice: null,
        maxPrice: null
      }
      this.pagination.pageIndex = 1
      this.loadData()
    },
    // 新增
    handleAdd() {
      this.currentProduct = null
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.currentProduct = { ...row }
      this.dialogVisible = true
    },
    // 冻结
    handleFreeze(row) {
      this.pendingAction = 'freeze'
      this.pendingProductId = row.productId
      this.authDialogVisible = true
    },
    // 启用
    handleEnable(row) {
      this.pendingAction = 'enable'
      this.pendingProductId = row.productId
      this.authDialogVisible = true
    },
    // 认证确认
    async handleAuthConfirm(authCode) {
      try {
        if (this.pendingAction === 'freeze') {
          await freezeProduct(this.pendingProductId, authCode)
          this.$message.success('冻结商品成功')
        } else if (this.pendingAction === 'enable') {
          await enableProduct(this.pendingProductId, authCode)
          this.$message.success('启用商品成功')
        }
        this.loadData()
      } catch (error) {
        const message = error.response?.data || error.message || '操作失败'
        this.$message.error(message)
      } finally {
        this.pendingAction = null
        this.pendingProductId = null
      }
    },
    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageIndex = 1
      this.loadData()
    },
    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.pageIndex = val
      this.loadData()
    },
    // 获取图片URL
    getImageUrl(imagePath) {
      return getImageUrlFromApi(imagePath)
    }
  }
}
</script>

<style scoped>
.product-list-container {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #ffe8e8;
}

.page-title {
  color: #c41e3a;
  font-size: 24px;
  margin: 0;
  text-shadow: 1px 1px 2px rgba(196, 30, 58, 0.2);
}

.festival-btn {
  background: linear-gradient(135deg, #c41e3a 0%, #e74c3c 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(196, 30, 58, 0.3);
  transition: all 0.3s;
}

.festival-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(196, 30, 58, 0.4);
}

.search-section {
  background: #fef5f5;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #ffe8e8;
}

.search-form {
  margin: 0;
}

.table-section {
  background: #fff;
}

.festival-table {
  border: 1px solid #ffe8e8;
}

.festival-table /deep/ .el-table__header {
  background-color: #fff5f5;
}

.festival-table /deep/ .el-table__header th {
  background-color: #ffe8e8 !important;
  color: #c41e3a;
  font-weight: bold;
}

.festival-table /deep/ .el-table__body tr:hover > td {
  background-color: #fff5f5 !important;
}

.price-text {
  color: #e74c3c;
  font-weight: bold;
  font-size: 16px;
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.festival-pagination /deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #c41e3a;
  color: #fff;
}

.festival-pagination /deep/ .el-pagination.is-background .btn-next,
.festival-pagination /deep/ .el-pagination.is-background .btn-prev {
  background-color: #fff5f5;
}

.festival-pagination /deep/ .el-pagination.is-background .btn-next:hover,
.festival-pagination /deep/ .el-pagination.is-background .btn-prev:hover {
  color: #c41e3a;
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}
</style>

