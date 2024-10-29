<template>
  <div class="wrapper">
    <div class="background"></div>
    <div class="header">
      <div class="website-logo">
        <img src="@/assets/logo.png" alt="logo" />
        <span>Vertex App</span>
      </div>
      <div class="website-theme">
        <a-button v-if="isDark" @click="toggleTheme(false)">
          <template #icon>
            <icon-sun-fill size="18px" />
          </template>
        </a-button>
        <a-button v-if="!isDark" @click="toggleTheme(true)">
          <template #icon>
            <icon-moon-fill size="18px" />
          </template>
        </a-button>
      </div>
    </div>
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
    <div class="footer">
      <div class="copyright">
        Copyright © 2023 - present Vertex App 版权所有
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import Banner from '@/views/login/components/banner.vue';
  import LoginForm from '@/views/login/components/login-from.vue';
  import useThemes from '@/hooks/themes';
  import useAppStore from '../../store/modules/app';

  const { toggleTheme } = useAppStore();
  const { isDark } = useThemes();
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
      position: absolute;
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 80px;
      width: 100%;
      padding: 20px 30px;
      z-index: 2;
      user-select: none;

      .website-logo {
        display: flex;
        align-items: center;

        img {
          width: 36px;
          height: 36px;
          vertical-align: middle;
          margin-right: 8px;
        }

        span {
          color: var(--color-text-1);
          font-weight: 500;
          font-size: 20px;
          line-height: 34px;
        }
      }

      .website-theme {
        button {
          width: 24px;
          height: 24px;
          background-color: transparent;
          transition: all 0.2s;

          &:hover {
            background: var(--color-secondary-hover);
            border-radius: 50%;
          }
        }
      }
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
    position: absolute;
    bottom: 0;
    z-index: 2;
    height: 40px;
    width: 100%;

    .copyright {
      color: var(--color-text-3);
      font-size: 12px;
      font-weight: 400;
      letter-spacing: 0.2px;
      line-height: 20px;
      text-align: center;
    }
  }
</style>
