<template>
  <div class="admin-goods-list">
    <h2>商品管理</h2>
    <div class="goods-table"><table><thead><tr><th>ID</th><th>商品名称</th><th>价格</th><th>创作者</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="goods in goodsList" :key="goods.id"><td>{{ goods.id }}</td><td>{{ goods.name }}</td><td>¥{{ goods.price }}</td><td>{{ goods.creator }}</td><td><span :class="['status', goods.status]">{{ goods.status === 'approved' ? '已上架' : '已下架' }}</span></td><td><button class="off-btn" @click="offShelf(goods)">下架</button></td></tr></tbody></table></div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const goodsList = ref([
  { id: 1, name: '青花瓷茶杯', price: 198, creator: '陶瓷匠人', status: 'approved' },
  { id: 2, name: '手工编织包', price: 299, creator: '编织达人', status: 'approved' }
])

const offShelf = (goods) => { goods.status = 'offline'; alert('已下架') }
</script>

<style scoped>
.admin-goods-list { padding: 0; }
h2 { font-size: 22px; margin-bottom: 20px; }
.goods-table { background: white; border-radius: 16px; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #f0f0f0; }
th { background: #fafafa; }
.status { padding: 4px 10px; border-radius: 20px; font-size: 12px; }
.status.approved { background: #d1fae5; color: #065f46; }
.status.offline { background: #fee2e2; color: #991b1b; }
.off-btn { background: #ef4444; color: white; padding: 5px 12px; font-size: 12px; }
</style>