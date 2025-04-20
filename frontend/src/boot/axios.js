import {boot} from 'quasar/wrappers'
import axios from 'axios'

const api = axios.create(
  {
    baseURL: '/api/v1',
    timeout: 5000
  }
)

export const fmsApi = {

  signup(firstName, lastName, email, phone, password, confirmPassword) {
    return api.post("/auth/signup", {
        firstName,
        lastName,
        email,
        phone,
        password,
        confirmPassword
      }
    )
  },

  login(username, password) {
    return api.post("/auth/token", {
      username,
      password
    })
  },

  me(token) {
    return api.get("/auth/me", {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  updateUserAccount(firstName, lastName, email, phone, telegramId, token) {
    return api.post("/users", {
      firstName,
      lastName,
      email,
      phone,
      telegramId
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  getSubscriptionGroupedBySymbol(token, page = 0, size = 10) {
    return api.get(`/subscriptions/symbols?page=${page}&size=${size}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  getSubscriptionsBySymbols(symbol, token, page = 0, size = 10) {
    return api.get(`/subscriptions/symbols/${symbol}?page=${page}&size=${size}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async getSubscriptionsById(id, token) {
    return api.get(`/subscriptions/${id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async getMarket(id) {
    return api.get(`/public/markets/${id}`)
  },

  async getAllMarket(page = 0, size = 20) {
    return api.get(`/public/markets?page=${page}&size=${size}`)
  },

  async getAllAssets(marketId) {
    return api.get(`/public/markets/${marketId}/assets`)
  },

  async createNewSignal(marketId, symbol, targetType, targetValue, message, notificationType, notificationEnabled, timeout, token) {
    return api.post('subscriptions', {
      marketId,
      symbol,
      targetType,
      targetValue,
      message,
      notificationType,
      notificationEnabled,
      timeout
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async deleteSubscription(subscriptionId, token) {
    return api.delete(`/subscriptions/${subscriptionId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async updateSubscription(
    subscriptionId,
    targetValue,
    message,
    notificationType,
    notificationEnabled,
    timeout,
    token) {
    return api.post(`/subscriptions/${subscriptionId}`, {
      targetValue,
      message,
      notificationType,
      notificationEnabled,
      timeout
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async getAllBinanceStream(token) {
    return api.get("/binance/streams", {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async addNewBinanceStream(streamName, token) {
    return api.post("/binance/streams", {
      streamName,
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async deleteBinanceStream(streamName, token) {
    return api.delete(`/binance/streams/${streamName}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async addNewMarket(name, type, description, token) {
    return api.post("/markets", {
      name,
      type,
      description
    }, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async deleteMarket(marketId, token) {
    return api.delete(`/markets/${marketId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
  },

  async requestResetPassword(email) {
    return api.post("/auth/request-reset-password", {email})
  },

  async resetPassword(password, confirmPassword, token) {
    return api.post("/auth/reset-password", {token, password, confirmPassword})
  },

  async changePassword(password, newPassword, confirmNewPassword, token) {
    return api.post("/users/change-password",
      {password, newPassword, confirmNewPassword},
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })
  }
}

export default boot(({app}) => {
  app.config.globalProperties.$axios = axios
  app.config.globalProperties.$api = fmsApi
})

export {api}
