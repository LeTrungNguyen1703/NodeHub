package com.modulith.auctionsystem.users.shared;

import com.modulith.auctionsystem.users.shared.dtos.LoginRequest;
import com.modulith.auctionsystem.users.shared.dtos.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
