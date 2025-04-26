<template>
  <div class="header">
    <div class="left-content">
      <div class="back-button">
        <a-button type="secondary" @click="goBack">
          <icon-left />
        </a-button>
      </div>
      <div class="flow-logo">
        <img
          src="https://lf9-appstore-sign.oceancloudapi.com/ocean-cloud-tos/plugin_icon/workflow.png?lk3s=81d4c505&x-expires=1745507938&x-signature=QBOrRkwnVEwA6J05M7D8wE0Pilo%3D"
          alt="logo"
        />
      </div>
      <div v-show="!!flowData.name" class="flow-info">
        <div class="flow-name">
          <span>{{ flowData.name }}</span>
        </div>
        <div class="flow-desc">
          <span>{{ flowData.remark || '审批流程' }}</span>
        </div>
      </div>
      <div v-show="!!flowData.status" class="flow-extra">
        <div class="flow-status">
          <ATypographyText :type="flowData.status === 1 ? 'success' : 'warning'" size="">
            {{ flowData.status === 1 ? '已发布' : '未发布' }}
          </ATypographyText>
        </div>
        <div class="flow-time">
          <span>{{ updateTime ?? flowData.updateTime }}</span>
        </div>
      </div>
    </div>
    <div class="right-content">
      <div class="flow-action">
        <a-space>
          <a-button type="secondary" :disabled="true">
            创建副本
          </a-button>
          <a-button type="primary" :loading="loading" @click="saveFlow">
            保存
          </a-button>
        </a-space>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { Message } from '@arco-design/web-vue'
import { useVueFlow } from '@vue-flow/core'
import { updateFlowTemplate } from '@/apis/approval'
import router from '@/router'

defineProps({
  flowData: {
    type: Object,
    required: true,
    default: () => ({
      name: '',
      remark: '',
    }),
  },
})

const route = useRoute()
const { toObject } = useVueFlow()

const loading = ref(false)
const updateTime = ref()

const saveFlow = async () => {
  loading.value = true
  const id = Number(route.query.id)
  try {
    const resp = await updateFlowTemplate({ id, content: JSON.stringify(toObject()) })
    if (resp.success) {
      updateTime.value = resp.data.updateTime
      Message.success('保存成功')
    }
  } catch (e) {
    Message.error('保存失败')
  } finally {
    setTimeout(() => {
      loading.value = false
    }, 500)
  }
}
const goBack = () => {
  router.back()
}
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
  height: 64px;
  padding: 0 16px;
  background: rgba(247, 247, 252, 1);
  border-bottom: 1px solid rgba(82, 100, 154, 0.13);

  .left-content {
    display: flex;
    justify-content: space-around;
    align-items: center;
    gap: 8px;

    .back-button {
      .arco-btn {
        padding: 8px;
        border-color: transparent;
      }
    }

    .flow-logo {
      width: 32px;
      height: 32px;

      img {
        display: block;
        height: 100%;
        object-fit: cover;
        width: 100%;
        border-radius: 6px;
      }
    }

    .flow-info {
      max-width: 300px;

      .flow-name {
        font-size: 14px;
        line-height: 1.6;
        font-weight: bold;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .flow-desc {
        font-size: 12px;
        line-height: 1.4;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .flow-extra {
      max-width: 300px;

      .flow-status {
        line-height: 1.6;
      }

      .flow-time {
        font-size: 12px;
        line-height: 1.4;
      }
    }
  }
}
</style>
