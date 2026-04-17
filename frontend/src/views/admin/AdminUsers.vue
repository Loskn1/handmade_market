<template>
  <div class="admin-users">
    <h2>用户管理</h2>
    <div class="filters"><input v-model="searchKeyword" placeholder="搜索用户"><select v-model="filterRole"><option value="all">全部角色</option><option value="consumer">消费者</option><option value="creator">创作者</option></select><select v-model="filterStatus"><option value="all">全部状态</option><option value="active">启用</option><option value="disabled">禁用</option></select><button class="export" @click="exportUsers">导出</button></div>
    <div class="user-table"><table><thead><tr><th>ID</th><th>账号</th><th>角色</th><th>信用分</th><th>注册时间</th><th>状态</th><th>操作</th></tr></thead><tbody><tr v-for="user in filteredUsers" :key="user.id"><td>{{ user.id }}</td><td>{{ user.account }}</td><td>{{ user.role === 'consumer' ? '消费者' : '创作者' }}</td><td :class="getCreditClass(user.creditScore)">{{ user.creditScore }}</td><td>{{ user.registerTime }}</td><td><span :class="['status', user.status]">{{ user.status === 'active' ? '启用' : '禁用' }}</span></td><td><button class="view" @click="viewDetail(user)">详情</button><button v-if="user.status==='active'" class="disable" @click="toggleStatus(user,'disable')">禁用</button><button v-else class="enable" @click="toggleStatus(user,'enable')">启用</button></td></tr></tbody></table></div>
    <div v-if="showModal" class="modal" @click="showModal=false"><div class="modal-content" @click.stop><h3>用户详情</h3><p>账号：{{ currentUser.account }}</p><p>真实姓名：{{ currentUser.realName || '未设置' }}</p><p>手机号：{{ currentUser.phone || '未设置' }}</p><p>信用分：{{ currentUser.creditScore }}</p><p>注册时间：{{ currentUser.registerTime }}</p><div v-if="currentUser.role==='creator'"><p>擅长领域：{{ currentUser.skill || '未设置' }}</p><p>作品数：{{ currentUser.worksCount || 0 }}</p></div><button @click="showModal=false">关闭</button></div></div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const searchKeyword = ref('')
const filterRole = ref('all')
const filterStatus = ref('all')
const showModal = ref(false)
const currentUser = ref({})

const users = ref([
  { id: 1, account: 'zhangsan', role: 'consumer', creditScore: 85, registerTime: '2024-10-01', status: 'active', realName: '张三', phone: '138****0000' },
  { id: 2, account: 'ceramic_master', role: 'creator', creditScore: 95, registerTime: '2024-09-01', status: 'active', realName: '王陶瓷', skill: '陶瓷', worksCount: 48 }
])

const filteredUsers = computed(() => {
  let result = users.value
  if (searchKeyword.value) result = result.filter(u => u.account.includes(searchKeyword.value))
  if (filterRole.value !== 'all') result = result.filter(u => u.role === filterRole.value)
  if (filterStatus.value !== 'all') result = result.filter(u => u.status === filterStatus.value)
  return result
})

const getCreditClass = (score) => { if(score>=90) return 'high'; if(score>=70) return 'medium'; return 'low' }
const viewDetail = (user) => { currentUser.value = user; showModal.value = true }
const toggleStatus = (user, action) => { user.status = action === 'disable' ? 'disabled' : 'active'; alert(action === 'disable' ? '用户已禁用' : '用户已启用') }
const exportUsers = () => { alert('导出功能开发中') }
</script>

<style scoped>
.admin-users { padding: 0; }
h2 { font-size: 22px; margin-bottom: 20px; }
.filters { display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap; }
.filters input, .filters select { padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 8px; }
.export { background: #f3f4f6; color: #666; padding: 8px 16px; }
.user-table { background: white; border-radius: 16px; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #f0f0f0; }
th { background: #fafafa; }
.high { color: #10b981; font-weight: bold; }
.medium { color: #f59e0b; font-weight: bold; }
.low { color: #ef4444; font-weight: bold; }
.status { padding: 4px 10px; border-radius: 20px; font-size: 12px; }
.status.active { background: #d1fae5; color: #065f46; }
.status.disabled { background: #fee2e2; color: #991b1b; }
.view { background: #f3f4f6; color: #666; padding: 5px 10px; font-size: 12px; margin-right: 5px; }
.disable { background: #ef4444; color: white; padding: 5px 10px; font-size: 12px; }
.enable { background: #10b981; color: white; padding: 5px 10px; font-size: 12px; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 16px; padding: 30px; width: 450px; }
.modal-content p { margin: 10px 0; }
.modal-content button { margin-top: 20px; width: 100%; background: #f3f4f6; color: #666; }
</style>