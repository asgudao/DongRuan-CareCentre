<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 70vh;">
    <div class="form-container">
      <!-- 活动信息录入表单 -->
      <el-form ref="formRef" :model="form" :rules="rules">
        <h1 style="text-align: center; margin-bottom: 20px; font-size: 20px;">活动信息录入</h1>

        <el-descriptions border :column="2" style="margin-bottom: 20px;">
          <!-- 活动名称 -->
          <el-descriptions-item label="活动名称">
            <el-form-item prop="name">
              <el-input v-model="form.name" placeholder="请输入活动名称" />
            </el-form-item>
          </el-descriptions-item>

          <!-- 活动地点 -->
          <el-descriptions-item label="活动地点">
            <el-form-item prop="location">
              <el-input v-model="form.location" placeholder="请输入活动地点" />
            </el-form-item>
          </el-descriptions-item>

          <!-- 开始日期 -->
          <el-descriptions-item label="活动开始日期">
            <el-form-item prop="startDate">
              <el-date-picker
                v-model="form.startDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择开始日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>

          <!-- 开始时间：format 显示时分，value-format 提交时补全秒 -->
          <el-descriptions-item label="开始时间">
            <el-form-item prop="startTime">
              <el-time-picker
                v-model="form.startTime"
                format="HH:mm"
                value-format="HH:mm:ss"
                placeholder="选择开始时间"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>

          <!-- 结束日期 -->
          <el-descriptions-item label="活动结束日期">
            <el-form-item prop="endDate">
              <el-date-picker
                v-model="form.endDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择结束日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>

          <!-- 组织方 -->
          <el-descriptions-item label="组织方">
            <el-form-item prop="organizer">
              <el-input v-model="form.organizer" placeholder="请输入组织方名称" />
            </el-form-item>
          </el-descriptions-item>

          <!-- 活动详情 -->
          <el-descriptions-item label="活动详情" :span="2">
            <el-form-item prop="detail">
              <el-input
                v-model="form.detail"
                type="textarea"
                :rows="4"
                placeholder="请输入活动详细内容（如流程、注意事项等）"
              />
            </el-form-item>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 提交与重置按钮 -->
        <div style="text-align: center;">
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { post } from '@/axios'
import { ElMessage } from 'element-plus'

// 表单引用
const formRef = ref()

// 表单数据
const form = reactive({
  name: '',
  location: '',
  startDate: '',
  startTime: '',
  endDate: '',
  organizer: '',
  detail: ''
})

// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入活动名称', trigger: 'blur' },
    { min: 2, max: 100, message: '活动名称长度为2-100个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入活动地点', trigger: 'blur' },
    { max: 100, message: '活动地点不能超过100个字符', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  startTime: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ],
  organizer: [
    { required: true, message: '请输入组织方', trigger: 'blur' },
    { max: 50, message: '组织方名称不能超过50个字符', trigger: 'blur' }
  ],
  detail: [
    { max: 2000, message: '活动详情不能超过2000个字符', trigger: 'blur' }
  ]
}

// 提交表单
function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return

 const submitData = {
      name: form.name,
      location: form.location,
      startDate: form.startDate,     
      startTime: form.startTime,      
      endDate: form.endDate,          
      organizer: form.organizer,
      detail: form.detail,
      status: 1
    }

    post('/activity/add', submitData, () => {
      ElMessage.success('活动信息添加成功')
      resetForm()
    }, formRef)
  })
}

// 重置表单
function resetForm() {
  formRef.value.resetFields()
  form.detail = ''
}
</script>

<style scoped lang="scss">
.form-container {
  background-color: rgba(255, 255, 255, 0.9);
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 800px;
}

.el-descriptions {
  ::v-deep .el-descriptions-item__content {
    min-height: 40px;
  }

  ::v-deep .el-input,
  .el-textarea {
    width: 100%;
  }

  ::v-deep .el-date-editor,
  .el-time-picker {
    width: 100%;
  }
}
</style>