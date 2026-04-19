<template>
  <div class="categories-page">
    <h2>商品分类</h2>
    <p class="subtitle">按分类浏览手工好物</p>

    <div class="categories-grid" v-if="loading">
      <div class="loading">加载中...</div>
    </div>
    <div class="categories-grid" v-else-if="categories.length > 0">
      <div class="category-card" v-for="cat in categories" :key="cat.id" @click="goToCategory(cat.id)">
        <div class="category-icon">{{ cat.icon }}</div>
        <div class="category-name">{{ cat.name }}</div>
        <div class="category-count">{{ cat.count }}件商品</div>
      </div>
    </div>
    <div class="empty" v-else>暂无分类</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const loading = ref(true)
const categories = ref([])

const fetchCategories = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/categories')
    if (response.data.code === 200) {
      categories.value = response.data.data
    }
  } catch (err) {
    console.error('获取分类失败', err)
    categories.value = []
  } finally {
    loading.value = false
  }
}

const goToCategory = (id) => { router.push({ path: '/search', query: { category: id } }) }

onMounted(() => { fetchCategories() })
</script>

<style scoped>
.categories-page { padding: 0; }
h2 { font-size: 24px; margin-bottom: 8px; }
.subtitle { color: #999; margin-bottom: 30px; }
.categories-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 25px; }
.category-card { background: white; padding: 30px 20px; text-align: center; border-radius: 20px; cursor: pointer; transition: all 0.3s ease; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.category-card:hover { transform: translateY(-5px); box-shadow: 0 10px 25px rgba(0,0,0,0.1); }
.category-icon { font-size: 48px; margin-bottom: 15px; }
.category-name { font-size: 18px; font-weight: 500; color: #333; margin-bottom: 8px; }
.category-count { font-size: 13px; color: #999; }
.loading, .empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>