<template>
  <!-- 登录整体容器，增加样式使居中 -->
  <div class="login-container" :class="{ 'admin-mode': selectedIdentity === 'admin' }"> 
    <!-- 页面标题，改为"医生登录系统" -->
    <div class="title-section">
      <h1>颐养天年养老院</h1> 
      <p class="sub-title">岁月温情处，夕阳暖心家</p >
      <!-- 动态角色指示器 -->
      <div class="role-indicator">
        <transition name="role-fade" mode="out-in">
          <span v-if="selectedIdentity === 'user'" key="user" class="role-text user">👤 用户登录</span>
          <span v-else key="admin" class="role-text admin">🛡️ 管理员登录</span>
        </transition>
      </div>
    </div>
    
    <!-- 身份选择区域 -->
    <div class="identity-select">
      <el-radio-group v-model="selectedIdentity" size="medium" @change="onIdentityChange">
        <el-radio label="user">用户登录</el-radio>
        <el-radio label="admin">管理员登录</el-radio>
      </el-radio-group>
    </div>

    <!-- 表单内容区域 - 添加动画包装 -->
    <transition name="form-slide" mode="out-in">
      <div :key="selectedIdentity" class="form-wrapper">
        <el-form
          ref="formObj"
          :rules="rules"
          :model="zxyform"
          label-width="80px"
        >
          <!-- 登录名输入项 -->
          <el-form-item label="登录名" prop="username"> 
            <el-input
              v-model="zxyform.username"
              :placeholder="selectedIdentity === 'admin' ? '请输入管理员账号' : '请输入手机号/电子邮件'"
            ></el-input>
          </el-form-item>
          <!-- 登录密码输入项，增加显示密码切换 -->
          <el-form-item label="登录密码" prop="password"> 
            <el-input
              v-model="zxyform.password"
              :placeholder="selectedIdentity === 'admin' ? '请输入管理员密码' : '请输入登录密码'"
              show-password
            ></el-input>
          </el-form-item>
          <!-- 记住登录状态与忘记密码 -->
		  <transition name="extra-fade">
		    <div v-if="selectedIdentity === 'user'"> 
          <el-form-item label-width="0px"> 
            <div style="display: flex; align-items: center;">
              <el-link type="primary" @click="findpwd" style="margin-left: auto;">忘记密码？</el-link>
            </div>
          </el-form-item>
		  </div>
		  </transition>
          <!-- 登录按钮 -->
          <el-form-item label-width="0px"> 
            <el-button
              style="width: 100%;"
              type="primary"
              @click="handleLogin"
              class="login-btn"
            >
              {{ selectedIdentity === 'admin' ? '管理员登录' : '用户登录' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <!-- 注册相关：仅当身份为user时显示 --> 
        <transition name="extra-fade">
          <div class="extra-op" v-if="selectedIdentity === 'user'"> 
            <el-link type="primary" @click="regist">注册新用户</el-link>
            <el-dialog
              v-model="showDialog"
              title="用户注册"
              width="550px"
              append-to-body>
              <Editor v-model:showtoedit="showDialog" />
            </el-dialog>
          </div>
        </transition>
      </div>
    </transition>
    
    <!-- 版权信息 -->
    <p class="copyright">© 2025 颐养天年养老院 版权所有</p > 
  </div>
</template>

<script setup>
import { reactive, ref, watch } from 'vue'
// 假设你的 Editor 组件路径正确，若不需要注册弹窗可调整
import Editor from '@/views/user/regist' 
import { post } from '@/axios'
import { useTokenStore } from '@/stores'
import router from '@/router'

const tokenStore = useTokenStore()
const formObj = ref()
const emits = defineEmits(['update:r'])
// 新增：身份选择变量
const selectedIdentity = ref('user')

// 表单数据
const zxyform = reactive({ 
  username: '',
  password: ''
})
// 记住登录状态
const remember = ref(false) 
// 表单校验规则
const rules = { 
  username: [
    {
      required: true,
      message: '请输入登录名',
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: '请输入登录密码',
      trigger: 'blur'
    }
  ]
}

// 忘记密码方法
const findpwd = () => { 
  emits('update:r', 180)
}
// 注册弹窗控制，若不需要可简化
const showDialog = ref(false) 
const regist = () => {
  showDialog.value = true
}

// 登录方法
const login = () => { 
  post('/user/login', zxyform, (content) => {
    tokenStore.setToken(content)
    router.push('/userHome')
  }, formObj)
}
const adminLogin = () => { 
  post('/admin/login', zxyform, (content) => {
    tokenStore.setToken(content)
    router.push('/adminHome')
  }, formObj)
}
// 统一登录处理方法
const handleLogin = () => {
  // 先校验表单合法性
  formObj.value.validate((isValid) => {
    if (isValid) {
      // 根据选中的身份调用不同登录方法
      if (selectedIdentity.value === 'user') {
        login()
      } else {
        adminLogin()  // 此处需与方法名一致（修复原大小写问题）
      }
    }
  })
}

// 新增：身份切换动画控制
const onIdentityChange = (value) => {
  // 清空表单数据，避免切换时数据混乱
  zxyform.username = ''
  zxyform.password = ''
  // 清除表单验证状态
  if (formObj.value) {
    formObj.value.clearValidate()
  }
}
</script>

<style scoped lang="scss">
/* 登录容器样式，使内容居中 */
.login-container { 
  text-align: center;
  margin: 0;
  width: auto;
  padding: 40px;
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  position: relative;
  overflow: hidden;
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  
  // 添加背景渐变动画
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 200%;
    height: 100%;
    background: linear-gradient(135deg, 
      rgba(49, 130, 206, 0.03) 0%, 
      rgba(59, 130, 246, 0.05) 50%, 
      rgba(37, 99, 235, 0.03) 100%);
    transition: transform 0.8s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: -1;
  }
  
  // 管理员模式的背景色变化
  &.admin-mode {
    &::before {
      transform: translateX(50%);
      background: linear-gradient(135deg, 
        rgba(239, 68, 68, 0.03) 0%, 
        rgba(220, 38, 38, 0.05) 50%, 
        rgba(185, 28, 28, 0.03) 100%);
    }
  }
}

// 标题区域动画
.title-section {
  position: relative;
  margin-bottom: 24px;
}

// 副标题样式 - 优雅现代
.sub-title { 
  margin-bottom: 28px;
  color: #64748b;
  font-size: 0.875rem;
  font-weight: 400;
  letter-spacing: 0.01em;
}

// 角色指示器
.role-indicator {
  height: 32px;
  margin: 16px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.role-text {
  font-size: 0.9rem;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  
  &.user {
    background: linear-gradient(135deg, rgba(34, 197, 94, 0.1), rgba(22, 163, 74, 0.15));
    color: #16a34a;
    box-shadow: 0 4px 12px rgba(34, 197, 94, 0.2);
  }
  
  &.admin {
    background: linear-gradient(135deg, rgba(239, 68, 68, 0.1), rgba(220, 38, 38, 0.15));
    color: #dc2626;
    box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
  }
}

// 角色切换动画
.role-fade-enter-active,
.role-fade-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.role-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px) scale(0.95);
}

