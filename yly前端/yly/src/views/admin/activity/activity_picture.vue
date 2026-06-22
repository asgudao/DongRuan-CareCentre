<template>
  <div class="image-management-container">
    <!-- 活动选择 -->
    <el-form :inline="true" class="activity-selector mb-4" @submit.prevent>
      <el-form-item label="选择活动" label-width="80px">
        <el-select
          v-model="form.activityId"
          placeholder="请选择活动"
          @change="loadActivityDetail"
          filterable
          clearable
          style="min-width: 300px"
        >
          <el-option
            v-for="act in activityList"
            :key="act.id"
            :label="act.name"
            :value="act.id"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <!-- 空状态：未选择活动 -->
    <el-empty
      v-if="!form.selectedActivity"
      description="请先选择一个活动以管理图片"
      :image-size="100"
      class="py-10"
    />

    <!-- 活动详情卡片 -->
    <el-card v-if="form.selectedActivity" shadow="never" class="activity-card mb-6" :body-style="{ padding: '16px' }">
      <template #header>
        <div class="card-header flex items-center gap-2 text-lg font-medium">
          <span>📌 活动详情</span>
        </div>
      </template>
      <el-descriptions :column="1" size="medium" class="desc" :border="false">
        <el-descriptions-item label="活动名称">{{ form.selectedActivity.name }}</el-descriptions-item>
        <el-descriptions-item label="活动时间">
          {{ form.selectedActivity.startDate }} 至 {{ form.selectedActivity.endDate }}
        </el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ form.selectedActivity.location }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 图片管理区域 -->
    <div v-if="form.selectedActivity" class="management-layout flex gap-6">
      <!-- 上传区域 -->
      <el-card class="upload-area" shadow="hover" :body-style="{ padding: '24px' }">
        <template #header>
          <div class="section-header flex justify-between items-center">
            <div class="flex items-center gap-2 font-medium">
              <span>📤 上传图片</span>
            </div>
          </div>
        </template>
        <el-upload
          class="upload-box"
          :action="uploadUrl"
          :data="{ bucket: 'activity', activityId: form.activityId }"
          :on-success="handleUploadSuccess"
          :on-error="() => ElMessage.error('上传失败')"
          :show-file-list="false"
          accept="image/*"
          multiple
          drag
        >
          <el-icon class="el-icon--upload"><Plus /></el-icon>
          <div class="el-upload__text">拖拽图片到此处，或 <em>点击选择文件</em></div>
          <template #tip>
            <div class="el-upload__tip text-sm text-gray-500 mt-2">支持 JPG/PNG/GIF 等格式，可多选</div>
          </template>
        </el-upload>
      </el-card>

      <!-- 预览区域 -->
      <el-card class="preview-area" shadow="hover" :body-style="{ padding: '24px' }">
        <template #header>
          <div class="section-header flex justify-between items-center align-items-center">
            <div class="flex items-center gap-2 font-medium">
              <span>🖼️ 图片预览 ({{ form.imageList.length }})</span>
            </div>
            <el-button size="small" type="primary" @click="loadImageList" plain class="align-self-center">刷新</el-button>
          </div>
        </template>

        <div v-if="form.imageList.length === 0" class="empty-state">
          <el-empty description="暂无图片上传" :image-size="60" />
        </div>

        <div v-else class="image-grid grid grid-cols-2 sm:grid-cols-3 gap-4">
          <div
            v-for="(file, index) in form.imageList"
            :key="file.id || index"
            class="image-item bg-white border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow transition-shadow duration-200"
          >
            <el-image
              :src="filePath(file.path)"
              :alt="`图片 ${index + 1}`"
              :title="`大小: ${(file.size / 1024).toFixed(1)} KB`"
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
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { get, post } from '@/axios'
import { filePath } from '@/utils'

// 图标引入（提升语义化）
import { Collection, Upload, Picture, Plus, Refresh, Delete } from '@element-plus/icons-vue'

const form = ref({
  activityId: '',
  selectedActivity: null,
  imageList: []
})

const activityList = ref([])
const uploadUrl = ref('http://127.0.0.1:8080/sysFile/upload')

// 计算预览图源列表（避免重复计算）
const previewSrcList = computed(() => form.value.imageList.map(f => filePath(f.path)))

onMounted(() => {
  get('/activity/list', {}, (data) => {
    activityList.value = data || []
  }, () => ElMessage.error('获取活动列表失败'))
})

function loadActivityDetail() {
  const act = activityList.value.find(a => a.id === form.value.activityId)
  if (act) {
    form.value.selectedActivity = act
    loadImageList()
  } else {
    form.value.selectedActivity = null
    form.value.imageList = []
  }
}

function loadImageList() {
  if (!form.value.activityId) return
  get('/sysFile/' + form.value.activityId + '/images', {}, (data) => {
    form.value.imageList = data || []
  }, () => ElMessage.error('获取图片列表失败'))
}

function handleUploadSuccess(response, file) {
  if (response.code === 200 && response.content) {
    ElMessage.success(response.message || '上传成功')

    const newFile = {
      contentType: file.raw.type,
      size: file.size,
      path: response.content,
      activityId: form.value.activityId
    }

    form.value.imageList.unshift(newFile)
  } else {
    ElMessage.error(response.message || '上传失败')
  }
}

function handleDeleteImage(index) {
  const file = form.value.imageList[index]
  ElMessageBox.confirm('确定要删除这张图片吗？', '提示', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  }).then(() => {
    post('/sysFile/delete', { id: file.id }, () => {
      ElMessage.success('删除成功')
      form.value.imageList.splice(index, 1)
    }, () => {
      ElMessage.error('删除失败，请刷新重试')
    })
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}
</script>

<style scoped>
.image-management-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* 响应式间距 */
.mb-4 { margin-bottom: 1rem; }
.mb-6 { margin-bottom: 1.5rem; }
.py-10 { padding-top: 2.5rem; padding-bottom: 2.5rem; }

/* 卡片标题统一风格 */
.card-header,
.section-header {
  font-weight: 600;
  color: #1f2937;
}

/* 图片网格 */
.image-grid {
  margin-top: 8px;
}

.image-item .thumbnail {
  height: 128px;
  background-color: #f8f9fa;
}

.empty-state {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 上传区域增强 */
.upload-box :deep(.el-upload-dragger) {
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center; /* 新增：使内容垂直居中 */
  padding: 20px;
}

/* ========== 修改重点：管理区域左右分布 ========== */
.management-layout {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap; /* 默认允许换行，移动端更友好 */
}

.upload-area,
.preview-area {
  flex: 1; /* 两个区域平分宽度 */
  min-width: 300px; /* 防止在窄屏下被压缩 */
}

/* 小屏幕下自动堆叠为上下布局（推荐） */
@media (max-width: 768px) {
  .management-layout {
    flex-direction: column;
  }
}

/* 刷新按钮与标题对齐 */
.section-header .align-items-center {
  align-items: center;
}
.section-header .align-self-center {
  align-self: center;
}
</style>