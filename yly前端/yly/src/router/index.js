import { createRouter, createWebHistory } from 'vue-router'
import { useTokenStore } from '@/stores'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
		{
			path: '/',
			name: 'login',
			component: () => import('@/views'),
		},
		{
			path: '/userHome',
			name: 'userHome',
			component: () => import('@/views/userHome'),
			children: [
				{
					path: '/look_activity',
					name: 'LookActivity',
					component: () => import('@/views/user/supv/look_activity')
				},
				{
					path: '/look_training',
					name: 'LookTraining',
					component: () => import('@/views/user/supv/look_training')
				},
				{
					path: '/look_check',
					name: 'LookChaeck',
					component: () => import('@/views/user/supv/look_check')
				},
				{
					path: '/info',
					name: 'Info',
					component: () => import('@/views/user/sys/info')
				},
				{
					path: '/order_list',
					name: 'listlistOrder',
					component: () => import('@/views/user/sys/order_list')
				},
				{
					path: '/success',
					name: 'paySuccess',
					component: () => import('@/views/user/pay/success')
				},
			],

		},
		{
			path: '/adminHome',
			name: 'adminHome',
			component: () => import('@/views/adminHome'),
			children: [
				{
					path: '/admininfo',
					name: 'adminInfo',
					component: () => import('@/views/admin/adminInfo')
				},
				{
					path: '/adminOrder_list',
					name: 'adminOrderList',
					component: () => import('@/views/admin/adminOrder_list')
				},
				{
					path: '/adminsuccess',
					name: 'adminpaySuccess',
					component: () => import('@/views/admin/pay/success')
				},
				{
					path: '/add_training',
					name: 'add_training',
					component: () => import('@/views/admin/training/add_training')
				},
				{
					path: '/training_list',
					name: 'training_list',
					component: () => import('@/views/admin/training/training_list')
				},
				{
					path: '/check_update',
					name: 'check_update',
					component: () => import('@/views/admin/elder_check/check_update')
				},
				{
					path: '/check_list',
					name: 'check_list',
					component: () => import('@/views/admin/elder_check/check_list')
				},
				{
					path: '/addActivity',
					name: 'addActivity',
					component: () => import('@/views/admin/activity/addActivity')
				},
				
				{
					path: '/listActivity',
					name: 'listActivity',
					component: () => import('@/views/admin/activity/listActivity')
				},
				{
					path: '/addNurse',
					name: 'addNurse',
					component: () => import('@/views/admin/nurse/add_nurse')
				},
				
				{
					path: '/listNurse',
					name: 'listNurse',
					component: () => import('@/views/admin/nurse/nurse_list')
				},
				{
					path: '/activityPicture',
					name: 'avtivityPicture',
					component: () => import('@/views/admin/activity/activity_picture')
				},
				{
					path: '/addRoom',
					name: 'addRoom',
					component: () => import('@/views/admin/room/addRoom')
				},
				{
					path: '/listRoom',
					name: 'listRoom',
					component: () => import('@/views/admin/room/listRoom')
				},
			],
		
		}
	]
})
router.beforeEach((to, from, next) => {
	// const tokenStore = useTokenStore()
	// const token = tokenStore.getToken()
	// if (token || to.fullPath === '/') {
	// 	next()
	// } 
	// else {
	// 	next('/')
	// }
	next()
})
export default router
