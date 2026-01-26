package com.modulith.auctionsystem.users.shared.public_api;

import com.modulith.auctionsystem.users.shared.dto.LoginRequest;
import com.modulith.auctionsystem.users.shared.dto.LoginResponse;

public interface AuthPublicApi {
    LoginResponse login(LoginRequest request);
}
