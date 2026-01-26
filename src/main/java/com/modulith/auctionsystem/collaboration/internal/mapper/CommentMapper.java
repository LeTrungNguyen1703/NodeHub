package com.modulith.auctionsystem.collaboration.internal.mapper;

import com.modulith.auctionsystem.collaboration.domain.entity.Comment;
import com.modulith.auctionsystem.collaboration.shared.dto.CommentResponse;
import com.modulith.auctionsystem.collaboration.shared.dto.CreateCommentRequest;
import com.modulith.auctionsystem.collaboration.shared.dto.UpdateCommentRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface CommentMapper {

    CommentResponse toCommentResponse(Comment comment);

    Comment toComment(CreateCommentRequest createCommentRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromDto(UpdateCommentRequest updateCommentRequest, @MappingTarget Comment comment);
}
