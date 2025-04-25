<template>
  <Handle type="source" :position="Position.Right" />
  <div class="flow-node-content">
    <div class="node-icon" v-html="icon" />
    <span class="node-title">{{ label }}</span>
  </div>
</template>

<script setup lang="ts">
import { Handle, type NodeProps, Position } from '@vue-flow/core'
import icon from './assets/icon-node-start.svg?raw'

defineOptions({ name: 'InputNode' })

defineProps<NodeProps>()
</script>

<style lang="scss">
@use './styles/flow-node.scss' as *;

.vue-flow__node-input.selected,
.vue-flow__node-input:focus,
.vue-flow__node-input:focus-visible {
  border: 2px solid #296dff;
}
</style>
