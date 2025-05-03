<template>
  <a-comment
    :content="comment.content"
    class="comment-item"
  >
    <template #avatar>
      <Avatar :src="comment.author.avatar" :name="comment.author.nickname" :size="32" />
    </template>
    <template #author>
      <div class="comment-item-author">
        <span class="author-name">{{ comment.author.nickname }}</span>
        <span v-if="userStore.userId === comment.author.id" class="author-tag">作者</span>
      </div>
    </template>
    <div class="comment-item-footer">
      <div class="comment-item-info">
        <span>{{ comment.createTime }}</span>
        <span>{{ comment.address ? ` · ${comment.address}` : '' }}</span>
      </div>
      <div class="comment-item-actions">
        <span key="heart" class="comment-item-action" @click="onUpvote">
          <span v-if="comment.upvoted">
            <icon-thumb-up-fill :style="{ color: 'rgb(var(--primary-6))' }" />
          </span>
          <span v-else>
            <icon-thumb-up />
          </span>
          {{ comment.upvotes || 0 }}
        </span>
        <span class="comment-item-action" @click="onReply">
          <icon-message />
          回复
        </span>
        <span class="comment-item-action">
          <a-dropdown @select="handleSelect">
            <icon-more />
            <template #content>
              <template v-for="item in MORE_OPTIONS" :key="item.value">
                <a-doption :value="item.value" :disabled="item.disabled">{{ item.label }}</a-doption>
              </template>
            </template>
          </a-dropdown>
        </span>
      </div>
    </div>
    <div v-if="showReplyContentInput" class="comment-item-reply">
      <div class="reply-content">
        <a-textarea
          v-model="replyContent"
          allow-clear
          show-word-limit
          :placeholder="`回复@${comment.author.nickname}`"
          :textarea-attrs="{ rows: 3 }"
          :max-length="1000"
          @keyup.enter="submitReply"
        />
      </div>
      <div class="reply-item-actions">
        <a-button type="secondary" @click="cancelReply">取消</a-button>
        <a-button type="primary" @click="submitReply">回复</a-button>
      </div>
    </div>
    <RecursiveComment v-for="reply in comment.replies" :key="reply.id" :comment="reply" :search="search" />
  </a-comment>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Message } from '@arco-design/web-vue'
import { type CommentReq, addComment, deleteComment, upvote } from '@/apis/preview/comment'
import { useUserStore } from '@/stores'

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
  search: {
    type: Function,
    required: true,
  },
})

const route = useRoute()
const userStore = useUserStore()

const { id = 10000 } = route.query

const showReplyContentInput = ref(false)
const replyContent = ref('')

const MORE_OPTIONS = [
  { label: '删除', value: 'delete', disabled: userStore.userId !== props.comment.author.id && !userStore.permissions.includes('comment:delete') },
  { label: '举报', value: 'report', disabled: true },
]

// 回复
const onReply = () => {
  showReplyContentInput.value = !showReplyContentInput.value
}

// 提交回复
const submitReply = async () => {
  if (replyContent.value.trim()) {
    const replyComment: CommentReq = {
      objectId: Number(id),
      parentId: props.comment.id,
      content: replyContent.value,
    }
    try {
      const resp = await addComment(replyComment)
      if (!resp.success) {
        Message.error('回复失败')
        return
      }
      Message.success('回复成功')
      replyContent.value = ''
      showReplyContentInput.value = false
    } finally {
      props.search()
    }
  }
}
// 取消回复
const cancelReply = () => {
  replyContent.value = ''
  showReplyContentInput.value = false
}
// 点赞
const onUpvote = () => {
  props.comment!.upvoted = !props.comment.upvoted
  props.comment!.upvotes = props.comment.upvoted ? props.comment.upvotes + 1 : props.comment.upvotes - 1
  upvote(props.comment.id)
  Message.success('点赞成功')
}
// 下拉事件处理
const handleSelect = async (key: string) => {
  if (key === 'delete') {
    const resp = await deleteComment(props.comment.id)
    if (!resp.success) {
      Message.error('删除失败')
      return
    }
    Message.success('删除成功')
    props.search()
  }
}
</script>

<style scoped lang="scss">
::v-deep {
  .arco-comment-inner-content {
    .arco-comment-title {
      height: 32px;
      display: flex;
      align-items: center;

      .arco-comment-author {
        display: block;
      }
    }

    .arco-comment-content {
      margin-top: 8px;
    }
  }

  .arco-comment-inner-comment {
    margin-top: 8px;
  }
}

.comment-item {
  .comment-item-author {
    display: flex;
    align-items: center;
    font-size: 14px;
    user-select: none;

    .author-name {
      margin-right: 4px;
      color: var(--color-text-1);
      font-weight: 500;
    }

    .author-tag {
      color: rgb(var(--primary-6));
      font-size: 12px;
      margin-left: 4px;
      padding: 2px 6px;
      border-radius: 6px;
      background-color: rgb(var(--primary-1));
    }

  }

  .comment-item-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 8px 0;
    user-select: none;

    .comment-item-info {
      color: var(--color-text-3);
    }

    .comment-item-actions {
      display: flex;
      align-items: center;
      gap: 8px;

      .comment-item-action {
        display: inline-block;
        padding: 0 4px;
        color: var(--color-text-3);
        line-height: 24px;
        background: transparent;
        border-radius: 2px;
        cursor: pointer;
        transition: all 0.1s ease;
        user-select: none;

        &:hover {
          color: var(--color-text-2);
        }
      }
    }
  }

  .comment-item-reply {
    .reply-item-content {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin-bottom: 8px;
    }

    .reply-item-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
    }
  }
}
</style>
