<template>
  <div class="layout-content">
    <Main></Main>
  </div>
</template>

<script setup lang="ts">
import Main from './components/Main.vue'

defineOptions({ name: 'LayoutContent' })
</script>

<style scoped lang="scss">
.layout-content {
  height: 100%;
}
</style>
