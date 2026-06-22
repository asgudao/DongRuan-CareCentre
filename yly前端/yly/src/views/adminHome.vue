<template>
	<div class="background-container">
	   <!-- 原有内容包裹在新容器中 -->
  <el-container class="wrapper">
		<el-aside width="200px">
			<PcMenu />
		</el-aside>
		<el-container>
			<el-header style="height: 110px;">
				<h1 style="font-size: 1.875rem;">欢迎来到东软颐养中心管理员界面</h1>
			</el-header>
			<el-main>
				<router-view />
			</el-main>
			<el-footer >
				<h3>2023东软颐养中心版权所有</h3>
			</el-footer>
		</el-container>
	</el-container>
	   </div>
</template>

<script setup>
import PcMenu from '@@/menu/adminMenu'
</script>

<style scoped lang="scss">
.wrapper {
	height: 100vh;
	.el-header, .el-footer {
		height: auto; /* 自动高度以适应内容 */
		background: linear-gradient(135deg, #2c5282 0%, #3182ce 50%, #4299e1 100%); /* 深蓝到浅蓝的渐变 - 专业稳重 */
		color: #ffffff; /* 纯白文字确保对比度 */
		display: flex;
		align-items: center;
		justify-content: center;
		letter-spacing: 0.5rem;
		padding: 0 20px; /* 增加内边距使内容不贴边 */
		box-sizing: border-box; /* 确保padding不会增加元素的实际宽度或高度 */
		box-shadow: 0 4px 20px rgba(44, 82, 130, 0.2); /* 添加阴影增强层次感 */
		
		h1, h3 {
			text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3); /* 文字阴影增强可读性 */
		}
	}
	.el-aside {
		background: linear-gradient(180deg, #f7fafc 0%, #edf2f7 100%); /* 温和的灰白渐变 - 中性舒适 */
		color: #2d3748; /* 深灰色文字 - 易读性佳 */
		border-right: 2px solid #e2e8f0; /* 浅灰色边框分割 */
		box-shadow: 2px 0 10px rgba(0, 0, 0, 0.05); /* 轻微阴影增加深度 */
	}
	.el-main {
		background-color: #fafafa; /* 极浅灰 - 舒适的阅读背景 */
	}
}

.background-container {
	height: 100vh;
	width: 100vw;
	background: linear-gradient(135deg, #f0f4f8 0%, #e2e8f0 100%); /* 柔和的灰蓝渐变背景 */
	background-size: cover;
	background-position: center;
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>