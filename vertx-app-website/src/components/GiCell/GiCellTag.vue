<template>
  <a-tag :color="dictItem.color">{{ dictItem.label }}</a-tag>
</template>

<script setup lang="ts">
import type { GiCellTagType } from '@/components/GiCell/type'

defineOptions({ name: 'GiCellTag' })
const props = withDefaults(defineProps<Partial<GiCellTagType>>(), {
  dict: [{
    label: '',
    value: '',
  }],
  value: '',
})

const dictItem = computed((): DictionaryEntry => {
  try {
    return props.dict.find(
      (d) => d.value === String(props.value) || d.value === Number(props.value),
    ) || { label: '', value: '' }
  } catch (error) {
    return { label: '', value: '' }
  }
})
</script>

<style scoped lang="scss"></style>
