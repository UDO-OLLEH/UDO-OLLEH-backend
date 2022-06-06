package com.udoolleh.backend.core.security.auth;

public interface AuthToken<T> {
    boolean validate();
    T getData();
}
