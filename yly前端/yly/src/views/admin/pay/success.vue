<template>
	<div style="display: flex;
	align-items: center;
	flex-direction: column; 
	justify-content: center;
	height: 70vh;
	background-color: rgba(255, 255, 255, 0.85);"
	>
		<h1 style="color: blue;
		font-size: 100px;">支付成功!</h1>
		<div style="margin-top: 20px;">三秒后跳转</div>
		
	</div>
</template>

<script setup>
	import router from '@/router'
	import {useRoute} from 'vue-router'
	import {post}from '@/axios'
	import { onMounted } from 'vue'
	const route = useRoute()
	post('/elder/updatePay',route.query,content=>{

		 setTimeout(() => {
		    router.push('/adminOrder_list')
		  }, 1000)

	})
</script>

<style scoped lang="scss">
	
</style>