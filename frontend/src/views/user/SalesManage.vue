<template>
  <div class="sales-manage">
    <h3>销售管理</h3>
    
    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-value">¥{{ stats.totalSales }}</div>
        <div class="stat-label">总销售额</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.totalOrders }}</div>
        <div class="stat-label">总订单</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.pendingOrders }}</div>
        <div class="stat-label">待处理订单</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ stats.totalGoods }}</div>
        <div class="stat-label">在售商品</div>
      </div>
    </div>

    <div class="order-tabs">
      <button 
        v-for="t in tabs" 
        :key="t.value" 
        :class="{ active: activeTab === t.value }" 
        @click="activeTab = t.value"
      >
        {{ t.label }} ({{ getCount(t.value) }})
      </button>
    </div>

    <div class="order-list">
      <div v-for="order in filteredOrders" :key="order.id" class="order-card">
        <div class="order-header">
          <span>订单号：{{ order.no }}</span>
          <span>买家：{{ order.buyer }}</span>
          <span>金额：¥{{ order.amount }}</span>
          <span :class="['status', order.status]">{{ getStatusText(order.status) }}</span>
        </div>
        
        <div class="order-goods">
          <img :src="order.image" alt="">
          <div class="order-goods-info">
            <h4>{{ order.name }}</h4>
            <p>数量：{{ order.quantity }} | 单价：¥{{ order.price }}</p>
          </div>
        </div>
        
        <div class="order-footer">
          <div class="address">收货地址：{{ order.address }}</div>
          <div class="order-actions">
            <button v-if="order.status === 'paid'" @click="shipOrder(order)" class="ship-btn">确认发货</button>
            <button v-if="order.status === 'pending'" @click="remindPayment(order)" class="remind-btn">提醒支付</button>
          </div>
        </div>
      </div>
      
      <div v-if="filteredOrders.length === 0" class="empty">暂无订单</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const activeTab = ref('all')

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待支付', value: 'pending' },
  { label: '已支付', value: 'paid' },
  { label: '已发货', value: 'shipped' },
  { label: '已完成', value: 'completed' }
]

const stats = ref({
  totalSales: 8940,
  totalOrders: 45,
  pendingOrders: 3,
  totalGoods: 12
})

const orders = ref([
  { 
    id: 1, 
    no: 'HM202412010001', 
    buyer: '张三', 
    name: '青花瓷茶杯', 
    price: 198, 
    quantity: 1, 
    amount: 198, 
    status: 'paid', 
    time: '2024-12-01',
    image: 'https://picsum.photos/id/30/60/60', 
    address: '北京市朝阳区xxx街道xxx号'
  },
  { 
    id: 2, 
    no: 'HM202412010002', 
    buyer: '李四', 
    name: '手工编织包', 
    price: 299, 
    quantity: 1, 
    amount: 299, 
    status: 'pending', 
    time: '2024-12-01',
    image: 'https://picsum.photos/id/31/60/60', 
    address: '上海市浦东新区xxx路xxx号'
  },
  { 
    id: 3, 
    no: 'HM202411300003', 
    buyer: '王五', 
    name: '黄杨木雕', 
    price: 399, 
    quantity: 1, 
    amount: 399, 
    status: 'shipped', 
    time: '2024-11-30',
    image: 'https://picsum.photos/id/32/60/60', 
    address: '广州市天河区xxx街xxx号'
  }
])

const getCount = (status) => {
  if (status === 'all') return orders.value.length
  return orders.value.filter(o => o.status === status).length
}

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(o => o.status === activeTab.value)
})

const getStatusText = (status) => {
  const map = { pending: '待支付', paid: '已支付', shipped: '已发货', completed: '已完成' }
  return map[status] || status
}

const shipOrder = (order) => {
  if (confirm(`确认发货订单 ${order.no} 吗？`)) {
    order.status = 'shipped'
    alert('发货成功')
  }
}

const remindPayment = (order) => {
  alert(`已向买家发送支付提醒`)
}
</script>

<style scoped>
.sales-manage { padding: 0; }
h3 { font-size: 20px; margin-bottom: 20px; }

.stats-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; margin-bottom: 25px; }
.stat-card { background: white; border-radius: 16px; padding: 20px; text-align: center; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.stat-value { font-size: 28px; font-weight: bold; color: #667eea; }
.stat-label { font-size: 13px; color: #999; margin-top: 5px; }

.order-tabs { display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 20px; }
.order-tabs button { background: #f3f4f6; padding: 8px 20px; border-radius: 25px; font-size: 14px; cursor: pointer; }
.order-tabs button.active { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }

.order-list { display: flex; flex-direction: column; gap: 15px; }
.order-card { background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.order-header { display: flex; gap: 20px; padding: 12px 20px; background: #fafafa; border-bottom: 1px solid #f0f0f0; font-size: 14px; flex-wrap: wrap; }
.status { padding: 2px 10px; border-radius: 20px; font-size: 12px; }
.status.pending { background: #fef3c7; color: #d97706; }
.status.paid { background: #d1fae5; color: #065f46; }
.status.shipped { background: #e0e7ff; color: #4338ca; }
.status.completed { background: #d1fae5; color: #065f46; }

.order-goods { display: flex; gap: 15px; padding: 15px 20px; }
.order-goods img { width: 60px; height: 60px; border-radius: 8px; object-fit: cover; }
.order-goods-info h4 { font-size: 16px; margin-bottom: 5px; }
.order-goods-info p { font-size: 13px; color: #999; }

.order-footer { display: flex; justify-content: space-between; align-items: center; padding: 12px 20px; background: #fafafa; border-top: 1px solid #f0f0f0; flex-wrap: wrap; gap: 10px; }
.address { font-size: 13px; color: #666; }
.order-actions { display: flex; gap: 10px; }
.ship-btn { background: #10b981; color: white; padding: 6px 16px; font-size: 13px; }
.remind-btn { background: #f59e0b; color: white; padding: 6px 16px; font-size: 13px; }

.empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>