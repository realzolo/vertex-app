package com.onezol.vertx.framework.schedule.enums;

import com.onezol.vertx.framework.common.annotation.EnumDictionary;
import com.onezol.vertx.framework.common.constant.enumeration.StandardEnumeration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(name = "任务执行原因")
@Getter
@RequiredArgsConstructor
@EnumDictionary(name = "任务执行原因", value = "job_execute_reason")
public enum JobExecuteReason implements StandardEnumeration<Integer> {

    NONE("无", 0),

    TIME_OUT("任务执行超时", 1),

    CLIENT_NOT_FOUND("无客户端节点", 2),

    TASK_CLOSED("任务已关闭", 3),

    TASK_DROPPED("任务丢弃", 4),

    TASK_COVERED("任务被覆盖", 5),

    TASK_NONE("无可执行任务项", 6),

    TASK_EXCEPTION("任务执行期间发生非预期异常", 7),

    MANUAL_STOP("手动停止", 8),

    NODE_EXCEPTION("条件节点执行异常", 9),

    TASK_INTERRUPT("任务中断", 10),

    CALLBACK_EXCEPTION("回调节点执行异常", 11),

    NO_NEED_PROCESS("无需处理", 12),

    NODE_SKIP("节点关闭跳过执行", 13),

    NOT_PASS("判定未通过", 14),

    TASK_FINISHED("任务已完成", 15),

    TASK_RUNNING("任务正在执行", 16),

    TASK_WAITING("任务等待执行", 17),

    TASK_FAILED("任务执行失败", 18),

    TASK_SUCCESS("任务执行成功", 19);

    private final String name;

    private final Integer value;

}
