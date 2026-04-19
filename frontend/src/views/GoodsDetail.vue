<template>
  <div class="goods-detail-page" v-if="goods">
    <header class="header">
      <button @click="$router.back()" class="back-btn">← 返回</button>
      <h2>商品详情</h2>
      <div class="header-actions">
        <button @click="toggleFavorite" class="favorite-btn">
          {{ isFavorite ? '❤️ 已收藏' : '🤍 收藏' }}
        </button>
      </div>
    </header>

    <div class="container">
      <div class="detail-card">
        <div class="goods-gallery">
          <div class="main-image">
            <img :src="goods.imageUrl || defaultImage" :alt="goods.name">
          </div>
        </div>

        <div class="goods-info">
          <h1 class="goods-title">{{ goods.name }}</h1>
          
          <div class="goods-price">
            <span class="current-price">¥{{ goods.price }}</span>
            <span class="original-price" v-if="goods.originalPrice">¥{{ goods.originalPrice }}</span>
          </div>
          
          <div class="goods-desc" v-if="goods.description">
            <h3>商品描述</h3>
            <p>{{ goods.description }}</p>
          </div>

          <div class="goods-specs" v-if="goods.specs && goods.specs.length">
            <h3>规格选择</h3>
            <div class="spec-buttons">
              <button v-for="spec in goods.specs" :key="spec" class="spec-btn" :class="{ active: selectedSpec === spec }" @click="selectedSpec = spec">
                {{ spec }}
              </button>
            </div>
          </div>

          <div class="quantity-selector">
            <h3>数量</h3>
            <div class="quantity-controls">
              <button @click="decreaseQuantity" :disabled="quantity <= 1">-</button>
              <span>{{ quantity }}</span>
              <button @click="increaseQuantity">+</button>
            </div>
          </div>

          <div class="action-buttons">
            <button @click="handleAddToCart" class="cart-btn" :class="{ added: isInCart }" :disabled="isInCart">
              {{ isInCart ? '✓ 已加入购物车' : '🛒 加入购物车' }}
            </button>
            <button @click="handleBuyNow" class="buy-btn">立即购买</button>
          </div>

          <div class="goods-meta" v-if="goods.stock !== undefined">
            <p>📦 库存: {{ goods.stock }}件</p>
            <p>🏪 发货: {{ goods.shippingTime || '48小时内发货' }}</p>
            <p>🛡️ 售后: 7天无理由退换</p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading-container"><div class="loading-spinner"></div><p>加载中...</p></div>
    <div v-if="error" class="error-container"><p>{{ error }}</p><button @click="$router.back()">返回上一页</button></div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { requireTransactionAuth } from '@/utils/auth'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const goodsId = route.params.id

const goods = ref(null)
const loading = ref(true)
const error = ref(null)
const selectedSpec = ref('')
const quantity = ref(1)
const isFavorite = ref(false)
const isInCart = ref(false)
const defaultImage = 'https://picsum.photos/id/30/400/400'

// 检查商品是否已在购物车中
const checkInCart = () => {
  const cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]')
  const exists = cart.some(item => item.id === goods.value?.id && item.spec === selectedSpec.value)
  isInCart.value = exists
}

// 获取商品详情
const fetchGoodsDetail = async () => {
  loading.value = true
  error.value = null
  try {
    const response = await axios.get(`/api/goods/${goodsId}`)
    if (response.data.code === 200) {
      goods.value = response.data.data
      if (goods.value.specs && goods.value.specs.length) {
        selectedSpec.value = goods.value.specs[0]
      }
      checkInCart()
    } else {
      error.value = response.data.message || '获取商品信息失败'
    }
  } catch (err) {
    // 模拟数据（后端未就绪时使用）
    goods.value = {
      id: parseInt(goodsId),
      name: '青花瓷茶杯',
      price: 198,
      originalPrice: 298,
      description: '手工拉坯，手绘青花，每一件都是独一无二的艺术品',
      imageUrl: 'https://picsum.photos/id/30/400/400',
      stock: 50,
      specs: ['标准款', '礼盒装'],
      shippingTime: '48小时内发货'
    }
    selectedSpec.value = goods.value.specs[0]
    checkInCart()
  } finally {
    loading.value = false
  }
}

const increaseQuantity = () => { quantity.value++ }
const decreaseQuantity = () => { if (quantity.value > 1) quantity.value-- }

const toggleFavorite = async () => {
  try {
    const response = await axios.post(`/api/favorite/${goodsId}`, { isFavorite: !isFavorite.value })
    if (response.data.code === 200) {
      isFavorite.value = !isFavorite.value
      alert(isFavorite.value ? '已添加到收藏' : '已取消收藏')
    }
  } catch (err) {
    isFavorite.value = !isFavorite.value
    alert(isFavorite.value ? '已添加到收藏' : '已取消收藏')
  }
}

// 加入购物车
const handleAddToCart = () => {
  requireTransactionAuth(router, () => {
    addToCart()
  })
}

