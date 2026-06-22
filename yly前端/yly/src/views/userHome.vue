<template>
  <el-container class="wrapper">
    <!-- 侧边栏宽度固定为200px，但可以根据需要调整 -->
    <el-aside width="200px" style="background-color: #ffffff;">
      <LRmenu></LRmenu>
    </el-aside>
    
    <el-container>
      <!-- 头部高度设定为80px，背景使用蓝色渐变 -->
      <el-header style="height: 80px;">
        <h1 style="margin: 0; line-height: 80px;">东软养老院</h1>
      </el-header>
      
      <!-- 主体内容区域，占据剩余所有可用空间 -->
      <el-main style="padding: 20px;">
        <router-view />
      </el-main>
      
      <!-- 底部高度设定为60px，背景同样使用蓝色渐变 -->
      <el-footer style="height: 60px; display: flex; align-items: center; justify-content: center;">
        <h3 style="margin: 0;">感谢您对东软养老院的支持</h3>
      </el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import LRmenu from '@@/menu/userMenu'
</script>

<style scoped lang="scss">
.wrapper {
	height: 100vh;
	.el-header, .el-footer {
		background: linear-gradient(135deg, #38a169 0%, #48bb78 50%, #68d391 100%); /* 温暖绿色渐变 - 生机与关怀 */
		color: #ffffff;
		box-shadow: 0 4px 20px rgba(56, 161, 105, 0.2); /* 绿色系阴影 */
		
		h1, h3 {
			text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3); /* 增强文字可读性 */
		}
	}
	.el-main {
		background: linear-gradient(135deg, #f0fff4 0%, #f7fafc 100%); /* 极淡的绿白渐变 - 清新舒适 */
		padding: 20px;
	}
	.el-aside {
		background: linear-gradient(180deg, #ffffff 0%, #f7fafc 100%); /* 纯白到浅灰渐变 */
		box-shadow: 2px 0 15px rgba(56, 161, 105, 0.1); /* 绿色调阴影与主色呼应 */
		border-right: 2px solid #e6fffa; /* 极淡青色边框 */
	}
}

/* 添加整体背景容器 */
.wrapper {
	background: linear-gradient(135deg, #f0fff4 0%, #e6fffa 100%); /* 清新的绿青背景 */
}
</style>