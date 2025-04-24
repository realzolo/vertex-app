<template>
  <BaseEdge :path="path[0]" />

  <EdgeLabelRenderer>
    <div
      :style="{
        pointerEvents: 'all',
        position: 'absolute',
        transform: `translate(-50%, -50%) translate(${path[1]}px,${path[2]}px)`,
      }"
    >
      <a-popover v-if="!isInteractive" position="rt" content="">
        <a-button shape="circle" type="secondary">
          <icon-plus />
        </a-button>
        <template #content>
          <div class="flow-edge-choice-popover">
            <div v-for="item in FLOW_NODE_CHOICES" :key="item.name" class="flow-edge-choice" @click="createNode(item as any)">
              <icon-plus />
              <span>{{ item.name }}</span>
            </div>
          </div>
        </template>
      </a-popover>
    </div>
  </EdgeLabelRenderer>
</template>

<script setup lang="ts">
import {
  BaseEdge,
  EdgeLabelRenderer,
  type EdgeProps,
  getBezierPath,
} from '@vue-flow/core'
import { computed } from 'vue'
import { FLOW_NODE_CHOICES } from './support'

defineOptions({ name: 'ApprovalEdge' })

const props = defineProps<EdgeProps>()

const emit = defineEmits(['add-node-on-edge'])

const path = computed(() => getBezierPath(props))

const isInteractive = inject<boolean>('isInteractive')

const createNode = (item: { name: string, value: string }) => {
  emit('add-node-on-edge', {
    edgeId: props.id,
    source: props.source,
    target: props.target,
    position: { x: (props.sourceX + props.targetX) / 2 - 110, y: (props.sourceY + props.targetY) / 2 - 38 },
    type: item.value,
  })
}
</script>

<script lang="ts">
export default {
  inheritAttrs: false,
}
</script>

<style scoped lang="scss">
.flow-edge-choice-popover {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 8px;
  width: 380px;

  .flow-edge-choice {
    display: inline-flex;
    align-items: center;
    cursor: pointer;
    color: #191F25;
    height: 32px;
    background: rgba(17, 31, 44, 0.03);
    padding: 8px;
    border: 1px solid #FFFFFF;
    border-radius: 6px;
    user-select: none;

    &:hover {
      background: #FFFFFF;
      border: 1px solid #ecedef;
      box-shadow: 0 2px 8px 0 rgba(17, 31, 44, 0.08);
    }
  }
}
</style>

<style lang="scss">
.vue-flow__edge-labels .arco-btn.arco-btn-secondary {
  background-color: #FFFFFF;
}
</style>
