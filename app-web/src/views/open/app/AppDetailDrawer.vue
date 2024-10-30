<template>
  <a-drawer v-model:visible="visible" title="应用详情" :width="width >= 580 ? 580 : '100%'" :footer="false">
    <a-descriptions :column="1" size="large" class="general-description" bordered>
      <a-descriptions-item label="应用名称">{{ dataDetail?.name }}</a-descriptions-item>
      <a-descriptions-item label="APPKEY">{{ dataDetail?.appKey }}</a-descriptions-item>
      <a-descriptions-item label="应用状态"><GiCellTag :value="dataDetail?.status" :dict="app_type" /></a-descriptions-item>
      <a-descriptions-item label="失效时间">{{ dataDetail?.expirationTime }}</a-descriptions-item>
      <a-descriptions-item label="创建人">{{ dataDetail?.createUserString }}</a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ dataDetail?.createTime }}</a-descriptions-item>
      <a-descriptions-item label="修改人">{{ dataDetail?.updateUserString }}</a-descriptions-item>
      <a-descriptions-item label="修改时间">{{ dataDetail?.updateTime }}</a-descriptions-item>
      <a-descriptions-item label="应用描述">{{ dataDetail?.appDesc }}</a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script lang="ts" setup>
import { useWindowSize } from '@vueuse/core'
import { type AppDetailResp, getApp } from '@/apis/open/app'
import { useDict } from '@/hooks/app'

const { app_type } = useDict('app_type')
const { width } = useWindowSize()

const visible = ref(false)
const dataId = ref('')
const dataDetail = ref<AppDetailResp>()
// 查询详情
const getDataDetail = async () => {
  const res = await getApp(dataId.value)
  dataDetail.value = res.data
}

// 打开详情
const onDetail = async (id: string) => {
  dataId.value = id
  await getDataDetail()
  visible.value = true
}

defineExpose({ onDetail })
</script>

<style lang="scss" scoped></style>
