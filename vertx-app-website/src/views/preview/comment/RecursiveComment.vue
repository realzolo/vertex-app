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
            <IconHeartFill :style="{ color: '#f53f3f' }" />
          </span>
          <span v-else>
            <IconHeart />
          </span>
          {{ comment.upvotes || 0 }}
        </span>
        <span class="comment-item-action" @click="onReply">
          <IconMessage />
          回复
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
import { type CommentReq, addComment } from '@/apis/preview/comment'
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
    const resp = await addComment(replyComment)
    if (!resp.success) {
      Message.error('添加评论失败')
      return
    }
    Message.success('回复成功')
    replyContent.value = ''
    showReplyContentInput.value = false
    props.search()
  }
}
// 取消回复
const cancelReply = () => {
  replyContent.value = ''
  showReplyContentInput.value = false
}
// 点赞
const onUpvote = () => {
  props.comment.upvoted = !props.comment.upvoted;
  props.comment.upvote = props.comment.upvoted ? props.comment.upvotes + 1 : props.comment.upvotes - 1;
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
      color: #fff;
      font-size: 12px;
      margin-left: 4px;
      padding: 2px 6px;
      border-radius: 6px;
      background-color: rgb(254, 44, 85);
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
