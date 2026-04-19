<template>
  <div class="forgot-page">
    <div class="forgot-container">
      <div class="forgot-card">
        <div class="forgot-header">
          <div class="logo-icon">🔐</div>
          <h2>重置密码</h2>
          <p>请输入注册账号，我们将发送验证码</p>
        </div>

        <div class="steps">
          <div class="step" :class="{ active: step === 1, completed: step > 1 }">
            <div class="step-number">1</div>
            <span>验证身份</span>
          </div>
          <div class="step-line"></div>
          <div class="step" :class="{ active: step === 2, completed: step > 2 }">
            <div class="step-number">2</div>
            <span>重置密码</span>
          </div>
          <div class="step-line"></div>
          <div class="step" :class="{ active: step === 3 }">
            <div class="step-number">3</div>
            <span>完成</span>
          </div>
        </div>

        <!-- 步骤1：验证身份 -->
        <div v-show="step === 1">
          <div class="form-group">
            <label>账号</label>
            <input type="text" v-model="form.account" placeholder="请输入注册时的手机号/邮箱" />
          </div>

          <div class="form-group">
            <label>验证码</label>
            <div class="captcha-group">
              <input type="text" v-model="form.captcha" placeholder="请输入验证码" />
              <button type="button" @click="sendCaptcha" :disabled="captchaSending" class="captcha-btn">
                {{ captchaText }}
              </button>
            </div>
          </div>

          <button @click="verifyIdentity" class="next-btn">下一步</button>
        </div>

        <!-- 步骤2：重置密码 -->
        <div v-show="step === 2">
          <div class="form-group">
            <label>新密码</label>
            <input 
              :type="showPassword ? 'text' : 'password'" 
              v-model="form.newPassword" 
              placeholder="6-20位，包含字母和数字"
            />
            <span class="password-toggle" @click="showPassword = !showPassword">
              {{ showPassword ? '🙈' : '👁️' }}
            </span>
          </div>

          <div class="form-group">
            <label>确认新密码</label>
            <input 
              :type="showConfirmPassword ? 'text' : 'password'" 
              v-model="form.confirmPassword" 
              placeholder="请再次输入新密码"
            />
            <span class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">
              {{ showConfirmPassword ? '🙈' : '👁️' }}
            </span>
          </div>

          <button @click="resetPassword" class="next-btn">确认重置</button>
        </div>

        <!-- 步骤3：完成 -->
        <div v-show="step === 3" class="success-content">
          <div class="success-icon">✅</div>
          <h3>密码重置成功！</h3>
          <p>请使用新密码重新登录</p>
          <router-link to="/login" class="login-link">前往登录</router-link>
        </div>

        <div class="back-link">
          <router-link to="/login">← 返回登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const step = ref(1)
const showPassword = ref(false)
const showConfirmPassword = ref(false)
const captchaSending = ref(false)
const captchaText = ref('获取验证码')

const form = ref({
  account: '',
  captcha: '',
  newPassword: '',
  confirmPassword: ''
})

const sendCaptcha = () => {
  if (!form.value.account) {
    alert('请输入账号')
    return
  }
  captchaSending.value = true
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
  alert('验证码已发送（演示模式）')
}

const verifyIdentity = () => {
  if (!form.value.account) {
    alert('请输入账号')
    return
  }
  if (!form.value.captcha) {
    alert('请输入验证码')
    return
  }
  step.value = 2
}

const resetPassword = () => {
  if (!form.value.newPassword) {
    alert('请输入新密码')
    return
  }
  if (form.value.newPassword.length < 6 || form.value.newPassword.length > 20) {
    alert('密码长度需为6-20位')
    return
  }
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(form.value.newPassword)) {
    alert('密码需包含字母和数字')
    return
  }
  if (form.value.newPassword !== form.value.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  step.value = 3
}
</script>

<style scoped>
.forgot-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.forgot-container {
  width: 100%;
  max-width: 450px;
}

.forgot-card {
  background: white;
  border-radius: 30px;
  padding: 40px;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
}

.forgot-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.forgot-header h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 8px;
}

.forgot-header p {
  color: #999;
  font-size: 14px;
}

.steps {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 30px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  color: #999;
}

.step.active .step-number {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.step.completed .step-number {
  background: #10b981;
  color: white;
}

.step span {
  font-size: 12px;
  color: #999;
}

.step.active span {
  color: var(--primary);
}

.step-line {
  flex: 1;
  height: 2px;
  background: #e5e7eb;
}

.form-group {
  margin-bottom: 20px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary);
}

.password-toggle {
  position: absolute;
  right: 15px;
  top: 42px;
  cursor: pointer;
}

.captcha-group {
  display: flex;
  gap: 10px;
}

.captcha-group input {
  flex: 1;
}

.captcha-btn {
  width: 120px;
  background: #f3f4f6;
  color: #666;
  font-size: 13px;
  padding: 0;
}

.next-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  margin-top: 10px;
}

.success-content {
  text-align: center;
  padding: 20px 0;
}

.success-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.success-content h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.success-content p {
  color: #666;
  margin-bottom: 20px;
}

.login-link {
  display: inline-block;
  padding: 10px 30px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-decoration: none;
  border-radius: 25px;
}

.back-link {
  text-align: center;
  margin-top: 25px;
}

.back-link a {
  color: #999;
  text-decoration: none;
  font-size: 14px;
}
</style>