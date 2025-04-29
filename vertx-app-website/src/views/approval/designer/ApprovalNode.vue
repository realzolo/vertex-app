<template>
  <Handle type="source" :position="Position.Right" />
  <Handle type="target" :position="Position.Left" />
  <div class="flow-node-content">
    <div v-if="data.type === NodeType.APPROVER" class="node-icon" style="background-color: #875BF7" v-html="approvalIcon" />
    <div v-if="data.type === NodeType.CC" class="node-icon" style="background-color: #06AED4" v-html="ccIcon" />
    <div class="node-title">{{ label }}</div>
    <div class="node-delete" @click="deleteNode" v-html="deleteIcon" />
  </div>
</template>

<script setup lang="ts">
import { Handle, type NodeProps, Position } from '@vue-flow/core'
import approvalIcon from './assets/icon-node-approver.svg?raw'
import ccIcon from './assets/icon-node-cc.svg?raw'
import deleteIcon from './assets/icon-node-delete.svg?raw'
import { NodeType } from '@/views/approval/type'

defineOptions({ name: 'ApprovalNode' })

const props = defineProps<NodeProps>()
const emit = defineEmits(['delete-node'])

const deleteNode = () => {
  emit('delete-node', props.id)
}
</script>

<style scoped lang="scss">
@use './styles/flow-node.scss' as *;

.vue-flow__node {
  &.selected {
    .node-delete {
      opacity: 1;
      pointer-events: all;
    }
  }

  &.selectable:hover {
    .node-delete {
      opacity: 1;
      pointer-events: all;
    }
  }

  .flow-node-content {
    padding: 0 12px;

    .node-delete {
      transition: all 0.2s;
      cursor: pointer;
      opacity: 0;
      pointer-events: none;

      &:hover {
        scale: 1.3;
      }
    }
  }
}
</style>
