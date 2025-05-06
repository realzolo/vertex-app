package com.onezol.vertx.framework.component.comment.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.comment.model.entity.CommentEntitySoft;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper extends BaseMapper<CommentEntitySoft> {

    /**
     * 分页查询评论(仅一级评论)
     *
     * @param page     分页参数
     * @param objectId 业务对象ID
     * @param sortType 排序类型
     * @return 评论列表
     */
    Page<CommentEntitySoft> queryTopLevelCommentPage(
            @Param("page") Page<CommentEntitySoft> page,
            @Param("objectType") String objectType,
            @Param("objectId") Long objectId,
            @Param("sortType") String sortType
    );

}
