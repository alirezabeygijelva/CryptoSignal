import {fmsApi as api} from 'boot/axios'

export async function getAccount ({commit}, accessToken) {
 await api.me(accessToken)
   .then((response) => commit('setUserAccount', response.data.data))
   .catch((error) => error)
}

export function logout({commit}) {
  const clearAccount = {
    clientId: undefined,
    firstName: undefined,
    lastName: undefined,
    email: undefined,
    phone: undefined,
    telegramId: undefined,
    roles: []
  }
  commit('setUserAccount', clearAccount)
}

export function isUserAuthorized({state}, pageRoles) {
  let isAuthorized = false
  state.account.roles.forEach(value => {
    if (pageRoles.includes(value)) {
      isAuthorized = true
    }
  })
  return isAuthorized
}
