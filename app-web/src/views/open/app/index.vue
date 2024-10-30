<template>
  <div class="table-page">
    <GiTable
      row-key="id"
      title="应用管理"
      :data="dataList"
      :columns="columns"
      :loading="loading"
      :scroll="{ x: '100%', y: '100%', minWidth: 1000 }"
      :pagination="pagination"
      :disabled-tools="['size']"
      :disabled-column-keys="['name']"
      @refresh="search"
    >
      <template #toolbar-left>
        <a-input v-model="queryForm.name" placeholder="请输入应用名称" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-input v-model="queryForm.appKey" placeholder="请输入APPKEY" allow-clear @change="search">
          <template #prefix><icon-search /></template>
        </a-input>
        <a-button @click="reset">
          <template #icon><icon-refresh /></template>
          <template #default>重置</template>
        </a-button>
      </template>
      <template #toolbar-right>
        <a-button v-permission="['open:app:add']" type="primary" @click="onAdd">
          <template #icon><icon-plus /></template>
          <template #default>新增</template>
        </a-button>
        <a-button v-permission="['open:app:export']" @click="onExport">
          <template #icon><icon-download /></template>
          <template #default>导出</template>
        </a-button>
      </template>
      <template #name="{ record }">
        <a-link @click="onDetail(record)">{{ record.name }}</a-link>
      </template>
      <template #appSecret="{ record }">
        *********
        <a-button size="mini" style="margin-left: 10px" @click="onGetSecret(record)">
          <template #icon><icon-eye /></template>
        </a-button>
        <a-button size="mini" style="margin-left: 2px" @click="onRefreshAK(record)">
          <template #icon><icon-refresh /></template>
        </a-button>
      </template>
      <template #status="{ record }">
        <GiCellTag :value="record.status" :dict="app_type" />
      </template>
      <template #action="{ record }">
        <a-space>
          <a-link v-permission="['open:app:update']" @click="onUpdate(record)">修改</a-link>
          <a-link
            v-permission="['open:app:delete']"
            status="danger"
            :disabled="record.disabled"
            @click="onDelete(record)"
          >
            删除
          </a-link>
        </a-space>
      </template>
    </GiTable>
    <AppAddModal ref="AppAddModalRef" @save-success="search" />
    <AppDetailDrawer ref="AppDetailDrawerRef" />
    <AppGetSecretModal ref="AppGetSecretModalRef" />
  </div>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import AppAddModal from './AppAddModal.vue'
import AppDetailDrawer from './AppDetailDrawer.vue'
import AppGetSecretModal from './AppGetSecretModal.vue'
import { type AppQuery, type AppResp, deleteApp, exportApp, listApp, refreshAppSecret } from '@/apis/open/app'
import type { TableInstanceColumns } from '@/components/GiTable/type'
import { useDownload, useTable } from '@/hooks'
import { isMobile } from '@/utils'
import has from '@/utils/has'
import { useDict } from '@/hooks/app'

defineOptions({ name: 'App' })

const { app_type } = useDict('app_type')

const queryForm = reactive<AppQuery>({
  name: '',
  appKey: '',
  sort: ['createTime,desc'],
})

const {
  tableData: dataList,
  loading,
  pagination,
  search,
  handleDelete,
} = useTable((page) => listApp({ ...queryForm, ...page }), { immediate: true })

const columns: TableInstanceColumns[] = [
  { title: '应用名称', dataIndex: 'name', slotName: 'name' },
  { title: '应用密钥', dataIndex: 'appKey', slotName: 'appKey' },
  { title: '应用密码', dataIndex: 'appSecret', slotName: 'appSecret' },
  { title: '应用状态', dataIndex: 'status', slotName: 'status' },
  { title: '失效时间', dataIndex: 'expirationTime', slotName: 'expirationTime' },
  {
    title: '操作',
    slotName: 'action',
    width: 130,
    align: 'center',
    fixed: !isMobile() ? 'right' : undefined,
    show: has.hasPermOr(['open:app:update', 'open:app:delete']),
  },
]

// 重置
const reset = () => {
  queryForm.name = ''
  queryForm.appKey = ''
  search()
}

// 删除
const onDelete = (record: AppResp) => {
  return handleDelete(() => deleteApp(record.id), {
    content: `是否确定删除该条数据？`,
    showModal: true,
  })
}

// 导出
const onExport = () => {
  useDownload(() => exportApp(queryForm))
}

const AppAddModalRef = ref<InstanceType<typeof AppAddModal>>()
// 新增
const onAdd = () => {
  AppAddModalRef.value?.onAdd()
}

// 修改
const onUpdate = (record: AppResp) => {
  AppAddModalRef.value?.onUpdate(record.id)
}

const AppDetailDrawerRef = ref<InstanceType<typeof AppDetailDrawer>>()
// 详情
const onDetail = (record: AppResp) => {
  AppDetailDrawerRef.value?.onDetail(record.id)
}

// 查看应用密钥/密码
const AppGetSecretModalRef = ref<InstanceType<typeof AppGetSecretModal>>()
const onGetSecret = (record: AppResp) => {
  AppGetSecretModalRef.value?.onGetSecret(record.id)
}

// 刷新应用密码
const onRefreshAK = async (record: AppResp) => {
  await refreshAppSecret(record.id)
  Message.success('刷新成功')
  search()
}
</script>

<style lang="scss" scoped></style>
