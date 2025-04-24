<template>
  <a-card v-show="selectedNode" :title="`设置${selectedNode?.data.label}`" class="flow-panel">
    <template #extra>
      <icon-close style="cursor: pointer" @click="selectedNode = undefined" />
    </template>
    <div class="flow-panel-content">
      <a-space direction="vertical" size="large">
        <a-radio-group v-model="form.strategy">
          <a-grid :cols="3" :colGap="24" :rowGap="16">
            <a-grid-item v-for="item in STRATEGIES" :key="item.value">
              <a-radio :value="item.value">{{ item.label }}</a-radio>
            </a-grid-item>
          </a-grid>
        </a-radio-group>
        <div class="flow-panel-user-selector">
          <a-form
            ref="formRef"
            :model="form"
            :rules="rules"
            size="large"
            layout="vertical"
            class="form"
          >
            <div v-if="form.strategy === 1">
              <a-form-item class="form-item" field="userIds" required>
                <template #label>
                  <a-space>
                    <span class="item-label">指定成员</span>
                    <span class="item-help">不能超过50人</span>
                  </a-space>
                </template>
                <a-select v-model="form.userIds" :options="[]" placeholder="请选择指定成员" />
              </a-form-item>
            </div>
            <div v-if="form.strategy === 2">
              <a-form-item class="form-item">
                <template #label>
                  <span class="item-label">发起人自己</span><br>
                  <span class="item-help">发起人自己将作为审批人处理审批单</span>
                </template>
              </a-form-item>
            </div>
            <div v-if="form.strategy === 3">
              <a-form-item class="form-item">
                <template #label>
                  <span class="item-label">发起人自选</span><br>
                </template>
                <div style="width: 100%">
                  <a-row :gutter="24" style="width: 100%">
                    <a-col :flex="1">
                      <a-select v-model="form.selectType" :options="SELF_SELECTION" />
                    </a-col>
                    <a-col :flex="1">
                      <a-select v-model="form.selectScope" :options="SELECTION_SCOPE" />
                    </a-col>
                  </a-row>
                </div>
              </a-form-item>
              <div v-if="form.selectScope === 2" class="form-extra">
                <a-select v-model="form.userIds" :options="[]" placeholder="请选择成员" />
              </div>
              <div v-if="form.selectScope === 3" class="form-extra">
                <a-select v-model="form.roleIds" :options="[]" placeholder="请选择角色" />
              </div>
              <div v-if="form.selectType === 2" class="form-extra">
                <div class="item-label">多人审批时采用的审批方式</div>
                <a-radio-group v-model="form.approvalType" direction="vertical" :options="APPROVAL_TYPES" />
              </div>
            </div>
            <div v-if="form.strategy === 4">
              <a-form-item class="form-item" field="roleIds" required>
                <template #label>
                  <a-space>
                    <span class="item-label">添加角色</span>
                    <span class="item-help">仅支持选择 1个 角色</span>
                  </a-space>
                </template>
                <a-select v-model="form.roleIds" :options="[]" />
              </a-form-item>
              <div class="form-extra">
                <div>
                  <div class="item-label">多人审批时采用的审批方式</div>
                  <a-radio-group v-model="form.approvalType" direction="vertical" :options="APPROVAL_TYPES" />
                </div>
                <div style="margin-top: 14px">
                  <div class="item-label">审批人为空时</div>
                  <a-radio-group v-model="form.emptyPersonStrategy" direction="vertical" :options="EMPTY_PERSON_STRATEGY" />
                </div>
              </div>
            </div>
          </a-form>
        </div>
      </a-space>
    </div>
    <div class="flow-panel-footer">
      <a-space>
        <a-button @click="selectedNode = undefined">取消</a-button>
        <a-button type="primary" @click="handleSave">保存</a-button>
      </a-space>
    </div>
  </a-card>
</template>

<script setup lang="ts">
import type { Node } from '@vue-flow/core'
import { type FormInstance, Message } from '@arco-design/web-vue'
import {
  APPROVAL_TYPES,
  EMPTY_PERSON_STRATEGY,
  SELECTION_SCOPE,
  SELF_SELECTION,
  STRATEGIES,
} from './support'
import { useResetReactive } from '@/hooks'

defineOptions({ name: 'ApprovalPanel' })

const selectedNode = ref()
const formRef = ref<FormInstance>()
const rules: FormInstance['rules'] = {
  userIds: [{ required: true, message: '请选择成员' }],
  roleIds: [{ required: true, message: '请选择角色' }],
}
const [form] = useResetReactive({
  strategy: 1,
  userIds: [], // 指定成员
  roleIds: [], // 指定角色
  himself: true, // 发起人自己
  selectType: 1, // 自选类型
  selectScope: 1, // 选择范围
  approvalType: 1, // 审批类型
  emptyPersonStrategy: 1, // 无审批人策略
})

// 保存
const handleSave = async () => {
  const isInvalid = await formRef.value?.validate()
  if (isInvalid) return false
  Message.success('保存成功')
}

const onVisible = (node?: Node) => {
  selectedNode.value = node
}
defineExpose({ onVisible })
</script>

<style scoped lang="scss">
.flow-panel {
  position: relative;
  width: 480px;
  height: calc(100vh - 224px);
  border-radius: 4px;
  background-color: var(--color-bg-3);
  border: 1px solid var(--color-border);
  //box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.05);
  box-shadow: 1px 3px 32px 0 rgba(50, 73, 198, 0.08), 6px 16px 48px 0 rgba(50, 73, 198, 0.12);
  user-select: none;

  .flow-panel-content {
    height: calc(100% - 56px);
    .form {
      .form-item {
        .item.label {
          color: var(--color-text-2);
        }
        .item-help {
          font-weight: 400;
          color: var(--color-text-3);
        }
      }

      .form-extra {
        .item-label {
          color: var(--color-text-2);
          line-height: 36px;
        }
      }
    }
  }

  .flow-panel-footer {
    position: absolute;
    box-sizing: border-box;
    bottom: 0;
    left: 0;
    width: 100%;
    border-top: 1px solid var(--color-border-2);
    padding: 10px 16px;
    text-align: right;
  }
}
</style>
