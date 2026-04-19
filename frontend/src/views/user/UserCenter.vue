<template>
  <div class="user-center">
    <div class="welcome-card">
      <div class="welcome-text">
        <h2>欢迎回来，{{ userNickname }}！</h2>
        <p>信用分：{{ creditScore }}分 | 会员等级：{{ memberLevel }}</p>
      </div>
      <div class="stats">
        <div><div>{{ stats.orderCount }}</div><span>累计订单</span></div>
        <div><div>{{ stats.favoriteCount }}</div><span>收藏商品</span></div>
        <div><div>{{ stats.goodsCount }}</div><span>在售商品</span></div>
        <div><div>{{ stats.salesCount }}</div><span>总销量</span></div>
      </div>
    </div>

    <div class="quick-links">
      <div class="link-card" @click="$router.push('/user/orders')">📦 我的订单</div>
      <div class="link-card" @click="$router.push('/user/favorites')">❤️ 我的收藏</div>
      <div class="link-card" @click="$router.push('/user/publish')">➕ 发布商品</div>
      <div class="link-card" @click="$router.push('/user/goods')">📝 我的商品</div>
      <div class="link-card" @click="$router.push('/user/sales')">💰 销售管理</div>
      <div class="link-card" @click="$router.push('/user/profile')">👤 个人资料</div>
    </div>

    <div class="section">
      <h3>最新订单</h3>
      <div class="order-list">
        <div v-for="order in recentOrders" :key="order.id" class="order-item">
          <div><span>{{ order.name }}</span><span>¥{{ order.price }}</span></div>
          <div><span class="status" :class="order.status">{{ order.status === 'pending' ? '待支付' : '已发货' }}</span><button @click="payOrder(order)" v-if="order.status === 'pending'">去支付</button></div>
        </div>
        <div v-if="recentOrders.length === 0" class="empty">暂无订单</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const userNickname = computed(() => localStorage.getItem('userNickname') || '用户')
const creditScore = ref(85)
const memberLevel = computed(() => {
  if (creditScore.value >= 90) return '黄金会员'
  if (creditScore.value >= 80) return '白银会员'
  return '青铜会员'
})

const stats = ref({ orderCount: 8, favoriteCount: 12, goodsCount: 5, salesCount: 23 })
const recentOrders = ref([
  { id: 1, name: '青花瓷茶杯', price: 198, status: 'pending' },
  { id: 2, name: '手工编织包', price: 299, status: 'shipped' }
])

const payOrder = (order) => alert(`支付订单 ${order.name}`)
</script>

<style scoped>
.user-center { padding: 0; }
.welcome-card { background: linear-gradient(135deg, #667eea, #764ba2); border-radius: 20px; padding: 30px; color: white; margin-bottom: 25px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 20px; }
.welcome-text h2 { font-size: 24px; margin-bottom: 8px; }
.stats { display: flex; gap: 30px; text-align: center; }
.stats > div div { font-size: 28px; font-weight: bold; }
.stats > div span { font-size: 13px; opacity: 0.8; }
.quick-links { display: grid; grid-template-columns: repeat(6,1fr); gap: 15px; margin-bottom: 25px; }
.link-card { background: white; padding: 20px; text-align: center; border-radius: 16px; cursor: pointer; transition: all 0.3s; font-size: 18px; }
.link-card:hover { transform: translateY(-3px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
.section { background: white; border-radius: 20px; padding: 20px; }
.section h3 { margin-bottom: 15px; }
.order-item { display: flex; justify-content: space-between; align-items: center; padding: 12px; border-bottom: 1px solid #f0f0f0; }
.status { padding: 2px 8px; border-radius: 10px; font-size: 12px; margin-right: 10px; }
.status.pending { background: #fef3c7; color: #d97706; }
.status.shipped { background: #d1fae5; color: #065f46; }
.empty { text-align: center; padding: 30px; color: #999; }
</style>