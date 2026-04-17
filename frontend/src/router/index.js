import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    // 公共路由（未登录可访问）
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/Register.vue')
    },

    // 用户端路由（需要登录，普通用户）
    {
        path: '/',
        component: () => import('@/layouts/MainLayout.vue'),
        meta: { requiresAuth: false },
        children: [
            { path: '', name: 'Home', component: () => import('@/views/Home.vue') },
            { path: 'search', name: 'Search', component: () => import('@/views/Search.vue') },
            { path: 'goods/:id', name: 'GoodsDetail', component: () => import('@/views/GoodsDetail.vue') },
            { path: 'categories', name: 'Categories', component: () => import('@/views/Categories.vue') }
        ]
    },
    {
        path: '/user',
        component: () => import('@/layouts/MainLayout.vue'),
        meta: { requiresAuth: true, role: 'user' },
        children: [
            { path: 'center', name: 'UserCenter', component: () => import('@/views/user/UserCenter.vue') },
            { path: 'profile', name: 'UserProfile', component: () => import('@/views/user/UserProfile.vue') },
            { path: 'orders', name: 'UserOrders', component: () => import('@/views/user/UserOrders.vue') },
            { path: 'favorites', name: 'UserFavorites', component: () => import('@/views/user/UserFavorites.vue') },
            { path: 'publish', name: 'PublishGoods', component: () => import('@/views/user/PublishGoods.vue') },
            { path: 'goods', name: 'MyGoods', component: () => import('@/views/user/MyGoods.vue') },
            { path: 'sales', name: 'SalesManage', component: () => import('@/views/user/SalesManage.vue') },
            { path: 'address', name: 'UserAddress', component: () => import('@/views/user/UserAddress.vue') },
            { path: 'cart', name: 'ShoppingCart', component: () => import('@/views/user/ShoppingCart.vue') }
        ]
    },

    // 管理员端路由（需要管理员权限）
    {
        path: '/admin',
        component: () => import('@/layouts/AdminLayout.vue'),
        meta: { requiresAdmin: true },
        children: [
            { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/AdminDashboard.vue') },
            { path: 'users', name: 'AdminUsers', component: () => import('@/views/admin/AdminUsers.vue') },
            { path: 'statistics', name: 'AdminStatistics', component: () => import('@/views/admin/AdminStatistics.vue') },
            { path: 'goods-review', name: 'AdminGoodsReview', component: () => import('@/views/admin/AdminGoodsReview.vue') },
            { path: 'goods-list', name: 'AdminGoodsList', component: () => import('@/views/admin/AdminGoodsList.vue') },
            { path: 'orders', name: 'AdminOrders', component: () => import('@/views/admin/AdminOrders.vue') },
            { path: 'comments', name: 'AdminComments', component: () => import('@/views/admin/AdminComments.vue') },
            { path: 'admins', name: 'AdminAdmins', component: () => import('@/views/admin/AdminAdmins.vue'), meta: { requiresSuperAdmin: true } }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    scrollBehavior() { return { top: 0 } }
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    const userRole = localStorage.getItem('userRole')
    const adminRole = localStorage.getItem('adminRole')

    // 检查是否需要管理员权限
    if (to.meta.requiresAdmin) {
        if (!token || userRole !== 'admin') {
            next('/login')
            return
        }
        if (to.meta.requiresSuperAdmin && adminRole !== 'super') {
            alert('您没有权限访问此页面')
            next('/admin/dashboard')
            return
        }
        next()
        return
    }

    // 检查是否需要登录（普通用户）
    if (to.meta.requiresAuth && !token) {
        next('/login')
        return
    }

    // 如果已登录且是管理员，访问用户页面时重定向到管理后台
    if (token && userRole === 'admin' && !to.path.startsWith('/admin') && to.path !== '/login' && to.path !== '/register') {
        next('/admin/dashboard')
        return
    }

    // 如果已登录且是普通用户，访问管理后台时重定向到首页
    if (token && userRole === 'user' && to.path.startsWith('/admin')) {
        next('/')
        return
    }

    next()
})

export default router