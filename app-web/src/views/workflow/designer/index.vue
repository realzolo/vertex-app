<template>
  <GiPageLayout>
    <VueFlow
      :nodes="nodes"
      :edges="edges"
      :min-zoom="0.5"
      :max-zoom="1.5"
    >
      <template #node-special="specialNodeProps">
        <SpecialNode v-bind="specialNodeProps" />
      </template>

      <template #edge-special="specialEdgeProps">
        <SpecialEdge v-bind="specialEdgeProps" :create-node="onCreateNode" />
      </template>

      <Background pattern-color="#aaa" :gap="16" />

      <Controls position="top-left" />

      <MiniMap position="bottom-left" />

      <Panel position="top-right">
        <SpecialPanel ref="SpecialPanelRef" />
      </Panel>
    </VueFlow>
  </GiPageLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { Edge, Node } from '@vue-flow/core'
import { Panel, VueFlow, useVueFlow } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { MiniMap } from '@vue-flow/minimap'
import { NodeType } from '../type'
import SpecialNode from './SpecialNode.vue'
import SpecialEdge from './SpecialEdge.vue'
import SpecialPanel from './SpecialPanel.vue'

const {
  onNodeDrag,
  onNodeClick,
  onPaneClick,
} = useVueFlow()

const SpecialPanelRef = ref<InstanceType<typeof SpecialPanel>>()

const nodes = ref<Node[]>([
  {
    id: 'START_NODE',
    type: 'input',
    position: { x: 1000, y: 80 },
    data: {
      label: '流程开始',
      type: NodeType.START,
    },
  },
  {
    id: 'END_NODE',
    type: 'output',
    position: { x: 1000, y: 800 },
    data: {
      label: '流程结束',
      type: NodeType.END,
    },
  },
])

const edges = ref<Edge[]>([])

const buildEdges = (newNodes: Node[]) => {
  const newEdges = [] as Edge[]
  for (let i = 0; i < newNodes.length - 1; i++) {
    const node = newNodes[i]
    const nextNode = newNodes[i + 1]
    const edge: Edge = {
      id: `${node.id}->${nextNode.id}`,
      source: node.id,
      target: nextNode.id,
      type: 'special',
      data: {
        index: i,
      },
    }
    newEdges.push(edge)
  }
  edges.value = newEdges
}

watch(() => nodes.value, (newNodes) => {
  buildEdges(newNodes)
}, { immediate: true })

const onNodeCreate = (node: Node) => {
  const indexIndex = node.data.index
  const newNodes = [...nodes.value]
  newNodes.splice(indexIndex + 1, 0, node)
  nodes.value = newNodes
}

onNodeDrag((event) => {
  const { x, y } = event.node.position
  const currentNode = nodes.value.find((node) => node.id === event.node.id)
  if (currentNode) {
    currentNode.position = { x, y }
  }
})

onNodeClick((event) => {
  if ([NodeType.START, NodeType.END].includes(event.node.data.type)) {
    SpecialPanelRef.value?.onVisible()
    return
  }
  SpecialPanelRef.value?.onVisible(event.node)
})

onPaneClick(() => {
  SpecialPanelRef.value?.onVisible()
})

provide('createNode', onNodeCreate)
</script>
