<template>
  <div class="main-layout">
    <header class="header">
      <div class="logo" @click="$router.push('/')">
        <span class="logo-icon">🎨</span>
        <span class="logo-text">手作趣集</span>
      </div>
      
      <div class="search-box">
        <input type="text" v-model="searchKeyword" placeholder="搜索商品、用户..." @keyup.enter="handleSearch" />
        <button @click="handleSearch">🔍</button>
        <button class="image-search" @click="showImageSearch = true">📷</button>
      </div>
      
      <div class="header-right">
        <template v-if="!isLoggedIn">
          <router-link to="/login" class="nav-link login-link">登录</router-link>
          <router-link to="/register" class="nav-link register-link">注册</router-link>
        </template>
        <template v-else>
          <div class="user-menu" @click.stop="toggleUserMenu">
            <img :src="userAvatar || defaultAvatar" class="user-avatar" />
            <span class="user-name">{{ userNickname || userAccount }}</span>
            <span class="dropdown-icon">▼</span>
          </div>
          <div v-if="showUserMenu" class="user-dropdown" @click.stop>
            <router-link to="/user/orders">我的订单</router-link>
            <router-link to="/user/favorites">我的收藏</router-link>
            <router-link to="/user/publish">发布商品</router-link>
            <router-link to="/user/goods">我的商品</router-link>
            <router-link to="/user/sales">销售管理</router-link>
            <router-link to="/user/profile">个人资料</router-link>
            <router-link to="/user/address">收货地址</router-link>
            <a href="#" @click.prevent="handleLogout">退出登录</a>
          </div>
        </template>
      </div>
    </header>

    <div class="main-container">
      <aside class="sidebar">
        <div class="sidebar-user" v-if="isLoggedIn">
          <img :src="userAvatar || defaultAvatar" class="sidebar-avatar" />
          <div class="sidebar-user-info">
            <h4>{{ userNickname || userAccount }}</h4>
            <p>信用分：{{ creditScore }}</p>
          </div>
        </div>
        <div class="sidebar-user" v-else>
          <div class="sidebar-guest">
            <span class="guest-icon">👤</span>
            <h4>游客模式</h4>
            <p>登录后体验更多功能</p>
            <div class="guest-buttons">
              <router-link to="/login" class="guest-login">登录</router-link>
              <router-link to="/register" class="guest-register">注册</router-link>
            </div>
          </div>
        </div>
        
        <nav class="sidebar-nav">
          <div class="nav-section">
            <h3>浏览</h3>
            <router-link to="/" class="nav-item" exact-active-class="active"><span class="nav-icon">🏠</span> 首页</router-link>
            <router-link to="/search" class="nav-item" active-class="active"><span class="nav-icon">🔍</span> 发现商品</router-link>
            <router-link to="/categories" class="nav-item" active-class="active"><span class="nav-icon">📂</span> 商品分类</router-link>
          </div>
          
          <div class="nav-section" v-if="isLoggedIn">
            <h3>我的交易</h3>
            <router-link to="/user/orders" class="nav-item" active-class="active"><span class="nav-icon">📦</span> 我的订单</router-link>
            <router-link to="/user/favorites" class="nav-item" active-class="active"><span class="nav-icon">❤️</span> 我的收藏</router-link>
          </div>

          <div class="nav-section" v-if="isLoggedIn">
          <h3>我的购物</h3>
          <router-link to="/user/cart" class="nav-item" active-class="active"><span class="nav-icon">🛒</span> 购物车</router-link>
          </div>

          <div class="nav-section" v-if="isLoggedIn">
            <h3>卖家中心</h3>
            <router-link to="/user/publish" class="nav-item" active-class="active"><span class="nav-icon">➕</span> 发布商品</router-link>
            <router-link to="/user/goods" class="nav-item" active-class="active"><span class="nav-icon">📝</span> 我的商品</router-link>
            <router-link to="/user/sales" class="nav-item" active-class="active"><span class="nav-icon">💰</span> 销售管理</router-link>
          </div>
          
          <div class="nav-section" v-if="isLoggedIn">
            <h3>设置</h3>
            <router-link to="/user/profile" class="nav-item" active-class="active"><span class="nav-icon">👤</span> 个人资料</router-link>
            <router-link to="/user/address" class="nav-item" active-class="active"><span class="nav-icon">📍</span> 收货地址</router-link>
          </div>

        </nav>
      </aside>
      
      <main class="main-content">
        <router-view />
      </main>
    </div>

    <div v-if="showImageSearch" class="modal" @click="showImageSearch = false">
      <div class="modal-content" @click.stop>
        <h3>图片搜索</h3>
        <p>上传图片，查找相似商品</p>
        <div class="image-upload-area" @click="triggerImageUpload">
          <input type="file" ref="imageInput" accept="image/jpeg,image/png" style="display: none" @change="handleImageUpload" />
          <div v-if="!uploadedImage" class="upload-placeholder"><span>📷</span><p>点击上传图片</p><small>支持 JPG/PNG，最大 10MB</small></div>
          <div v-else class="upload-preview"><img :src="uploadedImage" alt="预览图"><button @click.stop="clearImage" class="clear-btn">清除</button></div>
        </div>
        <button v-if="uploadedImage" @click="performImageSearch" class="search-btn">开始搜索</button>
        <button @click="showImageSearch = false" class="close-btn">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const searchKeyword = ref('')
