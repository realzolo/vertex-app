<template>
  <a-form
    ref="formRef"
    :model="form"
    :rules="rules"
    :label-col-style="{ display: 'none' }"
    :wrapper-col-style="{ flex: 1 }"
    size="large"
    @submit="handleLogin"
  >
    <a-form-item field="email" hide-label>
      <a-input v-model="form.email" placeholder="请输入邮箱" allow-clear />
    </a-form-item>
    <a-form-item field="verificationCode" hide-label>
      <a-input v-model="form.verificationCode" placeholder="请输入验证码" :max-length="6" allow-clear style="flex: 1 1" />
      <a-button
        class="captcha-btn"
        :loading="captchaLoading"
        :disabled="captchaDisable"
        size="large"
        @click="onCaptcha"
      >
        {{ captchaBtnName }}
      </a-button>
    </a-form-item>
    <a-form-item>
      <a-space direction="vertical" fill class="w-full">
        <a-button class="btn" type="primary" :loading="loading" html-type="submit" size="large" long>立即登录</a-button>
      </a-space>
    </a-form-item>
  </a-form>
</template>

<script setup lang="ts">
import { type FormInstance, Message } from '@arco-design/web-vue'
import { getEmailCaptcha } from '@/apis'
import { useTabsStore, useUserStore } from '@/stores'
import * as Regexp from '@/utils/regexp'

const formRef = ref<FormInstance>()
const form = reactive({
  email: '',
  verificationCode: '',
})

const rules: FormInstance['rules'] = {
  email: [
    { required: true, message: '请输入邮箱' },
    { match: Regexp.Email, message: '请输入正确的邮箱' },
  ],
  verificationCode: [{ required: true, message: '请输入验证码' }],
}

const userStore = useUserStore()
const tabsStore = useTabsStore()
const router = useRouter()
const loading = ref(false)
// 登录
const handleLogin = async () => {
  try {
    const isInvalid = await formRef.value?.validate()
    if (isInvalid) return
    loading.value = true
    await userStore.emailLogin(form)
    tabsStore.reset()
    const { redirect, ...othersQuery } = router.currentRoute.value.query
    if (redirect) {
      const url = new URL(decodeURIComponent(redirect as string), window.location.origin)
      await router.replace({
        path: url.pathname,
        query: {
          ...Object.fromEntries(url.searchParams.entries()),
          ...othersQuery,
        },
      })
    } else {
      await router.replace({
        path: '/',
        query: {
          ...othersQuery,
        },
      })
    }
    Message.success('欢迎使用')
  } catch (error) {
    form.verificationCode = ''
  } finally {
    loading.value = false
  }
}

const captchaLoading = ref(false)
const captchaTimer = ref()
const captchaTime = ref(60)
const captchaBtnName = ref('获取验证码')
const captchaDisable = ref(false)
// 重置验证码
const resetCaptcha = () => {
  window.clearInterval(captchaTimer.value)
  captchaTime.value = 60
  captchaBtnName.value = '获取验证码'
  captchaDisable.value = false
}

// 获取验证码
const getCaptcha = async () => {
  try {
    captchaLoading.value = true
    captchaBtnName.value = '发送中...'
    const resp = await getEmailCaptcha(form.email)
    captchaLoading.value = false
    captchaDisable.value = true
    if (!resp.success) {
      Message.error('验证码发送失败')
      return
    }
    captchaBtnName.value = `获取验证码(${(captchaTime.value -= 1)}s)`
    Message.success('验证码发送成功')
    captchaTimer.value = window.setInterval(() => {
      captchaTime.value -= 1
      captchaBtnName.value = `获取验证码(${captchaTime.value}s)`
      if (captchaTime.value <= 0) {
        resetCaptcha()
      }
    }, 1000)
  } catch (error) {
    resetCaptcha()
  } finally {
    captchaLoading.value = false
  }
}

const onCaptcha = async () => {
  if (captchaLoading.value) return
  const isInvalid = await formRef.value?.validateField('email')
  if (isInvalid) return
  await getCaptcha()
}
</script>

<style scoped lang="scss">
.arco-input-wrapper,
:deep(.arco-select-view-single) {
  height: 40px;
  border-radius: 4px;
  font-size: 13px;
}

.arco-input-wrapper.arco-input-error {
  background-color: rgb(var(--danger-1));
  border-color: rgb(var(--danger-3));
}
.arco-input-wrapper.arco-input-error:hover {
  background-color: rgb(var(--danger-1));
  border-color: rgb(var(--danger-6));
}

.arco-input-wrapper :deep(.arco-input) {
  font-size: 13px;
  color: var(--color-text-1);
}
.arco-input-wrapper:hover {
  border-color: rgb(var(--arcoblue-6));
}

.captcha-btn {
  height: 40px;
  margin-left: 12px;
  min-width: 98px;
  border-radius: 4px;
}

.btn {
  height: 40px;
}
</style>
