<template>
  <a-modal
    v-model:visible="visible"
    title="应用密钥/密码信息"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '520px' }"
    width="90%"
  >
    <a-watermark :content="content" :gap="[10, 10]">
      <div style="width: 100%; height: 150px;">
        <a-alert type="warning" style="margin-bottom: 18px">应用密码仅可查看一次，请妥善保管，遗失请刷新。</a-alert>
        <a-descriptions :data="data" :column="1" bordered />
      </div>
    </a-watermark>
  </a-modal>
</template>

<script setup lang="ts">
import type { DescData } from '@arco-design/web-vue'
import dayjs from 'dayjs'
import { getAppSecret } from '@/apis/open/app'
import { useUserStore } from '@/stores'

const dataId = ref('')
const initData = [{
  label: '应用密钥（appkey）',
  value: '*********',
}, {
  label: '应用密码（appsecret）',
  value: '*********',
}]
const userStore = useUserStore()
const content = ref([userStore.userInfo.username, dayjs().format('YYYY-MM-DD HH:mm:ss')])

let data = reactive<DescData[]>(initData)

const visible = ref(false)

const getAppSecretData = async (id: string) => {
  await getAppSecret(id).then((res) => {
    data = [{
      label: '应用密钥（appkey）',
      value: res.data.appKey,
    }, {
      label: '应用密码（appsecret）',
      value: res.data.appSecret,
    }]
  })
}

// 查看应用密钥/密码
const onGetSecret = async (id: string) => {
  dataId.value = ''
  await getAppSecretData(id)
  visible.value = true
}

defineExpose({ onGetSecret })
</script>
