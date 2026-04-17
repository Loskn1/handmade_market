// src/utils/auth.js

// 检查用户是否已实名认证
export const isRealNameVerified = () => {
    const isVerified = localStorage.getItem('isVerified')
    const userRealName = localStorage.getItem('userRealName')
    const userIdCard = localStorage.getItem('userIdCard')

    return isVerified === 'true' && userRealName && userIdCard
}

// 获取用户实名信息
export const getUserRealNameInfo = () => {
    return {
        realName: localStorage.getItem('userRealName') || '',
        idCard: localStorage.getItem('userIdCard') || '',
        phone: localStorage.getItem('userPhone') || '',
        isVerified: localStorage.getItem('isVerified') === 'true'
    }
}

// 保存实名认证信息到本地
export const saveRealNameInfo = (realName, idCard, phone) => {
    localStorage.setItem('userRealName', realName)
    localStorage.setItem('userIdCard', idCard)
    localStorage.setItem('userPhone', phone)
    localStorage.setItem('isVerified', 'true')
}

// 检查是否需要实名认证，并提示用户
export const checkAndRedirectForVerification = (router, actionName = '进行此操作') => {
    if (!isRealNameVerified()) {
        if (confirm(`您尚未完成实名认证，无法${actionName}。是否前往完善个人信息？`)) {
            router.push('/user/profile')
        }
        return false
    }
    return true
}

// 交易前检查（购买、发布商品、订单操作等）
export const requireTransactionAuth = (router, callback) => {
    if (!isRealNameVerified()) {
        if (confirm('您尚未完成实名认证，无法进行交易。请先完成实名认证。')) {
            router.push('/user/profile')
        }
        return false
    }
    if (callback) callback()
    return true
}