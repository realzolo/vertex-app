<template>
  <div class="wrapper">
    <div class="background"></div>
    <div class="header"></div>
    <div class="body">
      <div class="login-form-wrapper">
        <a-row class="gird-row-col">
          <a-col :span="13">
            <banner />
          </a-col>
          <a-col :span="11">
            <login-form />
          </a-col>
        </a-row>
      </div>
    </div>
    <div class="footer"></div>
  </div>
</template>

<script lang="ts" setup>
  import Banner from '@/views/login/components/banner.vue';
  import LoginForm from '@/views/login/components/login-from.vue';
</script>

<style lang="less" scoped>
  .wrapper {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;

    .background {
      position: absolute;
      width: 100%;
      height: 100%;
    }

    .header {
      z-index: 2;
    }

    .body {
      position: absolute;
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 1;
      width: 100%;
      height: 100%;

      .login-form-wrapper {
        width: 86%;
        max-width: 850px;
        height: 490px;
        display: flex;
        box-shadow: 0 2px 4px 2px #00000014;

        .gird-row-col {
          width: 100%;
          height: 100%;

          .arco-col {
            width: 100%;
            height: 100%;
          }
        }
      }
    }
  }

  .footer {
    z-index: 2;
  }
</style>
