package Marketplace.persistence.impl;

import org.springframework.stereotype.Repository;
import Marketplace.persistence.UserRepository;
import Marketplace.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {

    private static long NEXT_ID = 1;
    private final List<UserEntity> users;

    public FakeUserRepositoryImpl(){this.users = new ArrayList<>();}

    @Override
    public UserEntity saveUser(UserEntity user){
        if(user.getId() == null){
            user.setId(NEXT_ID);
            NEXT_ID++;
            this.users.add(user);
        }
        return user;
    }

    @Override
    public boolean deleteById(long userId){
        this.users.removeIf(userEntity -> userEntity.getId().equals(userId));
        return true;
    }

    @Override
    public Optional<UserEntity> findById(long userId){
        return this.users.stream()
                .filter(userEntity -> userEntity.getId().equals(userId))
                .findFirst();
    }
}
