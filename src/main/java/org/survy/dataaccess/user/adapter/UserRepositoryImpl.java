package org.survy.dataaccess.user.adapter;

import org.springframework.stereotype.Repository;
import org.survy.dataaccess.user.entity.MyUserEntity;
import org.survy.dataaccess.user.repository.UserJpaRepository;
import org.survy.domain.applicationservice.ports.output.repository.UserRepository;
import org.survy.domain.core.entity.MyUser;
import org.survy.domain.core.valueObject.UserId;

import java.util.Optional;
import java.util.UUID;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }




    public boolean existsByUserId(UUID userId){
        return userJpaRepository.existsById(userId);
    }

}
