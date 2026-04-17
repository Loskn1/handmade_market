<template>
  <div class="home-page">
    <!-- 英雄区域 - 高度缩小50%，背景图片更清晰 -->
    <div class="hero-section">
      <div class="hero-bg-image"></div>
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <div class="art-title">
          <span class="art-char" v-for="(char, i) in '手作趣集'" :key="i" :style="{ animationDelay: i * 0.1 + 's' }">
            {{ char }}
          </span>
        </div>
        <p class="hero-subtitle">每一件手作，都是独一无二的艺术品</p>
        <div class="hero-decoration">
          <span class="deco-line"></span>
          <span class="deco-diamond">✦</span>
          <span class="deco-line"></span>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h2 class="section-title"><span class="title-icon">✨</span>精选推荐</h2>
        <router-link to="/search" class="more-link">查看更多 →</router-link>
      </div>
      
      <div class="goods-grid" v-if="loading">
        <div class="loading">加载中...</div>
      </div>
      <div class="goods-grid" v-else-if="goodsList.length > 0">
        <div class="goods-card" v-for="item in goodsList" :key="item.id" @click="goToDetail(item.id)">
          <div class="card-image">
            <img :src="item.imageUrl" :alt="item.name">
            <div class="card-badge" v-if="item.isNew">新品</div>
            <div class="card-badge hot" v-if="item.isHot">热卖</div>
          </div>
          <div class="card-info">
            <h3>{{ item.name }}</h3>
            <p class="card-desc">{{ item.shortDesc }}</p>
            <div class="card-price">
              <span class="price">¥{{ item.price }}</span>
              <span class="original-price" v-if="item.originalPrice">¥{{ item.originalPrice }}</span>
            </div>
            <div class="card-footer">
              <span class="sales">❤️ {{ item.sales }}人购买</span>
              <span class="seller">卖家：{{ item.sellerName }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="empty" v-else>暂无商品</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const loading = ref(true)
const goodsList = ref([])

const fetchGoodsList = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/goods/recommend')
    if (response.data.code === 200) {
      goodsList.value = response.data.data
    }
  } catch (err) {
    console.error('获取商品列表失败', err)
    goodsList.value = []
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => { router.push(`/goods/${id}`) }

onMounted(() => {
  fetchGoodsList()
})
</script>

<style scoped>
.home-page { padding: 0; }

/* 英雄区域 - 高度缩小50%，背景图片更清晰，纯色透明度降低 */
.hero-section { 
  position: relative;
  border-radius: 30px; 
  margin-bottom: 40px; 
  padding: 30px 40px;
  overflow: hidden;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.hero-bg-image {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url('https://images.unsplash.com/photo-1458560871784-56d23406c091?q=80&w=2070&auto=format&fit=crop');
  background-size: cover;
  background-position: center;
  z-index: 0;
}
/* 纯色覆盖层 - 透明度降低，让背景图片更清晰 */
.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102,126,234,0.3) 0%, rgba(118,75,162,0.3) 100%);
  z-index: 1;
}
.hero-content { 
  position: relative; 
  z-index: 2; 
  text-align: center;
}
.art-title { display: flex; justify-content: center; gap: 12px; margin-bottom: 12px; }
.art-char { 
  font-size: 56px; 
  font-weight: bold; 
  color: white; 
  text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
  animation: bounceIn 0.5s ease-out forwards; 
  opacity: 0; 
  transform: translateY(20px); 
}
@keyframes bounceIn { 
  0% { opacity: 0; transform: translateY(20px); } 
  100% { opacity: 1; transform: translateY(0); } 
}
.hero-subtitle { 
  font-size: 16px; 
  color: rgba(255,255,255,0.95); 
  margin-bottom: 16px;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
}
.hero-decoration { display: flex; justify-content: center; align-items: center; gap: 15px; }
.deco-line { width: 50px; height: 1px; background: rgba(255,255,255,0.6); }
.deco-diamond { font-size: 18px; color: white; animation: rotate 4s linear infinite; }
@keyframes rotate { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }

.section { margin-bottom: 50px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
.section-title { font-size: 28px; display: flex; align-items: center; gap: 10px; }
.title-icon { font-size: 28px; }
.more-link { color: #667eea; text-decoration: none; font-size: 14px; }
.goods-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 25px; }
.goods-card { background: white; border-radius: 20px; overflow: hidden; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.goods-card:hover { transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0,0,0,0.15); }
.card-image { position: relative; height: 240px; overflow: hidden; }
.card-image img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.3s; }
.goods-card:hover .card-image img { transform: scale(1.05); }
.card-badge { position: absolute; top: 12px; left: 12px; background: #ff6b6b; color: white; padding: 4px 12px; border-radius: 20px; font-size: 12px; }
.card-badge.hot { background: #ff9800; }
.card-info { padding: 16px; }
.card-info h3 { font-size: 18px; margin-bottom: 8px; }
.card-desc { font-size: 13px; color: #999; margin-bottom: 12px; }
.card-price { display: flex; align-items: baseline; gap: 10px; margin-bottom: 12px; }
.price { font-size: 22px; font-weight: bold; color: #ff5757; }
.original-price { font-size: 14px; color: #999; text-decoration: line-through; }
.card-footer { display: flex; justify-content: space-between; font-size: 12px; color: #999; }
.loading, .empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>