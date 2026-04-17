<template>
  <div class="login-page">
    <div class="bg-overlay"></div>
    <button class="back-home-btn" @click="$router.push('/')">← 返回首页</button>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-icon">🎨</div>
          <h2>欢迎回来</h2>
          <p>登录手作趣集，发现更多美好</p>
        </div>

        <!-- 角色切换 -->
        <div class="role-tabs">
          <button :class="{ active: loginRole === 'user' }" @click="loginRole = 'user'">用户登录</button>
          <button :class="{ active: loginRole === 'admin' }" @click="loginRole = 'admin'">管理员登录</button>
        </div>

        <form @submit.prevent="handleLogin">
          <div class="form-group">
            <label>{{ loginRole === 'user' ? '手机号/用户名' : '管理员账号' }}</label>
            <input type="text" v-model="form.account" :placeholder="loginRole === 'user' ? '请输入手机号或用户名' : '请输入管理员账号'" required />
          </div>

          <div class="form-group">
            <label>密码</label>
            <input :type="showPassword ? 'text' : 'password'" v-model="form.password" placeholder="请输入密码" required />
            <span class="password-toggle" @click="showPassword = !showPassword">{{ showPassword ? '🙈' : '👁️' }}</span>
          </div>

          <div class="form-options" v-if="loginRole === 'user'">
            <label class="checkbox"><input type="checkbox" v-model="rememberMe" /> 记住密码</label>
            <a href="#" class="forgot-link" @click.prevent="showForgotModal = true">忘记密码？</a>
          </div>

          <button type="submit" class="submit-btn" :disabled="submitting">{{ submitting ? '登录中...' : '登 录' }}</button>
        </form>

        <div class="demo-accounts" v-if="loginRole === 'user'">
          <p>演示账号：</p>
          <div class="demo-buttons">
            <button @click="fillDemo('13800138000', '123456')">用户1</button>
            <button @click="fillDemo('user123', '123456')">用户2</button>
            <button @click="fillDemo('test@example.com', '123456')">用户3</button>
          </div>
        </div>

        <div class="demo-accounts" v-if="loginRole === 'admin'">
          <p>管理员演示账号：</p>
          <div class="demo-buttons">
            <button @click="fillDemo('admin', 'admin123')">超级管理员</button>
            <button @click="fillDemo('manager', 'admin123')">普通管理员</button>
          </div>
        </div>

        <div class="login-footer" v-if="loginRole === 'user'">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>
      </div>
    </div>

    <!-- 忘记密码弹窗 -->
    <div v-if="showForgotModal" class="modal" @click="showForgotModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-icon">🔐</div>
        <h3>忘记密码</h3>
        <p class="modal-desc">请输入您注册时使用的手机号</p>
        <input type="tel" v-model="forgotPhone" placeholder="请输入手机号" class="modal-input" />
        <div class="modal-actions">
          <button @click="showForgotModal = false" class="cancel-btn">取消</button>
          <button @click="sendResetRequest" class="confirm-btn">提交</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const showPassword = ref(false)
const submitting = ref(false)
const rememberMe = ref(false)
const showForgotModal = ref(false)
const forgotPhone = ref('')
const loginRole = ref('user')

const form = ref({
  account: '',
  password: ''
})

const fillDemo = (account, password) => {
  form.value.account = account
  form.value.password = password
}

const handleLogin = async () => {
  if (!form.value.account) { alert('请输入账号'); return }
  if (!form.value.password) { alert('请输入密码'); return }

  submitting.value = true

  // 模拟登录延迟
  setTimeout(() => {
    // 管理员登录
    if (loginRole.value === 'admin') {
      if (form.value.account === 'admin' && form.value.password === 'admin123') {
        localStorage.setItem('token', 'admin-token')
        localStorage.setItem('userRole', 'admin')
        localStorage.setItem('adminRole', 'super')
        localStorage.setItem('userAccount', 'admin')
        localStorage.setItem('userNickname', '超级管理员')
        localStorage.setItem('isVerified', 'true')
        alert('超级管理员登录成功！')
        router.push('/admin/dashboard')
      } else if (form.value.account === 'manager' && form.value.password === 'admin123') {
        localStorage.setItem('token', 'admin-token')
        localStorage.setItem('userRole', 'admin')
        localStorage.setItem('adminRole', 'normal')
        localStorage.setItem('userAccount', 'manager')
        localStorage.setItem('userNickname', '张经理')
        localStorage.setItem('isVerified', 'true')
        alert('普通管理员登录成功！')
        router.push('/admin/dashboard')
      } else {
        alert('管理员账号或密码错误！\n\n超级管理员：admin / admin123\n普通管理员：manager / admin123')
      }
    } 
    // 普通用户登录
    else {
      if (form.value.password === '123456') {
        localStorage.setItem('token', 'user-token')
        localStorage.setItem('userRole', 'user')
        localStorage.setItem('adminRole', '')
        localStorage.setItem('userAccount', form.value.account)
        localStorage.setItem('userNickname', form.value.account === '13800138000' ? '手作爱好者' : form.value.account)
        localStorage.setItem('isVerified', 'false')
        localStorage.setItem('userRealName', '')
        localStorage.setItem('userIdCard', '')
        localStorage.setItem('userPhone', form.value.account === '13800138000' ? '13800138000' : '')
        alert('登录成功！')
        router.push('/')
      } else {
        alert('密码错误！\n\n演示账号：任意账号 + 密码 123456')
      }
    }
    submitting.value = false
  }, 500)
}

