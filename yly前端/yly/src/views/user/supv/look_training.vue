<template>
  <div>
    <el-space class="section-select">
      <span>选择老人：</span>
      <el-select
        v-model="selectedElderId"
        placeholder="请选择老人"
        class="select-elder"
        @change="loadTrainings"
      >
        <el-option
          v-for="elder in elderList"
          :key="elder.id"
          :label="elder.name"
          :value="elder.id"
        />
      </el-select>
    </el-space>

    <div v-if="selectedElder" class="elder-info section-elder-info">
      <el-descriptions :column="2" size="medium" :labelStyle="{ fontWeight: 'bold', color: '#333' }">
        <el-descriptions-item label="姓名">{{ selectedElder.name }}</el-descriptions-item>
        <el-descriptions-item label="房间号">
          <el-tag type="warning" size="small">{{ selectedElder.roomNumber || '未分配' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="负责护士">
          <el-tag type="success" size="small">
            {{ getNurseName(selectedElder.nurseId) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-table
      v-if="selectedElderId"
      :data="trainings"
      border
      class="section-table"
    >
      <el-table-column label="训练名称" prop="plan_name">
		  <template #default="scope">
		    {{ scope.row.planName}}
		  </template>
	  </el-table-column>
      <el-table-column label="开始日期" prop="start_date">
        <template #default="scope">
          {{ formatDate(scope.row.startDate) }}
        </template>
      </el-table-column>
      <el-table-column label="结束日期" prop="end_date">
        <template #default="scope">
          {{ formatDate(scope.row.endDate)}}
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row)">
            {{ getStatusLabel(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="组织者" prop="nurse_name">
	  <template #default="scope">
	    {{ scope.row.organizer}}
	  </template></el-table-column>
      <el-table-column label="单次训练时长（分钟）" prop="duration" />
    </el-table>

    <el-empty
      v-else
      description="请选择一位老人查看其训练参与情况"
      class="section-empty"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { get } from '@/axios'
import dayjs from 'dayjs'

const selectedElderId = ref('')
const elderList = ref([])
const selectedElder = ref(null)
const trainings = ref([])
const nurses = ref([])

// 获取当前日期，用于状态判断
const now = computed(() => dayjs())

onMounted(() => {
  loadElderList()
  getNurses()
})

function loadElderList() {
  get('/elder/list', {}, (content) => {
    elderList.value = content || []
    console.log('名下老人列表：', content)
  })
}

function getNurses() {
  get('/nurse/getNurse', {}, (content) => {
    nurses.value = Array.isArray(content) ? content : []
    console.log('护士列表加载完成，共', nurses.value.length, '人')
  })
}

function getNurseName(nurseId) {
  if (!nurseId) return '未分配'
  const nurse = nurses.value.find(n => n.id === nurseId)
  return nurse ? nurse.name : '未分配'
}

function loadTrainings() {
  // 查找选定的老人
  const elder = elderList.value.find(e => e.id === selectedElderId.value);
  selectedElder.value = elder || null;

  if (elder) {
    // 发送GET请求到后端，获取培训列表
    get('/training/listByElderId', { elderId: elder.id }, (content) => {
      // 确保返回的内容是数组，然后赋值给trainings.value
      trainings.value = Array.isArray(content) ? content : [];
    });
  } else {
    // 如果没有找到对应的老人，则清空培训列表
    trainings.value = [];
  }
}

// 格式化日期显示
function formatDate(date) {
  return date ? dayjs(date).format('YYYY-MM-DD') : ''
}

// 获取状态标签
function getStatusLabel(training) {
  const startDate = dayjs(training.startDate)
  const endDate = training.endDate ? dayjs(training.endDate) : null
  
  if (now.value.isBefore(startDate)) {
    return '未开始'
  } else if (endDate && now.value.isAfter(endDate)) {
    return '已完成'
  } else {
    return '进行中'
  }
}

// 获取状态标签类型（用于el-tag的type属性）
function getStatusType(training) {
  const status = getStatusLabel(training)
  switch (status) {
    case '已完成':
      return 'success'
    case '进行中':
      return 'primary'
    case '未开始':
      return 'info'
    default:
      return 'info'
  }
}
</script>

<style scoped>
/* 整体布局间距 */
.section-select {
  margin-bottom: 20px;
}

.section-elder-info {
  margin: 20px 0;
  padding: 15px;
  background-color: #e5f5ff;
  border-radius: 8px;
  border: 3px solid #409eff;
}

.section-elder-info ::v-deep(.el-descriptions__body) {
  background-color: #e5f5ff;
}

.section-table {
  margin-top: 20px;
}

.section-empty {
  margin-top: 40px;
}

/* 选择器宽度统一 */
.select-elder {
  width: 200px;
}

/* 动画效果 */
.elder-info {
  animation: slideInUp 0.3s ease-out;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>