<template>
  <GiPageLayout>
    <GiTable
      row-key="id"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      @refresh="search"
    >
      <template #status="{ record }">
        <GiCellStatus :status="record.status" />
      </template>
      <template #toolbar-left>
        <a-input-search v-model="queryForm.name" placeholder="搜索标签/描述" allow-clear @search="search" />
        <a-button @click="reset">
          <template #icon><icon-refresh /></template>
          <template #default>重置</template>
        </a-button>
      </template>
      <template #toolbar-right>
        <a-button type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <template #default>新增</template>
        </a-button>
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link title="流程设计" @click="onDesign(record)">流程设计</a-link>
          <a-link status="danger" title="删除" @click="onDelete(record)">删除</a-link>
        </a-space>
      </template>
    </GiTable>

    <TemplateAddModal ref="TemplateAddModalRef" />
  </GiPageLayout>
</template>

<script setup lang="ts">
import type { TableInstance } from '@arco-design/web-vue'
import TemplateAddModal from './TemplateAddModal.vue'
import { useTable } from '@/hooks'
import { isMobile } from '@/utils'
import { deleteFlowGraph, listFlowGraph } from '@/apis/flow'
import type { FlowGraphPageQuery } from '@/apis/flow/type'

defineOptions({ name: 'MonitorOnline' })
const router = useRouter()

const TemplateAddModalRef = ref<InstanceType<typeof TemplateAddModal>>()
const queryForm = reactive<FlowGraphPageQuery>({
  name: '',
  sort: ['createTime,desc'],
})
const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete,
} = useTable((page) => listFlowGraph({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstance['columns'] = [
  { title: '流程名称', dataIndex: 'name', ellipsis: true, tooltip: true },
  { title: '流程状态', dataIndex: 'status', slotName: 'status', align: 'center' },
  { title: '描述', dataIndex: 'remark', ellipsis: true, tooltip: true },
  { title: '创建时间', dataIndex: 'createTime' },
  { title: '修改时间', dataIndex: 'updateTime' },
  { title: '操作', dataIndex: 'action', slotName: 'action', width: 160, align: 'center', fixed: !isMobile() ? 'right' : undefined },
]
const onAdd = () => {
  TemplateAddModalRef.value?.onAdd()
}
const onDesign = (record: any) => {
  router.push({ path: `/workflow/designer`, query: { id: record.id } })
}
// 重置
const reset = () => {
  queryForm.name = undefined
  search()
}
const onDelete = (record: any) => {
  return handleDelete(() => deleteFlowGraph(record.id), {
    content: `是否确定删除流程「${record.name}」？`,
    showModal: true,
  })
}
</script>

<style scoped lang="scss"></style>
