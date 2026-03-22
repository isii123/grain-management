import axios from 'axios'

const state = {
  token: localStorage.getItem('token') || null,
  userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
  isLoggedIn: false
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    state.isLoggedIn = !!token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
  },
  LOGOUT(state) {
    state.token = null
    state.userInfo = null
    state.isLoggedIn = false
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }
}

const actions = {
  login({ commit }, loginForm) {
    return new Promise((resolve, reject) => {
      axios.post('/api/auth/login', loginForm)
        .then(response => {
          const { token, username, fullName, role } = response.data
          commit('SET_TOKEN', token)
          commit('SET_USER_INFO', { username, fullName, role })
          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
          resolve(response)
        })
        .catch(error => reject(error))
    })
  },

  logout({ commit }) {
    commit('LOGOUT')
    delete axios.defaults.headers.common['Authorization']
  },

  checkAuth({ commit }) {
    const token = localStorage.getItem('token')
    if (token) {
      commit('SET_TOKEN', token)
      commit('SET_USER_INFO', JSON.parse(localStorage.getItem('userInfo') || '{}'))
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      return true
    }
    return false
  }
}

const getters = {
  isLoggedIn: state => state.isLoggedIn,
  userInfo: state => state.userInfo,
  token: state => state.token,
  isAdmin: state => state.userInfo?.role === 'admin',
  canManageUsers: state => state.userInfo?.role === 'admin'
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
}