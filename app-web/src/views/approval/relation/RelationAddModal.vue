<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :width="width >= 500 ? 500 : '100%'"
    draggable
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :columns="columns">
    </GiForm>
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { useWindowSize } from '@vueuse/core'
import { type ColumnItem, GiForm } from '@/components/GiForm'
import { useResetReactive } from '@/hooks'
import {bindFlowToBusinessType, createFlowTemplate, listFlowTemplateDict, updateFlowTemplate} from '@/apis/approval'
import { useDict } from '@/hooks/app'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const { business_type } = useDict('business_type')
const { width } = useWindowSize()

const dataId = ref()
const visible = ref(false)
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改业务关联' : '新增业务关联'))
const formRef = ref<InstanceType<typeof GiForm>>()

const [form, resetForm] = useResetReactive({
  id: null,
  sort: 999,
  status: 1,
})

const columns: ColumnItem[] = reactive([
  {
    label: '业务类型',
    field: 'businessTypeCode',
    type: 'select',
    span: 24,
    required: true,
    props: {
      options: business_type,
      placeholder: '业务类型',
    },
  },
  {
    label: '流程模板',
    field: 'flowTemplateId',
    type: 'select',
    span: 24,
    required: true,
    props: {
      options: [],
      placeholder: '流程模板',
    },
  },
])

const fetchFlowTemplates = async () => {
  const resp = await listFlowTemplateDict()
  if (resp.success) {
    columns[1].props!.options = resp.data
  }
}

// 重置
const reset = () => {
  formRef.value?.formRef?.resetFields()
  resetForm()
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      form.id = dataId.value
      await bindFlowToBusinessType(form)
      Message.success('修改成功')
    } else {
      await bindFlowToBusinessType(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

// 新增
const onAdd = (id: number) => {
  reset()
  visible.value = true
}

// 修改
const onUpdate = async (id: number) => {
  reset()
  dataId.value = id
  // Object.assign(form, res.data)
  visible.value = true
}

onMounted(() => {
  fetchFlowTemplates()
})
defineExpose({ onAdd, onUpdate })
</script>

<style scoped lang="scss"></style>
