package com.modulith.auctionsystem.users.internal;

import com.modulith.auctionsystem.users.domain.User;
import com.modulith.auctionsystem.users.domain.valueobject.Address;
import com.modulith.auctionsystem.users.domain.valueobject.Email;
import com.modulith.auctionsystem.users.domain.valueobject.PhoneNumber;
import com.modulith.auctionsystem.users.shared.dtos.CreateUserRequest;
import com.modulith.auctionsystem.users.shared.dtos.UpdateProfileRequest;
import com.modulith.auctionsystem.users.shared.dtos.UserResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {

    @Mapping(target = "balance", source = "balance.amount")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "phone", source = "phone.value")
    @Mapping(target = "address", source = "address.value")
    UserResponse toUserResponse(User user);

    @Mapping(target = "email", expression = "java(mapEmail(createUserRequest.email()))")
    User toUser(CreateUserRequest createUserRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phone", expression = "java(mapPhoneNumber(request.phone()))")
    @Mapping(target = "address", expression = "java(mapAddress(request.address()))")
    void updateUserFromRequest(UpdateProfileRequest request, @MappingTarget User user);

    default Email mapEmail(String value) {
        return value != null ? new Email(value) : null;
    }

    default PhoneNumber mapPhoneNumber(String value) {
        return value != null ? new PhoneNumber(value) : null;
    }

    default Address mapAddress(String value) {
        return value != null ? new Address(value) : null;
    }
}
