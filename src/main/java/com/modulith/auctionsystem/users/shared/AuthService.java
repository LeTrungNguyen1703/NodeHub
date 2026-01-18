package com.modulith.auctionsystem.users.shared;

import com.modulith.auctionsystem.users.shared.dto.LoginRequest;
import com.modulith.auctionsystem.users.shared.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
