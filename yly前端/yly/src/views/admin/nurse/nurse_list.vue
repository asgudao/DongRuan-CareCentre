<template>
  <div>
		<el-table style="margin-top: 20px;" :data="tableData" border>
			<el-table-column label="护士ID" prop="id"></el-table-column>
			<el-table-column label="护士姓名" prop="name"></el-table-column>
            <el-table-column label="手机号" prop="phone"></el-table-column>
            <el-table-column label="性别" prop="gender" :formatter="formatGender"></el-table-column>
            <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
		</el-table>
	</div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { get } from '@/axios'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

const tableData = ref([])

// 格式化性别显示
const formatGender = (row) => {
  return row.gender === 1 ? '男' : row.gender === 0 ? '女' : '';
}
// 初始加载数据
getTableData()

function getTableData() {
	const params = {}

	// 使用专门的日期范围查询接口
	get('/nurse/getNurse', params, content => {

		tableData.value = content
		console.log('康复训练数据：', content)
		
		// 添加与list_order一致的成功提示
		if (content && content.length > 0) {
			ElMessage.success(`加载成功，共${content.length}条康复训练记录`)
		} else {
			ElMessage.info('未找到符合条件的康复训练记录')
		}
	}, (error) => {
		ElMessage.error('数据加载失败: ' + (error.message || '未知错误'))
	})
}
</script>

<style scoped lang="scss">
</style>
