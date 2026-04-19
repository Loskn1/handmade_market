<template>
  <div class="admin-layout">
    <div class="bg-overlay"></div>
    <header class="admin-header">
      <div class="logo" @click="$router.push('/admin/dashboard')">
        <span class="logo-icon">⚙️</span>
        <span class="logo-text">手作趣集 · 管理后台</span>
      </div>
      <div class="user-info">
        <span>{{ adminAccount }}</span>
        <span class="role-badge" v-if="isSuperAdmin">超级管理员</span>
        <span class="role-badge normal" v-else>普通管理员</span>
        <button @click="handleLogout" class="logout-btn">退出登录</button>
      </div>
    </header>
    
    <div class="admin-container">
      <aside class="admin-sidebar">
        <nav class="sidebar-nav">
          <div class="nav-section">
            <h3>数据管理</h3>
            <router-link to="/admin/dashboard" class="nav-item">
              <span class="nav-icon">📊</span> 数据总览
            </router-link>
            <router-link to="/admin/users" class="nav-item">
              <span class="nav-icon">👥</span> 用户管理
            </router-link>
            <router-link to="/admin/statistics" class="nav-item">
              <span class="nav-icon">📈</span> 数据统计
            </router-link>
          </div>
          
          <div class="nav-section">
            <h3>商品管理</h3>
            <router-link to="/admin/goods-review" class="nav-item">
              <span class="nav-icon">🔍</span> 商品审核
            </router-link>
            <router-link to="/admin/goods-list" class="nav-item">
              <span class="nav-icon">📦</span> 商品管理
            </router-link>
          </div>
          
          <div class="nav-section">
            <h3>交易管理</h3>
            <router-link to="/admin/orders" class="nav-item">
              <span class="nav-icon">📝</span> 订单管理
            </router-link>
            <router-link to="/admin/comments" class="nav-item">
              <span class="nav-icon">💬</span> 评价管理
            </router-link>
          </div>
          
          <div class="nav-section" v-if="isSuperAdmin">
            <h3>系统管理</h3>
            <router-link to="/admin/admins" class="nav-item">
              <span class="nav-icon">👨‍💼</span> 管理员管理
            </router-link>
          </div>
        </nav>
      </aside>
      
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const adminAccount = computed(() => localStorage.getItem('userAccount') || 'admin')
const adminRole = computed(() => localStorage.getItem('adminRole') || 'normal')
const isSuperAdmin = computed(() => adminRole.value === 'super')

const handleLogout = () => {
  localStorage.clear()
  window.location.href = '/'
}
</script>

<style scoped>
.admin-layout { min-height: 100vh; position: relative; background-image: url('https://images.unsplash.com/photo-1513519245088-0e12902e5a38?q=80&w=2070&auto=format&fit=crop'); background-size: cover; background-position: center; background-attachment: fixed; }
.bg-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(240, 242, 245, 0.92); z-index: 0; }
.admin-header, .admin-container { position: relative; z-index: 1; }

.admin-header { background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); display: flex; justify-content: space-between; align-items: center; padding: 0 40px; height: 70px; color: white; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
.logo { display: flex; align-items: center; gap: 10px; cursor: pointer; }
.logo-icon { font-size: 28px; }
.logo-text { font-size: 20px; font-weight: bold; }
.user-info { display: flex; align-items: center; gap: 15px; }
.role-badge { background: #f59e0b; color: #333; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; }
.role-badge.normal { background: #10b981; }
.logout-btn { background: rgba(255,255,255,0.2); color: white; padding: 6px 16px; border-radius: 20px; font-size: 14px; cursor: pointer; transition: all 0.3s; }
.logout-btn:hover { background: rgba(255,255,255,0.3); }

.admin-container { display: flex; max-width: 1600px; margin: 20px auto; padding: 0 20px; gap: 20px; }
.admin-sidebar { width: 280px; flex-shrink: 0; }
.sidebar-nav { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 20px; padding: 10px 0; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.nav-section { margin-bottom: 10px; }
.nav-section h3 { padding: 12px 20px; font-size: 13px; color: #999; letter-spacing: 1px; border-bottom: 1px solid #f0f0f0; margin-bottom: 5px; }
.nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 20px; text-decoration: none; color: #4b5563; transition: all 0.3s; border-radius: 10px; margin: 0 8px; }
.nav-item:hover { background: #f3f4f6; }
.nav-item.router-link-active { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
.nav-icon { font-size: 20px; }

.admin-content { flex: 1; min-width: 0; background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 20px; padding: 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
</style>