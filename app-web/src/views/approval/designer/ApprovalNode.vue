<template>
  <div class="vue-flow__node-default approval-node-default">
    <div v-if="data.type !== NodeType.END" class="approval-node approval-process">
      <div class="node-title" :style="{ background: COLOR_MAP[data.type] }">
        <span>{{ data.label }}</span>
      </div>
      <div class="node-content">
        <div class="text">{{ data.user?.name }}</div>
        <div class="icon">
          <a-button shape="circle" type="text">
            <icon-right />
          </a-button>
        </div>
      </div>
    </div>

    <div v-if="data.type === NodeType.END" class="approval-node approval-end">
      <span>流程结束</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { NodeProps } from '@vue-flow/core'
import { NodeType } from '../type'

defineOptions({ name: 'ApprovalNode' })

defineProps<NodeProps>()

const COLOR_MAP = {
  0: 'rgb(87, 106, 149)',
  1: 'rgb(251, 96, 45)',
  2: 'rgb(50, 150, 250)',
  3: 'rgb(254, 162, 73)',
}
</script>

<style scoped lang="scss">
.approval-node-default {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: unset;
  background-color: var(--color-bg-2);
  border: none;
  border-radius: 4px;
  padding: 0;
  align-items: center;

  .approval-node.approval-process {
    width: 220px;
    min-height: 72px;
    background: #FFFFFF;
    border-radius: 4px;
    border: 1px solid var(--color-border);
    .node-title {
      display: flex;
      align-items: center;
      height: 24px;
      line-height: 24px;
      font-size: 12px;
      color: #FFFFFF;
      border-radius: 4px 4px 0 0;
      text-align: left;
      padding-left: 16px;
      padding-right: 30px;
    }

    .node-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      font-size: 14px;
      padding: 16px;

      .text {
        display: block;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        width: 140px;
        text-align: left;
      }

      .icon {
        width: 20px;
        cursor: pointer;
      }
    }
  }

  .approval-node.approval-end {
    border-radius: 24px;
    padding: 10px 28px;
    font-size: 14px;
    background: rgba(23, 26, 29, 0.03);
    color: var(--color-text-2);
  }
}
</style>

<style lang="scss">
.vue-flow__node-approval.selected .approval-node-default:not(:has(.approval-node.approval-end)) {
  box-shadow: 1px 3px 32px 0 rgba(50,73,198,.08),6px 16px 48px 0 rgba(50,73,198,.12);
}
</style>
