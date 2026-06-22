import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  return { count, doubleCount, increment }
})
export const useLoadingStore = defineStore('loading', () => {
	const loading = ref(0)
	const openLoading = () => { loading.value++ }
	const closeLoading = () => { loading.value-- }
	const getLoading = () => loading.value
	return { loading, openLoading, closeLoading,  getLoading }
})

export const useTokenStore = defineStore('token', () => {
	const token = ref(null)
	const setToken = value => { token.value = value }
	const getToken = () => token.value
	return { token, setToken, getToken }
}, {
	persist: {
		storage: sessionStorage
	}
})