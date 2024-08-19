package org.survy.domain.applicationservice.ports.output.repository;

import org.survy.domain.core.entity.MyUser;
import org.survy.domain.core.valueObject.UserId;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  {

    boolean existsByUserId(UUID userId);
}
