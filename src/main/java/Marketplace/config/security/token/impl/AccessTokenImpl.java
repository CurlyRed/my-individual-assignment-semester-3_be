package Marketplace.config.security.token.impl;

import Marketplace.config.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final String role;

    public AccessTokenImpl(String subject, Long userId, String role) {
        this.subject = subject;
        this.userId = userId;
        this.role = role != null ? role : "";
    }

    public boolean hasRole(String roleName) {
        return roleName.equals(role);
    }
}

