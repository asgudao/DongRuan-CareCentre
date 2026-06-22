<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 70vh;">
    <div class="form-container">
      <el-form ref="forObj" :model="pcform" :rules="rules">
        <h1 style="display: flex; justify-content: center; margin-bottom: 10px; font-size: 20px;">信息录入</h1>
        <el-descriptions border :column="2">
          <el-descriptions-item label="房间号">
            <el-form-item prop="roomNumber">
              <el-input v-model="pcform.roomNumber" placeholder="如：101"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="容量">
            <el-form-item prop="capacity">
              <el-input v-model.number="pcform.capacity" placeholder="如：2人/4人"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="价格">
            <el-form-item prop="price">
              <el-input v-model.number="pcform.price" placeholder="元/月"/>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-form-item prop="status">
              <el-select v-model="pcform.status" placeholder="请选择状态">
                <el-option label="空闲" value="0"></el-option>
                <el-option label="已住满" value="1"></el-option>
                <el-option label="维护中" value="2"></el-option>
                <el-option label="已预订" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>
        <el-button style="margin-top: 20px" type="primary" @click="save">确定</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { post } from '@/axios'

// 表单绑定
const pcform = reactive({
  roomNumber: '',
  capacity: null,
  price: null,
  status: '0'
})

// 验证规则
const rules = {
  roomNumber: [
    { required: true, message: '请输入房间号', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '请输入容量', trigger: 'blur' },
    { type: 'number', message: '容量必须是数字', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', message: '价格必须是数字', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const forObj = ref()

function save() {
  forObj.value.validate(valid => {
    if (!valid) return
    const formData = { ...pcform }
    post('/room/addRoom', formData, () => {
      ElMessage.success('提交成功')
      resetForm()
    })
  })
}

function resetForm() {
  Object.keys(pcform).forEach(key => {
    if (key === 'capacity' || key === 'price') {
      pcform[key] = null
    } else if (key === 'status') {
      pcform[key] = '0' // ✅ 重置为 '0'：空闲
    } else {
      pcform[key] = ''
    }
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