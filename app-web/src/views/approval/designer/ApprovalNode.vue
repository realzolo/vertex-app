<template>
  <Handle type="source" :position="Position.Right" />
  <Handle type="target" :position="Position.Left" />
  <div class="flow-node-content">
    <div v-if="data.type === NodeType.APPROVER" class="node-icon" style="background-color: #875BF7" v-html="approvalIcon" />
    <div v-if="data.type === NodeType.CC" class="node-icon" style="background-color: #06AED4" v-html="ccIcon" />
    <span class="node-title">{{ label }}</span>
  </div>
</template>

<script setup lang="ts">
import { Handle, type NodeProps, Position } from '@vue-flow/core'
import approvalIcon from './assets/icon-node-approver.svg?raw'
import ccIcon from './assets/icon-node-cc.svg?raw'
import { NodeType } from '@/views/approval/type'

defineOptions({ name: 'ApprovalNode' })

defineProps<NodeProps>()
</script>

<style scoped lang="scss">
.flow-node-content {
  padding: 0 8px !important;
}
</style>

<style lang="scss">
@use './styles/flow-node.scss' as *;

.vue-flow__node.selectable:hover {
  box-shadow: 0 1px 4px 1px rgba(0, 0, 0, 0.08);
}
</style>
