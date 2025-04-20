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
      <a-popover position="rt" content="">
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
import { BaseEdge, EdgeLabelRenderer, type EdgeProps, type Node, getBezierPath, useEdge } from '@vue-flow/core'
import { computed } from 'vue'
import { NodeType } from '../type'

defineOptions({ name: 'SpecialEdge' })

const props = defineProps<EdgeProps>()

const parentCreateNode: any = inject('createNode')

const path = computed(() => getBezierPath(props))

const { edge } = useEdge()
const FLOW_NODE_CHOICES = [
  { name: '审批人', value: NodeType.APPROVER },
  { name: '抄送人', value: NodeType.CC },
]

const createNode = (item: { name: string, value: string }) => {
  console.log("edge", edge)
  const node: Node = {
    id: `INTERMEDIATE_NODE_${Date.now()}`,
    type: 'special',
    position: { x: (edge.sourceX + edge.targetX) / 2 - 110, y: (edge.sourceY + edge.targetY) / 2 },
    data: {
      label: item.name,
      type: item.value,
      index: edge.data.index,
    },
  }
  parentCreateNode(node)
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
