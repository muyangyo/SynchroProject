import type { RoleType } from '@/stores/userStore'

export interface UserState {
  userName: string
  isLoggedIn: boolean
  role: RoleType | null
}

export interface KeyState {
  publicKey: string
  openEncrypt: boolean
}

export interface StoreActions {
  setUserName(name: string): void
  setLoginStatus(value: boolean, role: RoleType): void
  logout(): void
  setPublicKey(key: string): void
  encryptData(data: string): string
} 