import { ref, toRefs } from 'vue'
import { listCommonDict } from '@/apis'
import { useDictStore } from '@/stores'

const pendingRequests = new Map<string, Promise<any>>()

export function useDict(...groups: string[]) {
  const dictStore = useDictStore()
  const dictData = ref<Record<string, App.DictItem[]>>({})

  groups.forEach(async (group) => {
    dictData.value[group] = []

    const cached = dictStore.getDict(group)
    if (cached) {
      dictData.value[group] = cached
      return
    }

    if (!pendingRequests.has(group)) {
      const request = listCommonDict(group)
        .then(({ data }) => {
          dictStore.setDict(group, data)
          return data
        })
        .catch((error) => {
          console.error(`Failed to load dict: ${group}`, error)
          return []
        })
        .finally(() => {
          pendingRequests.delete(group)
        })

      pendingRequests.set(group, request)
    }

    pendingRequests.get(group)!.then((data) => {
      dictData.value[group] = data
    })
  })

  return toRefs(dictData.value)
}
