package com.codeit.project.deokhugam.domain.comment.mapper;

import com.codeit.project.deokhugam.domain.comment.dto.response.CommentDto;
import com.codeit.project.deokhugam.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.nickname", target = "userNickname")
    @Mapping(source = "review.id", target = "reviewId")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);
}