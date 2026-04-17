<template>
  <div class="admin-dashboard">
    <h2>数据总览</h2>
    
    <!-- 核心指标卡片 -->
    <div class="stats-grid">
      <div class="stat-card"><div class="stat-icon">👥</div><div><div class="stat-value">{{ stats.totalUsers }}</div><div class="stat-label">总用户数</div><div class="stat-trend">+{{ stats.newUsers }} 今日新增</div></div></div>
      <div class="stat-card"><div class="stat-icon">🎨</div><div><div class="stat-value">{{ stats.totalCreators }}</div><div class="stat-label">创作者数</div><div class="stat-trend">+{{ stats.newCreators }} 本周新增</div></div></div>
      <div class="stat-card"><div class="stat-icon">📦</div><div><div class="stat-value">{{ stats.totalGoods }}</div><div class="stat-label">商品总数</div><div class="stat-trend">{{ stats.pendingGoods }} 待审核</div></div></div>
      <div class="stat-card"><div class="stat-icon">💰</div><div><div class="stat-value">¥{{ stats.totalSales }}</div><div class="stat-label">总成交额</div><div class="stat-trend">+¥{{ stats.todaySales }} 今日</div></div></div>
    </div>

    <!-- 近7日订单趋势 -->
    <div class="section">
      <h3>近7日订单趋势</h3>
      <div class="chart">
        <div v-for="(d, idx) in weeklyOrders" :key="idx" class="bar-wrapper">
          <div class="bar" :style="{ height: (d.count / maxCount) * 150 + 'px' }"></div>
          <div class="label">{{ d.date }}</div>
          <div class="count">{{ d.count }}</div>
        </div>
      </div>
    </div>

    <!-- 商品分类统计 -->
    <div class="section">
      <h3>商品分类统计</h3>
      <div v-for="cat in categoryStats" :key="cat.name" class="category-item">
        <span class="cat-name">{{ cat.name }}</span>
        <div class="cat-bar"><div class="cat-fill" :style="{ width: (cat.count / maxCategory) * 100 + '%' }"></div></div>
        <span class="cat-count">{{ cat.count }}</span>
      </div>
    </div>

    <button @click="exportData" class="export-btn">导出数据报表</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const stats = ref({ totalUsers: 0, newUsers: 0, totalCreators: 0, newCreators: 0, totalGoods: 0, pendingGoods: 0, totalSales: 0, todaySales: 0 })
const weeklyOrders = ref([])
const categoryStats = ref([])
const maxCount = ref(100)
const maxCategory = ref(100)

const fetchDashboardData = async () => {
  try {
    const response = await axios.get('/api/admin/statistics')
    if (response.data.code === 200) {
      stats.value = response.data.data.stats
      weeklyOrders.value = response.data.data.weeklyOrders
      categoryStats.value = response.data.data.categoryStats
      maxCount.value = Math.max(...weeklyOrders.value.map(w => w.count), 100)
      maxCategory.value = Math.max(...categoryStats.value.map(c => c.count), 100)
    }
  } catch (err) {
    console.error('获取数据失败', err)
  }
}

const exportData = () => { alert('导出功能开发中') }

onMounted(() => { fetchDashboardData() })
</script>

<style scoped>
.admin-dashboard { padding: 0; }
h2 { font-size: 24px; margin-bottom: 20px; }
.stats-grid { display: grid; grid-template-columns: repeat(4,1fr); gap: 20px; margin-bottom: 30px; }
.stat-card { background: white; border-radius: 16px; padding: 20px; display: flex; align-items: center; gap: 15px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.stat-icon { font-size: 42px; }
.stat-value { font-size: 28px; font-weight: bold; color: #333; }
.stat-label { font-size: 13px; color: #999; margin-top: 5px; }
.stat-trend { font-size: 12px; margin-top: 5px; color: #10b981; }
.section { background: white; border-radius: 16px; padding: 20px; margin-bottom: 20px; }
.section h3 { font-size: 18px; margin-bottom: 20px; }
.chart { display: flex; justify-content: space-around; align-items: flex-end; height: 220px; }
.bar-wrapper { text-align: center; width: 60px; }
.bar { width: 40px; background: linear-gradient(180deg, #667eea, #764ba2); border-radius: 8px 8px 0 0; margin: 0 auto; transition: height 0.3s; }
.label { font-size: 12px; color: #999; margin-top: 8px; }
.count { font-size: 12px; color: #666; margin-top: 4px; }
.category-item { display: flex; align-items: center; gap: 15px; margin-bottom: 15px; }
.cat-name { width: 60px; font-size: 14px; }
.cat-bar { flex: 1; height: 24px; background: #f3f4f6; border-radius: 12px; overflow: hidden; }
.cat-fill { height: 100%; background: linear-gradient(90deg, #667eea, #764ba2); border-radius: 12px; transition: width 0.3s; }
.cat-count { width: 50px; text-align: right; color: #666; }
.export-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 12px 24px; margin-top: 10px; }
</style>