const addToCart = () => {
  if (!goods.value) return
  
  // 构建购物车商品对象
  const cartItem = {
    id: goods.value.id,
    name: goods.value.name,
    price: goods.value.price,
    quantity: quantity.value,
    spec: selectedSpec.value,
    imageUrl: goods.value.imageUrl || defaultImage,
    addTime: Date.now()
  }
  
  // 获取现有购物车
  let cart = JSON.parse(localStorage.getItem('shoppingCart') || '[]')
  
  // 检查是否已存在相同规格的商品
  const existingIndex = cart.findIndex(item => item.id === goods.value.id && item.spec === selectedSpec.value)
  
  if (existingIndex > -1) {
    // 已存在，增加数量
    cart[existingIndex].quantity += quantity.value
    alert(`「${goods.value.name}」已在购物车中，数量已增加 ${quantity.value} 件`)
  } else {
    // 不存在，添加新商品
    cart.push(cartItem)
    alert(`成功添加 ${quantity.value} 件「${goods.value.name}」到购物车`)
  }
  
  // 保存到 localStorage
  localStorage.setItem('shoppingCart', JSON.stringify(cart))
  
  // 更新按钮状态
  isInCart.value = true
}

// 立即购买
const handleBuyNow = () => {
  requireTransactionAuth(router, () => {
    buyNow()
  })
}

const buyNow = () => {
  // 先加入购物车再跳转结算
  addToCart()
  router.push('/user/cart')
}

// 监听规格变化，重新检查购物车状态
const checkCartOnSpecChange = () => {
  checkInCart()
}

// 监听规格变化
import { watch } from 'vue'
watch(selectedSpec, () => {
  checkInCart()
})

onMounted(() => {
  fetchGoodsDetail()
})
</script>

<style scoped>
.goods-detail-page { min-height: 100vh; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); }
.header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; display: flex; justify-content: space-between; align-items: center; padding: 0 30px; height: 70px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.back-btn, .favorite-btn { background: rgba(255,255,255,0.2); border: none; color: white; padding: 8px 20px; border-radius: 20px; cursor: pointer; transition: all 0.3s ease; }
.back-btn:hover, .favorite-btn:hover { background: rgba(255,255,255,0.3); transform: translateY(-2px); }
.header h2 { font-size: 24px; font-weight: 600; }
.header-actions { display: flex; gap: 10px; }
.container { max-width: 1200px; margin: 40px auto; padding: 0 20px; }
.detail-card { background: white; border-radius: 24px; overflow: hidden; display: grid; grid-template-columns: 1fr 1fr; gap: 40px; padding: 40px; margin-bottom: 40px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
.goods-gallery { display: flex; flex-direction: column; gap: 20px; }
.main-image { width: 100%; aspect-ratio: 1; border-radius: 16px; overflow: hidden; background: #f5f5f5; }
.main-image img { width: 100%; height: 100%; object-fit: cover; }
.goods-info { display: flex; flex-direction: column; gap: 20px; }
.goods-title { font-size: 28px; color: #333; margin: 0; }
.goods-price { display: flex; align-items: baseline; gap: 15px; padding: 15px 0; border-top: 1px solid #f0f0f0; border-bottom: 1px solid #f0f0f0; }
.current-price { font-size: 32px; font-weight: bold; color: #ff5757; }
.original-price { font-size: 18px; color: #999; text-decoration: line-through; }
.goods-desc h3, .goods-specs h3, .quantity-selector h3 { font-size: 16px; margin-bottom: 12px; color: #333; }
.goods-desc p { color: #666; line-height: 1.6; }
.spec-buttons { display: flex; gap: 12px; flex-wrap: wrap; }
.spec-btn { background: #f5f5f5; color: #666; padding: 8px 20px; border-radius: 20px; border: 1px solid #e0e0e0; cursor: pointer; transition: all 0.3s; }
.spec-btn.active { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-color: transparent; }
.quantity-controls { display: flex; align-items: center; gap: 15px; }
.quantity-controls button { width: 36px; height: 36px; border-radius: 8px; background: #f5f5f5; color: #333; font-size: 18px; padding: 0; cursor: pointer; }
.quantity-controls button:disabled { opacity: 0.5; cursor: not-allowed; }
.quantity-controls span { font-size: 18px; font-weight: 600; min-width: 40px; text-align: center; }
.action-buttons { display: flex; gap: 15px; margin-top: 10px; }
.cart-btn, .buy-btn { flex: 1; padding: 14px; font-size: 16px; font-weight: 600; border-radius: 12px; cursor: pointer; transition: all 0.3s; }
.cart-btn { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }
.cart-btn.added { background: #10b981; cursor: default; opacity: 0.8; }
.cart-btn.added:hover { transform: none; }
.cart-btn:disabled { cursor: not-allowed; opacity: 0.7; }
.buy-btn { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; }
.cart-btn:hover, .buy-btn:hover { transform: translateY(-2px); }
.goods-meta { background: #f9f9f9; padding: 15px; border-radius: 12px; display: flex; justify-content: space-between; }
.goods-meta p { font-size: 13px; color: #666; }
.loading-container { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 400px; }
.loading-spinner { width: 50px; height: 50px; border: 4px solid #f3f3f3; border-top: 4px solid #667eea; border-radius: 50%; animation: spin 1s linear infinite; }
@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
.error-container { text-align: center; padding: 60px 20px; }
.error-container p { color: #ff5757; margin-bottom: 20px; font-size: 18px; }
.error-container button { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 12px 30px; border-radius: 25px; }
@media (max-width: 768px) { .detail-card { grid-template-columns: 1fr; padding: 20px; gap: 20px; } .goods-title { font-size: 22px; } .current-price { font-size: 28px; } }
</style>