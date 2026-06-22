<template>
	<div id="payContent" v-html="props.payContent"></div>
</template>

<script setup>
import {nextTick, watch} from 'vue'
const props=defineProps(['payContent'])
watch(()=>props.payContent,(newval,oldval)=>{
	//页面加载完后调用函数
	nextTick(()=>{
		document.querySelector('#payContent').firstChild.submit()
	})
})
</script>

<style scoped lang="scss">
</style>