<template>
  <Handle id="end" type="target" :position="Position.Left" />
  <div class="flow-node-content">
    <div class="node-icon" v-html="icon" />
    <span class="node-title">{{ label }}</span>
  </div>
</template>

<script setup lang="ts">
import { Handle, type NodeProps, Position } from '@vue-flow/core'
import icon from './assets/icon-node-end.svg?raw'

defineOptions({ name: 'OutputNode' })

defineProps<NodeProps>()
</script>

<style scoped lang="scss">
.flow-node-content {
  .node-icon {
    background-color: #f79009 !important;
  }
}
</style>

<style lang="scss">
@use './styles/flow-node.scss' as *;

.vue-flow__node-output.selected,
.vue-flow__node-output:focus,
.vue-flow__node-output:focus-visible {
  border: 2px solid #296dff;
}
</style>
