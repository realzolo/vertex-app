package com.onezol.vertx.framework.component.upvote.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpvoteAction {

    private Long userId;

    private Long objectId;

    private Integer action; // 1:点赞 0:取消

}