.role-fade-leave-to {
  opacity: 0;
  transform: translateY(10px) scale(0.95);
}

// 表单滑动动画
.form-slide-enter-active,
.form-slide-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.form-slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.form-slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

// 表单样式 - 现代简洁
.el-form { 
  margin-top: 24px;
  
  :deep(.el-form-item) {
    margin-bottom: 20px;
      
    .el-form-item__label {
      color: #374151;
      font-weight: 600;
      font-size: 0.875rem;
      height: 42px; /* 设置标签高度与输入框相同 */
      line-height: 42px; /* 设置行高与输入框高度相同，实现垂直居中 */
      margin-bottom: 0; /* 移除底部间距 */
      display: flex;
      align-items: center; /* 使用 flexbox 垂直居中 */
    }
    
    .el-input {
      .el-input__wrapper {
        border-radius: 10px; /* 圆角样式 */
        border: 2px solid #e2e8f0;
        box-shadow: none;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        background: #ffffff;
        min-height: 42px;
        
        &:hover {
          border-color: #cbd5e1;
        }
        
        &.is-focus {
          border-color: #3182ce;
          box-shadow: 0 0 0 3px rgba(49, 130, 206, 0.1);
        }
      }
      
      .el-input__inner {
        color: #1f2937;
        font-size: 0.875rem;
        padding: 12px 14px;
        height: 42px;
        line-height: 1.4;
        
        &::placeholder {
          color: #9ca3af;
          font-weight: 400;
          font-size: 0.875rem;
        }
      }
    }
  }
}

