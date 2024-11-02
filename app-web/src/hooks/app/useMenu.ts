import { ref } from 'vue'
import type { TreeNodeData } from '@arco-design/web-vue'
import { listMenuTree } from '@/apis'

/** 菜单模块 */
export function useMenu(options?: { onSuccess?: () => void }) {
  const loading = ref(false)
  const menuList = ref<TreeNodeData[]>([])

  const getMenuList = async (name?: string) => {
    try {
      loading.value = true
      const res = await listMenuTree({ description: name })
      menuList.value = buildTree(res.data)
      console.log(menuList.value);
      options?.onSuccess && options.onSuccess()
    } finally {
      loading.value = false
    }
  }

  const buildTree = (data: any[]) => {
    return data.map((item) => {
      return {
        key: item.key,
        parentId: item.parentKey,
        title: item.title,
        children: item.children && buildTree(item.children),
      }
    })
  }
  return { menuList, getMenuList, loading }
}
