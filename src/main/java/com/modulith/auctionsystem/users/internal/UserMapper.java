package com.modulith.auctionsystem.users.internal;

import com.modulith.auctionsystem.users.domain.User;
import com.modulith.auctionsystem.users.web.dto.CreateUserRequest;
import com.modulith.auctionsystem.users.web.dto.UpdateProfileRequest;
import com.modulith.auctionsystem.users.web.dto.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(CreateUserRequest createUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateProfileRequest request, @MappingTarget User user);
}
