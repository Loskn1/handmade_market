<template>
  <div class="my-goods">
    <div class="goods-header"><h3>我的商品</h3><button class="publish-btn" @click="$router.push('/user/publish')">+ 发布商品</button></div>

    <div class="goods-tabs"><button v-for="t in tabs" :key="t.value" :class="{ active: activeTab === t.value }" @click="activeTab = t.value">{{ t.label }} ({{ getCount(t.value) }})</button></div>

    <div class="goods-list">
      <div v-for="goods in filteredGoods" :key="goods.id" class="goods-card">
        <img :src="goods.imageUrl" class="goods-img">
        <div class="goods-info"><h4>{{ goods.name }}</h4><p class="price">¥{{ goods.price }}</p><p class="stock">库存：{{ goods.stock }}</p><span :class="['status', goods.status]">{{ getStatusText(goods.status) }}</span><p v-if="goods.rejectReason" class="reject-reason">驳回原因：{{ goods.rejectReason }}</p></div>
        <div class="goods-actions"><button v-if="goods.status === 'pending'" class="edit-btn" disabled>审核中</button><button v-if="goods.status === 'approved'" class="off-btn" @click="offShelf(goods)">下架</button><button v-if="goods.status === 'rejected'" class="edit-btn" @click="editGoods(goods)">修改</button><button v-if="goods.status === 'offline'" class="edit-btn" @click="editGoods(goods)">编辑</button></div>
      </div>
      <div v-if="filteredGoods.length === 0" class="empty">暂无商品，点击发布按钮上架商品</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const activeTab = ref('all')
const tabs = [{ label: '全部', value: 'all' }, { label: '审核中', value: 'pending' }, { label: '已上架', value: 'approved' }, { label: '已下架', value: 'offline' }, { label: '审核未通过', value: 'rejected' }]

const goodsList = ref([
  { id: 1, name: '青花瓷茶杯', price: 198, stock: 50, status: 'approved', imageUrl: 'https://picsum.photos/id/30/100/100' },
  { id: 2, name: '手工编织包', price: 299, stock: 30, status: 'pending', imageUrl: 'https://picsum.photos/id/31/100/100' },
  { id: 3, name: '黄杨木雕', price: 399, stock: 20, status: 'rejected', rejectReason: '图片不够清晰', imageUrl: 'https://picsum.photos/id/32/100/100' }
])

const getCount = (status) => status === 'all' ? goodsList.value.length : goodsList.value.filter(g => g.status === status).length
const filteredGoods = computed(() => activeTab.value === 'all' ? goodsList.value : goodsList.value.filter(g => g.status === activeTab.value))
const getStatusText = (s) => ({ pending: '审核中', approved: '已上架', offline: '已下架', rejected: '审核未通过' }[s])

const offShelf = (goods) => { goods.status = 'offline'; alert('商品已下架') }
const editGoods = (goods) => { alert(`编辑商品 ${goods.name}`) }
</script>

<style scoped>
.my-goods { padding: 0; }
.goods-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.goods-header h3 { font-size: 20px; }
.publish-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 24px; }
.goods-tabs { display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 20px; }
.goods-tabs button { background: #f3f4f6; padding: 8px 20px; border-radius: 25px; font-size: 14px; }
.goods-tabs button.active { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
.goods-list { display: flex; flex-direction: column; gap: 15px; }
.goods-card { background: white; border-radius: 16px; padding: 15px; display: flex; gap: 20px; align-items: center; }
.goods-img { width: 100px; height: 100px; border-radius: 12px; object-fit: cover; }
.goods-info { flex: 1; }
.goods-info h4 { font-size: 16px; margin-bottom: 8px; }
.goods-info .price { font-size: 20px; font-weight: bold; color: #ff5757; }
.goods-info .stock { font-size: 13px; color: #999; }
.status { display: inline-block; padding: 2px 10px; border-radius: 12px; font-size: 12px; margin-top: 8px; }
.status.pending { background: #fef3c7; color: #d97706; }
.status.approved { background: #d1fae5; color: #065f46; }
.status.offline { background: #fee2e2; color: #991b1b; }
.status.rejected { background: #fee2e2; color: #991b1b; }
.reject-reason { font-size: 12px; color: #ef4444; margin-top: 5px; }
.goods-actions button { padding: 6px 16px; font-size: 12px; }
.edit-btn { background: #f3f4f6; color: #666; }
.off-btn { background: #ef4444; color: white; }
.empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>