<template>
  <a-tag v-if="props.gender === 1" color="arcoblue" size="small" class="gi_round">
    <template #icon><icon-man /></template>
    <template #default>男</template>
  </a-tag>
  <a-tag v-else-if="props.gender === 2" color="magenta" size="small" class="gi_round">
    <template #icon><icon-woman /></template>
    <template #default>女</template>
  </a-tag>
  <a-tag v-else color="gray" size="small" class="gi_round">
    <template #default>未知</template>
  </a-tag>
</template>

<script setup lang="ts">
defineOptions({ name: 'GiCellGender' })

const props = withDefaults(defineProps<Props>(), {
  gender: 0,
})

interface Props {
  gender: 0 | 1 | 2
}
</script>

<style scoped lang="scss"></style>
