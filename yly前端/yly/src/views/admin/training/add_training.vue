<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 70vh;">
    <div class="form-container">
      <el-form ref="forObj" :model="pcform" :rules="rules">
        <h1 style="display: flex; justify-content: center; margin-bottom: 10px; font-size: 20px;">康复训练计划录入</h1>
        <el-descriptions border :column="2">
          <el-descriptions-item label="老人姓名">
            <el-form-item prop="elderId">
              <el-select
                v-model="pcform.elderId"
                filterable
                placeholder="请选择老人"
                @change="changeElder"
              >
                <el-option
                  v-for="elder in elders"
                  :key="elder.id"
                  :label="elder.name"
                  :value="elder.id"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="计划名称">
            <el-form-item prop="planName">
              <el-input v-model="pcform.planName" placeholder="如：下肢力量恢复" />
            </el-form-item>
          </el-descriptions-item>

          <!-- 负责护士：选择护士ID -->
          <el-descriptions-item label="负责护士">
            <el-form-item prop="nurseId">
              <el-select
                v-model="pcform.nurseId"
                filterable
                placeholder="请选择负责护士"
              >
                <el-option
                  v-for="nurse in nurses"
                  :key="nurse.id"
                  :label="nurse.name"
                  :value="nurse.id"
                />
              </el-select>
            </el-form-item>
          </el-descriptions-item>

          <!-- 护士姓名栏改为：组织者（可手动输入） -->
          <el-descriptions-item label="组织者">
            <el-form-item prop="organizer">
              <el-input v-model="pcform.organizer" placeholder="请输入组织者姓名" />
            </el-form-item>
          </el-descriptions-item>

          <el-descriptions-item label="开始日期">
            <el-form-item prop="startDate">
              <el-date-picker
                v-model="pcform.startDate"
                value-format="YYYY-MM-DD"
                placeholder="选择开始日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="结束日期">
            <el-form-item prop="endDate">
              <el-date-picker
                v-model="pcform.endDate"
                value-format="YYYY-MM-DD"
                placeholder="选择结束日期"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="单次时长">
            <el-form-item prop="duration">
              <el-input-number
                v-model="pcform.duration"
                :min="1"
                :max="300"
                controls-position="right"
                placeholder="分钟"
                style="width: 100%;"
              />
            </el-form-item>
          </el-descriptions-item>
          <el-descriptions-item label="训练详情" :span="2">
            <el-form-item prop="details">
              <el-input
                type="textarea"
                v-model="pcform.details"
                :rows="3"
                placeholder="请输入训练内容详情，如具体动作、流程等"
              />
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
import { ref, reactive } from 'vue'
import { get, post } from '@/axios'
import { ElMessage } from 'element-plus'

// 表单引用
const forObj = ref()

// 下拉选项
const elders = ref([])
const nurses = ref([])

// 表单数据：注意去掉了 nurseName，增加了 organizer
const pcform = reactive({
  elderId: null,
  elderName: '',
  planName: '',
  nurseId: null,       // 保留：负责护士（用于后台记录执行人）
  organizer: '',       // 新增：组织者（可与护士不同，手动填写）
  startDate: '',
  endDate: '',
  duration: 30,
  details: ''
})

// 验证规则
const rules = {
  elderId: [
    { required: true, message: '请选择老人', trigger: 'blur' }
  ],
  planName: [
    { required: true, message: '请输入计划名称', trigger: 'blur' },
    { min: 2, max: 100, message: '计划名称长度为2-100个字符', trigger: 'blur' }
  ],
  nurseId: [
    { required: true, message: '请选择负责护士', trigger: 'blur' }
  ],
  organizer: [
    { required: true, message: '请输入组织者姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '组织者姓名长度为2-20个字符', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'blur' }
  ],
  duration: [
    { required: true, message: '请输入单次时长', trigger: 'blur' },
    { type: 'number', min: 1, max: 300, message: '单次时长应在1-300分钟之间', trigger: 'blur' }
  ],
  details: [
    { required: true, message: '请输入训练详情', trigger: 'blur' },
    { max: 1000, message: '训练详情不能超过1000个字符', trigger: 'blur' }
  ]
}

// 加载数据
getElders()
getNurses()

// 加载老人列表
function getElders() {
  get('/admin/list', {}, content => {
    elders.value = content
    console.log('老人数据：', content)
  })
}

// 加载护士列表
function getNurses() {
  get('/nurse/getNurse', {}, content => {
    nurses.value = content
    console.log('护士数据：', content)
  })
}

// 老人选择变化：填充老人姓名
function changeElder(elderId) {
  const elder = elders.value.find(e => e.id === elderId)
  if (elder) {
    pcform.elderName = elder.name
  }
}

// 保存表单
function save() {
  forObj.value.validate(valid => {
    if (!valid) return

    console.log('提交的表单数据:', pcform)
    const submitForm = copy(pcform)

    post('/training/add', submitForm, content => {
      ElMessage.success('康复训练计划添加成功')
      // 重置表单
      Object.keys(pcform).forEach(key => {
        if (key === 'duration') {
          pcform[key] = 30
        } else {
          pcform[key] = (key === 'elderId' || key === 'nurseId') ? null : ''
        }
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

::v-deep .el-input-number {
  width: 100%;
}
</style>