<template>
  <div>
		<SubmitPay :payContent="payContent"></SubmitPay>
		<el-space>
			<el-select
				style="width: 180px"
				v-model="params.active"
				@change="getTableData">
				<el-option :value="''" label="全部"></el-option>
				<el-option :value="1" label="已缴费"></el-option>
				<el-option :value="0" label="未缴费"></el-option>
			</el-select>
		</el-space>
		<el-table style="margin-top: 20px;" :data="tableData" border>
			<el-table-column label="老人姓名" prop="name"></el-table-column>
			<el-table-column label="性别" prop="gender" width="50px"></el-table-column>
			<el-table-column label="年龄" prop="age" width="50px"></el-table-column>
			<el-table-column label="身份证号" prop="idCard" show-overflow-tooltip width="150px"></el-table-column>
			<el-table-column label="家属联系电话" prop="familyPhone" show-overflow-tooltip width="150px"></el-table-column>
			<el-table-column label="自理能力">
				<template #default="scope">
					<span v-if="scope.row.selfCareAbility === 0">能自理</span>
					<span v-else-if="scope.row.selfCareAbility === 1">半自理</span>
					<span v-else>不能自理</span>
				</template>
			</el-table-column>
			<el-table-column label="房间号" prop="roomNumber"></el-table-column>
			<el-table-column label="入住时间" prop="checkInTime"></el-table-column>
			<el-table-column label="退住时间" prop="checkOutTime"></el-table-column>
			<el-table-column label="护理员" prop="nurseId">
				<template #default="scope">
					<span>{{getNurseName(scope.row.nurseId)}}</span>
				</template>
			</el-table-column>
			<el-table-column label="护理费" prop="price">
				<template #default="scope">
					<span>{{scope.row.price}}</span>
				</template>
			</el-table-column>
			<el-table-column label="缴费状态">
				<template #default="scope">
					<span v-if="scope.row.active === 1">已缴费</span>
					<span v-else>未缴费</span>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="150px">
				<template #default="scope">
					<el-button
						v-if="scope.row.active === 0"
						@click="pay(scope.row.outTradeNo, scope.row.price)"
						size="small"
						plain
						type="primary">缴费</el-button>
					<el-button
						@click="deleteElder(scope.row.outTradeNo)"
						size="small"
						plain
						type="danger">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
	</div>
</template>

<script setup>
import SubmitPay from '@@/submitPay'
import { ref, reactive } from 'vue'
import { get, post } from '@/axios'
import dayjs from 'dayjs'
import { ElMessageBox, ElMessage } from 'element-plus'

const payContent = ref('')
const tableData = ref([])
const nurses = ref([])


const params = reactive({
  name: '',
  idCard: '',
  familyPhone: '',
  selfCareAbility:'',
  gender: '',
  age: '',
  roomNumber: null,
  nurseId: null,
  userId:'',
  checkInTime: '',
  checkOutTime: '',
  outTradeNo: '',
  price: null,
  active: ''
})
// 初始加载数据
getNurses()
getTableData()

function getNurses() {
	get('/nurse/getNurse', {}, content => {
		nurses.value = content
		console.log('护士数据：', content)
	})
}

function getNurseName(nurseId) {
	const nurse = nurses.value.find(n => n.id === nurseId)
	return nurse ? nurse.name : '未分配'
}

function getTableData() {
	get('/admin/list', params, content => {
		tableData.value = content
		console.log('老人数据：', content)
	})
}

function pay(outTradeNo, price) {
	post('/admin/pay', { out_trade_no: outTradeNo, total_amount: price }, content => {
		payContent.value = content
	})
}

function deleteElder(outTradeNo) {
	ElMessageBox.confirm('确定要删除这个老人的订单吗？', '提示', {
		confirmButtonText: '确定',
		cancelButtonText: '取消',
		type: 'warning'
	}).then(() => {
		post('/elder/del', { out_trade_no: outTradeNo }, content => {
			ElMessage.success('删除成功')
			getTableData() // 重新加载数据
		})
	}).catch(() => {
		ElMessage.info('已取消删除')
	})
}
</script>

<style scoped lang="scss">
</style>