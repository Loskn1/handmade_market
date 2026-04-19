<template>
  <div class="admin-admins">
    <div class="page-header">
      <h2>管理员管理</h2>
      <button class="add-btn" @click="showAddModal = true">+ 新建管理员</button>
    </div>

    <div class="filters">
      <input type="text" v-model="searchKeyword" placeholder="搜索管理员账号" class="search-input">
      <select v-model="filterRole">
        <option value="all">全部权限</option>
        <option value="super">超级管理员</option>
        <option value="normal">普通管理员</option>
      </select>
    </div>

    <div class="admin-table" v-if="loading">
      <div class="loading">加载中...</div>
    </div>
    <div class="admin-table" v-else>
      <table>
        <thead>
          <tr><th>ID</th><th>账号</th><th>真实姓名</th><th>权限等级</th><th>状态</th><th>创建时间</th><th>操作</th></tr>
        </thead>
        <tbody>
          <tr v-for="admin in filteredAdmins" :key="admin.id">
            <td>{{ admin.id }}</td>
            <td>{{ admin.account }}</td>
            <td>{{ admin.realName }}</td>
            <td><span :class="['role-tag', admin.role]">{{ admin.role === 'super' ? '超级管理员' : '普通管理员' }}</span></td>
            <td><span :class="['status-tag', admin.status]">{{ admin.status === 'active' ? '启用' : '禁用' }}</span></td>
            <td>{{ admin.createTime }}</td>
            <td class="actions">
              <button class="edit-btn" @click="editAdmin(admin)">编辑</button>
              <button v-if="admin.status === 'active'" class="disable-btn" @click="toggleStatus(admin, 'disable')">禁用</button>
              <button v-else class="enable-btn" @click="toggleStatus(admin, 'enable')">启用</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新建/编辑模态框 -->
    <div v-if="showAddModal" class="modal" @click="showAddModal = false">
      <div class="modal-content" @click.stop>
        <h3>{{ editingAdmin ? '编辑管理员' : '新建管理员' }}</h3>
        <div class="form-group"><label>账号</label><input v-model="form.account" placeholder="请输入账号"></div>
        <div class="form-group"><label>密码</label><input type="password" v-model="form.password" placeholder="请输入密码"></div>
        <div class="form-group"><label>真实姓名</label><input v-model="form.realName" placeholder="请输入真实姓名"></div>
        <div class="form-group"><label>权限等级</label><select v-model="form.role"><option value="normal">普通管理员</option><option value="super">超级管理员</option></select></div>
        <div class="modal-actions"><button @click="showAddModal = false" class="cancel-btn">取消</button><button @click="saveAdmin" class="save-btn">保存</button></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const searchKeyword = ref('')
const filterRole = ref('all')
const showAddModal = ref(false)
const editingAdmin = ref(null)
const loading = ref(true)
const admins = ref([])

const form = ref({ account: '', password: '', realName: '', role: 'normal' })

const fetchAdmins = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/admin/admins', {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (response.data.code === 200) {
      admins.value = response.data.data
    }
  } catch (err) {
    console.error('获取管理员列表失败', err)
    admins.value = []
  } finally {
    loading.value = false
  }
}

const filteredAdmins = computed(() => {
  let result = admins.value
  if (searchKeyword.value) result = result.filter(a => a.account.includes(searchKeyword.value))
  if (filterRole.value !== 'all') result = result.filter(a => a.role === filterRole.value)
  return result
})

const editAdmin = (admin) => {
  editingAdmin.value = admin
  form.value = { ...admin, password: '' }
  showAddModal.value = true
}

const saveAdmin = async () => {
  if (!form.value.account) { alert('请输入账号'); return }
  if (!editingAdmin.value && !form.value.password) { alert('请输入密码'); return }
  
  try {
    let response
    if (editingAdmin.value) {
      response = await axios.put(`/api/admin/admins/${editingAdmin.value.id}`, form.value, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      })
    } else {
      response = await axios.post('/api/admin/admins', form.value, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      })
    }
    if (response.data.code === 200) {
      alert(editingAdmin.value ? '管理员信息已更新' : '管理员创建成功')
      fetchAdmins()
      showAddModal.value = false
      editingAdmin.value = null
      form.value = { account: '', password: '', realName: '', role: 'normal' }
    } else {
      alert(response.data.message || '操作失败')
    }
  } catch (err) {
    alert('操作失败，请稍后重试')
  }
}

const toggleStatus = async (admin, action) => {
  const newStatus = action === 'disable' ? 'disabled' : 'active'
  try {
    const response = await axios.put(`/api/admin/admins/${admin.id}/status`, { status: newStatus }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (response.data.code === 200) {
      admin.status = newStatus
      alert(action === 'disable' ? '管理员已禁用' : '管理员已启用')
    } else {
      alert(response.data.message || '操作失败')
    }
  } catch (err) {
    alert('操作失败，请稍后重试')
  }
}

onMounted(() => {
  fetchAdmins()
})
</script>

<style scoped>
.admin-admins { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { font-size: 22px; }
.add-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 20px; }
.filters { display: flex; gap: 10px; margin-bottom: 20px; }
.search-input { padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 8px; width: 200px; }
.filters select { padding: 8px 12px; border: 1px solid #e5e7eb; border-radius: 8px; }
.admin-table { background: white; border-radius: 16px; overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #f0f0f0; }
th { background: #fafafa; font-weight: 600; }
.role-tag { padding: 4px 10px; border-radius: 20px; font-size: 12px; }
.role-tag.super { background: #fef3c7; color: #d97706; }
.role-tag.normal { background: #e0e7ff; color: #4338ca; }
.status-tag { padding: 4px 10px; border-radius: 20px; font-size: 12px; }
.status-tag.active { background: #d1fae5; color: #065f46; }
.status-tag.disabled { background: #fee2e2; color: #991b1b; }
.actions { display: flex; gap: 8px; }
.edit-btn, .disable-btn, .enable-btn { padding: 5px 12px; font-size: 12px; }
.edit-btn { background: #f3f4f6; color: #666; }
.disable-btn { background: #ef4444; color: white; }
.enable-btn { background: #10b981; color: white; }
.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 450px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: 500; }
.form-group input, .form-group select { width: 100%; padding: 10px; border: 1px solid #e5e7eb; border-radius: 8px; }
.modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
.cancel-btn { background: #f3f4f6; color: #666; padding: 8px 20px; }
.save-btn { background: #10b981; color: white; padding: 8px 20px; }
.loading { text-align: center; padding: 40px; color: #999; }
</style>