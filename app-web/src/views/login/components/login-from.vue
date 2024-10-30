<template>
  <div class="login-form">
    <a-tabs default-active-key="idPassword" @change="handleTabChange">
      <a-tab-pane key="idPassword" title="账号登录">
        <a-form
          size="large"
          :model="form"
          :rules="rules"
          :label-col-props="{ span: 0, offset: 0 }"
          @submit-success="handleSubmit"
        >
          <a-form-item field="username">
            <a-input v-model="form.username" placeholder="请输入用户名" />
          </a-form-item>
          <a-form-item field="password">
            <a-input-password
              v-model="form.password"
              allow-clear
              placeholder="请输入密码"
            />
          </a-form-item>
          <a-form-item field="captcha">
            <a-input v-model="form.captcha" placeholder="请输入验证码" />
            <div class="captcha-container">
              <img src="../assets/captcha.png" alt="captcha" />
              <div v-show="false" class="overlay">
                <p>已过期，请刷新</p>
              </div>
            </div>
          </a-form-item>
          <a-form-item field="remember">
            <div class="extra-options">
              <a-checkbox value="1">记住我</a-checkbox>
              <a-button type="text">忘记密码</a-button>
            </div>
          </a-form-item>
          <a-form-item>
            <a-button
              long
              html-type="submit"
              type="primary"
              class="submit-button"
              :loading="loading"
            >
              立即登录
            </a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
      <a-tab-pane key="email" title="邮箱登录">
        <a-form
          size="large"
          :model="form"
          :rules="rules"
          :label-col-props="{ span: 0, offset: 0 }"
          @submit-success="handleSubmit"
        >
          <a-form-item field="email">
            <a-input v-model="form.email" placeholder="请输入电子邮箱地址" />
          </a-form-item>
          <a-form-item field="captcha">
            <a-input v-model="form.captcha" placeholder="请输入验证码" />
            <div class="captcha-container">
              <a-button disabled>获取验证码</a-button>
            </div>
          </a-form-item>
          <a-form-item>
            <a-button
              long
              html-type="submit"
              type="primary"
              class="submit-button"
              :loading="loading"
            >
              立即登录
            </a-button>
          </a-form-item>
        </a-form>
      </a-tab-pane>
    </a-tabs>
    <div class="from-extra">
      <a-divider orientation="center">其他登录方式</a-divider>
      <a-space fill align="center">
        <svg
          class="icon"
          aria-hidden="true"
          @click="thirdPartyLogin('Authenticator')"
        >
          <use xlink:href="#vertex-a-MicrosoftAuthenticator" />
        </svg>
        <svg class="icon" aria-hidden="true" @click="thirdPartyLogin('Github')">
          <use xlink:href="#vertex-github" />
        </svg>
        <svg class="icon" aria-hidden="true" @click="thirdPartyLogin('Gitee')">
          <use xlink:href="#vertex-gitee" />
        </svg>
      </a-space>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue';
  import { Message } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { useStorage } from '@vueuse/core';
  import { useUserStore } from '@/store';
  import { login as userLogin, LoginData } from '@/api/user';

  const userStore = useUserStore();

  const loginType = ref('idPassword');
  const form = reactive({
    username: '',
    email: '',
    password: '',
    captcha: '',
  });

  const { loading, setLoading } = useLoading();

  const rules = {
    username: [
      {
        required: true,
        message: '请输入用户名',
      },
    ],
    password: [
      {
        required: true,
        message: '请输入密码',
      },
    ],
    email: [
      {
        type: 'email',
        required: true,
        message: '请输入正确的电子邮箱地址',
      },
    ],
    captcha: [
      {
        required: true,
        message: '请输入验证码',
      },
    ],
  };

  /**
   * 切换Tab时清除共用字段
   */
  const handleTabChange = (tabKey: string) => {
    loginType.value = tabKey;
    form.captcha = '';
  };

  const handleSubmit = async () => {
    setLoading(true);

    if (loginType.value === 'idPassword') {
      useStorage('login-config', {
        username: form.username,
        password: form.password,
      });
    }
    try {
      const resp = await userLogin(loginType.value, form as LoginData);
      console.log(resp.data.);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const thirdPartyLogin = (type: string) => {
    Message.error(`暂不支持${type}登录，请使用账号密码登录`);
  };
</script>

<style scoped lang="less">
  .login-form {
    width: 100%;
    height: 100%;
    background: var(--color-bg-1);
    display: flex;
    flex-direction: column;
    padding: 30px 30px 0;
    user-select: none;

    .arco-form {
      .arco-form-item {
        .captcha-container {
          position: relative;
          margin-left: 8px;
          border-radius: 4px;
          cursor: pointer;

          img {
            width: 111px;
            height: 36px;
            margin: 0 0 0 5px;
          }

          .overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            border-radius: 4px;
            background-color: #333c;

            p {
              font-size: 12px;
              color: #fff;
            }
          }

          button {
            height: 40px;
            background-color: transparent;
            border: 1px solid var(--color-border-3);

            &:hover {
              background: var(--color-secondary-hover);
            }

            &.arco-btn-disabled {
              background: var(--color-secondary-hover);
            }
          }
        }

        .extra-options {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 100%;

          button {
            padding: 0;

            &:hover {
              color: rgb(var(--primary-4));
              background: transparent;
            }
          }
        }

        .submit-button {
          height: 40px;
          border-radius: 4px;
        }
      }
    }

    .from-extra {
      margin-top: auto;
      margin-bottom: 20px;

      .arco-space {
        .arco-space-item {
          .icon {
            width: 24px;
            height: 24px;
            cursor: pointer;
            border-radius: 50%;
          }
        }
      }
    }
  }
</style>

<style scoped lang="less">
  :deep(.arco-tabs) {
    .arco-tabs-nav {
      &::before {
        display: none;
      }

      .arco-tabs-nav-tab {
        justify-content: center;

        .arco-tabs-tab {
          .arco-tabs-tab-title {
            font-size: 16px;
            font-weight: 500;
            line-height: 22px;
          }
        }

        .arco-tabs-nav-ink {
          height: 1.6px;
        }
      }

      .arco-tabs-nav-type-line,
      .arco-tabs-tab:hover .arco-tabs-tab-title::before {
        background-color: unset;
      }
    }

    .arco-tabs-content {
      margin-top: 10px;
    }
  }

  :deep(.arco-form.arco-form-size-large) {
    .arco-form-item {
      .arco-form-item-wrapper-col {
        flex: 1 1 0;

        .arco-form-item-content-wrapper {
          .arco-form-item-content {
            min-height: 36px;

            .arco-input-wrapper {
              height: 40px;
              border-radius: 4px;
              font-size: 13px;
              background-color: transparent;
              border-color: var(--color-border-3);

              &.arco-input-error {
                border-color: rgb(var(--danger-3));
              }

              &:hover,
              &:focus-within {
                border-color: rgb(var(--arcoblue-6));
              }
            }
          }
        }
      }
    }
  }

  :deep(.arco-divider) {
    .arco-divider-text {
      color: var(--color-text-3);
      font-size: 12px;
      font-weight: 400;
      line-height: 20px;
    }
  }

  :deep(.arco-space) {
    justify-content: center;
  }
</style>
