<template>
  <div class="my-goods">
    <div class="goods-header"><h3>我的商品</h3><button class="publish-btn" @click="$router.push('/user/publish')">+ 发布商品</button></div>

    <div class="goods-tabs"><button v-for="t in tabs" :key="t.value" :class="{ active: activeTab === t.value }" @click="activeTab = t.value">{{ t.label }} ({{ getCount(t.value) }})</button></div>

    <div class="goods-list">
      <div v-for="goods in filteredGoods" :key="goods.id" class="goods-card">
        <img :src="goods.imageUrls && goods.imageUrls[0] ? goods.imageUrls[0] : 'https://via.placeholder.com/100'" class="goods-img">
        <div class="goods-info"><h4>{{ goods.title }}</h4><p class="price">¥{{ goods.price }}</p><p class="stock">库存：{{ goods.stock }}</p><span :class="['status', mapStatus(goods.auditStatus, goods.shelfStatus)]">{{ getStatusText(goods.auditStatus, goods.shelfStatus) }}</span><p v-if="goods.rejectReason" class="reject-reason">驳回原因：{{ goods.rejectReason }}</p></div>
        <div class="goods-actions"><button v-if="goods.auditStatus === 'PENDING'" class="edit-btn" disabled>审核中</button><button v-if="goods.auditStatus === 'APPROVED' && goods.shelfStatus === 'ON_SHELF'" class="off-btn" @click="offShelf(goods)">下架</button><button v-if="goods.auditStatus === 'REJECTED'" class="edit-btn" @click="editGoods(goods)">修改</button><button v-if="goods.shelfStatus === 'OFF_SHELF'" class="edit-btn" @click="editGoods(goods)">编辑</button></div>
      </div>
      <div v-if="filteredGoods.length === 0" class="empty">暂无商品，点击发布按钮上架商品</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const activeTab = ref('all')
const tabs = [{ label: '全部', value: 'all' }, { label: '审核中', value: 'pending' }, { label: '已上架', value: 'approved' }, { label: '已下架', value: 'offline' }, { label: '审核未通过', value: 'rejected' }]

const goodsList = ref([])

const getCount = (status) => {
  if (status === 'all') return goodsList.value.length
  return goodsList.value.filter(g => mapStatus(g.auditStatus, g.shelfStatus) === status).length
}

const filteredGoods = computed(() => {
  if (activeTab.value === 'all') return goodsList.value
  return goodsList.value.filter(g => mapStatus(g.auditStatus, g.shelfStatus) === activeTab.value)
})

const mapStatus = (auditStatus, shelfStatus) => {
  if (auditStatus === 'PENDING') return 'pending'
  if (auditStatus === 'REJECTED') return 'rejected'
  if (shelfStatus === 'ON_SHELF') return 'approved'
  return 'offline'
}

const getStatusText = (auditStatus, shelfStatus) => {
  if (auditStatus === 'PENDING') return '审核中'
  if (auditStatus === 'REJECTED') return '审核未通过'
  if (shelfStatus === 'ON_SHELF') return '已上架'
  return '已下架'
}

const offShelf = async (goods) => {
  try {
    const userId = localStorage.getItem('userId') || 1
    await axios.delete(`http://localhost:8080/api/goods/${goods.id}/off-shelf`, {
      headers: { 'X-User-Id': userId }
    })
    goods.shelfStatus = 'OFF_SHELF'
    alert('商品已下架')
  } catch (error) {
    alert('下架失败：' + (error.response?.data?.message || error.message))
  }
}

const editGoods = (goods) => {
  alert(`编辑商品 ${goods.title}`)
}

const loadGoods = async () => {
  try {
    const userId = localStorage.getItem('userId') || 1
    const response = await axios.get(`http://localhost:8080/api/goods/seller/${userId}`)
    if (response.data.success) {
      goodsList.value = response.data.data || []
    }
  } catch (error) {
    console.error('加载商品失败:', error)
  }
}

onMounted(() => {
  loadGoods()
})
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