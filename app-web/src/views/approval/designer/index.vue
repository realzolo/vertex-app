<template>
  <Header :flow-data="flowData" />
  <VueFlow
    :nodes="nodes"
    :edges="edges"
    :min-zoom="0.5"
    :max-zoom="1.5"
    :node-types="nodeTypes"
    :edge-types="edgeTypes"
  >
    <template #node-approval="props">
      <ApprovalNode v-bind="props" />
    </template>

    <template #edge-approval="props">
      <ApprovalEdge v-bind="props" @add-node-on-edge="handleAddNodeOnEdge" />
    </template>

    <Background pattern-color="rgb(121, 121, 122)" :size="1.2" :gap="24" />

    <Controls position="top-left" @interaction-change="handleInteractionChange" />

    <MiniMap position="bottom-left" />

    <Panel position="top-right">
      <ApprovalPanel ref="ApprovalPanelRef" />
    </Panel>
  </VueFlow>
</template>

<script setup lang="ts">
import { markRaw, ref } from 'vue'
import { type Edge, type Node, Position } from '@vue-flow/core'
import { Panel, VueFlow, useVueFlow } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { MiniMap } from '@vue-flow/minimap'
import { type EdgeEmitData, NodeType } from '../type'
import Header from './components/Header/index.vue'
import ApprovalNode from './ApprovalNode.vue'
import ApprovalEdge from './ApprovalEdge.vue'
import ApprovalPanel from './ApprovalPanel.vue'
import { FLOW_NODE_CHOICES } from './support'
import { getFlowTemplate } from '@/apis/approval'

const nodeTypes = {
  approval: markRaw(ApprovalNode),
}
const edgeTypes = {
  approval: markRaw(ApprovalEdge),
}
const route = useRoute()
const {
  onNodeDragStop,
  onNodeClick,
  onPaneClick,
  fromObject,
} = useVueFlow()

const ApprovalPanelRef = ref<InstanceType<typeof ApprovalPanel>>()
const isInteractive = ref<boolean>(false)
const flowData = ref()

const nodes = ref<Node[]>([
  {
    id: 'START_NODE',
    label: '流程开始',
    type: 'input',
    sourcePosition: Position.Right,
    position: { x: 500, y: 520 },
    data: {
      label: '流程开始',
      type: NodeType.START,
    },
  },
  {
    id: 'END_NODE',
    label: '流程结束',
    type: 'output',
    targetPosition: Position.Left,
    position: { x: 500, y: 520 },
    data: {
      label: '流程结束',
      type: NodeType.END,
    },
  },
])

const edges = ref<Edge[]>([
  {
    id: 'START_NODE->END_NODE',
    source: 'START_NODE',
    target: 'END_NODE',
    type: 'approval',
  },
])

const createNode = (data: EdgeEmitData) => {
  const choice = FLOW_NODE_CHOICES.find((item) => item.value === data.type)
  const node: Node = {
    id: `APPROVAL_NODE_${nodes.value.length - 2}`,
    type: 'approval',
    sourcePosition: Position.Right,
    targetPosition: Position.Left,
    position: data.position,
    data: {
      label: choice?.name,
      type: choice?.value,
    },
  }
  const targetIndex = nodes.value.findIndex((item) => item.id === data.target)
  if (targetIndex > -1) {
    nodes.value.splice(targetIndex + 1, 0, node)
  }
  return node.id
}
const createEdge = (sourceId: string, targetId: string) => {
  const edge: Edge = {
    id: `${sourceId}->${targetId}`,
    source: sourceId,
    target: targetId,
    type: 'approval',
  }
  edges.value.push(edge)
}
const removeEdge = (edgeId: string) => {
  const edgeIndex = edges.value.findIndex((item) => item.id === edgeId)
  if (edgeIndex > -1) {
    edges.value.splice(edgeIndex, 1)
  }
}
const handleAddNodeOnEdge = (data: EdgeEmitData) => {
  const nodeId = createNode(data)
  createEdge(data.source, nodeId)
  createEdge(nodeId, data.target)
  removeEdge(data.edgeId)
}
const toggleApprovalPanel = (data?: Node) => {
  ApprovalPanelRef.value?.onVisible(isInteractive.value ? undefined : data)
}
const handleInteractionChange = (interactive: boolean) => {
  isInteractive.value = interactive
  toggleApprovalPanel()
}

const fetchData = async (id: number) => {
  const resp = await getFlowTemplate(id)
  if (resp.success) {
    flowData.value = resp.data
    const flowGraph = JSON.parse(resp.data.content)
    if (!flowGraph) return
    nodes.value = flowGraph.nodes
    edges.value = flowGraph.edges
    await fromObject(flowGraph)
  }
}
onMounted(() => {
  if (route.query.id) {
    fetchData(Number(route.query.id))
  }
})

onNodeDragStop((event) => {
  const currentNode = nodes.value.find((node) => node.id === event.node.id)
  if (currentNode) {
    currentNode.position = event.node.position
  }
})
onNodeClick((event) => {
  if ([NodeType.START, NodeType.END].includes(event.node.data.type)) {
    toggleApprovalPanel()
    return
  }
  toggleApprovalPanel(event.node)
})
onPaneClick(() => {
  toggleApprovalPanel()
})
provide('isInteractive', isInteractive)
</script>
