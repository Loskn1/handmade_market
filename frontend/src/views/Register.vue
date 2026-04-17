<template>
  <div class="register-page">
    <div class="bg-overlay"></div>
    <button class="back-home-btn" @click="$router.push('/')">← 返回首页</button>
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="logo-icon">🎨</div>
          <h2>注册账号</h2>
          <p>加入手作趣集，开启艺术之旅</p>
        </div>

        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <label>手机号 <span class="required">*</span></label>
            <input type="tel" v-model="form.phone" placeholder="请输入手机号" required />
          </div>

          <div class="form-group">
            <label>密码 <span class="required">*</span></label>
            <input :type="showPassword ? 'text' : 'password'" v-model="form.password" placeholder="6-20位，包含字母和数字" required />
            <span class="password-toggle" @click="showPassword = !showPassword">{{ showPassword ? '🙈' : '👁️' }}</span>
          </div>

          <div class="form-group">
            <label>确认密码 <span class="required">*</span></label>
            <input :type="showConfirmPassword ? 'text' : 'password'" v-model="form.confirmPassword" placeholder="请再次输入密码" required />
            <span class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">{{ showConfirmPassword ? '🙈' : '👁️' }}</span>
          </div>

          <div class="form-group">
            <label>验证码 <span class="required">*</span></label>
            <div class="captcha-group">
              <input type="text" v-model="form.captcha" placeholder="请输入验证码" required />
              <button type="button" @click="sendCaptcha" :disabled="captchaSending" class="captcha-btn">{{ captchaText }}</button>
            </div>
          </div>

          <div class="form-group checkbox">
            <label><input type="checkbox" v-model="agreeTerms" /> 我已阅读并同意 <a href="#" @click.prevent="showTerms">《用户协议》</a></label>
          </div>

          <button type="submit" class="submit-btn" :disabled="submitting">{{ submitting ? '注册中...' : '立即注册' }}</button>
        </form>

        <div class="register-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
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
const showConfirmPassword = ref(false)
const captchaSending = ref(false)
const captchaText = ref('获取验证码')
const submitting = ref(false)
const agreeTerms = ref(false)

const form = ref({
  phone: '',
  password: '',
  confirmPassword: '',
  captcha: ''
})

const sendCaptcha = () => {
  if (!form.value.phone) { alert('请先输入手机号'); return }
  captchaSending.value = true
  let time = 60
  captchaText.value = `${time}秒后重试`
  const timer = setInterval(() => {
    time--
    if (time <= 0) { clearInterval(timer); captchaText.value = '获取验证码'; captchaSending.value = false }
    else { captchaText.value = `${time}秒后重试` }
  }, 1000)
  alert('验证码已发送（演示模式）')
}

const handleRegister = () => {
  if (!form.value.phone) { alert('请输入手机号'); return }
  if (!form.value.password) { alert('请输入密码'); return }
  if (form.value.password.length < 6 || form.value.password.length > 20) { alert('密码长度需为6-20位'); return }
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(form.value.password)) { alert('密码需包含字母和数字'); return }
  if (form.value.password !== form.value.confirmPassword) { alert('两次输入的密码不一致'); return }
  if (!form.value.captcha) { alert('请输入验证码'); return }
  if (!agreeTerms.value) { alert('请阅读并同意用户协议'); return }

  submitting.value = true
  setTimeout(() => {
    // 模拟生成用户ID
    const userId = 'U' + Date.now()
    alert('注册成功！用户号：' + userId + '\n请登录')
    router.push('/login')
    submitting.value = false
  }, 1000)
}

const showTerms = () => { alert('用户协议内容...') }
</script>

<style scoped>
.register-page { 
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
.register-container { width: 100%; max-width: 450px; position: relative; z-index: 1; }
.register-card { background: rgba(255,255,255,0.95); backdrop-filter: blur(10px); border-radius: 30px; padding: 40px; box-shadow: 0 25px 50px rgba(0,0,0,0.2); }
.register-header { text-align: center; margin-bottom: 30px; }
.logo-icon { font-size: 48px; margin-bottom: 10px; }
.register-header h2 { font-size: 28px; color: #333; margin-bottom: 8px; }
.register-header p { color: #666; font-size: 14px; }
.form-group { margin-bottom: 20px; position: relative; }
.form-group label { display: block; margin-bottom: 8px; font-weight: 500; color: #333; }
.required { color: #ef4444; }
.form-group input { width: 100%; padding: 12px 15px; border: 2px solid #e5e7eb; border-radius: 12px; font-size: 14px; background: white; }
.form-group input:focus { outline: none; border-color: #667eea; }
.password-toggle { position: absolute; right: 15px; top: 42px; cursor: pointer; font-size: 18px; }
.captcha-group { display: flex; gap: 10px; }
.captcha-group input { flex: 1; }
.captcha-btn { width: 120px; background: #f3f4f6; color: #666; font-size: 13px; padding: 0; }
.captcha-btn:disabled { opacity: 0.6; }
.checkbox label { display: flex; align-items: center; gap: 8px; font-size: 14px; font-weight: normal; }
.checkbox input { width: auto; }
.checkbox a { color: #667eea; text-decoration: none; }
.submit-btn { width: 100%; padding: 14px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; font-size: 16px; font-weight: 600; border-radius: 12px; margin-top: 10px; }
.submit-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.register-footer { text-align: center; margin-top: 25px; font-size: 14px; color: #666; }
.register-footer a { color: #667eea; text-decoration: none; }
</style>