import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useLoadingStore ,useTokenStore} from '@/stores'
import router from '@/router'
const loadingStore = useLoadingStore()
const tokenStore =useTokenStore()
// 封装baseUrl
const _axios = axios.create({
	baseURL: 'http://127.0.0.1:8080',
})
// 定义通用请求
export const request = (url, method, data, callback) => {
	// 定义请求参数
	const axiosConfig = {
		url,
		method
	}
	//如果有token 就添加到请求头
	if(tokenStore.getToken()){
		axiosConfig.headers={
			token:tokenStore.getToken()
		}
	}
	// 封装数据
	if (method === 'post') {
		// 我们统一传递json
		// 如果是post请求我们就必须把json转换成FormData
		const formData = new FormData()
		// 循环json，如果要循环json。循环的是key
		for (const key in data) {
			formData.append(key, data[key])
		}
		axiosConfig.data = formData
	} else {
		axiosConfig.params = data
	}
	// 请求之前开启loading
	loadingStore.openLoading()
	// 发起请求
	_axios(axiosConfig).then(response => {
		if (response.data.code === 200) {
			// 如果成功并且有message，提示一个成功消息
			if (response.data.message) {
				ElMessage.success(response.data.message)
			}
			// 调用回调函数
			callback(response.data.content)
		} else {
			// 如果失败，提示一个失败消息
			ElMessage.error(response.data.message)
			//401是登录问题
			if(response.data.code===401){
				router.push('/')
			}
		}
	}).catch(error => {
		console.dir(error)
		ElMessage.error('网络故障')
	}).finally(() => {
		// 请求之后关闭loading
		loadingStore.closeLoading()
	})
}
export const get = (url, data, callback) => { request(url, 'get', data, callback) }
export const post = (url, data, callback, formObj = null) => {
	if (formObj) {
		formObj.value.validate(valid => {
			if (valid) {
				request(url, 'post', data, callback)
			}
		})
	} else {
		request(url, 'post', data, callback)
	}
}

