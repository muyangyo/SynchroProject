import { defineStore } from 'pinia'
import { ref } from 'vue'

export const ROLES = {
  admin: "admin",
  user: "user"
} as const

export type RoleType = typeof ROLES[keyof typeof ROLES]

export const useUserStore = defineStore('userStore', () => {
  const userName = ref<string>(getFromLocalStorage('userName') || "")
  const isLoggedIn = ref<boolean>(getFromLocalStorage('isLoggedIn') === 'true')

  function getFromLocalStorage(key: string): string | null {
    return localStorage.getItem(key)
  }

  function setToLocalStorage(key: string, value: string): void {
    localStorage.setItem(key, value)
  }

  const setUserName = (name: string): void => {
    userName.value = name
    setToLocalStorage('userName', name)
  }

  const getUserName = (): string => {
    return userName.value === "" ? "unknown" : userName.value
  }

  const setLoginStatus = (value: boolean, role: RoleType): void => {
    isLoggedIn.value = value
    setToLocalStorage('isLoggedIn', value.toString())
    setToLocalStorage('role', role)
  }

  const IsLoggedIn = (): boolean => {
    return isLoggedIn.value
  }

  const logout = (): void => {
    setLoginStatus(false, ROLES.user)
    setUserName("")
  }

  return {
    setLoginStatus,
    IsLoggedIn,
    setUserName,
    getUserName,
    logout
  }
}) 