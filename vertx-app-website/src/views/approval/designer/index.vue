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
    <template #node-input="props">
      <InputNode v-bind="props" />
    </template>
    <template #node-output="props">
      <OutputNode v-bind="props" />
    </template>
    <template #node-approval="props">
      <ApprovalNode v-bind="props" @delete-node="handleDeleteNode" />
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
import { Message } from '@arco-design/web-vue'
import { type EdgeEmitData, NodeType } from '../type'
import Header from './components/Header/index.vue'
import InputNode from './InputNode.vue'
import OutputNode from './OutputNode.vue'
import ApprovalNode from './ApprovalNode.vue'
import ApprovalEdge from './ApprovalEdge.vue'
import ApprovalPanel from './ApprovalPanel.vue'
import { FLOW_NODE_OPTIONS, initialEdges, initialNodes } from './support'
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
  findNode,
} = useVueFlow()

const ApprovalPanelRef = ref<InstanceType<typeof ApprovalPanel>>()
const nodes = ref<Node[]>([])
const edges = ref<Edge[]>([])
const flowData = ref()
const isInteractive = ref<boolean>(false)

const createNode = (data: EdgeEmitData) => {
  const choice = FLOW_NODE_OPTIONS.find((item) => item.value === data.type)
  const node: Node = {
    id: `APPROVAL_NODE_${nodes.value.length - 2}`,
    label: choice?.name,
    type: 'approval',
    sourcePosition: Position.Right,
    targetPosition: Position.Left,
    position: data.position,
    data: {
      label: choice?.name,
      type: choice?.value,
      source: data.source,
      target: data.target,
    },
  }
  const targetIndex = nodes.value.findIndex((item) => item.id === data.target)
  if (targetIndex > -1) {
    nodes.value.splice(targetIndex + 1, 0, node)
  }
  findNode(data.source)!.data.target = node.id
  findNode(data.target)!.data.source = node.id
  return node.id
}
const removeNode = (nodeId: string) => {
  const nodeIndex = nodes.value.findIndex((item) => item.id === nodeId)
  if (nodeIndex > -1) {
    nodes.value.splice(nodeIndex, 1)
  }
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
const removeNodeEdges = (node: Node) => {
  edges.value = edges.value.filter((item) => {
    return !(item.source === node.id || item.target === node.id)
  })
}

const toggleApprovalPanel = (data?: Node) => {
  ApprovalPanelRef.value?.onVisible(isInteractive.value ? undefined : data)
}

/** 添加Node */
const handleAddNodeOnEdge = (data: EdgeEmitData) => {
  const nodeId = createNode(data)
  createEdge(data.source, nodeId)
  createEdge(nodeId, data.target)
  removeEdge(data.edgeId)
}
/** 删除Node */
const handleDeleteNode = (nodeId: string) => {
  const node = findNode(nodeId)
  removeNode(nodeId)
  removeNodeEdges(node!)
  createEdge(node!.data.source, node!.data.target)
  findNode(node?.data.source)!.data.target = node!.data.target
  findNode(node?.data.target)!.data.source = node!.data.source
  toggleApprovalPanel()
}

const handleInteractionChange = (interactive: boolean) => {
  isInteractive.value = interactive
  toggleApprovalPanel()
}

const fetchData = async (id: number) => {
  const resp = await getFlowTemplate(id)
  if (resp.success) {
    flowData.value = resp.data
    if (!resp.data.content) {
      nodes.value = initialNodes
      edges.value = initialEdges
      return
    }
    const flowGraph = JSON.parse(resp.data.content)
    nodes.value = flowGraph.nodes
    edges.value = flowGraph.edges
    await fromObject(flowGraph)
  }
}
onMounted(() => {
  if (!route.query.id) {
    Message.error('未找到流程模板')
    return
  }
  fetchData(Number(route.query.id))
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
