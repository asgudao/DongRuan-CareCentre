<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 70vh;">
    <div id="payContent" v-html="payContent"></div>
    <div class="form-container">
      <el-form ref="forObj" :model="pcform" :rules="rules">
        <h1 style="display: flex; justify-content: center; margin-bottom: 10px; font-size: 20px;">信息录入</h1>
        <el-descriptions border :column="2">
          <el-descriptions-item label="管理员录入的姓名">
            <el-form-item prop="name">
              <el-input v-model="pcform.name" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="手机号">
            <el-form-item prop="familyPhone">
              <el-input v-model="pcform.familyPhone" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="身份证">
            <el-form-item prop="idCard">
              <el-input v-model="pcform.idCard" @blur="idCardBlur" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-form-item>
              <el-input readonly v-model="pcform.gender" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="年龄">
            <el-form-item>
              <el-input readonly v-model="pcform.age" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="自理能力">
            <el-form-item prop="selfCareAbility">
              <el-select
                v-model="pcform.selfCareAbility"
                filterable
                @change="calculatePrice"
              >
                <el-option :value="0" label="能自理"></el-option>
                <el-option :value="1" label="半自理"></el-option>
                <el-option :value="2" label="不能自理"></el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="房间号">
            <el-form-item prop="roomNumber">
              <el-select
                v-model="pcform.roomNumber"
                filterable
                @change="calculatePrice"
                placeholder="请选择房间号"
              >
                <el-option
                  v-for="room in rooms"
                  :key="room.id"
                  :label="room.roomNumber"
                  :value="room.roomNumber"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>

          <el-descriptions-item label="负责护士">
            <el-form-item prop="nurseId">
              <el-select
                v-model="pcform.nurseId"
                filterable
                @change="changeNurse"
                placeholder="请选择负责护士"
              >
                <el-option
                  v-for="nurse in nurses"
                  :key="nurse.id"
                  :label="nurse.name"
                  :value="nurse.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>

          <el-descriptions-item label="开始日期">
            <el-form-item prop="checkInTime">
              <el-date-picker
                v-model="pcform.checkInTime"
                value-format="YYYY-MM-DD"
                placeholder="选择入住日期"
                @change="calculatePrice"
              ></el-date-picker>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="结束日期">
            <el-form-item prop="checkOutTime">
              <el-date-picker
                v-model="pcform.checkOutTime"
                value-format="YYYY-MM-DD"
                placeholder="选择结束日期"
                @change="calculatePrice"
              ></el-date-picker>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="入住金额">
            <el-form-item>
              <el-input readonly v-model="pcform.price" />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
        <el-button style="margin-top: 20px" type="primary" @click="save">确定</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { copy } from 'copy-anything'
import { ref, reactive, nextTick } from 'vue'
import { get, post } from '@/axios'
import DateDiff from 'date-diff'

// 页面数据
const payContent = ref('')
const forObj = ref()

// 下拉选项
const deptList = ref([])
const nurses = ref([])
const rooms = ref([]) 
const registerLevelList = ref({})

// 表单绑定
const pcform = reactive({
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
  active: '0'
})



// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  familyPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证', trigger: 'blur' }
  ],
  roomNumber: [
    { required: true, message: '请选择房间号', trigger: 'blur' }
  ],
  nurseId: [ 
    { required: true, message: '请选择负责护士', trigger: 'blur' }
  ],
  selfCareAbility: [
    { required: true, message: '请选择自理能力', trigger: 'blur' }
  ],
  checkInTime: [
    { required: true, message: '请选择入住日期', trigger: 'blur' }
  ],
  checkOutTime: [
    { required: true, message: '请选择结束日期', trigger: 'blur' }
  ]
}

// 加载数据
getNurses()
getRooms()

// 加载护士列表
function getNurses() {
  get('/nurse/getNurse', {}, content => {
    nurses.value = content
  })
}

function getRooms() {
  get('/room/getRoom', {}, content => {
    rooms.value = content
  })
}

// 护士选择变化
function changeNurse(nurseId) {
  // 护士选择不影响价格计算，保持原有逻辑
  const nurse = nurses.value.find(n => n.id === nurseId)
}

// 计算入住金额
function calculatePrice() {
  // 检查是否所有必要条件都已选择
  const hasAllRequired = 
   pcform.roomNumber &&
    pcform.selfCareAbility !== null && 
    pcform.checkInTime && 
    pcform.checkOutTime
  
  if (!hasAllRequired) {
    pcform.price = null  // 条件不全时清空金额
    return
  }
  
  // 查找选中的房间获取基础金额
  const selectedRoom = rooms.value.find(room => 
    room.roomNumber === pcform.roomNumber || room.room_number === pcform.roomNumber
  )
  
  if (!selectedRoom) {
    pcform.price = null
    return
  }
  
  // 自理能力系数映射
  const abilityCoefficients = {
    0: 1,    // 能自理
    1: 1.5,  // 半自理
    2: 2     // 不能自理
  }
  
  // 计算天数
  const startDate = new Date(pcform.checkInTime)
  const endDate = new Date(pcform.checkOutTime)
  
  // 确保结束日期不早于开始日期
  if (endDate <= startDate) {
    pcform.price = null
    return
  }
  
  const diff = new DateDiff(endDate, startDate)
  const days = diff.days()
  
  // 计算总金额：天数 × 房间基础金额 × 自理能力系数
  const basePrice = selectedRoom.price || 0
  const coefficient = abilityCoefficients[pcform.selfCareAbility] || 1
  const totalPrice = days * (basePrice / 100) * coefficient  // 假设basePrice是分，转换为元
  
  // 保留两位小数
  pcform.price = totalPrice.toFixed(2)
}

function idCardBlur() {
  const idCard = pcform.idCard?.trim()

  if (idCard && idCard.length === 18) {
    // 解析性别
    const genderDigit = parseInt(idCard.charAt(16))
    pcform.gender = genderDigit % 2 === 1 ? '男' : '女'

    // 解析出生日期和年龄
    const birth = idCard.substring(6, 14)
    const birthDate = new Date(`${birth.substring(0,4)}-${birth.substring(4,6)}-${birth.substring(6,8)}`)
    
    const now = new Date()
    const diff = new DateDiff(now, birthDate)
    pcform.age = parseInt(diff.years())
  } else {
    // 身份证格式错误时清空相关字段
    pcform.gender = ''
    pcform.age = ''
  }
}

function save() {
  forObj.value.validate(valid => {
    if (!valid) return

    const submitForm = copy(pcform)
	
    submitForm.price = Math.round(submitForm.price) // 转为分

    post('/elder/add', submitForm, content => {
      payContent.value = content
      nextTick(() => {
        document.querySelector('#payContent').firstChild?.submit?.()
      })
    }, forObj)
  })
}
</script>

<style scoped lang="scss">
.el-descriptions {
  width: 700px;
  .el-select {
    width: 100%;
  }
  ::v-deep .el-date-editor {
    width: 100%;
  }
}

.form-container {
  background-color: rgba(255, 255, 255, 0.85);
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  width: 750px;
}

::v-deep .el-date-editor {
  width: 100%;
}
</style>