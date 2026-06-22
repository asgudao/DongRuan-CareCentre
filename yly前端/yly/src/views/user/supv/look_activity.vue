<template>
  <div class="activity-image-management-container" style="padding: 20px; max-width: 1400px; margin: 0 auto;">
    <!-- 日期筛选 -->
    <el-space style="margin-bottom: 20px;">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
        @change="loadActivityData"
      />
    </el-space>

    <!-- 活动表格 -->
    <el-table :data="tableData" border style="margin-top: 20px;" @row-click="handleRowClick">
      <el-table-column label="活动ID" prop="id" width="100"></el-table-column>
      <el-table-column label="活动名称" prop="name" min-width="180"></el-table-column>
      <el-table-column label="活动地点" prop="location" width="150"></el-table-column>
      <el-table-column label="开始日期" prop="startDate" width="120"></el-table-column>
      <el-table-column label="结束日期" prop="endDate" width="120"></el-table-column>
      <el-table-column label="组织方" prop="organizer" width="130"></el-table-column>
      <el-table-column label="开始时间" :formatter="formatStartTime" prop="startTime" width="100"></el-table-column>
    </el-table>

    <!-- 图片预览区域（选中活动后显示） -->
    <el-card
      v-if="selectedActivity"
      shadow="hover"
      style="margin-top: 24px; padding: 24px;"
    >
      <template #header>
        <div class="section-header flex justify-between items-center">
          <div class="flex items-center gap-2 font-medium">
            <span>🖼️ {{ selectedActivity.name }} 的图片 ({{ imageList.length }})</span>
          </div>
          <el-button size="small" type="primary" @click="loadImageList" plain>刷新图片</el-button>
        </div>
      </template>

      <div v-if="imageList.length === 0" class="empty-state" style="min-height: 150px;">
        <el-empty description="暂无图片" :image-size="60" />
      </div>

      <div v-else class="image-grid grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <div
          v-for="(file, index) in imageList"
          :key="file.id || index"
          class="image-item bg-white border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow transition-shadow duration-200"
        >
          <el-image
            :src="filePath(file.path)"
            fit="cover"
            class="thumbnail w-full h-32 object-cover cursor-zoom-in"
            :preview-src-list="previewSrcList"
          />
          <div class="image-footer flex justify-between items-center p-2 text-xs text-gray-500 bg-gray-50">
            <span>{{ (file.size / 1024).toFixed(1) }} KB</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { get } from '@/axios'
import { ElMessage } from 'element-plus'
import { filePath } from '@/utils'

// 数据定义
const tableData = ref([])
const imageList = ref([])
const selectedActivity = ref(null)
const dateRange = ref([])

// 计算预览图列表
const previewSrcList = computed(() => imageList.value.map(f => filePath(f.path)))

// 格式化开始时间（只显示 HH:mm）
function formatStartTime(row, column, cellValue) {
  if (!cellValue) return '--'
  return cellValue.slice(0, 5) // HH:mm
}

// 加载活动数据
function loadActivityData() {
  const params = {}
  if (dateRange.value && dateRange.value.length === 2) {
    params.startDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }

  get('/activity/listByDateRange', params, (data) => {
    tableData.value = data || []
    if (data && data.length > 0) {
      ElMessage.success(`共加载 ${data.length} 条活动`)
    } else {
      ElMessage.info('无符合条件的活动')
    }
  }, () => {
    ElMessage.error('获取活动数据失败')
  })
}

// 行点击事件：选中活动并加载其图片
function handleRowClick(row) {
  selectedActivity.value = row
  loadImageList()
}

// 加载图片列表
function loadImageList() {
  if (!selectedActivity.value?.id) return

  get(`/sysFile/${selectedActivity.value.id}/images`, {}, (data) => {
    imageList.value = data || []
  }, () => {
    ElMessage.error('图片加载失败')
  })
}

// 初始化加载
loadActivityData()
</script>

<style scoped>
.grid {
  display: grid;
}
.grid-cols-2 { grid-template-columns: repeat(2, 1fr); }
.grid-cols-3 { grid-template-columns: repeat(3, 1fr); }
.grid-cols-4 { grid-template-columns: repeat(4, 1fr); }
.gap-4 { gap: 1rem; }
.flex { display: flex; }
.items-center { align-items: center; }
.justify-between { justify-content: space-between; }
.font-medium { font-weight: 500; }
.text-xs { font-size: 0.75rem; }

@media (max-width: 640px) {
  .grid-cols-2, .grid-cols-3, .grid-cols-4 {
    grid-template-columns: repeat(1, 1fr);
  }
}
</style>