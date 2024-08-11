import {createRouter, createWebHistory} from 'vue-router'

import DashboardView from '@/view/Dashboard.vue'
import LoginView from '@/view/Login.vue'

const routes = [
    {
        path: '/index.html',
        name: 'Login',
        component: LoginView
    },
    {
        path: '/dashboard',
        name: 'DashboardView',
        component: DashboardView
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
