<template>
  <div class="user-favorites">
    <h3>我的收藏 ({{ favorites.length }})</h3>
    <div class="favorites-grid">
      <div v-for="item in favorites" :key="item.id" class="favorite-card">
        <div class="card-image" @click="goToDetail(item.id)">
          <img :src="item.imageUrl">
          <button class="remove-btn" @click.stop="removeFavorite(item)">✕</button>
        </div>
        <div class="card-info" @click="goToDetail(item.id)">
          <h4>{{ item.name }}</h4>
          <p class="price">¥{{ item.price }}</p>
          <p class="seller">卖家：{{ item.sellerName }}</p>
        </div>
        <div class="card-actions">
          <button @click="goToDetail(item.id)" class="view-btn">查看详情</button>
          <button @click="addToCart(item)" class="cart-btn">加入购物车</button>
        </div>
      </div>
    </div>
    <div v-if="favorites.length === 0" class="empty">暂无收藏商品，去首页逛逛吧~</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const favorites = ref([
  { id: 1, name: '青花瓷茶杯', price: 198, sellerName: '陶瓷匠人', imageUrl: 'https://picsum.photos/id/30/200/200' },
  { id: 2, name: '手工编织包', price: 299, sellerName: '编织达人', imageUrl: 'https://picsum.photos/id/31/200/200' }
])

const goToDetail = (id) => router.push(`/goods/${id}`)
const removeFavorite = (item) => { const idx = favorites.value.findIndex(f => f.id === item.id); favorites.value.splice(idx, 1); alert('已取消收藏') }
const addToCart = (item) => alert(`已将「${item.name}」加入购物车`)
</script>

<style scoped>
.user-favorites { padding: 0; }
h3 { margin-bottom: 20px; }
.favorites-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(260px, 1fr)); gap: 25px; }
.favorite-card { background: white; border-radius: 16px; overflow: hidden; transition: all 0.3s; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.favorite-card:hover { transform: translateY(-5px); box-shadow: 0 10px 25px rgba(0,0,0,0.1); }
.card-image { position: relative; height: 200px; cursor: pointer; }
.card-image img { width: 100%; height: 100%; object-fit: cover; }
.remove-btn { position: absolute; top: 10px; right: 10px; width: 28px; height: 28px; border-radius: 50%; background: rgba(0,0,0,0.5); color: white; font-size: 16px; padding: 0; }
.card-info { padding: 15px; cursor: pointer; }
.card-info h4 { font-size: 16px; margin-bottom: 8px; }
.card-info .price { font-size: 20px; font-weight: bold; color: #ff5757; }
.card-info .seller { font-size: 12px; color: #999; margin-top: 5px; }
.card-actions { display: flex; gap: 10px; padding: 0 15px 15px; }
.view-btn, .cart-btn { flex: 1; padding: 8px; font-size: 13px; }
.view-btn { background: #f3f4f6; color: #666; }
.cart-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
.empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>