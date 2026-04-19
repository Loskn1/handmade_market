<template>
  <div class="admin-statistics">
    <h2>数据统计</h2>
    <div class="stats-cards"><div class="stat"><div class="value">{{ stats.userCount }}</div><div class="label">注册用户</div></div><div class="stat"><div class="value">{{ stats.orderCount }}</div><div class="label">总订单</div></div><div class="stat"><div class="value">{{ stats.goodsCount }}</div><div class="label">商品总数</div></div><div class="stat"><div class="value">{{ stats.commentCount }}</div><div class="label">评价总数</div></div></div>
    <div class="chart-section"><h3>近7日订单趋势</h3><div class="chart"><div v-for="(d,i) in weeklyOrders" :key="i" class="bar-wrapper"><div class="bar" :style="{ height: (d.count / maxCount) * 150 + 'px' }"></div><div class="label">{{ d.date }}</div><div class="count">{{ d.count }}</div></div></div></div>
    <div class="export"><button @click="exportData" class="export-btn">导出数据报表</button></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const stats = ref({ userCount: 12580, orderCount: 894, goodsCount: 2340, commentCount: 567 })
const weeklyOrders = ref([{ date: '12/1', count: 23 },{ date: '12/2', count: 45 },{ date: '12/3', count: 38 },{ date: '12/4', count: 67 },{ date: '12/5', count: 52 },{ date: '12/6', count: 89 },{ date: '12/7', count: 76 }])
const maxCount = 100
const exportData = () => { alert('导出功能开发中') }
</script>

<style scoped>
.admin-statistics { padding: 0; }
h2 { font-size: 22px; margin-bottom: 20px; }
.stats-cards { display: grid; grid-template-columns: repeat(4,1fr); gap: 20px; margin-bottom: 30px; }
.stat { background: white; border-radius: 16px; padding: 25px; text-align: center; }
.stat .value { font-size: 32px; font-weight: bold; color: #667eea; }
.stat .label { font-size: 14px; color: #999; margin-top: 8px; }
.chart-section { background: white; border-radius: 16px; padding: 20px; margin-bottom: 20px; }
.chart-section h3 { font-size: 18px; margin-bottom: 20px; }
.chart { display: flex; justify-content: space-around; align-items: flex-end; height: 220px; }
.bar-wrapper { text-align: center; width: 60px; }
.bar { width: 40px; background: linear-gradient(180deg, #667eea, #764ba2); border-radius: 8px 8px 0 0; transition: height 0.3s; margin: 0 auto; }
.label { font-size: 12px; color: #999; margin-top: 8px; }
.count { font-size: 12px; color: #666; margin-top: 4px; }
.export-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 12px 24px; }
</style>