import { defineStore } from 'pinia'

const storeSetup = () => {
  const dictData = ref<Record<string, App.DictItem[]>>({})

  // 设置字典
  const setDict = (group: string, items: App.DictItem[]) => {
    if (group) {
      dictData.value[group] = items
    }
  }

  // 获取字典
  const getDict = (group: string) => {
    if (!group) {
      return null
    }
    return dictData.value[group] || null
  }

  // 删除字典
  const deleteDict = (group: string) => {
    if (!group || !(group in dictData.value)) {
      return false
    }
    delete dictData.value[group]
    return true
  }

  // 清空字典
  const cleanDict = () => {
    dictData.value = {}
  }

  return {
    dictData,
    setDict,
    getDict,
    deleteDict,
    cleanDict,
  }
}

export const useDictStore = defineStore('dict', storeSetup)
