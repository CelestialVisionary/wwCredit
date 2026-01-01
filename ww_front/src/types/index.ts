// 用户相关类型
export interface User {
  id: number
  name: string
  phone: string
}

// 借款相关类型
export interface BorrowInfo {
  id: number
  borrowerId: number
  borrowAmount: number
  borrowTerm: number
  interestRate: number
  borrowStatus: number
  createTime: string
  updateTime: string
}

// 账户相关类型
export interface UserAccount {
  id: number
  userId: number
  accountBalance: number
  frozenAmount: number
  totalIncome: number
  lastUpdateTime: string
}