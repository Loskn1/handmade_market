<template>
  <div class="search-page">
    <div class="search-header">
      <h2>搜索结果</h2>
      <p>共找到 {{ totalResults }} 件商品</p>
    </div>

    <div class="search-filters">
      <div class="filter-group">
        <span class="filter-label">价格：</span>
        <div class="price-filter">
          <input v-model.number="priceMin" type="number" placeholder="最低价">
          <span>-</span>
          <input v-model.number="priceMax" type="number" placeholder="最高价">
          <button @click="applyFilter">确定</button>
        </div>
      </div>
      <div class="filter-group">
        <span class="filter-label">排序：</span>
        <select v-model="sortBy">
          <option value="default">默认排序</option>
          <option value="price_asc">价格升序</option>
          <option value="price_desc">价格降序</option>
          <option value="sales">销量优先</option>
        </select>
      </div>
      <button @click="resetFilter" class="reset-btn">重置筛选</button>
    </div>

    <div class="goods-grid" v-if="loading">
      <div class="loading">加载中...</div>
    </div>
    <div class="goods-grid" v-else-if="goodsList.length > 0">
      <div class="goods-card" v-for="item in goodsList" :key="item.id" @click="goToDetail(item.id)">
        <div class="card-image"><img :src="item.imageUrl" :alt="item.name"></div>
        <div class="card-info">
          <h3>{{ item.name }}</h3>
          <p class="card-desc">{{ item.shortDesc }}</p>
          <div class="card-price"><span class="price">¥{{ item.price }}</span><span class="original-price" v-if="item.originalPrice">¥{{ item.originalPrice }}</span></div>
          <div class="card-footer"><span class="sales">❤️ {{ item.sales }}人购买</span><span class="seller">卖家：{{ item.sellerName }}</span></div>
        </div>
      </div>
    </div>
    <div class="empty" v-else>未找到相关商品</div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const goodsList = ref([])
const totalResults = ref(0)
const priceMin = ref(null)
const priceMax = ref(null)
const sortBy = ref('default')
const keyword = ref('')

const fetchSearchResults = async () => {
  loading.value = true
  try {
    const params = {
      keyword: keyword.value,
      minPrice: priceMin.value,
      maxPrice: priceMax.value,
      sort: sortBy.value
    }
    const response = await axios.get('/api/search', { params })
    if (response.data.code === 200) {
      goodsList.value = response.data.data.list
      totalResults.value = response.data.data.total
    }
  } catch (err) {
    console.error('搜索失败', err)
    goodsList.value = []
    totalResults.value = 0
  } finally {
    loading.value = false
  }
}

const applyFilter = () => { fetchSearchResults() }
const resetFilter = () => { priceMin.value = null; priceMax.value = null; sortBy.value = 'default'; fetchSearchResults() }
const goToDetail = (id) => { router.push(`/goods/${id}`) }

watch([priceMin, priceMax, sortBy], () => { fetchSearchResults() })
onMounted(() => {
  keyword.value = route.query.keyword || ''
  fetchSearchResults()
})
</script>

<style scoped>
.search-page { padding: 0; }
.search-header { margin-bottom: 25px; }
.search-header h2 { font-size: 24px; margin-bottom: 8px; }
.search-header p { color: #999; }
.search-filters { background: white; border-radius: 16px; padding: 20px; margin-bottom: 25px; display: flex; flex-wrap: wrap; gap: 20px; align-items: center; }
.filter-group { display: flex; align-items: center; gap: 10px; }
.filter-label { font-size: 14px; color: #666; }
.price-filter { display: flex; align-items: center; gap: 8px; }
.price-filter input { width: 100px; padding: 6px 10px; border: 1px solid #e5e7eb; border-radius: 6px; }
.price-filter button { padding: 6px 12px; background: #667eea; color: white; font-size: 12px; }
select { padding: 6px 12px; border: 1px solid #e5e7eb; border-radius: 6px; }
.reset-btn { background: #f3f4f6; color: #666; padding: 6px 16px; font-size: 13px; }
.goods-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 25px; }
.goods-card { background: white; border-radius: 20px; overflow: hidden; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.goods-card:hover { transform: translateY(-8px); box-shadow: 0 20px 40px rgba(0,0,0,0.15); }
.card-image { height: 240px; overflow: hidden; }
.card-image img { width: 100%; height: 100%; object-fit: cover; }
.card-info { padding: 16px; }
.card-info h3 { font-size: 18px; margin-bottom: 8px; }
.card-desc { font-size: 13px; color: #999; margin-bottom: 12px; }
.card-price { display: flex; align-items: baseline; gap: 10px; margin-bottom: 12px; }
.price { font-size: 22px; font-weight: bold; color: #ff5757; }
.original-price { font-size: 14px; color: #999; text-decoration: line-through; }
.card-footer { display: flex; justify-content: space-between; font-size: 12px; color: #999; }
.loading, .empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>