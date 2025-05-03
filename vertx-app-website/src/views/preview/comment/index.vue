<template>
  <GiPageLayout
    :margin="true"
    :default-collapsed="false"
    :header-style="isDesktop ? { padding: 0, borderBottomWidth: 0 } : { borderBottomWidth: '1px' } "
    class="layout"
  >
    <div class="comment">
      <div class="comment-header">
        <h3>评论 {{ comments.length }}</h3>
      </div>
      <div class="comment-body">
        <div class="comment-input">
          <a-textarea
            v-model="commentContent"
            allow-clear
            show-word-limit
            placeholder="请输入评论内容..."
            :textarea-attrs="{ rows: 4 }"
            :max-length="1000"
          />
          <div class="comment-actions">
            <a-button type="primary" @click="submitComment">发送</a-button>
          </div>
        </div>
        <div class="comment-content">
          <div class="content-actions">
            <a-typography-text :type="sortType === 'latest' ? 'primary' : 'secondary'" @click="onSortType('latest')">最新</a-typography-text>
            <a-typography-text :type="sortType === 'hottest' ? 'primary' : 'secondary'" @click="onSortType('hottest')">最热</a-typography-text>
          </div>
          <a-spin :loading="loading">
            <div class="comment-items">
              <RecursiveComment v-for="comment in comments" :key="comment.id" :comment="comment" :search="search" />
            </div>
            <div v-if="!comments.length" class="content-empty">
              <a-empty />
            </div>
          </a-spin>
        </div>
      </div>
      <div class="comment-footer">
        <div v-if="pagination.current * pagination.pageSize < pagination.total" class="comment-more">
          <a-button type="text" @click="loadMore">查看更多</a-button>
        </div>
      </div>
    </div>
  </GiPageLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import RecursiveComment from './RecursiveComment.vue'
import { useDevice, useResetReactive, useTable } from '@/hooks'
import { type CommentReq, type CommentResp, addComment, listComment } from '@/apis/preview/comment'

const route = useRoute()
const { isDesktop } = useDevice()

const { id = 10000 } = route.query

// 初始化评论数据
const comments = ref<CommentResp[]>([])

// 评论输入框内容
const commentContent = ref('')

// 评论排序：最新(latest)、最热(hottest)
const sortType = ref('latest')

const [queryForm] = useResetReactive({
  objectId: Number(id),
  sortType: 'latest',
})

const {
  tableData,
  loading,
  pagination,
  search,
  handleDelete,
} = useTable((page) => listComment({ ...queryForm, ...page }), { immediate: true })

// 提交评论
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    Message.warning('请输入评论内容')
  }
  const comment: CommentReq = {
    objectId: Number(id),
    parentId: null,
    content: commentContent.value,
  }
  const resp = await addComment(comment)
  if (!resp.success) {
    Message.error('添加评论失败')
    return
  }
  Message.success('评论成功')
  commentContent.value = ''
  search()
}

// 排序评论
const onSortType = (type: string) => {
  sortType.value = type
  if (type === 'latest') {
    comments.value.sort((a, b) => b.createTime - a.createTime)
  } else if (type === 'hottest') {
    comments.value.sort((a, b) => b.upvotes - a.upvotes)
  }
}

// 加载更多评论
const loadMore = () => {
}
watch(() => tableData.value, (newValue: CommentResp[]) => {
  comments.value = newValue
})
onMounted(() => {
})
</script>

<style scoped lang="scss">
.layout {
  background: transparent;
}

.comment {
  padding: 16px;
  background-color: var(--color-bg-1);
  border-radius: 4px;
  width: 1080px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  margin: 0 auto;

  .comment-header {
    margin-bottom: 16px;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 500;
      color: var(--color-text-1);
    }
  }

  .comment-body {
    margin-bottom: 16px;

    .comment-input {
      .comment-actions {
        display: flex;
        justify-content: flex-end;
        margin-top: 8px;
      }
    }

    .comment-content {
      margin-top: 16px;

      .comment-items {
        max-height: 796px;
        overflow-y: auto;
        padding-right: 8px;
      }

      .content-actions {
        display: flex;
        margin-bottom: 16px;
        gap: 8px;
        border-bottom: 1px solid var(--color-border-3);
        padding-bottom: 8px;
        cursor: pointer;
        user-select: none;
      }

      .content-empty {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100px;
        gap: 8px;
      }
    }
  }

  .comment-footer {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;

    .comment-more {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 8px;
    }
  }
}
</style>
