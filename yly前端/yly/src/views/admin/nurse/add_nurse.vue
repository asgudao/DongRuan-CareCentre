<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 70vh;">
    <div class="form-container">
      <el-form ref="forObj" :model="pcform" :rules="rules">
        <h1 style="display: flex; justify-content: center; margin-bottom: 10px; font-size: 20px;">信息录入</h1>
        <el-descriptions border :column="2">
          <el-descriptions-item label="姓名">
            <el-form-item prop="name">
              <el-input v-model="pcform.name" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="手机号">
            <el-form-item prop="phone">
              <el-input v-model="pcform.phone" />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-radio-group v-model="pcform.gender">
				<el-radio :value="1">男</el-radio>
				<el-radio :value="0">女</el-radio>
			</el-radio-group>
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
const forObj = ref()

// 下拉选项
const deptList = ref([])
const registerLevelList = ref({})

// 表单绑定
const pcform = reactive({
  name: '',
  gender: '',
  active: '0'
})



// 验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}

function save() {
  forObj.value.validate(valid => {
    if (!valid) return
    const submitForm = copy(pcform)
    post('/nurse/add', submitForm, () => {
      // 提交成功后的处理
      ElMessage.success('添加成功')
      // 可以添加路由跳转等逻辑
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