import { ref } from 'vue'
import { listRoleDict } from '@/apis'

/** 角色模块 */
export function useRole(options?: { onSuccess?: () => void }) {
  const loading = ref(false)
  const roleList = ref<DictionaryEntry[]>([])

  const getRoleList = async () => {
    try {
      loading.value = true
      const res = await listRoleDict()
      roleList.value = res.data
      options?.onSuccess?.()
    } finally {
      loading.value = false
    }
  }
  return { roleList, getRoleList, loading }
}
