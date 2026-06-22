<template>
  <div class="sidebar-container">
    <el-menu
      router
      class="sidebar-menu"
      active-text-color="#2563eb"
      background-color="transparent"
      text-color="#64748b"
      :default-active="$route.path"
      unique-opened
    >
          <el-sub-menu index="adminsys">
                  <template #title>管理员菜单</template>
                  <el-menu-item index="/adminInfo">预约入住</el-menu-item>
                  <el-menu-item index="/adminOrder_list">所有老人</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="pms">
                  <template #title>业务管理</template>
                  <el-menu-item-group>
                      <template #title>生活娱乐</template>
                      <el-menu-item index="/addActivity">添加活动</el-menu-item>
                      <el-menu-item index="/listActivity">活动安排</el-menu-item>
					  <el-menu-item index="/activityPicture">活动记录</el-menu-item>
                  </el-menu-item-group>
                      <el-menu-item-group>
                          <template #title>医疗康复</template>
                          <el-menu-item index="/add_training">新增康复训练</el-menu-item>
                          <el-menu-item index="/training_list">查看康复训练</el-menu-item>
                      </el-menu-item-group>
                      <el-menu-item-group>
                          <template #title>体检信息</template>
                          <el-menu-item index="/check_update">上传日常体检</el-menu-item>
                          <el-menu-item index="/check_list">查看日常体检</el-menu-item>
                      </el-menu-item-group>
              </el-sub-menu>
			  <el-sub-menu index="room">
			          <template #title>房间管理</template>
			          <el-menu-item index="/addRoom">添加房间</el-menu-item>
			          <el-menu-item index="/listRoom">查看房间</el-menu-item>
			      </el-sub-menu>
			 <el-sub-menu index="room">
				     <template #title>护工管理</template>
				     <el-menu-item index="/addNurse">添加护工</el-menu-item>
				     <el-menu-item index="/listNurse">查看护工</el-menu-item>
				 </el-sub-menu>
          </el-menu>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
const route = useRoute()
// 可选：根据当前路由设置 default-active
</script>

<style scoped lang="scss">
.sidebar-container {
  height: 100%;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e2e8f0;
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
    background: linear-gradient(90deg, #3182ce 0%, #4299e1 100%);
    z-index: 10;
  }
}

.sidebar-menu {
  border: none;
  flex: 1;
  font-size: 14px;
  background-color: transparent;
  padding: 20px 0;

  // 主菜单标题样式
  :deep(.el-sub-menu__title) {
    height: 56px;
    line-height: 56px;
    font-size: 16px;
    font-weight: 600;
    color: #1e293b;
    margin: 8px 16px;
    border-radius: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    overflow: hidden;

    &:hover {
      background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
      color: #3182ce;
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
      background: linear-gradient(180deg, #3182ce 0%, #4299e1 100%);
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
    height: 44px;
    line-height: 44px;
    font-size: 14px;
    color: #64748b;
    margin: 4px 24px;
    border-radius: 8px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    font-weight: 500;

    &:hover {
      background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
      color: #2563eb;
      transform: translateX(4px);
    }

    &.is-active {
      background: linear-gradient(135deg, #3182ce 0%, #4299e1 100%);
      color: #ffffff !important;
      font-weight: 600;
      box-shadow: 0 4px 12px rgba(49, 130, 206, 0.3);
      
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

  // 菜单组标题样式
  :deep(.el-menu-item-group__title) {
    font-size: 12px;
    color: #94a3b8;
    font-weight: 600;
    padding: 16px 24px 8px 24px !important;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    position: relative;

    &::after {
      content: '';
      position: absolute;
      left: 24px;
      bottom: 4px;
      width: 20px;
      height: 2px;
      background: linear-gradient(90deg, #cbd5e1 0%, transparent 100%);
      border-radius: 1px;
    }
  }

  // 子菜单内容区域
  :deep(.el-sub-menu__title) {
    &.is-active {
      background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
      color: #3182ce;
    }
  }

  // 子菜单展开时的背景
  :deep(.el-sub-menu.is-opened) {
    > .el-sub-menu__title {
      background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
      color: #3182ce;
    }
  }
  
  // 移除默认边框
  :deep(.el-menu-item-group) {
    .el-menu-item {
      padding-left: 40px !important;
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
  background: linear-gradient(180deg, #cbd5e1 0%, #94a3b8 100%);
  border-radius: 3px;
  
  &:hover {
    background: linear-gradient(180deg, #94a3b8 0%, #64748b 100%);
  }
}
</style>