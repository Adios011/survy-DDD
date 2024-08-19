package org.survy.dataaccess.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.survy.dataaccess.user.entity.MyUserEntity;
import org.survy.domain.core.entity.MyUser;

import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<MyUserEntity, UUID> {
}
