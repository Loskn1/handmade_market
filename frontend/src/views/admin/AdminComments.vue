<template>
  <div class="admin-comments">
    <div class="comments-header">
      <h2>评价管理</h2>
      <div class="filters">
        <select v-model="filterType">
          <option value="pending">待审核</option>
          <option value="reported">被举报</option>
          <option value="all">全部</option>
        </select>
      </div>
    </div>

    <div class="comments-list">
      <div v-for="comment in filteredComments" :key="comment.id" class="comment-card">
        <div class="comment-header">
          <div class="user-info">
            <span class="user-name">{{ comment.userName }}</span>
            <span class="rating">★★★★★ {{ comment.rating }}</span>
          </div>
          <div class="goods-info">
            商品：{{ comment.goodsName }}
          </div>
          <div class="comment-time">{{ comment.createTime }}</div>
        </div>

        <div class="comment-content">
          <p>{{ comment.content }}</p>
          <div class="comment-images" v-if="comment.images && comment.images.length">
            <img v-for="img in comment.images" :key="img" :src="img" @click="previewImage(img)">
          </div>
        </div>

        <div class="comment-footer" v-if="comment.reportReason">
          <div class="report-info">
            <span class="report-icon">⚠️</span>
            <span>举报原因：{{ comment.reportReason }}</span>
          </div>
        </div>

        <div class="review-actions">
          <button v-if="comment.status === 'pending'" class="approve-btn" @click="approveComment(comment)">通过</button>
          <button class="delete-btn" @click="deleteComment(comment)">删除</button>
          <button v-if="comment.status === 'pending'" class="reject-btn" @click="rejectComment(comment)">驳回举报</button>
        </div>
      </div>

      <div v-if="filteredComments.length === 0" class="empty-state">
        <p>暂无评价</p>
      </div>
    </div>

    <!-- 图片预览模态框 -->
    <div v-if="previewUrl" class="preview-modal" @click="previewUrl = null">
      <img :src="previewUrl" @click.stop>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const filterType = ref('pending')
const previewUrl = ref(null)

const comments = ref([
  {
    id: 1,
    userName: '张三',
    goodsName: '青花瓷茶杯',
    rating: 5,
    content: '非常精美的杯子，做工细腻，很喜欢！',
    images: ['https://picsum.photos/id/30/100/100'],
    createTime: '2024-12-01 14:30',
    status: 'pending',
    reportReason: ''
  },
  {
    id: 2,
    userName: '李四',
    goodsName: '手工编织包',
    rating: 2,
    content: '质量一般，和图片有差距',
    images: [],
    createTime: '2024-11-30 10:15',
    status: 'reported',
    reportReason: '恶意差评'
  },
  {
    id: 3,
    userName: '王五',
    goodsName: '黄杨木雕',
    rating: 5,
    content: '非常精美，值得收藏！',
    images: ['https://picsum.photos/id/32/100/100'],
    createTime: '2024-11-29 09:20',
    status: 'approved',
    reportReason: ''
  }
])

const filteredComments = computed(() => {
  if (filterType.value === 'pending') {
    return comments.value.filter(c => c.status === 'pending')
  }
  if (filterType.value === 'reported') {
    return comments.value.filter(c => c.status === 'reported')
  }
  return comments.value
})

const approveComment = (comment) => {
  if (confirm('确定通过该评价吗？')) {
    comment.status = 'approved'
    alert('评价已通过')
  }
}

const deleteComment = (comment) => {
  if (confirm('确定删除该评价吗？')) {
    const index = comments.value.findIndex(c => c.id === comment.id)
    comments.value.splice(index, 1)
    alert('评价已删除，已扣除用户信用分2分')
  }
}

const rejectComment = (comment) => {
  if (confirm('确定驳回举报吗？')) {
    comment.status = 'approved'
    alert('已驳回举报，评价正常展示')
  }
}

const previewImage = (url) => {
  previewUrl.value = url
}
</script>

<style scoped>
.admin-comments {
  padding: 0;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.comments-header h2 {
  font-size: 22px;
  color: #333;
}

.filters select {
  padding: 8px 15px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
  padding-bottom: 15px;
  margin-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.user-name {
  font-weight: bold;
  color: #333;
}

.rating {
  margin-left: 10px;
  color: #ffc107;
}

.goods-info {
  color: #666;
  font-size: 13px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-content p {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 15px;
}

.comment-images {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.comment-images img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
  cursor: pointer;
}

.comment-footer {
  margin-top: 15px;
  padding: 10px;
  background: #fff3cd;
  border-radius: 8px;
}

.report-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #856404;
}

.report-icon {
  font-size: 16px;
}

.review-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.approve-btn {
  background: #10b981;
  color: white;
  padding: 8px 20px;
}

.delete-btn {
  background: #ff5757;
  color: white;
  padding: 8px 20px;
}

.reject-btn {
  background: #f3f4f6;
  color: #666;
  padding: 8px 20px;
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