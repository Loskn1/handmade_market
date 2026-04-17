<template>
  <div class="user-address">
    <div class="address-header">
      <h3>收货地址</h3>
      <button class="add-btn" @click="showAddModal = true">+ 新增地址</button>
    </div>

    <div class="address-list">
      <div v-for="(addr, idx) in addresses" :key="idx" class="address-card">
        <div class="address-info">
          <div class="address-name">
            <span class="receiver">{{ addr.name }}</span>
            <span class="phone">{{ addr.phone }}</span>
            <span v-if="addr.isDefault" class="default-badge">默认</span>
          </div>
          <div class="address-detail">{{ addr.province }} {{ addr.city }} {{ addr.district }} {{ addr.detail }}</div>
        </div>
        <div class="address-actions">
          <button class="edit-btn" @click="editAddress(addr, idx)">编辑</button>
          <button class="delete-btn" @click="deleteAddress(idx)">删除</button>
          <button v-if="!addr.isDefault" class="set-default-btn" @click="setDefault(idx)">设为默认</button>
        </div>
      </div>
      <div v-if="addresses.length === 0" class="empty">暂无收货地址，点击新增</div>
    </div>

    <!-- 新增/编辑地址弹窗 -->
    <div v-if="showAddModal" class="modal" @click="showAddModal = false">
      <div class="modal-content" @click.stop>
        <h3>{{ editingIndex !== null ? '编辑地址' : '新增地址' }}</h3>
        
        <div class="form-group"><label>收货人 <span class="required">*</span></label><input v-model="form.name" placeholder="请输入收货人姓名"></div>
        <div class="form-group"><label>手机号 <span class="required">*</span></label><input v-model="form.phone" placeholder="请输入手机号"></div>
        
        <div class="form-row">
          <div class="form-group"><label>省份</label><input v-model="form.province" placeholder="省份"></div>
          <div class="form-group"><label>城市</label><input v-model="form.city" placeholder="城市"></div>
          <div class="form-group"><label>区/县</label><input v-model="form.district" placeholder="区/县"></div>
        </div>
        
        <div class="form-group"><label>详细地址 <span class="required">*</span></label><input v-model="form.detail" placeholder="街道、门牌号等"></div>
        
        <div class="form-group checkbox"><label><input type="checkbox" v-model="form.isDefault"> 设为默认地址</label></div>
        
        <div class="modal-actions">
          <button class="cancel-btn" @click="closeModal">取消</button>
          <button class="save-btn" @click="saveAddress">保存</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const showAddModal = ref(false)
const editingIndex = ref(null)

const form = reactive({ name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false })

const addresses = ref([
  { name: '张三', phone: '138****0000', province: '北京市', city: '北京市', district: '朝阳区', detail: 'xxx街道xxx号', isDefault: true },
  { name: '张三', phone: '138****0000', province: '上海市', city: '上海市', district: '浦东新区', detail: 'xxx路xxx号', isDefault: false }
])

const closeModal = () => {
  showAddModal.value = false
  editingIndex.value = null
  Object.assign(form, { name: '', phone: '', province: '', city: '', district: '', detail: '', isDefault: false })
}

const saveAddress = () => {
  if (!form.name || !form.phone || !form.detail) return alert('请填写完整信息')
  
  const newAddress = { ...form }
  
  if (form.isDefault) {
    addresses.value.forEach(addr => addr.isDefault = false)
  }
  
  if (editingIndex.value !== null) {
    addresses.value[editingIndex.value] = newAddress
    alert('地址已更新')
  } else {
    addresses.value.push(newAddress)
    alert('地址已添加')
  }
  closeModal()
}

const editAddress = (addr, idx) => {
  editingIndex.value = idx
  Object.assign(form, addr)
  showAddModal.value = true
}

const deleteAddress = (idx) => {
  if (confirm('确定删除该地址吗？')) {
    addresses.value.splice(idx, 1)
    alert('地址已删除')
  }
}

const setDefault = (idx) => {
  addresses.value.forEach((addr, i) => addr.isDefault = (i === idx))
  alert('已设为默认地址')
}
</script>

<style scoped>
.user-address { padding: 0; }
.address-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.address-header h3 { font-size: 20px; }
.add-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 20px; }

.address-list { display: flex; flex-direction: column; gap: 15px; }
.address-card { background: white; border-radius: 16px; padding: 20px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.address-name { display: flex; align-items: center; gap: 15px; margin-bottom: 8px; flex-wrap: wrap; }
.receiver { font-weight: bold; font-size: 16px; }
.phone { color: #666; }
.default-badge { background: #d1fae5; color: #065f46; padding: 2px 8px; border-radius: 10px; font-size: 12px; }
.address-detail { color: #666; font-size: 14px; }
.address-actions { display: flex; gap: 10px; }
.edit-btn, .delete-btn, .set-default-btn { padding: 6px 16px; font-size: 13px; }
.edit-btn { background: #f3f4f6; color: #666; }
.delete-btn { background: #ef4444; color: white; }
.set-default-btn { background: #667eea; color: white; }

.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 500px; max-height: 80vh; overflow-y: auto; }
.modal-content h3 { margin-bottom: 20px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: 500; }
.required { color: #ef4444; }
.form-group input { width: 100%; padding: 10px 12px; border: 2px solid #e5e7eb; border-radius: 8px; }
.form-row { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 10px; }
.checkbox label { display: flex; align-items: center; gap: 8px; font-weight: normal; }
.checkbox input { width: auto; }
.modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 20px; }
.cancel-btn { background: #f3f4f6; color: #666; padding: 10px 20px; }
.save-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 30px; }
.empty { text-align: center; padding: 60px; background: white; border-radius: 16px; color: #999; }
</style>