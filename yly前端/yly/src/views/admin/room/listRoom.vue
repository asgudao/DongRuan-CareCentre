<template>
  <div class="room-container">
    <!-- 页面标题与搜索 -->
    <div class="header-section">
      <h2 class="section-title">
        <el-icon><House /></el-icon>
        房间示意图
      </h2>
      <el-input
        v-model="searchQuery"
        placeholder="输入房间号搜索"
        class="search-input"
        prefix-icon="Search"
        clearable
      />
    </div>

    <!-- 骨架加载 -->
    <el-skeleton v-if="loading" :rows="4" :animated="true" style="margin: 16px 0;" />

    <!-- 无数据 -->
    <el-empty v-else-if="roomData.length === 0" description="暂无房间数据" />

    <!-- 房间卡片列表 -->
    <div class="room-grid">
      <el-card
        v-for="room in limitedRoomData"
        :key="room.id"
        :class="['room-card', getRoomStatusClass(room.status)]"
      >
        <div class="room-header">
          <el-icon class="house-icon"><House /></el-icon>
          <div class="room-number">房间号: {{ room.roomNumber }}</div>
          <el-badge :type="getStatusType(room.status)" class="status-badge">
            <el-icon>
              <component :is="getStatusIcon(room.status)" />
            </el-icon>
            {{ getStatusText(room.status) }}
          </el-badge>
        </div>
        <div class="room-details">
          <p>容量: {{ room.capacity }} 人</p>
          <p>价格: {{ room.price }} 元/月</p>
        </div>

        <!-- 操作按钮：右下角 修改/删除 -->
        <template #footer>
          <div class="card-footer">
            <el-button size="small" type="primary" @click="openEditDialog(room)">
              修改
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(room)">
              删除
            </el-button>
          </div>
        </template>
      </el-card>
    </div>

    <!-- 数据量提醒 -->
    <div v-if="roomData.length > MAX_ROOMS" class="data-limit-warning">
      <el-alert :title="`仅显示前 ${MAX_ROOMS} 个房间`" type="warning" :closable="false" />
    </div>

    <!-- 修改房间弹窗 -->
    <el-dialog v-model="editDialogVisible" title="修改房间信息" width="500px">
      <el-form ref="editFormRef" :model="editForm" :rules="editRules" label-width="80px">
        <el-form-item label="房间号" prop="roomNumber">
          <el-input v-model="editForm.roomNumber" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input v-model.number="editForm.capacity" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model.number="editForm.price" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="editForm.status" style="width: 100%">
            <el-option label="空闲" value="0" />
            <el-option label="已住满" value="1" />
            <el-option label="维护中" value="2" />
            <el-option label="已预订" value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { House, Check, Close, WarningFilled, Calendar } from '@element-plus/icons-vue';
import { get, post } from '@/axios'; // 确保 post 已封装
import { ElMessage, ElMessageBox } from 'element-plus';

// 常量定义
const MAX_ROOMS = 20;

// 响应式数据
const roomData = ref([]);
const loading = ref(false);
const searchQuery = ref('');