const showUserMenu = ref(false)
const showImageSearch = ref(false)
const uploadedImage = ref(null)
const imageInput = ref(null)
const defaultAvatar = 'https://via.placeholder.com/40'
const creditScore = ref(0)

const isLoggedIn = computed(() => !!localStorage.getItem('token'))
const userRole = computed(() => localStorage.getItem('userRole') || '')
const userAccount = computed(() => localStorage.getItem('userAccount') || '')
const userNickname = computed(() => localStorage.getItem('userNickname') || '')
const userAvatar = computed(() => localStorage.getItem('userAvatar') || '')

const fetchCreditScore = async () => {
  if (!isLoggedIn.value) return
  try {
    const response = await axios.get('/api/user/credit-score', {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (response.data.code === 200) {
      creditScore.value = response.data.data.score
    }
  } catch (err) {
    console.error('获取信用分失败', err)
  }
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { keyword: searchKeyword.value } })
  }
}

const toggleUserMenu = () => { showUserMenu.value = !showUserMenu.value }

const handleLogout = () => {
  localStorage.clear()
  window.location.href = '/'
}

const triggerImageUpload = () => { imageInput.value.click() }

const handleImageUpload = (e) => {
  const file = e.target.files[0]
  if (file && file.size <= 10 * 1024 * 1024) {
    const reader = new FileReader()
    reader.onload = (ev) => { uploadedImage.value = ev.target.result }
    reader.readAsDataURL(file)
  } else { alert('图片大小不能超过10MB') }
}

const clearImage = () => {
  uploadedImage.value = null
  if (imageInput.value) imageInput.value.value = ''
}

const performImageSearch = async () => {
  if (!uploadedImage.value) return
  alert('图片识别搜索功能开发中，请联系后端实现')
}

const closeMenu = () => { showUserMenu.value = false }

watch(isLoggedIn, (newVal) => {
  if (newVal) {
    fetchCreditScore()
  } else {
    creditScore.value = 0
  }
})

onMounted(() => {
  document.addEventListener('click', closeMenu)
  if (isLoggedIn.value) {
    fetchCreditScore()
  }
})
onUnmounted(() => document.removeEventListener('click', closeMenu))
</script>

<style scoped>
.main-layout { 
  min-height: 100vh; 
  background-image: url('https://images.unsplash.com/photo-1513519245088-0e12902e5a38?q=80&w=2070&auto=format&fit=crop');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  position: relative;
}
.main-layout::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(245, 247, 250, 0.9);
  z-index: 0;
}
.header, .main-container, .modal { position: relative; z-index: 1; }

