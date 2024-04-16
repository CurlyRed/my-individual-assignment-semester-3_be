/*package Marketplace.config.security.token.impl;

import Marketplace.config.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final Long userId;
    private final String role;

    public AccessTokenImpl(String subject, Long userId, String role){
        this.subject=subject;
        this.userId=userId;
        if(role==null || role.isEmpty()){
            throw new IllegalArgumentException("Role can not be empty");
        }
        this.role=role;
    }

    @Override
    public boolean hasRole(String roleName){
        return this.role.equals(roleName);
    }
}*/
