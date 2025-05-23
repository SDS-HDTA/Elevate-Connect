<template>
  <div class="workpiece-container">
    <div class="header">
      <div class="search-box">
        <span class="search-icon">🔍</span>
        <input type="text" placeholder="搜索" />
      </div>
      <span class="title">Workpiece</span>
    </div>
    <div class="card-list">
      <div class="card">
        <div class="card-title">Interview<br />Meeting<br />Minutes</div>
        <div class="card-type">Folder</div>
      </div>
      <div class="card">
        <div class="card-title">Empathy<br />Map</div>
        <div class="card-type selected">Whiteboard</div>
      </div>
      <div class="card">
        <div class="card-title">Community<br />Map</div>
        <div class="card-type">Map</div>
      </div>
    </div>
    <div class="add-menu-container">
      <div v-if="showMenu" class="add-menu">
        <div class="menu-item">Page</div>
        <div class="menu-item">Whiteboard</div>
        <div class="menu-item">Map</div>
        <div class="menu-item">Picture</div>
        <div class="menu-item">Video</div>
        <div class="menu-item">Folder</div>
      </div>
      <button class="add-btn" @click="showMenu = !showMenu">＋</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const showMenu = ref(false)
</script>

<style scoped>
.workpiece-container {
  padding: 20px;
  min-height: 100vh;
  position: relative;
}
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.search-box {
  display: flex;
  align-items: center;
  border: 2px solid #e74c3c;
  border-radius: 20px;
  padding: 2px 10px;
  margin-right: 10px;
}
.search-icon {
  color: #e74c3c;
  margin-right: 5px;
}
.search-box input {
  border: none;
  outline: none;
  background: transparent;
}
.title {
  color: #888;
  font-size: 20px;
}
.card-list {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}
.card {
  border: 1px solid #bbb;
  width: 150px;
  height: 160px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  background: #fff;
}
.card-title {
  font-size: 18px;
  text-align: center;
  margin-top: 20px;
}
.card-type {
  width: 100%;
  text-align: center;
  font-size: 22px;
  font-weight: bold;
  border-top: 1px solid #bbb;
  padding: 10px 0;
}
.card-type.selected {
  background: #cce0ff;
  color: #2d5be3;
}
.add-menu-container {
  position: absolute;
  right: 30px;
  top: 30px;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.add-btn {
  width: 48px;
  height: 48px;
  font-size: 36px;
  background: #fff;
  border: 2px solid #000;
  border-radius: 6px;
  cursor: pointer;
}
.add-menu {
  margin-bottom: 10px;
  background: #fff;
  border: 1px solid #bbb;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.menu-item {
  padding: 8px 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.menu-item:last-child {
  border-bottom: none;
}
.menu-item:hover {
  background: #f0f0f0;
}
</style> 