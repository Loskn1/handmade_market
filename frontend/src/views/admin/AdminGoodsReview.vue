<template>
  <div class="admin-goods-review">
    <div class="review-header">
      <h2>商品审核</h2>
      <div class="filters">
        <select v-model="filterStatus">
          <option value="pending">待审核</option>
          <option value="all">全部</option>
        </select>
      </div>
    </div>

    <div class="review-list">
      <div v-for="goods in pendingGoods" :key="goods.id" class="review-card">
        <div class="goods-preview">
          <img :src="goods.imageUrl" class="goods-img">
          <div class="goods-detail">
            <h3>{{ goods.name }}</h3>
            <p>价格：¥{{ goods.price }}</p>
            <p>分类：{{ goods.category }}</p>
            <p>创作者：{{ goods.creatorName }}</p>
            <p>提交时间：{{ goods.submitTime }}</p>
          </div>
        </div>

        <div class="goods-description">
          <h4>商品描述</h4>
          <p>{{ goods.description }}</p>
        </div>

        <div class="goods-images" v-if="goods.images && goods.images.length">
          <h4>商品图片</h4>
          <div class="image-list">
            <img v-for="(img, idx) in goods.images" :key="idx" :src="img" @click="previewImage(img)">
          </div>
        </div>

        <div class="review-actions">
          <div class="reject-form">
            <textarea v-model="goods.rejectReason" placeholder="驳回原因（必填）" rows="2"></textarea>
          </div>
          <div class="action-buttons">
            <button class="reject-btn" @click="rejectGoods(goods)">驳回</button>
            <button class="approve-btn" @click="approveGoods(goods)">通过审核</button>
          </div>
        </div>
      </div>

      <div v-if="pendingGoods.length === 0" class="empty-state">
        <p>暂无待审核商品</p>
      </div>
    </div>

    <!-- 图片预览模态框 -->
    <div v-if="previewUrl" class="preview-modal" @click="previewUrl = null">
      <img :src="previewUrl" @click.stop>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const filterStatus = ref('pending')
const previewUrl = ref(null)

const pendingGoods = ref([
  {
    id: 1,
    name: '手工陶瓷杯',
    price: 198,
    category: '陶瓷',
    creatorName: '陶瓷匠人',
    submitTime: '2024-12-01 10:30',
    description: '纯手工制作，釉色温润，每一件都是独一无二的艺术品。采用传统工艺，1300度高温烧制。',
    images: [
      'https://picsum.photos/id/30/200/200',
      'https://picsum.photos/id/31/200/200'
    ],
    rejectReason: ''
  },
  {
    id: 2,
    name: '手工编织包',
    price: 299,
    category: '编织',
    creatorName: '编织达人',
    submitTime: '2024-12-01 14:20',
    description: '手工编织，独特设计，时尚百搭。',
    images: [
      'https://picsum.photos/id/32/200/200'
    ],
    rejectReason: ''
  }
])

const approveGoods = (goods) => {
  if (confirm(`确定通过「${goods.name}」的审核吗？`)) {
    const index = pendingGoods.value.findIndex(g => g.id === goods.id)
    pendingGoods.value.splice(index, 1)
    alert('审核通过，商品已上架')
  }
}

const rejectGoods = (goods) => {
  if (!goods.rejectReason.trim()) {
    alert('请填写驳回原因')
    return
  }
  if (confirm(`确定驳回「${goods.name}」吗？`)) {
    const index = pendingGoods.value.findIndex(g => g.id === goods.id)
    pendingGoods.value.splice(index, 1)
    alert(`已驳回商品，原因：${goods.rejectReason}`)
  }
}

const previewImage = (url) => {
  previewUrl.value = url
}
</script>

<style scoped>
.admin-goods-review {
  padding: 0;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.review-header h2 {
  font-size: 22px;
  color: #333;
}

.filters select {
  padding: 8px 15px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.review-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.goods-preview {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.goods-img {
  width: 120px;
  height: 120px;
  border-radius: 12px;
  object-fit: cover;
}

.goods-detail h3 {
  font-size: 18px;
  margin-bottom: 8px;
}

.goods-detail p {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.goods-description {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.goods-description h4 {
  font-size: 14px;
  margin-bottom: 10px;
  color: #666;
}

.goods-description p {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
}

.goods-images {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.goods-images h4 {
  font-size: 14px;
  margin-bottom: 10px;
  color: #666;
}

.image-list {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.image-list img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
}

.review-actions {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.reject-form {
  flex: 1;
}

.reject-form textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  font-size: 13px;
  resize: vertical;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.reject-btn {
  background: #ff5757;
  color: white;
  padding: 10px 25px;
}

.approve-btn {
  background: #10b981;
  color: white;
  padding: 10px 25px;
}

.empty-state {
  text-align: center;
  padding: 60px;
  background: white;
  border-radius: 16px;
  color: #999;
}

.preview-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.preview-modal img {
  max-width: 90%;
  max-height: 90%;
  border-radius: 8px;
}
</style>