// 忘记密码链接样式
:deep(.el-link) {
  color: #3182ce;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  
  &:hover {
    color: #2563eb;
    text-decoration: underline;
  }
}

// 额外操作淡入淡出
.extra-fade-enter-active,
.extra-fade-leave-active {
  transition: all 0.3s ease;
}

.extra-fade-enter-from,
.extra-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

// 额外操作（注册等）样式 - 现代按钮风格
.extra-op { 
  margin-top: 20px;
  
  :deep(.el-link) {
    display: inline-block;
    padding: 10px 20px;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border: 1px solid #cbd5e1;
    border-radius: 10px; /* 圆角样式 */
    color: #475569;
    text-decoration: none;
    font-weight: 500;
    font-size: 0.875rem;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    &:hover {
      background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
      color: #1e293b;
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      border-color: #94a3b8;
    }
  }
}

// 页面标题样式 - 现代化设计
h1 { 
  margin-bottom: 8px;
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #1e293b 0%, #475569 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.01em;
  transition: all 0.4s ease;
}

// 身份选择区域样式 - 现代卡片风格
.identity-select {
  margin-bottom: 28px;
  
  :deep(.el-radio-group) {
    display: flex;
    background: #f8fafc;
    border-radius: 10px;
    padding: 3px;
    border: 1px solid #e2e8f0;
    position: relative;
    overflow: hidden;
    
    // 添加选中项的滑动背景
    &::before {
      content: '';
      position: absolute;
      top: 3px;
      left: 3px;
      width: calc(50% - 3px);
      height: calc(100% - 6px);
      background: linear-gradient(135deg, #3182ce 0%, #4299e1 100%);
      border-radius: 7px;
      transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
      box-shadow: 0 4px 12px rgba(49, 130, 206, 0.3);
      z-index: 0;
    }
    
    .el-radio {
      flex: 1;
      margin: 0;
      position: relative;
      z-index: 1;
      
      .el-radio__input {
        display: none;
      }
      
      .el-radio__label {
        width: 100%;
        padding: 10px 16px;
        border-radius: 7px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        font-weight: 500;
        font-size: 0.875rem;
        color: #64748b;
        cursor: pointer;
        text-align: center;
        position: relative;
        z-index: 2;
      }
      
      &.is-checked {
        .el-radio__label {
          color: #ffffff;
          text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
        }
      }
      
      &:not(.is-checked) {
        .el-radio__label:hover {
          color: #1e293b;
          background: rgba(255, 255, 255, 0.5);
        }
      }
    }
  }
}

// 登录按钮动态颜色
.login-btn {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

// 管理员模式下的样式调整
.admin-mode {
  .role-text.admin {
    animation: pulse-red 2s infinite;
  }
  
  :deep(.el-radio-group) {
    &::before {
      transform: translateX(100%);
      background: linear-gradient(135deg, #dc2626 0%, #ef4444 100%) !important;
      box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3) !important;
    }
  }
  
  :deep(.el-button--primary) {
    background: linear-gradient(135deg, #dc2626 0%, #ef4444 100%) !important;
    
    &:hover {
      background: linear-gradient(135deg, #b91c1c 0%, #dc2626 100%) !important;
      box-shadow: 0 8px 25px rgba(220, 38, 38, 0.4) !important;
    }
  }
  
  :deep(.el-input__wrapper.is-focus) {
    border-color: #dc2626 !important;
    box-shadow: 0 0 0 3px rgba(220, 38, 38, 0.1) !important;
  }
}

// 脉动动画
@keyframes pulse-red {
  0%, 100% {
    box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
  }
  50% {
    box-shadow: 0 4px 20px rgba(239, 68, 68, 0.4);
  }
}

// 版权信息样式 - 精致现代
.copyright { 
  margin-top: 24px;
  color: #9ca3af;
  font-size: 0.75rem;
  font-weight: 400;
  letter-spacing: 0.025em;
}

// 对话框样式优化
:deep(.el-dialog) {
  border-radius: 16px;
  
  .el-dialog__header {
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    border-radius: 16px 16px 0 0;
    
    .el-dialog__title {
      font-weight: 600;
      color: #1e293b;
    }
  }
}
</style>