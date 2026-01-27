package com.modulith.auctionsystem.users.internal;

import com.modulith.auctionsystem.users.domain.User;
import com.modulith.auctionsystem.users.domain.valueobject.Email;
import com.modulith.auctionsystem.users.shared.dto.CreateUserRequest;
import com.modulith.auctionsystem.users.shared.dto.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dto.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {

    @Mapping(target = "email", source = "email.value")
    UserResponse toUserResponse(User user);

    @Mapping(target = "email", expression = "java(mapEmail(createUserRequest.email()))")
    User toUser(CreateUserRequest createUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateProfileRequest request, @MappingTarget User user);

    default Email mapEmail(String value) {
        return value != null ? new Email(value) : null;
    }
}