// 编辑相关
const editDialogVisible = ref(false);
const editFormRef = ref();
const editForm = ref({
  id: null,
  roomNumber: '',
  capacity: null,
  price: null,
  status: ''
});
const editRules = {
  roomNumber: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
  capacity: [
    { required: true, message: '请输入容量', trigger: 'blur' },
    { type: 'number', message: '容量必须是数字', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', message: '价格必须是数字', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
};

// 搜索 + 限制显示数量
const limitedRoomData = computed(() => {
  let filtered = roomData.value;
  if (searchQuery.value) {
    const query = searchQuery.value.trim().toLowerCase();
    filtered = filtered.filter(room =>
      room.roomNumber?.toLowerCase().includes(query)
    );
  }
  return filtered.slice(0, MAX_ROOMS);
});

// 状态配置
const STATUS_CONFIG = {
  0: { text: '空闲', type: 'success', icon: Check },
  1: { text: '已住满', type: 'danger', icon: Close },
  2: { text: '维护中', type: 'warning', icon: WarningFilled },
  3: { text: '已预订', type: 'info', icon: Calendar }
};

const getStatusText = (status) => {
  return STATUS_CONFIG[status]?.text || '未知';
};
const getStatusType = (status) => {
  return STATUS_CONFIG[status]?.type || 'info';
};
const getStatusIcon = (status) => {
  return STATUS_CONFIG[status]?.icon || null;
};
const getRoomStatusClass = (status) => {
  const classes = { 0: 'available', 1: 'occupied', 2: 'maintenance', 3: 'reserved' };
  return classes[status] || '';
};

// 打开编辑弹窗
const openEditDialog = (room) => {
  Object.assign(editForm.value, {
    id: room.id,
    roomNumber: room.roomNumber,
    capacity: room.capacity,
    price: room.price,
    status: room.status
  });
  editDialogVisible.value = true;
};

// 保存修改
const saveEdit = () => {
  editFormRef.value.validate(valid => {
    if (!valid) return;

    // 直接把 editForm.value 拆成 key-value 传（不需要 JSON.stringify）
    post('/room/update', editForm.value, () => {
      ElMessage.success('修改成功');
      const index = roomData.value.findIndex(r => r.id === editForm.value.id);
      if (index !== -1) {
        Object.assign(roomData.value[index], editForm.value);
      }
      editDialogVisible.value = false;
    });
  });
};

// 删除房间
const handleDelete = (room) => {
  ElMessageBox.confirm(`确定删除房间 ${room.roomNumber} 吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 直接传 { id: room.id }，不要包装成 data 字段
    post('/room/delete', { id: room.id }, () => {
      ElMessage.success('删除成功');
      roomData.value = roomData.value.filter(r => r.id !== room.id);
    });
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};


// 获取数据
onMounted(() => {
  get('/room/getAllRoom', {}, (content) => {
    if (Array.isArray(content)) {
      roomData.value = content.map(room => ({
        id: room.id,
        roomNumber: room.roomNumber,
        capacity: room.capacity,
        price: room.price,
        status: room.status
      }));
    } else {
      console.warn('⚠️ content 不是数组，使用模拟数据');
      roomData.value = [
        { id: 1, roomNumber: '101', capacity: 2, price: 200, status: 0 },
        { id: 2, roomNumber: '102', capacity: 3, price: 250, status: 1 },
        { id: 3, roomNumber: '201', capacity: 1, price: 180, status: 2 },
        { id: 4, roomNumber: '301', capacity: 2, price: 220, status: 3 }
      ];
    }
  });
});
</script>

<style scoped>
.room-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 10px;
}

.section-title {
  font-size: 24px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-input {
  width: 240px;
}

.room-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.room-card {
  transition: all 0.3s;
  border-radius: 12px;
  overflow: hidden;
}

.room-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.room-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background-color: #f0f2f5;
  border-bottom: 1px solid #ebeef5;
}

.house-icon {
  color: #409eff;
}

.room-number {
  font-weight: 600;
  color: #333;
}

.status-badge {
  font-size: 12px;
}

.room-details {
  padding: 16px;
  color: #666;
  font-size: 14px;
}

.room-details p {
  margin: 6px 0;
}

/* 房间状态顶部边框颜色 */
.room-card.available {
  border-top: 4px solid #00ff00; /* 绿色 - 空闲 */
}

.room-card.occupied {
  border-top: 4px solid #dd0000; /* 红色 - 已入住 */
}

.room-card.maintenance {
  border-top: 4px solid #ffee00; /* 黄色 - 维护中 */
}

.room-card.reserved {
  border-top: 4px solid #00aaff; /* 蓝色 - 已预订 */
}

.data-limit-warning {
  margin-top: 10px;
}

/* 操作按钮样式 */
.card-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 10px;
}
</style>