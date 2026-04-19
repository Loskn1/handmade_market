<!-- <template>
  <div class="creator-layout">
    <header class="creator-header">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🎨</span>
        <span class="logo-text">手作趣集 · 创作者中心</span>
      </div>
      <div class="user-info">
        <img :src="userAvatar || defaultAvatar" class="avatar" />
        <span>{{ userNickname || userAccount }}</span>
      </div>
    </header>
    
    <div class="creator-container">
      <aside class="creator-sidebar">
        <div class="stats-card">
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalGoods }}</div>
            <div class="stat-label">总商品</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.pendingGoods }}</div>
            <div class="stat-label">审核中</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalOrders }}</div>
            <div class="stat-label">总订单</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">{{ stats.totalSales }}</div>
            <div class="stat-label">总销售额</div>
          </div>
        </div>
        
        <nav class="sidebar-nav">
          <router-link to="/creator/center" class="nav-item">
            <span class="nav-icon">📊</span>
            <span>工作台</span>
          </router-link>
          <router-link to="/creator/goods" class="nav-item">
            <span class="nav-icon">📦</span>
            <span>商品管理</span>
          </router-link>
          <router-link to="/creator/publish" class="nav-item">
            <span class="nav-icon">➕</span>
            <span>发布商品</span>
          </router-link>
          <router-link to="/creator/orders" class="nav-item">
            <span class="nav-icon">📝</span>
            <span>订单管理</span>
          </router-link>
          <router-link to="/creator/custom-requests" class="nav-item">
            <span class="nav-icon">💬</span>
            <span>定制需求</span>
          </router-link>
          <router-link to="/creator/profile" class="nav-item">
            <span class="nav-icon">👤</span>
            <span>个人资料</span>
          </router-link>
        </nav>
      </aside>
      
      <main class="creator-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const defaultAvatar = 'https://via.placeholder.com/40'
const userAccount = computed(() => localStorage.getItem('userAccount') || '')
const userNickname = computed(() => localStorage.getItem('userNickname') || '')
const userAvatar = computed(() => localStorage.getItem('userAvatar') || '')

const stats = ref({
  totalGoods: 12,
  pendingGoods: 3,
  totalOrders: 45,
  totalSales: 8940
})
</script>

<style scoped>
.creator-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.creator-header {
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

.creator-container {
  display: flex;
  max-width: 1400px;
  margin: 30px auto;
  padding: 0 20px;
  gap: 30px;
}

.creator-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.stats-card {
  background: white;
  border-radius: 20px;
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: var(--primary);
}

.stat-label {
  font-size: 12px;
  color: #6b7280;
  margin-top: 5px;
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

.creator-content {
  flex: 1;
  min-width: 0;
}
</style> -->