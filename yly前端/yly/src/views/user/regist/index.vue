<template>
	<el-form ref="formtest" :model="hyxform" :rules="rules" label-width="100px">
		<el-form-item label="姓名" prop="name">
			<el-input v-model="hyxform.name" placeholder="请输入姓名"></el-input>
		</el-form-item>
		<el-form-item label="手机号" prop="phone">
			<el-input v-model="hyxform.phone" placeholder="请输入手机号"></el-input>
		</el-form-item>
		<el-form-item label="电子信箱" prop="email">
			<el-input v-model="hyxform.email" placeholder="请输入电子信箱"></el-input>
		</el-form-item>
		<el-form-item label="性别" prop="sex">
			<el-radio-group v-model="hyxform.sex">
				<el-radio :value="1">男</el-radio>
				<el-radio :value="0">女</el-radio>
			</el-radio-group>
		</el-form-item>
		<el-form-item v-if="!props.id" label="登录密码" prop="password">
			<el-input v-model="hyxform.password" placeholder="请输入登录密码" show-password></el-input>
		</el-form-item>
		<el-form-item v-if="!props.id" label="重复密码" prop="repassword">
			<el-input v-model="hyxform.repassword" placeholder="请输入重复密码" show-password></el-input>
		</el-form-item>
		<el-form-item>
			<el-button type="primary" @click="save">注册</el-button>
		</el-form-item>
	</el-form>
</template>

<script setup>
import { post, get } from '@/axios'
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus' // 导入消息组件

const formtest = ref()
const props = defineProps(['showtoedit', 'id'])
const emits = defineEmits(['update:showtoedit'])

const hyxform = reactive({
	name: '',
	phone: '',
	email: '',
	sex: 1,
	password: '',
	repassword: '',
})

// 密码一致性校验（修复参数格式）
function checkpwd(rule, value, callback) {
	if (value === hyxform.password) {
		callback()
	} else {
		callback(new Error('两次密码必须一致'))
	}
}

// 手机号/邮箱唯一性校验（修复参数格式）
function check(rule, value, callback) {
	get('/user/check', { 
		field: rule.field, 
		value: value, 
		id: props.id 
	}, (content) => {
		content ? callback() : callback(new Error(`${rule.field === 'phone' ? '手机号' : '邮箱'}已被使用`))
	}, (error) => {
		callback(new Error('验证失败，请稍后重试'))
	})
}

// 表单验证规则
const rules = reactive({
	name: [
		{ required: true, message: '请输入用户名', trigger: 'blur' },
		{ max: 20, message: '用户名不得超过20个字符', trigger: 'blur' }
	],
	phone: [  
		{ required: true, message: '请输入手机号', trigger: 'blur' },
		{ pattern: /^1[3-9]\d{9}$/, message: '请输入规范的手机号', trigger: 'blur' },
		{ validator: check, trigger: 'blur' }
	],  
	email: [ 
		{ required: true, message: '请输入邮箱', trigger: 'blur' },
		{ type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
		{ validator: check, trigger: 'blur' }
	],
	sex: [
		{ required: true, message: '请选择性别', trigger: 'change' }
	],
	password: [
		{ required: true, message: '请输入密码', trigger: 'blur' },
		{ min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
	],
	repassword: [
		{ required: true, message: '请再次输入密码', trigger: 'blur' },
		{ validator: checkpwd, trigger: 'blur' }
	]
})



function save() {
  // 调整参数顺序：将表单对象作为第四个参数传入post函数
  post(
    '/user/regist', 
    hyxform, 
    // 成功回调
    (content) => {
      emits('update:showtoedit', false);
      ElMessage.success('注册成功');
    }, 
    // 第四个参数传入表单对象（用于验证）
    formtest,
    // 单独处理错误情况（现有post函数不支持error回调，这里用catch模拟）
    (error) => {
      ElMessage.error('注册失败，请稍后重试');
      console.error('注册错误:', error);
    }
  );
}
</script>

<style scoped lang="scss">
</style>