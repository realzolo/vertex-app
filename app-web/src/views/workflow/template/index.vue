<template>
  <GiPageLayout>
    <GiTable
      row-key="id"
      :data="MOCK_DATA"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      @refresh="search"
    >
      <template #action="{ record }">
        <a-space>
          <a-link title="流程设计" @click="onDesign(record)">流程设计</a-link>
          <a-link status="danger" title="删除">删除</a-link>
        </a-space>
      </template>
    </GiTable>

    <TemplateAddModal ref="TemplateAddModalRef" />
  </GiPageLayout>
</template>

<script setup lang="ts">
import type { TableInstance } from '@arco-design/web-vue'
import TemplateAddModal from './TemplateAddModal.vue'
import { type OnlineUserQuery, listOnlineUser } from '@/apis/monitor'
import { useTable } from '@/hooks'
import { isMobile } from '@/utils'

defineOptions({ name: 'MonitorOnline' })

const TemplateAddModalRef = ref<InstanceType<typeof TemplateAddModal>>()
const queryForm = reactive<OnlineUserQuery>({
  sort: ['createTime,desc'],
})
const MOCK_DATA = ref([
  {
    id: 754534,
    name: '测试流程',
    remark: '测试流程',
    author: 'admin',
    createTime: '2023-04-01 12:00:00',
    updateTime: '2023-04-01 12:00:00',
  },
])
const {
  tableData: dataList,
  loading,
  pagination,
  search,
} = useTable((page) => listOnlineUser({ ...queryForm, ...page }), { immediate: true })
const columns: TableInstance['columns'] = [
  { title: '流程名称', dataIndex: 'name', ellipsis: true, tooltip: true },
  { title: '描述', dataIndex: 'remark', ellipsis: true, tooltip: true },
  { title: '作者名称', dataIndex: 'author' },
  { title: '创建时间', dataIndex: 'createTime' },
  { title: '修改时间', dataIndex: 'updateTime' },
  { title: '操作', dataIndex: 'action', slotName: 'action', width: 160, align: 'center', fixed: !isMobile() ? 'right' : undefined },
]

// 重置
const reset = () => {
  queryForm.nickname = undefined
  queryForm.loginTime = undefined
  search()
}
const router = useRouter()
const onDesign = (record: any) => {
  router.push({ path: '/workflow/designer' })
}
</script>

<style scoped lang="scss"></style>
