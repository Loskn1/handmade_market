<!-- <template>
  <div class="consumer-layout">
    <header class="consumer-header">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🎨</span>
        <span class="logo-text">手作趣集</span>
      </div>
      <div class="user-info">
        <img :src="userAvatar || defaultAvatar" class="avatar" />
        <span>{{ userNickname || userAccount }}</span>
      </div>
    </header>
    
    <div class="consumer-container">
      <aside class="consumer-sidebar">
        <div class="user-card">
          <img :src="userAvatar || defaultAvatar" class="user-avatar" />
          <h3>{{ userNickname || userAccount }}</h3>
          <p>信用分: {{ creditScore }}</p>
          <div class="credit-bar">
            <div class="credit-fill" :style="{ width: creditScore + '%' }"></div>
          </div>
        </div>
        
        <nav class="sidebar-nav">
          <router-link to="/consumer/center" class="nav-item">
            <span class="nav-icon">📊</span>
            <span>个人中心</span>
          </router-link>
          <router-link to="/consumer/orders" class="nav-item">
            <span class="nav-icon">📦</span>
            <span>我的订单</span>
          </router-link>
          <router-link to="/consumer/favorites" class="nav-item">
            <span class="nav-icon">❤️</span>
            <span>我的收藏</span>
          </router-link>
          <router-link to="/consumer/custom-request" class="nav-item">
            <span class="nav-icon">✏️</span>
            <span>定制需求</span>
          </router-link>
          <router-link to="/consumer/profile" class="nav-item">
            <span class="nav-icon">👤</span>
            <span>个人信息</span>
          </router-link>
        </nav>
      </aside>
      
      <main class="consumer-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const defaultAvatar = 'https://via.placeholder.com/80'
const userAccount = computed(() => localStorage.getItem('userAccount') || '')
const userNickname = computed(() => localStorage.getItem('userNickname') || '')
const userAvatar = computed(() => localStorage.getItem('userAvatar') || '')
const creditScore = ref(80)
</script>

<style scoped>
.consumer-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

.consumer-header {
  background: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40px;
  height: 70px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-icon {
  font-size: 28px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.consumer-container {
  display: flex;
  max-width: 1400px;
  margin: 30px auto;
  padding: 0 20px;
  gap: 30px;
}

.consumer-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.user-card {
  background: white;
  border-radius: 20px;
  padding: 30px 20px;
  text-align: center;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.user-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: 15px;
  border: 3px solid var(--primary);
}

.user-card h3 {
  font-size: 18px;
  margin-bottom: 10px;
}

.credit-bar {
  width: 100%;
  height: 8px;
  background: #e5e7eb;
  border-radius: 4px;
  margin-top: 10px;
  overflow: hidden;
}

.credit-fill {
  height: 100%;
  background: linear-gradient(90deg, #10b981, #34d399);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.sidebar-nav {
  background: white;
  border-radius: 20px;
  padding: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  border-radius: 12px;
  text-decoration: none;
  color: #4b5563;
  transition: all 0.3s ease;
}

.nav-item:hover {
  background: #f3f4f6;
  transform: translateX(5px);
}

.nav-item.router-link-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.nav-icon {
  font-size: 20px;
}

.consumer-content {
  flex: 1;
  min-width: 0;
}
</style> -->