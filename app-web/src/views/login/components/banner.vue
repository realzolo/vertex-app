<template>
  <div class="banner">
    <img src="../assets/login-illustration.png" alt="" />
  </div>
</template>

<script lang="ts" setup></script>

<style lang="less" scoped>
  .banner {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    position: relative;
    overflow: hidden;
    background: linear-gradient(
      60deg,
      rgb(var(--primary-6)),
      rgb(var(--primary-3))
    );
    user-select: none;

    img {
      width: 100%;
      position: absolute;
      bottom: 0;
      right: 0;
      top: 50%;
      left: 50%;
      transform: translate(-50%) translateY(-50%);
      transition: all 0.3s;
      object-fit: cover;
    }
  }
</style>
