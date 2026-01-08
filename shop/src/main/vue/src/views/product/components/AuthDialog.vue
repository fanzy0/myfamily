<template>
  <el-dialog
    title="🔐 身份认证 🔐"
    :visible.sync="dialogVisible"
    width="450px"
    :close-on-click-modal="false"
    :append-to-body="true"
    class="auth-dialog"
    @opened="handleOpened"
    @close="handleClose"
  >
    <div class="auth-content">
      <div class="auth-icon">
        <i class="el-icon-lock" style="font-size: 48px; color: #c41e3a;"></i>
      </div>
      <p class="auth-tips">此操作需要身份认证，请输入认证码</p>
      <el-form ref="authForm" :model="formData" :rules="rules">
        <el-form-item prop="authCode">
          <el-input
            ref="authCodeInput"
            v-model="formData.authCode"
            type="password"
            placeholder="请输入认证码"
            show-password
            @keyup.enter.native="handleConfirm"
          />
        </el-form-item>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="danger" @click="handleConfirm" class="festival-btn">
        确认
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AuthDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogVisible: false,
      formData: {
        authCode: ''
      },
      rules: {
        authCode: [
          { required: true, message: '请输入认证码', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.formData.authCode = ''
        this.$nextTick(() => {
          if (this.$refs.authForm) {
            this.$refs.authForm.clearValidate()
          }
        })
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 对话框打开后的回调
    handleOpened() {
      // 对话框完全打开后，聚焦到输入框
      this.$nextTick(() => {
        if (this.$refs.authCodeInput) {
          const input = this.$refs.authCodeInput.$el.querySelector('input')
          if (input) {
            input.focus()
            input.select()
          }
        }
      })
    },
    handleConfirm() {
      this.$refs.authForm.validate(valid => {
        if (valid) {
          this.$emit('confirm', this.formData.authCode)
          this.handleClose()
        }
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.formData.authCode = ''
      if (this.$refs.authForm) {
        this.$refs.authForm.resetFields()
      }
    }
  }
}
</script>

<style scoped>
.auth-dialog /deep/ .el-dialog__header {
  background: linear-gradient(135deg, #c41e3a 0%, #e74c3c 100%);
  padding: 20px;
  border-radius: 4px 4px 0 0;
}

.auth-dialog /deep/ .el-dialog__title {
  color: #ffd700;
  font-size: 18px;
  font-weight: bold;
}

.auth-dialog /deep/ .el-dialog__headerbtn .el-dialog__close {
  color: #fff;
}

.auth-content {
  padding: 30px 20px;
  text-align: center;
}

.auth-icon {
  margin-bottom: 20px;
}

.auth-tips {
  color: #666;
  font-size: 14px;
  margin-bottom: 30px;
  line-height: 1.6;
}

.dialog-footer {
  text-align: right;
}

.festival-btn {
  background: linear-gradient(135deg, #c41e3a 0%, #e74c3c 100%);
  border: none;
}
</style>

