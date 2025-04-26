<template>
  <GiPageLayout>
    <div class="approval-task">
      <div class="approval-task-details">
        <div class="task-header">
          <div class="task-header-title">
            <icon-relation />
            <span>李秀明发起的流程审批</span>
          </div>
          <div class="task-header-order">
            申请编号: APP-2023-0520-001
          </div>
        </div>
        <div class="task-content">
          <a-tabs default-active-key="2">
            <a-tab-pane key="1" title="申请详情">
              Content of Tab Panel 1
            </a-tab-pane>
            <a-tab-pane key="2" title="附件材料">
              Content of Tab Panel 2
            </a-tab-pane>
            <a-tab-pane key="3" title="审批记录">
              Content of Tab Panel 3
            </a-tab-pane>
          </a-tabs>
        </div>
      </div>
      <div class="approval-task-operation">
        <div class="approval-task-content-item">
          <div class="item-title">
            <icon-thunderbolt />
            <span>审批概览</span>
          </div>
          <div class="item-body">
            <div class="subitem">
              <span>审批业务</span>
              <span>通知公告</span>
            </div>
            <div class="subitem">
              <span>节点等待时间</span>
              <span>1.2天</span>
            </div>
            <div class="subitem">
              <span>节点限时</span>
              <span>1天</span>
            </div>
            <div class="subitem">
              <span>下一节点</span>
              <span>总经理审批</span>
            </div>
          </div>
        </div>
        <div class="approval-task-content-item">
          <div class="item-title">
            <icon-notification />
            <span>设置提醒</span>
          </div>
          <div class="item-body">
            <div class="date-range-picker">
              <input type="datetime-local" class="flex-1 p-2 border border-gray-300 rounded-lg text-sm" value="">
              <button class="ml-2 px-3 py-2 bg-orange-500 text-white text-sm rounded-lg hover:bg-orange-600 transition-colors">设置</button>
            </div>
            <div class="tip">设置后将在指定时间提醒您处理此审批</div>
          </div>
        </div>
        <div class="approval-task-content-item">
          <div class="item-title">
            <icon-message />
            <span>审批意见</span>
          </div>
          <div class="item-body">
            <div class="comment">
              <a-textarea :textarea-attrs="{ rows: 6 }" placeholder="请输入审批意见" />
            </div>
            <div class="options">
              <a-button long type="primary" status="danger" size="large">
                <a-space>
                  <icon-close />
                  <span>驳回</span>
                </a-space>
              </a-button>
              <a-button long type="primary" status="success" size="large">
                <a-space>
                  <icon-check />
                  <span>同意</span>
                </a-space>
              </a-button>
            </div>
          </div>
        </div>
        <div class="approval-task-content-item">
          <div class="item-title">
            <icon-info-circle />
            <span>审批帮助</span>
          </div>
          <div class="item-body">
            此审批单涉及大额采购，请仔细核对预算信息。如有疑问，可咨询财务部。
          </div>
        </div>
      </div>
    </div>
  </GiPageLayout>
</template>

<script lang="ts">
</script>

<style lang="scss" scoped>
.approval-task {
  display: flex;
  width: 100%;
  height: 100%;

  .approval-task-details {
    flex-grow: 1;

    .task-header {
      margin-bottom: 32px;

      .task-header-title {
        font-size: 20px;
        font-weight: 600;
        color: var(--color-text-2);
        line-height: 2;

        span {
          margin-left: 8px;
        }
      }

      .task-header-order {
        font-size: 14px;
        color: var(--color-text-3);
        line-height: 2;
      }
    }
  }

  .approval-task-operation {
    width: 28%;
    min-width: 320px;
    max-width: 520px;
    border-left: 1px solid var(--color-border-1);
    margin-left: 16px;
    padding-left: 16px;
    padding-right: 8px;
    overflow-y: auto;

    .approval-task-content-item {
      padding: 14px;
      border-radius: 8px;
      margin-bottom: 16px;
      box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.1);

      .item-title {
        font-size: 16px;
        font-weight: bold;
        line-height: 1.5;
        margin-bottom: 8px;

        span {
          margin-left: 6px;
        }
      }

      .item-body {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        grid-gap: 12px;

        .subitem {
          display: flex;
          align-items: center;
          flex-direction: column;
          font-size: 14px;
          color: var(--color-text-2);
          background-color: #FFFFFF;
          padding: 12px;
          box-sizing: border-box;
          border-radius: 8px;

          span:first-child {
            line-height: 2;
          }

          span:last-child {
            font-weight: 700;
            line-height: 1.5;
          }
        }
      }
    }

    .approval-task-content-item:nth-child(1) {
      background-color: rgb(250, 245, 255);

      .item-title {
        color: rgb(107, 33, 168);
      }

      .item-body .subitem span:last-child {
        color: rgb(126, 34, 206);
      }
    }

    .approval-task-content-item:nth-child(2) {
      background-color: rgb(255, 247, 237);

      .item-title {
        color: rgb(154, 52, 18);
      }

      .item-body {
        display: flex;
        flex-direction: column;

        .date-range-picker {
          display: flex;
          gap: 12px;
          margin: 12px 0;

          input {
            line-height: 24px;
            height: 44px;
            border: 1px solid rgb(209, 213, 219);
            border-radius: 8px;
            background-color: transparent;
            padding: 8px;
            box-sizing: border-box;
            outline: none;
            cursor: pointer;
          }

          button {
            background-color: rgb(249, 115, 22);
            width: 64px;
            padding: 4px 8px;
            color: #FFFFFF;
            box-sizing: border-box;
            border-radius: 8px;
            border: none;
            cursor: pointer;
          }
        }

        .tip {
          color: rgb(234, 88, 12);
          font-size: 14px;
        }
      }
    }

    .approval-task-content-item:nth-child(3) {
      background-color: rgb(249, 250, 251);

      .item-title {
        color: rgb(31, 41, 55 );
      }

      .item-body {
        display: flex;
        flex-direction: column;

        .comment {
          .arco-textarea-wrapper {
            background-color: #FFFFFF;
          }
        }

        .options {
          display: flex;
          justify-content: center;
          align-content: center;
          gap: 12px;
          margin: 10px 0;
          padding: 10px 0;
        }
      }
    }

    .approval-task-content-item:nth-child(4) {
      background-color: rgb(239, 246, 255);

      .item-title {
        color: rgb(30, 64, 175);
      }

      .item-body {
        display: flex;
        flex-direction: column;
        color: rgb(29, 78, 216);
        line-height: 2;
      }
    }
  }
}
</style>
