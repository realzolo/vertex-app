<template>
  <GiPageLayout>
    <GiTable
      row-key="id"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1200 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['title']"
      @refresh="search"
    >
      <template #toolbar-left>
        <a-input-search v-model="queryForm.title" placeholder="搜索标题" allow-clear @search="search" />
        <a-select
          v-model="queryForm.type"
          :options="notice_type"
          placeholder="请选择类型"
          allow-clear
          style="width: 150px"
          @change="search"
        />
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
      <template #type="{ record }">
        <GiCellTag :value="record.type" :dict="notice_type" />
      </template>
      <template #status="{ record }">
        <GiCellTag :value="record.status" :dict="notice_status" />
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['system:notice:get']" title="详情" @click="onDetail(record)">详情</a-link>
          <a-link v-permission="['system:notice:update']" title="修改" @click="onUpdate(record)">修改</a-link>
          <a-link v-permission="['system:notice:delete']" status="danger" title="删除" @click="onDelete(record)"> 删除 </a-link>
        </a-space>
      </template>
    </GiTable>
    <RelationAddModal ref="RelationAddModalRef" />
  </GiPageLayout>
</template>

<script setup lang="ts">
import type { TableInstance } from '@arco-design/web-vue'
import RelationAddModal from './RelationAddModal.vue'
import { type NoticeQuery, type NoticeResp, deleteNotice } from '@/apis/system'
import { useTable } from '@/hooks'
import { useDict } from '@/hooks/app'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { listFlowTemplateBindRelations } from '@/apis/approval'

defineOptions({ name: 'SystemNotice' })

const { notice_type, notice_status } = useDict('notice_type', 'notice_status')

const router = useRouter()

const RelationAddModalRef = ref<InstanceType<typeof RelationAddModal>>()

const queryForm = reactive<NoticeQuery>({
  sort: ['id,desc'],
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete,
} = useTable((page) => listFlowTemplateBindRelations({ ...queryForm, ...page }), { immediate: true })
const columns: TableInstance['columns'] = [
  {
    title: '序号',
    width: 66,
    align: 'center',
    render: ({ rowIndex }) => h('span', {}, rowIndex + 1 + (pagination.current - 1) * pagination.pageSize),
  },
  { title: '业务类型', dataIndex: 'businessTypeName', ellipsis: true, tooltip: true },
  { title: '流程模板', dataIndex: 'flowTemplateName', align: 'center' },
  { title: '创建时间', dataIndex: 'createTime', width: 180 },
  {
    title: '操作',
    dataIndex: 'action',
    slotName: 'action',
    width: 160,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['system:notice:get', 'system:notice:update', 'system:notice:delete']),
  },
]

// 重置
const reset = () => {
  queryForm.title = undefined
  queryForm.type = undefined
  search()
}

// 删除
const onDelete = (record: NoticeResp) => {
  return handleDelete(() => deleteNotice(record.id), {
    content: `是否确定删除公告「${record.title}」？`,
    showModal: true,
  })
}

// 新增
const onAdd = () => {
  RelationAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (record: NoticeResp) => {
  RelationAddModalRef.value?.onAdd(record.id)
}

// 详情
const onDetail = (record: NoticeResp) => {
  RelationAddModalRef.value?.onAdd(record.id)
}
</script>

<style scoped lang="scss"></style>
