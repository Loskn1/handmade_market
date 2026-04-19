<template>
  <div class="publish-goods">
    <h3>发布商品</h3>
    <p class="subtitle">填写商品信息，提交后即可上架销售</p>

    <!-- 未实名认证提示 -->
    <div v-if="!isVerified" class="verify-warning">
      <span>⚠️</span>
      <div>
        <strong>需要实名认证</strong>
        <p>发布商品需要进行实名认证，请先完成实名认证</p>
      </div>
      <button @click="$router.push('/user/profile')" class="verify-link">去认证</button>
    </div>

    <form v-else @submit.prevent="submitGoods" class="publish-form">
      <div class="form-group"><label>商品名称 <span class="required">*</span></label><input v-model="form.name" placeholder="请输入商品名称（最多50字）" maxlength="50" required></div>
      <div class="form-row"><div class="form-group"><label>价格 <span class="required">*</span></label><input v-model.number="form.price" type="number" step="0.01" placeholder="售价" required></div><div class="form-group"><label>原价</label><input v-model.number="form.originalPrice" type="number" step="0.01" placeholder="原价"></div></div>
      <div class="form-row"><div class="form-group"><label>库存 <span class="required">*</span></label><input v-model.number="form.stock" type="number" placeholder="库存数量" required></div><div class="form-group"><label>商品分类</label><select v-model="form.category"><option value="">请选择分类</option><option>陶瓷</option><option>编织</option><option>木雕</option><option>皮具</option><option>饰品</option><option>刺绣</option></select></div></div>
      <div class="form-group"><label>商品图片 <span class="required">*</span> (至少2张)</label><div class="image-upload"><div class="image-list"><div v-for="(img, idx) in form.images" :key="idx" class="image-item"><img :src="img"><button type="button" @click="removeImage(idx)">✕</button></div><div v-if="form.images.length < 5" class="upload-btn" @click="triggerUpload">+ 上传图片</div></div><input type="file" ref="fileInput" accept="image/jpeg,image/png" style="display: none" multiple @change="handleUpload"><p class="tip">支持jpg、png格式，建议尺寸800x800，单张≤5M</p></div></div>
      <div class="form-group"><label>商品描述 <span class="required">*</span></label><textarea v-model="form.description" rows="6" placeholder="详细描述商品的材质、工艺、尺寸等信息" required></textarea></div>
      <div class="form-group"><label>发货信息</label><div class="form-row"><div class="form-group"><select v-model="form.shippingTime"><option>24小时内发货</option><option>48小时内发货</option><option>72小时内发货</option></select></div><div class="form-group"><input v-model.number="form.shippingFee" type="number" step="0.01" placeholder="运费（0表示包邮）"></div></div></div>
      <div class="form-actions"><button type="button" class="cancel-btn" @click="$router.back()">取消</button><button type="submit" class="submit-btn" :disabled="submitting">{{ submitting ? '提交中...' : '发布商品' }}</button></div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { isRealNameVerified } from '@/utils/auth'
import axios from 'axios'

const router = useRouter()
const submitting = ref(false)
const fileInput = ref(null)
const isVerified = ref(false)

const form = ref({ name: '', price: '', originalPrice: '', stock: '', category: '', images: [], description: '', shippingTime: '48小时内发货', shippingFee: 0 })

const triggerUpload = () => { fileInput.value.click() }

const handleUpload = (e) => {
  const files = Array.from(e.target.files)
  for (const file of files) {
    if (file.size > 5 * 1024 * 1024) { alert('图片不能超过5MB'); continue }
    const reader = new FileReader()
    reader.onload = (ev) => { form.value.images.push(ev.target.result) }
    reader.readAsDataURL(file)
  }
  fileInput.value.value = ''
}

const removeImage = (idx) => { form.value.images.splice(idx, 1) }

const submitGoods = () => {
  if (!form.value.name) return alert('请输入商品名称')
  if (!form.value.price) return alert('请输入商品价格')
  if (!form.value.stock) return alert('请输入库存数量')
  if (form.value.images.length < 2) return alert('请至少上传2张商品图片')
  if (!form.value.description) return alert('请输入商品描述')
  
  submitting.value = true
  setTimeout(() => { alert('商品发布成功！'); router.push('/user/goods'); submitting.value = false }, 1000)
}

onMounted(() => {
  isVerified.value = isRealNameVerified()
})
</script>

<style scoped>
.publish-goods { padding: 0; }
h3 { font-size: 22px; margin-bottom: 8px; }
.subtitle { color: #999; margin-bottom: 25px; }

.verify-warning { display: flex; align-items: center; gap: 15px; background: #fef3c7; border: 1px solid #f59e0b; border-radius: 16px; padding: 20px; margin-bottom: 25px; }
.verify-warning span { font-size: 28px; }
.verify-warning strong { font-size: 16px; color: #d97706; }
.verify-warning p { font-size: 13px; color: #856404; margin-top: 4px; }
.verify-link { background: linear-gradient(135deg, #f59e0b, #d97706); color: white; padding: 8px 20px; border-radius: 25px; font-size: 13px; }

.publish-form { background: white; border-radius: 20px; padding: 25px; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; margin-bottom: 8px; font-weight: 500; }
.required { color: #ef4444; }
input, select, textarea { width: 100%; padding: 10px 12px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; }
input:focus, select:focus, textarea:focus { outline: none; border-color: #667eea; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.image-upload { margin-top: 10px; }
.image-list { display: flex; flex-wrap: wrap; gap: 15px; }
.image-item { position: relative; width: 100px; height: 100px; border-radius: 12px; overflow: hidden; border: 2px solid #e5e7eb; }
.image-item img { width: 100%; height: 100%; object-fit: cover; }
.image-item button { position: absolute; top: 5px; right: 5px; width: 22px; height: 22px; border-radius: 50%; background: rgba(0,0,0,0.6); color: white; font-size: 14px; padding: 0; }
.upload-btn { width: 100px; height: 100px; border: 2px dashed #ccc; border-radius: 12px; display: flex; align-items: center; justify-content: center; cursor: pointer; font-size: 28px; color: #999; }
.tip { font-size: 12px; color: #999; margin-top: 10px; }
.form-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 20px; }
.cancel-btn { background: #f3f4f6; color: #666; padding: 12px 30px; }
.submit-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 12px 40px; font-size: 16px; }
</style>