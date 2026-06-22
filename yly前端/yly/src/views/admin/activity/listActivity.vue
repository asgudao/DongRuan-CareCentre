<template>
  <div>
    <!-- 日期范围选择器 -->
    <el-space>
      <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          @change="getTableData">
      </el-date-picker>
    </el-space>

    <!-- 活动数据表格 -->
    <el-table style="margin-top: 20px;" :data="tableData" border>
      <el-table-column label="活动ID" prop="id"></el-table-column>
      <el-table-column label="活动名称" prop="name"></el-table-column>
      <el-table-column label="活动地点" prop="location"></el-table-column>
      <el-table-column label="开始日期" prop="startDate"></el-table-column>
      <el-table-column label="结束日期" prop="endDate"></el-table-column>
      <el-table-column label="组织方" prop="organizer"></el-table-column>
      <el-table-column label="活动详情" prop="detail"></el-table-column>
	  <el-table-column 
	    label="开始时间" 
	    :formatter="formatStartTime"
	    prop="startTime">
	  </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { get } from '@/axios'
import { ElMessage } from 'element-plus'

// 初始化数据
const tableData = ref([])
const dateRange = ref([]) 
// 格式化开始时间，只显示 HH:mm
function formatStartTime(row, column, cellValue) {
  if (!cellValue) return '--'
  const time = cellValue.slice(0, 5) // 取前5位：HH:mm
  return time
}
// 获取表格数据的方法
function getTableData() {
  const params = {}

  // 如果选择了日期范围，则添加到查询参数中
  if (dateRange.value && dateRange.value.length === 2) {
    params.startDate = dateRange.value[0]
    params.endDate = dateRange.value[1]
  }

  // 发起请求获取活动列表数据
  get('/activity/listByDateRange', params, content => {
    tableData.value = content
    console.log('活动数据：', content)

    // 显示成功或无数据消息
    if (content && content.length > 0) {
      ElMessage.success(`加载成功，共${content.length}条活动记录`)
    } else {
      ElMessage.info('未找到符合条件的活动记录')
    }
  }, (error) => {
    ElMessage.error('数据加载失败: ' + (error.message || '未知错误'))
  })
}

// 页面初始化时自动加载数据
getTableData()
</script>

<style scoped lang="scss">
/* 样式可以根据需要调整 */
</style>