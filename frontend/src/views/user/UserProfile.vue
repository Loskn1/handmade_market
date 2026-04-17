<template>
  <div class="user-profile">
    <div class="profile-header">
      <h3>个人资料</h3>
      <button class="edit-btn" @click="isEditing = !isEditing">{{ isEditing ? '保存' : '编辑资料' }}</button>
    </div>

    <!-- 实名认证状态 -->
    <div class="verify-status" :class="{ verified: isVerified, unverified: !isVerified }">
      <div class="verify-icon">{{ isVerified ? '✅' : '⚠️' }}</div>
      <div class="verify-info">
        <h4>{{ isVerified ? '已实名认证' : '未实名认证' }}</h4>
        <p v-if="isVerified">您已通过实名认证，可以进行交易</p>
        <p v-else>请完成实名认证后才能进行发布商品、购买商品等交易操作</p>
      </div>
      <button v-if="!isVerified" @click="showVerifyModal = true" class="verify-btn">立即认证</button>
    </div>

    <div class="profile-content">
      <!-- 头像 -->
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <img :src="form.avatar || defaultAvatar" class="avatar">
          <label v-if="isEditing" class="avatar-upload">
            <input type="file" accept="image/*" @change="uploadAvatar" style="display: none">
            <span>📷 更换头像</span>
          </label>
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info-section">
        <div class="info-item"><label>用户名</label><div class="info-value"><input v-if="isEditing" v-model="form.username"><span v-else>{{ form.username || '未设置' }}</span></div></div>
        <div class="info-item"><label>昵称</label><div class="info-value"><input v-if="isEditing" v-model="form.nickname"><span v-else>{{ form.nickname || '未设置' }}</span></div></div>
        <div class="info-item"><label>手机号</label><div class="info-value"><input v-if="isEditing" v-model="form.phone" :disabled="isVerified"><span v-else>{{ form.phone || '未设置' }}</span><span v-if="isVerified" class="verified-hint">已认证，不可修改</span></div></div>
        <div class="info-item"><label>邮箱</label><div class="info-value"><input v-if="isEditing" v-model="form.email"><span v-else>{{ form.email || '未设置' }}</span></div></div>
        <div class="info-item"><label>个性签名</label><div class="info-value"><input v-if="isEditing" v-model="form.bio"><span v-else>{{ form.bio || '未设置' }}</span></div></div>
      </div>
    </div>

    <!-- 修改密码 -->
    <div class="password-section">
      <h4>修改密码</h4>
      <div class="password-form">
        <input type="password" v-model="password.old" placeholder="当前密码">
        <input type="password" v-model="password.new" placeholder="新密码（6-20位，含字母+数字）">
        <input type="password" v-model="password.confirm" placeholder="确认新密码">
        <button @click="changePassword" class="change-btn">确认修改</button>
      </div>
    </div>

    <!-- 实名认证弹窗 -->
    <div v-if="showVerifyModal" class="modal" @click="showVerifyModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-icon">🔐</div>
        <h3>实名认证</h3>
        <p class="modal-desc">根据国家法律法规，进行交易前需要完成实名认证</p>
        
        <div class="form-group">
          <label>真实姓名 <span class="required">*</span></label>
          <input type="text" v-model="verifyForm.realName" placeholder="请输入身份证上的真实姓名" />
        </div>
        
        <div class="form-group">
          <label>身份证号 <span class="required">*</span></label>
          <input type="text" v-model="verifyForm.idCard" placeholder="请输入18位身份证号码" />
        </div>
        
        <div class="form-group">
          <label>手机号 <span class="required">*</span></label>
          <input type="tel" v-model="verifyForm.phone" placeholder="请输入手机号" />
        </div>
        
        <div class="form-group">
          <label>验证码 <span class="required">*</span></label>
          <div class="captcha-group">
            <input type="text" v-model="verifyForm.captcha" placeholder="请输入验证码" />
            <button type="button" @click="sendVerifyCaptcha" :disabled="captchaSending" class="captcha-btn">{{ captchaText }}</button>
          </div>
        </div>
        
        <div class="modal-actions">
          <button @click="showVerifyModal = false" class="cancel-btn">取消</button>
          <button @click="submitVerification" class="confirm-btn" :disabled="verifying">{{ verifying ? '提交中...' : '提交认证' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { isRealNameVerified, saveRealNameInfo } from '@/utils/auth'
import axios from 'axios'

const defaultAvatar = 'https://via.placeholder.com/100'
const isEditing = ref(false)
const isVerified = ref(false)
const showVerifyModal = ref(false)
const verifying = ref(false)
const captchaSending = ref(false)
const captchaText = ref('获取验证码')

const form = reactive({
  username: '',
  nickname: '',
  phone: '',
  email: '',
  bio: '',
  avatar: ''
})

const verifyForm = reactive({
  realName: '',
  idCard: '',
  phone: '',
  captcha: ''
})

const password = reactive({ old: '', new: '', confirm: '' })

// 验证身份证号
const validateIdCard = (idCard) => {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  return reg.test(idCard)
}

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await axios.get('/api/user/info', {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (response.data.code === 200) {
      const user = response.data.data
      form.username = user.username || ''
      form.nickname = user.nickname || ''
      form.phone = user.phone || ''
      form.email = user.email || ''
      form.bio = user.bio || ''
      form.avatar = user.avatar || ''
      isVerified.value = user.isVerified || false
      
      if (user.isVerified) {
        verifyForm.phone = user.phone || ''
      }
    }
  } catch (err) {
    console.error('获取用户信息失败', err)
  }
}

const uploadAvatar = (e) => {
  const file = e.target.files[0]
  if (file && file.size <= 5 * 1024 * 1024) {
    const reader = new FileReader()
    reader.onload = (ev) => { form.avatar = ev.target.result }
    reader.readAsDataURL(file)
  } else { alert('图片大小不能超过5MB') }
}

const sendVerifyCaptcha = async () => {
  if (!verifyForm.phone) {
    alert('请先输入手机号')
    return
  }
  captchaSending.value = true
  try {
    const response = await axios.post('/api/send-captcha', { phone: verifyForm.phone })
    if (response.data.code === 200) {
      let time = 60
      captchaText.value = `${time}秒后重试`
      const timer = setInterval(() => {
        time--
        if (time <= 0) {
          clearInterval(timer)
          captchaText.value = '获取验证码'
          captchaSending.value = false
        } else {
          captchaText.value = `${time}秒后重试`
        }
      }, 1000)
    } else {
      alert(response.data.message || '发送失败')
      captchaSending.value = false
    }
  } catch (err) {
    alert('网络错误，请稍后重试')
    captchaSending.value = false
  }
}

const submitVerification = async () => {
  if (!verifyForm.realName) { alert('请输入真实姓名'); return }
  if (!verifyForm.idCard) { alert('请输入身份证号'); return }
  if (!validateIdCard(verifyForm.idCard)) { alert('请输入正确的身份证号'); return }
  if (!verifyForm.phone) { alert('请输入手机号'); return }
  if (!verifyForm.captcha) { alert('请输入验证码'); return }
  
  verifying.value = true
  try {
    const response = await axios.post('/api/user/verify', {
      realName: verifyForm.realName,
      idCard: verifyForm.idCard,
      phone: verifyForm.phone,
      captcha: verifyForm.captcha
    }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    
    if (response.data.code === 200) {
      saveRealNameInfo(verifyForm.realName, verifyForm.idCard, verifyForm.phone)
      isVerified.value = true
      form.phone = verifyForm.phone
      alert('实名认证成功！您现在可以进行交易了')
      showVerifyModal.value = false
    } else {
      alert(response.data.message || '认证失败')
    }
  } catch (err) {
    alert('网络错误，请稍后重试')
  } finally {
    verifying.value = false
  }
}

const changePassword = async () => {
  if (!password.old) { alert('请输入当前密码'); return }
  if (!password.new) { alert('请输入新密码'); return }
  if (password.new.length < 6 || password.new.length > 20) { alert('密码长度6-20位'); return }
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(password.new)) { alert('密码需包含字母和数字'); return }
  if (password.new !== password.confirm) { alert('两次输入的密码不一致'); return }
  
  try {
    const response = await axios.put('/api/user/password', {
      oldPassword: password.old,
      newPassword: password.new
    }, {
      headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
    })
    if (response.data.code === 200) {
      alert('密码修改成功，请重新登录')
      localStorage.clear()
      window.location.href = '/login'
    } else {
      alert(response.data.message || '修改失败')
    }
  } catch (err) {
    alert('网络错误，请稍后重试')
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.user-profile { padding: 0; }
.profile-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
.profile-header h3 { font-size: 20px; }
.edit-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 8px 20px; }

.verify-status { display: flex; align-items: center; gap: 15px; padding: 15px 20px; border-radius: 16px; margin-bottom: 25px; }
.verify-status.verified { background: #d1fae5; border: 1px solid #10b981; }
.verify-status.unverified { background: #fef3c7; border: 1px solid #f59e0b; }
.verify-icon { font-size: 28px; }
.verify-info { flex: 1; }
.verify-info h4 { font-size: 16px; margin-bottom: 4px; }
.verify-info p { font-size: 13px; color: #666; }
.verify-btn { background: linear-gradient(135deg, #f59e0b, #d97706); color: white; padding: 8px 20px; font-size: 14px; }

.profile-content { background: white; border-radius: 20px; padding: 30px; margin-bottom: 25px; }
.avatar-section { text-align: center; margin-bottom: 30px; padding-bottom: 20px; border-bottom: 1px solid #f0f0f0; }
.avatar { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; border: 3px solid #667eea; }
.avatar-upload { display: block; margin-top: 10px; cursor: pointer; color: #667eea; font-size: 13px; }
.info-section { display: flex; flex-direction: column; gap: 20px; }
.info-item { display: flex; align-items: flex-start; gap: 20px; }
.info-item label { width: 100px; font-weight: 500; color: #555; padding-top: 8px; }
.info-value { flex: 1; }
.info-value input, .info-value textarea { width: 100%; padding: 8px 12px; border: 2px solid #e5e7eb; border-radius: 8px; font-size: 14px; }
.info-value input:focus, .info-value textarea:focus { outline: none; border-color: #667eea; }
.info-value span { display: inline-block; padding: 8px 0; color: #333; }
.verified-hint { font-size: 12px; color: #10b981; margin-left: 10px; }

.password-section { background: white; border-radius: 20px; padding: 30px; }
.password-section h4 { font-size: 18px; margin-bottom: 20px; }
.password-form { display: flex; flex-direction: column; gap: 15px; max-width: 400px; }
.password-form input { padding: 12px 15px; border: 2px solid #e5e7eb; border-radius: 10px; font-size: 14px; }
.password-form input:focus { outline: none; border-color: #667eea; }
.change-btn { background: #f3f4f6; color: #666; padding: 10px; margin-top: 5px; }

.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 450px; }
.modal-icon { font-size: 48px; text-align: center; margin-bottom: 15px; }
.modal-content h3 { text-align: center; margin-bottom: 10px; }
.modal-desc { text-align: center; color: #666; margin-bottom: 25px; }
.form-group { margin-bottom: 20px; }
.form-group label { display: block; margin-bottom: 8px; font-weight: 500; }
.required { color: #ef4444; }
.form-group input { width: 100%; padding: 12px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; }
.captcha-group { display: flex; gap: 10px; }
.captcha-group input { flex: 1; }
.captcha-btn { width: 120px; background: #f3f4f6; color: #666; font-size: 13px; padding: 0; }
.modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 20px; }
.cancel-btn { background: #f3f4f6; color: #666; padding: 10px 20px; }
.confirm-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 20px; }
</style>