const sendResetRequest = () => {
  if (!forgotPhone.value) { alert('请输入手机号'); return }
  alert('重置链接已发送至您的手机，请查收')
  showForgotModal.value = false
  forgotPhone.value = ''
}
</script>

<style scoped>
.login-page { 
  min-height: 100vh; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  padding: 40px 20px; 
  position: relative;
  background-image: url('https://images.unsplash.com/photo-1513519245088-0e12902e5a38?q=80&w=2070&auto=format&fit=crop');
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
}
.bg-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102,126,234,0.3) 0%, rgba(118,75,162,0.3) 100%);
  z-index: 0;
}
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
.login-container { width: 100%; max-width: 420px; position: relative; z-index: 1; }
.login-card { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 30px; padding: 40px; box-shadow: 0 25px 50px rgba(0,0,0,0.2); }
.login-header { text-align: center; margin-bottom: 30px; }
.logo-icon { font-size: 48px; margin-bottom: 10px; }
.login-header h2 { font-size: 28px; color: #333; margin-bottom: 8px; }
.login-header p { color: #666; font-size: 14px; }

.role-tabs { display: flex; gap: 10px; margin-bottom: 25px; border-bottom: 1px solid #e5e7eb; padding-bottom: 10px; }
.role-tabs button { flex: 1; background: none; padding: 10px; font-size: 16px; color: #999; border-radius: 0; }
.role-tabs button.active { color: #667eea; border-bottom: 2px solid #667eea; }

.form-group { margin-bottom: 20px; position: relative; }
.form-group label { display: block; margin-bottom: 8px; font-weight: 500; color: #333; }
.form-group input { width: 100%; padding: 12px 15px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; background: white; }
.form-group input:focus { outline: none; border-color: #667eea; }
.password-toggle { position: absolute; right: 15px; top: 42px; cursor: pointer; font-size: 18px; }
.form-options { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
.checkbox { display: flex; align-items: center; gap: 8px; font-size: 14px; cursor: pointer; }
.checkbox input { width: auto; cursor: pointer; }
.forgot-link { color: #667eea; text-decoration: none; font-size: 14px; cursor: pointer; }
.submit-btn { width: 100%; padding: 14px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; font-size: 16px; font-weight: 600; border-radius: 12px; }
.submit-btn:disabled { opacity: 0.6; cursor: not-allowed; }

.demo-accounts { margin-top: 20px; padding-top: 15px; border-top: 1px solid #e5e7eb; text-align: center; }
.demo-accounts p { font-size: 12px; color: #999; margin-bottom: 10px; }
.demo-buttons { display: flex; gap: 10px; justify-content: center; flex-wrap: wrap; }
.demo-buttons button { background: #f3f4f6; color: #666; padding: 6px 12px; font-size: 12px; border-radius: 20px; }

.login-footer { text-align: center; margin-top: 20px; font-size: 14px; color: #666; }
.login-footer a { color: #667eea; text-decoration: none; }

.modal { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; border-radius: 20px; padding: 30px; width: 400px; text-align: center; }
.modal-icon { font-size: 48px; margin-bottom: 15px; }
.modal-content h3 { font-size: 22px; margin-bottom: 10px; }
.modal-desc { color: #666; margin-bottom: 20px; }
.modal-input { width: 100%; padding: 12px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; margin-bottom: 20px; }
.modal-actions { display: flex; gap: 15px; justify-content: center; }
.cancel-btn { background: #f3f4f6; color: #666; padding: 10px 25px; }
.confirm-btn { background: linear-gradient(135deg, #667eea, #764ba2); color: white; padding: 10px 25px; }
</style>