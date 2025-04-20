export function clientId(state) {
  return state.account.clientId
}

export function firstName(state) {
  return state.account.firstName
}

export function lastName(state) {
  return state.account.lastName
}

export function email(state) {
  return state.account.email
}

export function phone(state) {
  return state.account.phone
}

export function telegramId(state){
  return state.account.telegramId
}

export function roles(state) {
  return state.account.roles
}

export function isUserAccountAlreadyLoad(state) {
  return state.account.clientId !== undefined
}

export function isUser(state) {
  return state.account.roles.includes('ROLE_USER')
}

export function isAdmin(state) {
  return state.account.roles.includes('ROLE_ADMIN')
}
