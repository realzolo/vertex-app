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
    <GiForm ref="formRef" v-model="form" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { useWindowSize } from '@vueuse/core'
import { createDictGroup, getDictGroup, updateDictGroup } from '@/apis/system/dict'
import { type ColumnItem, GiForm } from '@/components/GiForm'
import { useResetReactive } from '@/hooks'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const { width } = useWindowSize()

const dataId = ref()
const visible = ref(false)
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改字典分组' : '新增字典分组'))
const formRef = ref<InstanceType<typeof GiForm>>()

const [form, resetForm] = useResetReactive({
  id: null,
})

const columns: ColumnItem[] = reactive([
  {
    label: '名称',
    field: 'name',
    type: 'input',
    span: 24,
    required: true,
    props: {
      maxLength: 30,
    },
  },
  {
    label: '编码',
    field: 'value',
    type: 'input',
    span: 24,
    required: true,
    rules: [
      {
        validator: (value, callback) => {
          if (!/^[a-z0-9_]+$/.test(value)) {
            callback('字典编码只能包含小写字母以及下划线！')
          } else {
            callback()
          }
        },
      },
    ],
    props: {
      maxLength: 30,
    },
    disabled: () => isUpdate.value,
  },
  {
    label: '描述',
    field: 'remark',
    type: 'textarea',
    span: 24,
  },
])

// 重置
const reset = () => {
  formRef.value?.formRef?.resetFields()
  resetForm()
}

// 保存
const save = async () => {
  const isInvalid = await formRef.value?.formRef?.validate()
  if (isInvalid) return false
  try {
    if (isUpdate.value) {
      form.id = dataId.value
      await updateDictGroup(form)
      Message.success('修改成功')
    } else {
      await createDictGroup(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

// 新增
const onAdd = () => {
  reset()
  dataId.value = ''
  visible.value = true
}

// 修改
const onUpdate = async (id: number) => {
  reset()
  dataId.value = id
  const { data } = await getDictGroup(id)
  Object.assign(form, data)
  visible.value = true
}

defineExpose({ onAdd, onUpdate })
</script>

<style scoped lang="scss"></style>