.header { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); display: flex; justify-content: space-between; align-items: center; padding: 0 40px; height: 70px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 100; }
.logo { display: flex; align-items: center; gap: 10px; cursor: pointer; }
.logo-icon { font-size: 32px; animation: float 3s ease-in-out infinite; }
@keyframes float { 0%,100% { transform: translateY(0); } 50% { transform: translateY(-5px); } }
.logo-text { font-size: 24px; font-weight: bold; background: linear-gradient(135deg, #667eea, #764ba2); -webkit-background-clip: text; background-clip: text; color: transparent; }
.search-box { flex: 1; max-width: 500px; display: flex; gap: 10px; margin: 0 40px; }
.search-box input { flex: 1; padding: 10px 18px; border: 2px solid #e5e7eb; border-radius: 30px; font-size: 14px; background: rgba(255,255,255,0.9); }
.search-box input:focus { outline: none; border-color: #667eea; }
.search-box button { width: 44px; height: 44px; border-radius: 50%; background: linear-gradient(135deg, #667eea, #764ba2); color: white; font-size: 18px; padding: 0; }
.image-search { background: rgba(243,244,246,0.9) !important; color: #666 !important; }
.header-right { display: flex; align-items: center; gap: 15px; }
.login-link, .register-link { text-decoration: none; padding: 8px 20px; border-radius: 25px; font-size: 14px; }
.login-link { color: #667eea; border: 1px solid #667eea; background: rgba(255,255,255,0.9); }
.login-link:hover { background: #667eea; color: white; }
.register-link { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
.user-menu { display: flex; align-items: center; gap: 10px; cursor: pointer; padding: 5px 12px; border-radius: 30px; transition: background 0.3s; background: rgba(255,255,255,0.9); }
.user-menu:hover { background: #f3f4f6; }
.user-avatar { width: 36px; height: 36px; border-radius: 50%; object-fit: cover; }
.user-name { font-size: 14px; color: #333; }
.user-dropdown { position: absolute; top: 70px; right: 40px; background: white; border-radius: 12px; box-shadow: 0 10px 40px rgba(0,0,0,0.15); overflow: hidden; z-index: 101; min-width: 160px; }
.user-dropdown a { display: block; padding: 12px 20px; text-decoration: none; color: #4b5563; transition: background 0.3s; font-size: 14px; }
.user-dropdown a:hover { background: #f3f4f6; }
.main-container { display: flex; max-width: 1400px; margin: 0 auto; padding: 20px; gap: 20px; }
.sidebar { width: 280px; flex-shrink: 0; }
.sidebar-user { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 20px; padding: 20px; text-align: center; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.sidebar-avatar { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 3px solid #667eea; margin-bottom: 12px; }
.sidebar-user-info h4 { font-size: 18px; margin-bottom: 5px; }
.sidebar-user-info p { font-size: 13px; color: #999; }
.sidebar-guest { text-align: center; }
.guest-icon { font-size: 48px; display: block; margin-bottom: 10px; }
.sidebar-guest h4 { font-size: 18px; margin-bottom: 5px; }
.sidebar-guest p { font-size: 13px; color: #999; margin-bottom: 15px; }
.guest-buttons { display: flex; gap: 10px; justify-content: center; }
.guest-login, .guest-register { padding: 6px 16px; border-radius: 20px; text-decoration: none; font-size: 13px; }
.guest-login { border: 1px solid #667eea; color: #667eea; background: white; }
.guest-register { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
.sidebar-nav { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 20px; padding: 10px 0; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.nav-section { margin-bottom: 10px; }
.nav-section h3 { padding: 12px 20px; font-size: 13px; color: #999; letter-spacing: 1px; }
.nav-item { display: flex; align-items: center; gap: 12px; padding: 12px 20px; text-decoration: none; color: #4b5563; transition: all 0.3s; }
.nav-item:hover { background: #f3f4f6; }
.nav-item.active { background: linear-gradient(135deg, #667eea10, #764ba210); color: #667eea; border-right: 3px solid #667eea; }
.nav-icon { font-size: 20px; }
.main-content { flex: 1; min-width: 0; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 450px; text-align: center; }
.image-upload-area { width: 100%; min-height: 200px; border: 2px dashed #ccc; border-radius: 12px; margin: 20px 0; cursor: pointer; display: flex; align-items: center; justify-content: center; }
.upload-placeholder { text-align: center; padding: 40px; }
.upload-placeholder span { font-size: 48px; }
.upload-preview img { max-width: 100%; max-height: 300px; border-radius: 8px; }
.clear-btn { position: absolute; top: 10px; right: 10px; background: rgba(0,0,0,0.6); color: white; padding: 5px 10px; }
.search-btn, .close-btn { margin-top: 10px; padding: 10px 20px; }
.search-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; margin-right: 10px; }
</style>