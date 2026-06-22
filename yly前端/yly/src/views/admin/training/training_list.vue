<template>
  <div>
		<el-space>
			<el-date-picker
				v-model="dateRange"
				type="daterange"
				range-separator="至"
				start-placeholder="开始日期"
				end-placeholder="结束日期"
				value-format="YYYY-MM-DD"
				@change="getTableData"
			></el-date-picker>
		</el-space>
		<el-table style="margin-top: 20px;" :data="tableData" border>
			<el-table-column label="计划ID" prop="id"></el-table-column>
			<el-table-column label="老人姓名" prop="elderName"></el-table-column>
			<el-table-column label="计划名称" prop="planName"></el-table-column>
			<el-table-column label="组织者" prop="organizer"></el-table-column>
			<el-table-column label="开始日期" prop="startDate"></el-table-column>
			<el-table-column label="结束日期" prop="endDate"></el-table-column>
			<el-table-column label="单次时长(分钟)" prop="duration"></el-table-column>
			<el-table-column label="训练详情" prop="details"></el-table-column>
		</el-table>
	</div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { get } from '@/axios'
import dayjs from 'dayjs'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dateRange = ref([])

// 初始加载数据
getTableData()

function getTableData() {
	const params = {}
	
	// 如果日期范围已选择，则更新params
	if (dateRange.value && dateRange.value.length === 2) {
		params.startDate = dateRange.value[0]
		params.endDate = dateRange.value[1]
	}

	// 使用专门的日期范围查询接口
	get('/training/listByDateRange', params, content => {
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
