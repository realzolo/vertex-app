<template>
  <a-drawer v-model:visible="visible" title="用户详情" :width="width >= 500 ? 500 : '100%'" :footer="false">
    <a-descriptions :column="2" size="large" class="general-description">
      <a-descriptions-item label="ID" :span="2">
        <a-typography-paragraph copyable>{{ dataDetail?.id }}</a-typography-paragraph>
      </a-descriptions-item>
      <a-descriptions-item label="用户名">{{ dataDetail?.username }}</a-descriptions-item>
      <a-descriptions-item label="昵称">{{ dataDetail?.nickname }}</a-descriptions-item>
      <a-descriptions-item label="性别">
        <span v-if="dataDetail?.gender === 0">男</span>
        <span v-else-if="dataDetail?.gender === 1">女</span>
        <span v-else>未知</span>
      </a-descriptions-item>
      <a-descriptions-item label="状态">
        <a-tag v-if="dataDetail?.status === 0" color="green">正常</a-tag>
        <a-tag v-if="dataDetail?.status === 1" color="orangered">锁定</a-tag>
        <a-tag v-if="dataDetail?.status === 2" color="orangered">禁用</a-tag>
        <a-tag v-if="dataDetail?.status === 3" color="orangered">过期</a-tag>
      </a-descriptions-item>
      <a-descriptions-item label="手机号">{{ dataDetail?.phone || '暂无' }}</a-descriptions-item>
      <a-descriptions-item label="邮箱">{{ dataDetail?.email || '暂无' }}</a-descriptions-item>
      <a-descriptions-item label="所属部门">{{ dataDetail?.department?.name }}</a-descriptions-item>
      <a-descriptions-item label="角色"><GiCellTags :data="dataDetail?.roles?.map(role => role.name) as string[]" /></a-descriptions-item>
      <a-descriptions-item label="创建时间">{{ dataDetail?.createTime }}</a-descriptions-item>
      <a-descriptions-item label="修改时间">{{ dataDetail?.updateTime }}</a-descriptions-item>
      <a-descriptions-item label="描述" :span="2">{{ dataDetail?.description }}</a-descriptions-item>
    </a-descriptions>
  </a-drawer>
</template>

<script setup lang="ts">
import { useWindowSize } from '@vueuse/core'
import { getUser as getDetail } from '@/apis/system/user'
import type { UserDetailResp } from '@/apis/system/type'

const { width } = useWindowSize()

const dataId = ref()
const dataDetail = ref<UserDetailResp>()
const visible = ref(false)

// 查询详情
const getDataDetail = async () => {
  const { data } = await getDetail(dataId.value)
  dataDetail.value = data
}

// 打开
const onOpen = async (id: number) => {
  dataId.value = id
  visible.value = true
  await getDataDetail()
}

defineExpose({ onOpen })
</script>

<style scoped lang="scss"></style>
