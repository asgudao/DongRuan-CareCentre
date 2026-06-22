<script setup>
import { useLoadingStore } from '@/stores'
const loadingStore = useLoadingStore()
</script>

<template>
	<router-view v-loading.fullscreen.lock="loadingStore.getLoading() > 0" />
</template>

<style scoped></style>
