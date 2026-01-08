<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  margin: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  overflow: hidden; /* 防止页面整体滚动 */
}

#app {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  /* 覆盖 main.css 中的样式 */
  max-width: none !important;
  margin: 0 !important;
  padding: 0 !important;
  display: block !important;
}
</style>
