<template>
  <div class="sidebar-container">
    <el-menu
      router
      class="sidebar-menu"
      active-text-color="#059669"
      background-color="transparent"
      text-color="#64748b"
      :default-active="$route.path"
      unique-opened
    >
        <el-sub-menu index="sys">
          <template #title>老人报名</template>
          <el-menu-item index="/info">预约入住</el-menu-item>
          <el-menu-item index="/order_list">我的预约</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="supv">
          <template #title>老人监管</template>
			<el-menu-item index="/look_activity">查看活动</el-menu-item>
            <el-menu-item index="/look_check">查看体检</el-menu-item>
            <el-menu-item index="/look_training">查看训练</el-menu-item>
      </el-sub-menu>
  </el-menu>
  </div>
</template>

<script setup>
	
</script>

<style scoped lang="scss">
.sidebar-container {
  height: 100%;
  background: linear-gradient(180deg, #ffffff 0%, #f0fdf4 100%);
  display: flex;
  flex-direction: column;
  border-right: 1px solid #dcfce7;
  overflow-y: auto;
  position: relative;
  
  // 添加顶部装饰条
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #059669 0%, #10b981 100%);
    z-index: 10;
  }
}

.sidebar-menu {
  border: none;
  flex: 1;
  font-size: 16px;
  background-color: transparent;
  padding: 20px 0;

  // 主菜单标题样式
  :deep(.el-sub-menu__title) {
    height: 56px;
    line-height: 56px;
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
    margin: 8px 16px;
    border-radius: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;

    &:hover {
      background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
      color: #059669;
      transform: translateX(4px);
    }

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 4px;
      height: 24px;
      background: linear-gradient(180deg, #059669 0%, #10b981 100%);
      border-radius: 0 2px 2px 0;
      opacity: 0;
      transition: opacity 0.3s ease;
    }

    &:hover::before {
      opacity: 1;
    }
  }

  // 菜单项样式
  :deep(.el-menu-item) {
    height: 48px;
    line-height: 48px;
    font-size: 16px;
    color: #64748b;
    margin: 4px 24px;
    border-radius: 10px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    font-weight: 500;

    &:hover {
      background: linear-gradient(135deg, #d1fae5 0%, #bbf7d0 100%);
      color: #065f46;
      transform: translateX(4px);
    }

    &.is-active {
      background: linear-gradient(135deg, #059669 0%, #10b981 100%);
      color: #ffffff !important;
      font-weight: 600;
      box-shadow: 0 4px 12px rgba(5, 150, 105, 0.3);
      
      &::after {
        content: '';
        position: absolute;
        right: 12px;
        top: 50%;
        transform: translateY(-50%);
        width: 6px;
        height: 6px;
        background: #ffffff;
        border-radius: 50%;
      }
    }
  }

  // 子菜单展开时的背景
  :deep(.el-sub-menu.is-opened) {
    > .el-sub-menu__title {
      background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
      color: #059669;
    }
  }
}

// 滚动条美化
.sidebar-container::-webkit-scrollbar {
  width: 6px;
}

.sidebar-container::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-container::-webkit-scrollbar-thumb {
  background: linear-gradient(180deg, #bbf7d0 0%, #86efac 100%);
  border-radius: 3px;
  
  &:hover {
    background: linear-gradient(180deg, #86efac 0%, #4ade80 100%);
  }
}
</style>