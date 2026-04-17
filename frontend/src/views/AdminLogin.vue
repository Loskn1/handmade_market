<template>
  <div class="admin-login-page">
    <button class="back-home-btn" @click="$router.push('/')">← 返回首页</button>
    <div class="admin-login-container">
      <div class="admin-login-card">
        <div class="admin-icon">⚙️</div>
        <h2>管理员登录</h2>
        <p class="subtitle">请使用管理员账号登录</p>
        <form @submit.prevent="handleLogin">
          <div class="form-group"><input type="text" v-model="form.username" placeholder="管理员账号" required /></div>
          <div class="form-group"><input type="password" v-model="form.password" placeholder="管理员密码" required /></div>
          <div class="form-options">
            <a href="#" class="forgot-link" @click.prevent="showForgotModal = true">忘记密码？</a>
          </div>
          <button type="submit" class="admin-btn" :disabled="submitting">{{ submitting ? '登录中...' : '登 录' }}</button>
        </form>
        <p class="back-link" @click="$router.push('/login')">← 返回用户登录</p>
      </div>
    </div>

    <!-- 忘记密码弹窗 - 联系超级管理员 -->
    <div v-if="showForgotModal" class="modal" @click="showForgotModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-icon">🔐</div>
        <h3>重置管理员密码</h3>
        <p class="modal-desc">请联系超级管理员重置您的密码</p>
        <div class="contact-info">
          <p>📧 邮箱：admin@shouzuoji.com</p>
          <p>📞 电话：400-888-8888</p>
        </div>
        <div class="modal-actions">
          <button @click="showForgotModal = false" class="confirm-btn">我知道了</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const submitting = ref(false)
const showForgotModal = ref(false)
const form = ref({ username: '', password: '' })

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) { alert('请输入账号和密码'); return }
  submitting.value = true
  
  try {
    const response = await axios.post('/api/admin/login', {
      username: form.value.username,
      password: form.value.password
    })
    if (response.data.code === 200) {
      const { token, admin } = response.data.data
      localStorage.setItem('token', token)
      localStorage.setItem('userRole', 'admin')
      localStorage.setItem('adminRole', admin.role)
      localStorage.setItem('userAccount', admin.username)
      localStorage.setItem('userNickname', admin.realName || admin.username)
      alert('登录成功！')
      router.push('/admin/dashboard')
    } else {
      alert(response.data.message || '登录失败')
    }
  } catch (err) {
    alert('网络错误，请稍后重试')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.admin-login-page { min-height: 100vh; background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); display: flex; align-items: center; justify-content: center; position: relative; }
.back-home-btn {
  position: absolute;
  top: 20px;
  left: 20px;
  background: rgba(255,255,255,0.2);
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 25px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s;
  z-index: 10;
}
.back-home-btn:hover {
  background: rgba(255,255,255,0.3);
  transform: translateX(-3px);
}
.admin-login-page::before { content: ''; position: absolute; width: 200%; height: 200%; background: radial-gradient(circle, rgba(255,255,255,0.05) 1px, transparent 1px); background-size: 40px 40px; animation: moveBackground 20s linear infinite; }
@keyframes moveBackground { 0% { transform: translate(0,0); } 100% { transform: translate(40px,40px); } }
.admin-login-container { position: relative; z-index: 1; width: 100%; max-width: 420px; padding: 20px; }
.admin-login-card { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 24px; padding: 48px 40px; text-align: center; box-shadow: 0 20px 60px rgba(0,0,0,0.3); }
.admin-icon { width: 80px; height: 80px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; font-size: 40px; color: white; }
.admin-login-card h2 { font-size: 28px; color: #333; margin-bottom: 8px; }
.subtitle { color: #666; margin-bottom: 32px; }
.form-group { margin-bottom: 20px; }
.form-group input { width: 100%; padding: 14px 18px; border: 2px solid #e0e0e0; border-radius: 12px; font-size: 16px; transition: all 0.3s ease; outline: none; background: white; }
.form-group input:focus { border-color: #667eea; box-shadow: 0 0 0 3px rgba(102,126,234,0.1); }
.form-options { text-align: right; margin-bottom: 20px; }
.forgot-link { color: #667eea; text-decoration: none; font-size: 14px; cursor: pointer; }
.admin-btn { width: 100%; padding: 14px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 12px; font-size: 16px; font-weight: 600; cursor: pointer; transition: all 0.3s ease; }
.admin-btn:hover { transform: translateY(-2px); box-shadow: 0 10px 30px rgba(102,126,234,0.4); }
.back-link { margin-top: 20px; color: #667eea; cursor: pointer; font-size: 14px; }
.back-link:hover { text-decoration: underline; }

.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 400px; text-align: center; }
.modal-icon { font-size: 48px; margin-bottom: 15px; }
.modal-content h3 { font-size: 22px; margin-bottom: 10px; }
.modal-desc { color: #666; margin-bottom: 20px; }
.contact-info { background: #f3f4f6; padding: 15px; border-radius: 12px; margin-bottom: 20px; text-align: left; }
.contact-info p { margin: 8px 0; color: #333; font-size: 14px; }
.modal-actions { display: flex; gap: 15px; justify-content: center; }
.confirm-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 25px; }
</style>