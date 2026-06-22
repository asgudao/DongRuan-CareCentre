<template>
  <div>
    <!-- 筛选栏 -->
    <el-space>
      
      <!-- 添加日期筛选 -->
      <el-date-picker
        v-model="filter.checkDate"
        type="date"
        placeholder="选择体检日期"
        value-format="YYYY-MM-DD"
        @change="getTableData"
        clearable
      />
      
      <el-button @click="resetFilter">重置筛选</el-button>
    </el-space>
    
    <!-- 体检结果表格 -->
    <el-table style="margin-top: 20px;" :data="tableData" border>
      <el-table-column label="老人姓名" prop="elderName"></el-table-column>
      <el-table-column label="体检时间" prop="checkDate"></el-table-column>
      <el-table-column label="体重(kg)" prop="weight"></el-table-column>
	  <el-table-column label="血压(mmHg)" prop="bloodPressure"></el-table-column>
      <el-table-column label="血糖(mmol/L)" prop="bloodGlucose"></el-table-column>
      <el-table-column label="血脂(mmol/L)" prop="bloodLipid"></el-table-column>
      <el-table-column label="护工" prop="checkPerson"></el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { get, post } from '@/axios'
import { ElMessageBox, ElMessage } from 'element-plus'

const filter = reactive({
  active: '',
  checkDate: '' // 添加日期筛选字段
})
const tableData = ref([])

// 初始加载数据
getTableData()

function getTableData() {
  // 如果有日期筛选条件，则调用按日期筛选的接口
  if (filter.checkDate) {
    get('/elderCheck/listByDate', { checkDate: filter.checkDate }, content => {
      tableData.value = content
      console.log('按日期筛选的体检数据：', content)
    }, (error) => {
      ElMessage.error('获取数据失败: ' + (error.message || '未知错误'))
    })
  } else {
    // 否则获取所有数据
    get('/elderCheck/list', {}, content => {
      tableData.value = content
      console.log('所有体检数据：', content)
    }, (error) => {
      ElMessage.error('获取数据失败: ' + (error.message || '未知错误'))
    })
  }
}

// 重置筛选条件
function resetFilter() {
  filter.checkDate = ''
  getTableData()
}
</script>

<style scoped lang="scss">
</style>
