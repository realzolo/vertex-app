<template>
  <a-modal
    v-model:visible="visible"
    :title="title"
    :mask-closable="false"
    :esc-to-close="false"
    :modal-style="{ maxWidth: '520px' }"
    width="90%"
    @before-ok="save"
    @close="reset"
  >
    <GiForm ref="formRef" v-model="form" :options="options" :columns="columns" />
  </a-modal>
</template>

<script setup lang="ts">
import { Message } from '@arco-design/web-vue'
import { addApp, getApp, updateApp } from '@/apis/open/app'
import { type Columns, GiForm, type Options } from '@/components/GiForm'
import { useForm } from '@/hooks'
import { useDict } from '@/hooks/app'

const emit = defineEmits<{
  (e: 'save-success'): void
}>()

const dataId = ref('')
const isUpdate = computed(() => !!dataId.value)
const title = computed(() => (isUpdate.value ? '修改应用' : '新增应用'))
const formRef = ref<InstanceType<typeof GiForm>>()

const { app_type } = useDict('app_type')

const options: Options = {
  form: {},
  btns: { hide: true },
}

const columns: Columns = reactive([
  {
    label: '应用名称',
    field: 'name',
    type: 'input',
    rules: [{ required: true, message: '请输入应用名称' }],
  },
  {
    label: 'APPKEY',
    field: 'appKey',
    type: 'input',
    rules: [{ required: true, message: '请输入APPKEY' }],
  },
  {
    label: '应用状态',
    field: 'status',
    type: 'switch',
    options: app_type,
    props: {
      type: 'round',
      defaultChecked: true,
      checkedValue: '1',
      uncheckedValue: '0',
      checkedText: '启用',
      uncheckedText: '禁用',
      checkedColor: 'green',
    },
    rules: [{ required: false, message: '请输入应用状态' }],
  },
  {
    label: '失效时间',
    field: 'expirationTime',
    type: 'date-picker',
    props: {
      showTime: true,
    },
    rules: [{ required: true, message: '请输入失效时间' }],
  },
  {
    label: '应用描述',
    field: 'appDesc',
    type: 'textarea',
  },
])

const { form, resetForm } = useForm({
  // todo 待补充
})

// 重置
const reset = () => {
  formRef.value?.formRef?.resetFields()
  resetForm()
}

const visible = ref(false)
// 新增
const onAdd = () => {
  reset()
  dataId.value = ''
  visible.value = true
}

// 修改
const onUpdate = async (id: string) => {
  reset()
  dataId.value = id
  const res = await getApp(id)
  Object.assign(form, res.data)
  visible.value = true
}

// 保存
const save = async () => {
  try {
    const isInvalid = await formRef.value?.formRef?.validate()
    if (isInvalid) return false
    if (isUpdate.value) {
      await updateApp(form, dataId.value)
      Message.success('修改成功')
    } else {
      await addApp(form)
      Message.success('新增成功')
    }
    emit('save-success')
    return true
  } catch (error) {
    return false
  }
}

defineExpose({ onAdd, onUpdate })
</script>
