<template>
	<div class="wrapper">
		<!-- 背景装饰元素 -->
		<div class="bg-decorations">
			<div class="circle circle-1"></div>
			<div class="circle circle-2"></div>
			<div class="circle circle-3"></div>
			<div class="floating-shapes">
				<div class="shape shape-1"></div>
				<div class="shape shape-2"></div>
				<div class="shape shape-3"></div>
			</div>
		</div>
		
		<!-- 主要内容区域 -->
		<div class="main-content">
			<!-- 品牌标识区域 -->
			<div class="brand-section">
				<div class="logo-container">
					<div class="logo-icon">
						<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
							<path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
							<path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
							<path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
						</svg>
					</div>
					<h1 class="brand-title">颐养天年</h1>
					<p class="brand-subtitle">专业养老服务管理平台</p>
				</div>
			</div>
			
			<!-- 登录卡片区域 -->
			<div class="login-section">
				<div class="content" :style="{ transform: 'rotateY(' + r + 'deg)' }">
					<Login class="login-card" v-model:r="r" />
				</div>
			</div>
		</div>
		
		<!-- 底部信息 -->
		<div class="footer-info">
			<p>© 2025 颐养天年养老院 · 让每一个夕阳都温暖如春</p>
		</div>
	</div>
</template>

<script setup>
import Login from '@/views/login'
import { ref } from 'vue'
const r = ref(0)
</script>

<style scoped lang="scss">
.wrapper {
	height: 100vh;
	width: 100vw;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	perspective: 1000px;
	position: relative;
	overflow: hidden;
	
	// 现代渐变背景
	background: linear-gradient(135deg, 
		#667eea 0%, 
		#764ba2 25%, 
		#f093fb 50%, 
		#f5576c 75%, 
		#4facfe 100%);
	background-size: 400% 400%;
	animation: gradientShift 15s ease infinite;
}

// 渐变动画
@keyframes gradientShift {
	0% { background-position: 0% 50%; }
	50% { background-position: 100% 50%; }
	100% { background-position: 0% 50%; }
}

// 背景装饰元素
.bg-decorations {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	pointer-events: none;
	z-index: 1;
}

// 浮动圆形装饰
.circle {
	position: absolute;
	border-radius: 50%;
	background: rgba(255, 255, 255, 0.1);
	backdrop-filter: blur(10px);
	animation: float 20s infinite ease-in-out;
	
	&.circle-1 {
		width: 200px;
		height: 200px;
		top: 10%;
		left: 10%;
		animation-delay: 0s;
	}
	
	&.circle-2 {
		width: 300px;
		height: 300px;
		top: 60%;
		right: 10%;
		animation-delay: -7s;
	}
	
	&.circle-3 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		left: 20%;
		animation-delay: -14s;
	}
}

@keyframes float {
	0%, 100% { transform: translate(0, 0) rotate(0deg); }
	33% { transform: translate(30px, -30px) rotate(120deg); }
	66% { transform: translate(-20px, 20px) rotate(240deg); }
}

// 几何形状装饰
.floating-shapes {
	.shape {
		position: absolute;
		background: rgba(255, 255, 255, 0.15);
		animation: rotate 25s linear infinite;
		
		&.shape-1 {
			width: 60px;
			height: 60px;
			top: 25%;
			right: 25%;
			transform: rotate(45deg);
			animation-delay: 0s;
		}
		
		&.shape-2 {
			width: 80px;
			height: 80px;
			bottom: 30%;
			right: 15%;
			clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
			animation-delay: -8s;
		}
		
		&.shape-3 {
			width: 40px;
			height: 40px;
			top: 15%;
			left: 60%;
			border-radius: 50% 0;
			animation-delay: -16s;
		}
	}
}

@keyframes rotate {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

// 主要内容区域
.main-content {
	display: flex;
	width: 100%;
	max-width: 1400px;
	height: 100%;
	align-items: center;
	justify-content: space-between;
	padding: 0 80px;
	z-index: 2;
	position: relative;
}

// 品牌标识区域
.brand-section {
	flex: 1;
	padding-right: 60px;
}

.logo-container {
	text-align: left;
}

.logo-icon {
	width: 80px;
	height: 80px;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.1));
	border-radius: 20px;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 24px;
	backdrop-filter: blur(20px);
	border: 1px solid rgba(255, 255, 255, 0.2);
	box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
	
	svg {
		width: 40px;
		height: 40px;
		color: #ffffff;
		filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
	}
}

.brand-title {
	font-size: 4rem;
	font-weight: 800;
	color: #ffffff;
	margin: 0 0 16px 0;
	text-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
	letter-spacing: -0.02em;
	line-height: 1.1;
	
	// 渐变文字效果
	background: linear-gradient(135deg, #ffffff 0%, #f1f5f9 100%);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
	background-clip: text;
}

.brand-subtitle {
	font-size: 1.25rem;
	color: rgba(255, 255, 255, 0.9);
	margin: 0;
	font-weight: 400;
	text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
	letter-spacing: 0.02em;
}

// 登录区域
.login-section {
	flex: 0 0 450px;
}

.content {
	transition: transform 0.8s cubic-bezier(0.4, 0, 0.2, 1);
	transform-style: preserve-3d;
}

.login-card {
	background: rgba(255, 255, 255, 0.95);
	backdrop-filter: blur(20px);
	border-radius: 24px;
	border: 1px solid rgba(255, 255, 255, 0.2);
	box-shadow: 
		0 25px 50px -12px rgba(0, 0, 0, 0.25),
		0 0 0 1px rgba(255, 255, 255, 0.05);
	overflow: hidden;
	transform: translateZ(0);
	
	// 卡片内容区域样式增强
	:deep(.login-container) {
		background: transparent;
		box-shadow: none;
		border-radius: 0;
		padding: 40px;
		margin: 0;
		width: auto;
	}
}

// 底部信息
.footer-info {
	position: absolute;
	bottom: 30px;
	left: 50%;
	transform: translateX(-50%);
	z-index: 2;
	
	p {
		color: rgba(255, 255, 255, 0.8);
		font-size: 0.875rem;
		margin: 0;
		text-align: center;
		text-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
		font-weight: 300;
		letter-spacing: 0.025em;
	}
}

// 响应式设计
@media (max-width: 1200px) {
	.main-content {
		flex-direction: column;
		text-align: center;
		padding: 40px;
		gap: 40px;
	}
	
	.brand-section {
		padding-right: 0;
	}
	
	.brand-title {
		font-size: 3rem;
	}
	
	.login-section {
		flex: none;
	}
}

@media (max-width: 768px) {
	.main-content {
		padding: 20px;
	}
	
	.brand-title {
		font-size: 2.5rem;
	}
	
	.login-card {
		:deep(.login-container) {
			padding: 30px 20px;
		}
	}
	
	.circle {
		display: none;
	}
}
</style>