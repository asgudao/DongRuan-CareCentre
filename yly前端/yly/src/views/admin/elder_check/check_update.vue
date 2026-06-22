<template>
  <div class="simple-health-form">
    <h2>体检信息录入</h2>
    
    <!-- 老人选择区域 -->
    <div class="form-group">
      <label>选择老人</label>
      <el-select
        v-model="selectedElderId"
        placeholder="请选择老人"
        filterable
        @change="loadElderInfo"
      >
        <el-option
          v-for="elder in elderList"
          :key="elder.id"
          :label="elder.name"
          :value="elder.id"
        ></el-option>
      </el-select>
    </div>

    <!-- 老人基础信息 -->
    <div v-if="selectedElder" class="basic-info">
      <p>性别: {{ selectedElder.gender || '未知' }}</p>
      <p>年龄: {{ selectedElder.age || '未知' }}</p>
      <p>房间号: {{ selectedElder.roomNumber || '未知' }}</p>
    </div>

    <!-- 体检信息输入 -->
    <div class="form-fields">
      <el-input
        v-model.number="healthData.weight"
        placeholder="体重(kg)"
        type="number"
        class="form-field"
      ></el-input>
      <el-input
        v-model.number="healthData.bloodPressure"
        placeholder="血压(mmHg)"
        type="number"
        step="0.01"
        class="form-field"
      ></el-input>
      <el-input
        v-model.number="healthData.bloodGlucose"
        placeholder="血糖(mmol/L)"
        type="number"
        step="0.01"
        class="form-field"
      ></el-input>
      
      <el-input
        v-model.number="healthData.bloodLipid"
        placeholder="血脂(mmol/L)"
        type="number"
        step="0.01"
        class="form-field"
      ></el-input>
      
      <el-date-picker
        v-model="healthData.checkDate"
        placeholder="体检日期"
        value-format="YYYY-MM-DD"
        class="form-field"
      ></el-date-picker>
      <el-select
        v-model="selectedNursesId"
        placeholder="请选择护工"
        filterable
        @change="getNurses"
      >
        <el-option
          v-for="nurse in nurses"
          :key="nurse.id"
          :label="nurse.name"
          :value="nurse.id"
        ></el-option>
      </el-select>
    </div>

    <!-- 操作按钮 -->
    <div class="form-actions">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { get, post } from '@/axios'
import { ElMessage } from 'element-plus'

// 老人列表数据
const elderList = ref([])
const selectedElderId = ref('')
const selectedNursesId = ref('')
const selectedElder = ref(null)
// 护士列表数据
const nurses = ref([])

// 体检数据
const healthData = reactive({
  bloodPressure: null,
  bloodGlucose: null,
  bloodLipid: null,
  weight: null,
  checkDate: '',
})
getNurses()

// 获取护工列表
function getNurses() {
  get('/nurse/getNurse', {}, content => {
    nurses.value = content
  })
}
// 页面加载时获取老人列表
onMounted(() => {
  fetchElderList()
})

// 获取老人列表
const fetchElderList = () => {
  get('/admin/list', {}, (res) => {
    elderList.value = res || []
  }, (err) => {
    ElMessage.error('加载老人列表失败')
    console.error(err)
  })
}

// 加载选中老人信息
const loadElderInfo = (id) => {
  const elder = elderList.value.find(item => item.id === id)
  selectedElder.value = elder || null
}


// 提交表单
const submitForm = () => {
  // 简单验证
  if (!selectedElderId.value) {
    return ElMessage.warning('请选择老人')
  }
  
  const requiredFields = [  'bloodPressure', 'bloodGlucose', 'bloodLipid', 'weight', 'checkDate']
  const hasEmptyField = requiredFields.some(field => healthData[field] === null || healthData[field] === '')
  
  if (hasEmptyField) {
    return ElMessage.warning('请填写完整体检信息')
  }
  
  // 构造提交数据，包含 elderName 字段
  const formData = {
    elderId: selectedElderId.value,
    elderName: selectedElder.value.name, // 添加老人姓名字段
    checkPerson: nurses.value.find(nurse => nurse.id === selectedNursesId.value)?.name || '', // 添加检查人字段
    ...healthData
  }
  
  post('/elderCheck/add', formData, () => {
    ElMessage.success('提交成功')
    resetForm()
  })
}

// 重置表单
const resetForm = () => {
  selectedElderId.value = ''
  selectedElder.value = null
  selectedNursesId.value = ''  // 新增：重置护工选择
  
  Object.keys(healthData).forEach(key => {
    healthData[key] = null
  })
  healthData.checkDate = ''
}
</script>

<style scoped>
.simple-health-form {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-weight: 500;
}

.basic-info {
  margin: 15px 0;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  display: flex;
  gap: 20px;
}

.form-fields {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin: 20px 0;
}

.form-field {
  width: 100%;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}
</style>
