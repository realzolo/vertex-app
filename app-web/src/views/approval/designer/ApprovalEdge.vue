<template>
  <BaseEdge :path="path[0]" />

  <EdgeLabelRenderer>
    <div
      class="edge-label-render-content"
      :style="{ transform: `translate(-50%, -50%) translate(${path[1]}px,${path[2]}px)` }"
    >
      <a-popover v-if="!isInteractive" position="bottom">
        <div class="flow-edge-edge-button" v-html="plusIcon" />
        <template #content>
          <div class="flow-edge-option-popover">
            <div v-for="item in FLOW_NODE_OPTIONS" :key="item.name" class="flow-edge-option" @click="createNode(item as any)">
              <div class="flow-edge-option-icon" :style="{ background: item.background }" v-html="item.icon" />
              <span class="flow-edge-option-text">{{ item.name }}</span>
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
import plusIcon from './assets/icon-edge-plus.svg?raw'
import { FLOW_NODE_OPTIONS } from './support'

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
    position: { x: (props.sourceX + props.targetX) / 2 - 110, y: (props.sourceY + props.targetY) / 2 - 26 },
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
.edge-label-render-content {
  position: absolute;
  pointer-events: all;
}

.flow-edge-edge-button {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 16px;
  height: 16px;
  color: #FFFFFF;
  border-radius: 50%;
  background-color: #155aef;
  transition: all 0.2s ease-in-out;
  cursor: pointer;

  &:hover {
    scale: 1.3;
  }
}

.flow-edge-option-popover {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    grid-gap: 8px;
    width: 380px;

    .flow-edge-option {
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

      .flow-edge-option-icon {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 24px;
        height: 24px;
        border-radius: 8px;
      }
      .flow-edge-option-text {
        margin-left: 8px;
      }
    }
  }
</style>
