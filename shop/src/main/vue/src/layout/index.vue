<template>
  <div class="app-container">
    <!-- 顶部装饰横幅 -->
    <div class="festival-banner">
      <div class="banner-content">
        <span class="festival-text">🎉 新年快乐 🎉</span>
        <span class="festival-subtitle">恭贺新禧 · 万事如意</span>
      </div>
    </div>

    <!-- 主体布局 -->
    <div class="main-layout">
      <!-- 左侧导航栏 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h2 class="sidebar-title">🎊 商品管理 🎊</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="festival-menu"
          background-color="#c41e3a"
          text-color="#fff"
          active-text-color="#ffd700"
          router
        >
          <el-menu-item index="/product" class="menu-item">
            <i class="el-icon-menu"></i>
            <span slot="title">云里雾里</span>
          </el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧内容区 -->
      <div class="content-wrapper">
        <div class="content-container">
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    activeMenu() {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #fff5f5 0%, #ffe8e8 100%);
  overflow: hidden;
}

.festival-banner {
  background: linear-gradient(90deg, #c41e3a 0%, #e74c3c 50%, #c41e3a 100%);
  padding: 15px 0;
  text-align: center;
  box-shadow: 0 2px 8px rgba(196, 30, 58, 0.3);
  position: relative;
  overflow: hidden;
}

.festival-banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% { left: -100%; }
  100% { left: 100%; }
}

.banner-content {
  position: relative;
  z-index: 1;
}

.festival-text {
  font-size: 28px;
  font-weight: bold;
  color: #ffd700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  margin-right: 20px;
}

.festival-subtitle {
  font-size: 16px;
  color: #fff;
  letter-spacing: 3px;
}

.main-layout {
  display: flex;
  height: calc(100vh - 70px);
}

.sidebar {
  width: 250px;
  background: #c41e3a;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.sidebar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    repeating-linear-gradient(
      45deg,
      transparent,
      transparent 10px,
      rgba(255, 255, 255, 0.03) 10px,
      rgba(255, 255, 255, 0.03) 20px
    );
  pointer-events: none;
}

.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 2px solid rgba(255, 255, 255, 0.2);
}

.sidebar-title {
  color: #ffd700;
  font-size: 20px;
  margin: 0;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.festival-menu {
  border: none;
  padding-top: 10px;
}

.menu-item {
  margin: 5px 10px;
  border-radius: 8px;
  transition: all 0.3s;
}

.menu-item:hover {
  background-color: rgba(255, 215, 0, 0.2) !important;
}

.menu-item.is-active {
  background-color: rgba(255, 215, 0, 0.3) !important;
  font-weight: bold;
}

.content-wrapper {
  flex: 1;
  overflow: auto;
  background: #fafafa;
  padding: 20px;
}

.content-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(196, 30, 58, 0.1);
  padding: 20px;
  min-height: calc(100vh - 140px);
}
</style>

