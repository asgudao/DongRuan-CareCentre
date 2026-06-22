<template>
  <div>
    <el-space class="section-select">
      <span>选择老人：</span>
      <el-select
        v-model="selectedElderId"
        placeholder="请选择老人"
        class="select-elder"
        @change="loadCheckResults"
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

    <!-- 体检报告展示区域 -->
    <div v-if="checkResults.length > 0" class="section-check-reports">
      <h3>体检报告记录</h3>
      
      <!-- 时间线展示历史报告 -->
      <el-timeline class="check-timeline">
        <el-timeline-item
          v-for="report in checkResults"
          :key="report.id"
          :timestamp="formatDate(report.checkDate)"
          :color="getTimelineColor(report)"
        >
          <el-card shadow="hover" class="check-card">
            <template #header>
              <div class="card-header">
                <span>体检日期：{{ formatDate(report.checkDate) }}</span>
                <el-tag :type="getReportStatusType(report)">
                  {{ getReportStatusText(report) }}
                </el-tag>
              </div>
            </template>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-descriptions :column="1" size="small" :labelStyle="{ fontWeight: 'bold' }">
                  <el-descriptions-item label="血压">
                    {{ report.bloodPressure }} mmHg
                  </el-descriptions-item>
                  <el-descriptions-item label="血糖">
                    {{ report.bloodGlucose }} mmol/L
                  </el-descriptions-item>
                  <el-descriptions-item label="血脂">
                    {{ report.bloodLipid }} mmol/L
                  </el-descriptions-item>
                </el-descriptions>
              </el-col>
              <el-col :span="12">
                <el-descriptions :column="1" size="small" :labelStyle="{ fontWeight: 'bold' }">
                  <el-descriptions-item label="体重">
                    {{ report.weight }} kg
                  </el-descriptions-item>
                  <el-descriptions-item label="检查人员">
                    {{ report.checkPerson }}
                  </el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>
            
            <!-- 报告详情 -->
            <div v-if="report.checkDetails" class="check-details">
              <h4>详细检查结果：</h4>
              <p>{{ report.checkDetails }}</p>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>

    <el-empty
      v-else-if="selectedElderId"
      description="暂无体检记录"
      class="section-empty"
    />

    <el-empty
      v-else
      description="请选择一位老人查看其体检报告"
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
const allCheckResults = ref([])
const nurses = ref([])

// 计算属性：根据当前选中的老人筛选体检结果
const checkResults = computed(() => {
  if (!selectedElderId.value) return []
  return allCheckResults.value
    .filter(result => result.elderId === selectedElderId.value)
    .sort((a, b) => new Date(b.checkDate) - new Date(a.checkDate)) // 按时间倒序排列
})

onMounted(() => {
  loadElderList()
  loadAllCheckResults()
  getNurses()
})

function loadElderList() {
  get('/elder/list', {}, (content) => {
    elderList.value = content || []
    console.log('名下老人列表：', content)
  })
}

function loadAllCheckResults() {
  get('/elderCheck/list', {}, (content) => {
    allCheckResults.value = Array.isArray(content) ? content : []
    console.log('所有体检记录加载完成，共', allCheckResults.value.length, '条')
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

// 格式化日期显示
function formatDate(dateString) {
  return dayjs(dateString).format('YYYY年MM月DD日 HH:mm')
}

// 根据体检时间判断状态
function getReportStatusType(report) {
  const now = dayjs()
  const checkTime = dayjs(report.checkDate)
  const diffDays = now.diff(checkTime, 'day')
  
  if (diffDays < 7) return 'success' // 一周内
  if (diffDays < 30) return 'warning' // 一个月内
  return 'info' // 超过一个月
}

function getReportStatusText(report) {
  const now = dayjs()
  const checkTime = dayjs(report.checkDate)
  const diffDays = now.diff(checkTime, 'day')
  
  if (diffDays < 7) return '近期体检'
  if (diffDays < 30) return '本月体检'
  return '历史体检'
}

// 时间线颜色
function getTimelineColor(report) {
  const statusType = getReportStatusType(report)
  const colors = {
    success: '#67C23A',
    warning: '#E6A23C',
    info: '#909399'
  }
  return colors[statusType]
}

// 当选择老人时，不需要额外操作，因为使用了计算属性
function loadCheckResults() {
  const elder = elderList.value.find(e => e.id === selectedElderId.value)
  selectedElder.value = elder || null
}
</script>

<style scoped>
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

.section-check-reports {
  margin-top: 20px;
}

.check-timeline {
  padding: 10px 0;
}

.check-card {
  cursor: pointer;
  transition: all 0.3s;
}

.check-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.check-details {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.section-empty {
  margin-top: 40px;
}

.select-elder {
  width: 200px;
}

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